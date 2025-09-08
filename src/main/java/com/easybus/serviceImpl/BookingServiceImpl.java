package com.easybus.serviceImpl;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.easybus.entity.Booking;
import com.easybus.entity.Bus;
import com.easybus.exceptions.ResourceNotFoundException;
import com.easybus.model.BookingRequest;
import com.easybus.model.BookingResponse;
import com.easybus.model.RescheduleRequest;
import com.easybus.repository.BookingRepository;
import com.easybus.repository.BusRepository;
import com.easybus.service.BookingService;
import com.easybus.service.SeatLockService;

import jakarta.transaction.Transactional;

@Service
public class BookingServiceImpl implements BookingService {

    private static final Logger log = LoggerFactory.getLogger(BookingServiceImpl.class);

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private BusRepository busRepository;

    @Autowired
    private SeatLockService seatLockService;

    @Transactional
    @Override
    public BookingResponse bookTicket(BookingRequest request) {
        log.info("🎟️ Booking ticket request received: {}", request);

        // 1. Fetch Bus
        Bus bus = busRepository.findById(request.getBusId())
                .orElseThrow(() -> new ResourceNotFoundException("Bus not found with id: " + request.getBusId()));
        log.debug("✅ Bus found: {}", bus);

        // 2. Check already booked seats
        List<String> alreadyBookedSeats = bookingRepository.findBookedSeats(bus.getId(), request.getJourneyDate());
        log.debug("🔍 Already booked seats: {}", alreadyBookedSeats);

        for (String seat : request.getSeatNumbers()) {
            if (alreadyBookedSeats.contains(seat)) {
                log.error("❌ Seat {} is already booked!", seat);
                throw new IllegalArgumentException("Seat " + seat + " already booked");
            }
        }

        // 3. Lock seats
        boolean locked = seatLockService.lockSeats(bus.getId(), request.getJourneyDate(), request.getSeatNumbers());
        if (!locked) {
            log.error("❌ Failed to lock seats for busId={}, date={}", bus.getId(), request.getJourneyDate());
            throw new RuntimeException("Failed to lock seats. Please try again.");
        }
        log.info("✅ Seats locked: {}", request.getSeatNumbers());

        // 4. Fare calculation
        double fare = request.getSeatNumbers().size() * bus.getFare();
        log.info("💰 Fare calculated: {}", fare);

        // 5. Create booking object
        Booking booking = new Booking();
        booking.setUserId(request.getUserId());
        booking.setBus(bus);
        booking.setSeatNumbers(String.join(",", request.getSeatNumbers()));
        booking.setJourneyDate(request.getJourneyDate());
        booking.setBoardingPoint(request.getBoardingPoint());
        booking.setDroppingPoint(request.getDroppingPoint());
        booking.setStatus("CONFIRMED");
        booking.setPnr(generatePNR());
        booking.setTotalFare(fare);

        bookingRepository.save(booking);
        log.info("✅ Booking saved successfully with PNR: {}", booking.getPnr());

        return mapToResponse(booking, bus);
    }

    @Transactional
    @Override
    public void cancelTicket(Long bookingId) {
        log.warn("⚠️ Cancel ticket request received for bookingId={}", bookingId);

        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found"));

        booking.setStatus("CANCELLED");
        bookingRepository.save(booking);

        seatLockService.unlockSeats(
                booking.getBus().getId(),
                booking.getJourneyDate(),
                Arrays.asList(booking.getSeatNumbers().split(","))
        );
        log.info("✅ Booking {} cancelled and seats unlocked", booking.getPnr());
    }

    @Transactional
    @Override
    public BookingResponse rescheduleTicket(Long bookingId, RescheduleRequest request) {
        log.info("🔄 Reschedule request for bookingId={} with new details: {}", bookingId, request);

        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found"));

        Bus newBus = busRepository.findById(request.getNewBusId())
                .orElseThrow(() -> new ResourceNotFoundException("New bus not found"));
        log.debug("✅ New Bus found: {}", newBus);

        List<String> alreadyBookedSeats = bookingRepository.findBookedSeats(newBus.getId(), request.getNewJourneyDate());
        log.debug("🔍 Already booked seats in new bus: {}", alreadyBookedSeats);

        for (String seat : request.getNewSeatNumbers()) {
            if (alreadyBookedSeats.contains(seat)) {
                log.error("❌ Seat {} already booked in new bus {}", seat, newBus.getId());
                throw new IllegalArgumentException("Seat " + seat + " already booked");
            }
        }

        boolean locked = seatLockService.lockSeats(newBus.getId(), request.getNewJourneyDate(), request.getNewSeatNumbers());
        if (!locked) {
            log.error("❌ Failed to lock new seats for busId={}, date={}", newBus.getId(), request.getNewJourneyDate());
            throw new RuntimeException("Failed to lock new seats. Please try again.");
        }
        log.info("✅ New seats locked: {}", request.getNewSeatNumbers());

        seatLockService.unlockSeats(
                booking.getBus().getId(),
                booking.getJourneyDate(),
                Arrays.asList(booking.getSeatNumbers().split(","))
        );
        log.info("🔓 Old seats unlocked for bookingId={}", bookingId);

        booking.setBus(newBus);
        booking.setSeatNumbers(String.join(",", request.getNewSeatNumbers()));
        booking.setJourneyDate(request.getNewJourneyDate());
        booking.setStatus("RESCHEDULED");
        booking.setTotalFare(request.getNewSeatNumbers().size() * newBus.getFare());

        bookingRepository.save(booking);
        log.info("✅ Booking {} rescheduled successfully with new busId={}", booking.getPnr(), newBus.getId());

        return mapToResponse(booking, newBus);
    }

    private String generatePNR() {
        String pnr = "PNR" + UUID.randomUUID().toString().replace("-", "").substring(0, 8).toUpperCase();
        log.debug("Generated PNR: {}", pnr);
        return pnr;
    }

    private BookingResponse mapToResponse(Booking booking, Bus bus) {
        BookingResponse response = new BookingResponse();
        response.setBookingId(booking.getId());
        response.setPnr(booking.getPnr());
        response.setBusName(bus.getName());
        response.setStatus(booking.getStatus());
        response.setSeatNumbers(Arrays.asList(booking.getSeatNumbers().split(",")));
        response.setTotalFare(booking.getTotalFare());
        response.setJourneyDate(booking.getJourneyDate());
        response.setMessage("Booking processed successfully");
        log.debug("Mapped BookingResponse: {}", response);
        return response;
    }
}

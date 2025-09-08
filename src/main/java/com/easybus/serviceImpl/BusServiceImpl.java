package com.easybus.serviceImpl;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.easybus.entity.Bus;
import com.easybus.entity.Fare;
import com.easybus.entity.Seat;
import com.easybus.repository.BookingRepository;
import com.easybus.repository.BusRepository;
import com.easybus.repository.FareRepository;
import com.easybus.repository.SeatRepository;
import com.easybus.service.BusService;

@Service
public class BusServiceImpl implements BusService {

    private static final Logger log = LoggerFactory.getLogger(BusServiceImpl.class);

    @Autowired private BusRepository busRepository;
    @Autowired private SeatRepository seatRepository;
    @Autowired private FareRepository fareRepository;
    @Autowired private BookingRepository bookingRepository;

    @Override
    public List<Bus> searchBuses(String from, String to) {
        log.info(" Searching buses from '{}' to '{}'", from, to);
        List<Bus> buses = busRepository.findBySourceAndDestination(from, to);
        log.debug(" Found {} buses", buses.size());
        return buses;
    }

    @Override
    public Optional<Bus> getBusDetails(Long id) {
        log.info(" Fetching details for busId={}", id);
        Optional<Bus> bus = busRepository.findById(id);
        if (bus.isPresent()) {
            log.debug(" Bus found: {}", bus.get());
        } else {
            log.warn(" No bus found with id={}", id);
        }
        return bus;
    }

    @Override
    public List<Seat> getSeatLayout(Long busId) {
        log.info(" Fetching seat layout for busId={}", busId);
        List<Seat> seats = seatRepository.findByBusId(busId);
        log.debug(" Found {} seats for busId={}", seats.size(), busId);
        return seats;
    }

    @Override
    public long getSeatAvailability(Long busId) {
        log.info(" Checking seat availability for busId={}", busId);
        long availableSeats = seatRepository.findByBusId(busId)
                .stream()
                .filter(Seat::isAvailable)
                .count();
        log.debug(" Available seats for busId={}: {}", busId, availableSeats);
        return availableSeats;
    }

    @Override
    public Fare getFareDetails(Long busId) {
        log.info(" Fetching fare details for busId={}", busId);
        Fare fare = fareRepository.findByBusId(busId);
        if (fare != null) {
            log.debug(" Fare details: {}", fare);
        } else {
            log.warn(" No fare details found for busId={}", busId);
        }
        return fare;
    }

    @Override
    public List<String> getPopularRoutes() {
        log.info(" Fetching popular routes from booking data");
        List<String> routes = bookingRepository.findTopPopularRoutes();
        log.debug(" Popular routes: {}", routes);
        return routes;
    }
}

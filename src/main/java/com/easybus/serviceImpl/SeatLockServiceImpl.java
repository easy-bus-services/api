package com.easybus.serviceImpl;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.easybus.repository.BookingRepository;
import com.easybus.service.SeatLockService;

@Service
public class SeatLockServiceImpl implements SeatLockService {

    private static final Logger log = LoggerFactory.getLogger(SeatLockServiceImpl.class);

    private final Set<String> lockedSeats = new HashSet<>();
    
    @Autowired 
    BookingRepository bookingRepository;

    @Override
    public boolean lockSeats(Long busId, LocalDate journeyDate, List<String> seatNumbers) {
        log.info(" Trying to lock seats {} for busId={} on date={}", seatNumbers, busId, journeyDate);

        // Check if seats are already booked
        List<String> alreadyBooked = bookingRepository.findBookedSeats(busId, journeyDate);
        for (String seat : seatNumbers) {
            if (alreadyBooked.contains(seat)) {
                log.warn(" Seat {} already booked for busId={} on {}", seat, busId, journeyDate);
                return false; // seat already booked
            }
        }

        // Mark seats as locked in memory (for demo purpose)
        for (String seat : seatNumbers) {
            String key = busId + "-" + journeyDate.toString() + "-" + seat;
            lockedSeats.add(key);
            log.debug(" Locked seat: {}", key);
        }

        log.info(" Successfully locked {} seats for busId={} on {}", seatNumbers.size(), busId, journeyDate);
        return true;
    }

    @Override
    public void unlockSeats(Long busId, LocalDate journeyDate, List<String> seats) {
        log.info(" Unlocking seats {} for busId={} on date={}", seats, busId, journeyDate);

        for (String seat : seats) {
            String key = busId + "-" + journeyDate.toString() + "-" + seat;
            if (lockedSeats.remove(key)) {
                log.debug(" Unlocked seat: {}", key);
            } else {
                log.warn(" Seat {} was not locked, skipping...", seat);
            }
        }

        log.info(" Completed unlocking seats for busId={} on {}", busId, journeyDate);
    }
}

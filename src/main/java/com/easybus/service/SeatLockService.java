package com.easybus.service;

import java.time.LocalDate;
import java.util.List;

import java.time.LocalDate;
import java.util.List;

public interface SeatLockService {
    boolean lockSeats(Long busId, LocalDate journeyDate, List<String> seatNumbers);
    void unlockSeats(Long busId, LocalDate journeyDate, List<String> seats);
}


package com.easybus.service;

import java.util.List;
import java.util.Optional;

import com.easybus.entity.Bus;
import com.easybus.entity.Fare;
import com.easybus.entity.Seat;

public interface BusService {
    List<Bus> searchBuses(String from, String to);
    Optional<Bus> getBusDetails(Long id);
    List<Seat> getSeatLayout(Long busId);
    long getSeatAvailability(Long busId);
    Fare getFareDetails(Long busId);
    List<String> getPopularRoutes();
}

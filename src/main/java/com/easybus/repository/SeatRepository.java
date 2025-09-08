package com.easybus.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.easybus.entity.Seat;

public interface SeatRepository extends JpaRepository<Seat, Long> {
    List<Seat> findByBusId(Long busId);
}
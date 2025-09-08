package com.easybus.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.easybus.entity.Bus;

public interface BusRepository extends JpaRepository<Bus, Long> {
    List<Bus> findBySourceAndDestination(String source, String destination);
}
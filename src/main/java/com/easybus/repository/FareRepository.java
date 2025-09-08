package com.easybus.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.easybus.entity.Fare;

public interface FareRepository extends JpaRepository<Fare, Long> {
    Fare findByBusId(Long busId);
}
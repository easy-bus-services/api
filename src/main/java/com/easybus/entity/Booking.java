package com.easybus.entity;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "bookings")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long userId;
    private String pnr;

    private String seatNumbers;

    private double totalFare;

    private String status;
    private String boardingPoint; // ✅ Add this

    private String droppingPoint; // ✅ Add this
    private LocalDate journeyDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bus_id") // FK column in bookings table
    private Bus bus;

    // other fields like user, timestamps, etc.
}


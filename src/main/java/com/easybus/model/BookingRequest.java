package com.easybus.model;

import java.time.LocalDate;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookingRequest {
    private Long userId;
    private Long busId;
    private List<String> seatNumbers;
    private LocalDate journeyDate;
    private String boardingPoint;
    private String droppingPoint;
}

package com.easybus.model;

import java.time.LocalDate;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RescheduleRequest {
    private Long newBusId;
    private LocalDate newJourneyDate;
    private List<String> newSeatNumbers;

}

package com.easybus.model;

import java.time.LocalDate;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookingResponse {
	private Long bookingId;
	private String pnr;
	private String busName;
	private String status;
	private List<String> seatNumbers;
	private Double totalFare;
	private LocalDate journeyDate;
	private String message;

}

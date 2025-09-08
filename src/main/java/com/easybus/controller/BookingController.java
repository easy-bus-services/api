package com.easybus.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.easybus.Constants;
import com.easybus.model.BookingRequest;
import com.easybus.model.BookingResponse;
import com.easybus.model.RescheduleRequest;
import com.easybus.model.ResponseMessage;
import com.easybus.service.BookingService;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {

    private static final Logger log = LoggerFactory.getLogger(BookingController.class);

    @Autowired
    private BookingService bookingService;

    // ✅ Book Ticket
    @PostMapping("/book")
    public ResponseEntity<ResponseMessage> bookTicket(@RequestBody BookingRequest request) {
        log.info(" Booking request received: {}", request);

        if (request == null || request.getSeatNumbers() == null || request.getSeatNumbers().isEmpty()) {
            log.error("Invalid booking request: {}", request);
            return ResponseEntity.badRequest().body(
                    new ResponseMessage(400, Constants.FAILURE, "Invalid booking request", null));
        }

        try {
            BookingResponse response = bookingService.bookTicket(request);
            log.info(" Booking successful, response: {}", response);
            return ResponseEntity.ok(
                    new ResponseMessage(200, Constants.SUCCESS, "Booking successful", response));
        } catch (Exception e) {
            log.error(" Booking failed: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().body(
                    new ResponseMessage(500, Constants.FAILED, "Booking failed: " + e.getMessage(), null));
        }
    }

    //  Cancel Ticket
    @DeleteMapping("/cancel/{bookingId}")
    public ResponseEntity<ResponseMessage> cancelTicket(@PathVariable Long bookingId) {
        log.warn(" Cancel request received for bookingId: {}", bookingId);

        if (bookingId == null) {
            return ResponseEntity.badRequest().body(
                    new ResponseMessage(400, Constants.FAILURE, "Booking ID cannot be null"));
        }

        try {
            bookingService.cancelTicket(bookingId);
            log.info(" Booking {} cancelled successfully", bookingId);
            return ResponseEntity.ok(
                    new ResponseMessage(200, Constants.SUCCESS, "Booking cancelled successfully"));
        } catch (Exception e) {
            log.error(" Cancellation failed: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().body(
                    new ResponseMessage(500, Constants.FAILED, "Cancellation failed: " + e.getMessage()));
        }
    }

    // 🔁 Reschedule Ticket
    @PutMapping("/reschedule/{bookingId}")
    public ResponseEntity<ResponseMessage> rescheduleTicket(
            @PathVariable Long bookingId,
            @RequestBody RescheduleRequest request) {

        log.info(" Reschedule request received for bookingId: {} with new details: {}", bookingId, request);

        if (bookingId == null || request == null || request.getNewSeatNumbers() == null) {
            return ResponseEntity.badRequest().body(
                    new ResponseMessage(400, Constants.FAILURE, "Invalid reschedule request"));
        }

        try {
            BookingResponse response = bookingService.rescheduleTicket(bookingId, request);
            log.info(" Reschedule successful for bookingId: {}, new response: {}", bookingId, response);
            return ResponseEntity.ok(
                    new ResponseMessage(200, Constants.SUCCESS, "Reschedule successful", response));
        } catch (Exception e) {
            log.error(" Reschedule failed: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().body(
                    new ResponseMessage(500, Constants.FAILED, "Reschedule failed: " + e.getMessage()));
        }
    }
}

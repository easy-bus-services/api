package com.easybus.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.easybus.Constants;
import com.easybus.entity.Bus;
import com.easybus.entity.Fare;
import com.easybus.entity.Seat;
import com.easybus.model.ResponseMessage;
import com.easybus.service.BusService;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/buses")
public class BusController {

    private static final Logger log = LoggerFactory.getLogger(BusController.class);

    @Autowired
    private BusService busService;

    // 1. Search buses by source & destination
    @GetMapping("/search")
    public ResponseEntity<ResponseMessage> searchBuses(
            @RequestParam String from,
            @RequestParam String to) {

        log.info(" Searching buses from '{}' to '{}'", from, to);

        if (from == null || to == null) {
            return ResponseEntity.badRequest().body(
                    new ResponseMessage(400, Constants.FAILURE, "Source and destination must be provided"));
        }

        try {
            List<Bus> buses = busService.searchBuses(from, to);
            if (buses.isEmpty()) {
                log.warn(" No buses found for route {} -> {}", from, to);
                return ResponseEntity.ok(
                        new ResponseMessage(200, Constants.SUCCESS, "No buses found for this route", buses));
            }
            log.info(" Found {} buses for route {} -> {}", buses.size(), from, to);
            return ResponseEntity.ok(
                    new ResponseMessage(200, Constants.SUCCESS, "Buses retrieved successfully", buses));
        } catch (Exception e) {
            log.error(" Error searching buses: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().body(
                    new ResponseMessage(500, Constants.FAILED, "Error fetching buses: " + e.getMessage()));
        }
    }

    // 2. Get bus details
    @GetMapping("/{id}")
    public ResponseEntity<ResponseMessage> getBusDetails(@PathVariable Long id) {
        log.info(" Fetching bus details for ID: {}", id);

        if (id == null) {
            return ResponseEntity.badRequest().body(
                    new ResponseMessage(400, Constants.FAILURE, "Bus ID cannot be null"));
        }

        return busService.getBusDetails(id)
                .map(bus -> {
                    log.info(" Bus details found: {}", bus);
                    return ResponseEntity.ok(
                            new ResponseMessage(200, Constants.SUCCESS, "Bus details retrieved", bus));
                })
                .orElseGet(() -> {
                    log.warn(" No bus found with ID: {}", id);
                    return ResponseEntity.ok(
                            new ResponseMessage(404, Constants.FAILURE, "No bus found with ID: " + id));
                });
    }

    // 3. Get seat layout
    @GetMapping("/{id}/seats")
    public ResponseEntity<ResponseMessage> getSeatLayout(@PathVariable Long id) {
        log.info(" Fetching seat layout for Bus ID: {}", id);

        if (id == null) {
            return ResponseEntity.badRequest().body(
                    new ResponseMessage(400, Constants.FAILURE, "Bus ID cannot be null"));
        }

        try {
            List<Seat> seats = busService.getSeatLayout(id);
            if (seats.isEmpty()) {
                return ResponseEntity.ok(
                        new ResponseMessage(200, Constants.SUCCESS, "No seats available", seats));
            }
            return ResponseEntity.ok(
                    new ResponseMessage(200, Constants.SUCCESS, "Seat layout retrieved successfully", seats));
        } catch (Exception e) {
            log.error(" Error fetching seat layout: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().body(
                    new ResponseMessage(500, Constants.FAILED, "Error fetching seat layout: " + e.getMessage()));
        }
    }

    // 4. Check seat availability
    @GetMapping("/{id}/availability")
    public ResponseEntity<ResponseMessage> getSeatAvailability(@PathVariable Long id) {
        log.info(" Checking seat availability for Bus ID: {}", id);

        if (id == null) {
            return ResponseEntity.badRequest().body(
                    new ResponseMessage(400, Constants.FAILURE, "Bus ID cannot be null"));
        }

        try {
            Long availableSeats = busService.getSeatAvailability(id);
            return ResponseEntity.ok(
                    new ResponseMessage(200, Constants.SUCCESS, "Seat availability retrieved", availableSeats));
        } catch (Exception e) {
            log.error(" Error fetching availability: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().body(
                    new ResponseMessage(500, Constants.FAILED, "Error fetching seat availability: " + e.getMessage()));
        }
    }

    // 5. Get fare details
    @GetMapping("/{id}/fares")
    public ResponseEntity<ResponseMessage> getFareDetails(@PathVariable Long id) {
        log.info(" Fetching fare details for Bus ID: {}", id);

        if (id == null) {
            return ResponseEntity.badRequest().body(
                    new ResponseMessage(400, Constants.FAILURE, "Bus ID cannot be null"));
        }

        try {
            Fare fare = busService.getFareDetails(id);
            if (fare == null) {
                return ResponseEntity.ok(
                        new ResponseMessage(404, Constants.FAILURE, "Fare details not found for Bus ID: " + id));
            }
            return ResponseEntity.ok(
                    new ResponseMessage(200, Constants.SUCCESS, "Fare details retrieved", fare));
        } catch (Exception e) {
            log.error(" Error fetching fare: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().body(
                    new ResponseMessage(500, Constants.FAILED, "Error fetching fare: " + e.getMessage()));
        }
    }

    // 6. Get popular routes
    @GetMapping("/routes/popular")
    public ResponseEntity<ResponseMessage> getPopularRoutes() {
        log.info(" Fetching popular routes...");

        try {
            List<String> routes = busService.getPopularRoutes();
            if (routes.isEmpty()) {
                return ResponseEntity.ok(
                        new ResponseMessage(200, Constants.SUCCESS, "No popular routes found", routes));
            }
            return ResponseEntity.ok(
                    new ResponseMessage(200, Constants.SUCCESS, "Popular routes retrieved", routes));
        } catch (Exception e) {
            log.error(" Error fetching routes: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().body(
                    new ResponseMessage(500, Constants.FAILED, "Error fetching routes: " + e.getMessage()));
        }
    }
}

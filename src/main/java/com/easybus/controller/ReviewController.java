package com.easybus.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.easybus.Constants;
import com.easybus.model.ResponseMessage;
import com.easybus.model.ReviewRequest;
import com.easybus.model.ReviewResponse;
import com.easybus.service.ReviewService;

@RestController
@RequestMapping("/api/reviews")
public class ReviewController {

    private static final Logger log = LoggerFactory.getLogger(ReviewController.class);

    @Autowired
    private ReviewService reviewService;

    // ✅ Submit Review
    @PostMapping("/busreview")
    public ResponseEntity<ResponseMessage> submitReview(@RequestBody ReviewRequest request) {
        log.info(" Submit review request received: {}", request);

        if (request == null || request.getBusId() == null || request.getUserId() == null) {
            return ResponseEntity.badRequest().body(
                    new ResponseMessage(400, Constants.FAILURE, "Invalid review request. BusId and UserId are required")
            );
        }

        try {
            ReviewResponse response = reviewService.addReview(request);
            return ResponseEntity.ok(
                    new ResponseMessage(200, Constants.SUCCESS, "Review submitted successfully", response)
            );
        } catch (Exception e) {
            log.error(" Error submitting review: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().body(
                    new ResponseMessage(500, Constants.FAILED, "Failed to submit review: " + e.getMessage())
            );
        }
    }

    // ✅ Get Reviews for a Bus
    @GetMapping("/bus/{busId}")
    public ResponseEntity<ResponseMessage> getReviews(@PathVariable Long busId) {
        log.info(" Fetching reviews for Bus ID: {}", busId);

        if (busId == null) {
            return ResponseEntity.badRequest().body(
                    new ResponseMessage(400, Constants.FAILURE, "Bus ID cannot be null")
            );
        }

        List<ReviewResponse> reviews = reviewService.getReviewsByBus(busId);

        if (reviews == null || reviews.isEmpty()) {
            return ResponseEntity.ok(
                    new ResponseMessage(200, Constants.SUCCESS, "No reviews found for this bus", reviews)
            );
        }

        return ResponseEntity.ok(
                new ResponseMessage(200, Constants.SUCCESS, "Reviews retrieved successfully", reviews)
        );
    }
}

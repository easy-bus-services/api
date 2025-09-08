package com.easybus.serviceImpl;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.easybus.entity.Review;
import com.easybus.model.ReviewRequest;
import com.easybus.model.ReviewResponse;
import com.easybus.repository.ReviewRepository;
import com.easybus.service.ReviewService;

@Service
public class ReviewServiceImpl implements ReviewService {

    private static final Logger log = LoggerFactory.getLogger(ReviewServiceImpl.class);

    @Autowired
    private ReviewRepository reviewRepository;

    @Override
    public ReviewResponse addReview(ReviewRequest request) {
        log.info("📝 Adding review for busId={} by userId={}", request.getBusId(), request.getUserId());

        Review review = new Review();
        review.setUserId(request.getUserId());
        review.setBusId(request.getBusId());
        review.setRating(request.getRating());
        review.setFeedback(request.getFeedback());

        Review saved = reviewRepository.save(review);
        log.debug("✅ Saved review: {}", saved);

        ReviewResponse response = new ReviewResponse();
        response.setId(saved.getId());
        response.setUserId(saved.getUserId());
        response.setBusId(saved.getBusId());
        response.setRating(saved.getRating());
        response.setFeedback(saved.getFeedback());
        response.setMessage("Review submitted successfully!");

        log.info("✅ Review added successfully for busId={}", saved.getBusId());
        return response;
    }

    @Override
    public List<ReviewResponse> getReviewsByBus(Long busId) {
        log.info("🔎 Fetching reviews for busId={}", busId);

        List<ReviewResponse> responses = reviewRepository.findByBusId(busId)
                .stream()
                .map(review -> {
                    ReviewResponse response = new ReviewResponse();
                    response.setId(review.getId());
                    response.setUserId(review.getUserId());
                    response.setBusId(review.getBusId());
                    response.setRating(review.getRating());
                    response.setFeedback(review.getFeedback());
                    response.setMessage("Success");
                    return response;
                })
                .collect(Collectors.toList());

        log.info("📋 Found {} reviews for busId={}", responses.size(), busId);
        return responses;
    }
}

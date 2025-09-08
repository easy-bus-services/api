package com.easybus.service;

import java.util.List;

import com.easybus.model.ReviewRequest;
import com.easybus.model.ReviewResponse;

public interface ReviewService {
    ReviewResponse addReview(ReviewRequest request);
    List<ReviewResponse> getReviewsByBus(Long busId);
}
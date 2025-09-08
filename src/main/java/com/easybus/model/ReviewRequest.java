package com.easybus.model;


import lombok.Data;

@Data
public class ReviewRequest {
    private Long userId;
    private Long busId;
    private int rating;
    private String feedback;
}


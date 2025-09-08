package com.easybus.model;


import lombok.Data;

@Data
public class ReviewResponse {
    private Long id;
    private Long userId;
    private Long busId;
    private int rating;
    private String feedback;
    private String message;
}

package com.easybus.model;

import java.util.List;

import lombok.Data;

@Data
public class PagedResponse<T> {
    private List<T> content;
    private int totalPages;
    private long totalElements;
    private int numberOfElements;

    public PagedResponse(List<T> content, int totalPages, long totalElements, int numberOfElements) {
        this.content = content;
        this.totalPages = totalPages;
        this.totalElements = totalElements;
        this.numberOfElements = numberOfElements;
    }

    // getters and setters
}

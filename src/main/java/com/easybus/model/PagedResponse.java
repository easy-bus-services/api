package com.easybus.model;

import java.util.List;

import com.easybus.entity.User;

import lombok.Data;

@Data
public class PagedResponse<T> {
    private List<T> content;
    private int totalPages;
    private long totalElements;
    private int numberOfElements;
	private int statusCode;
	private String status;
	private String message;
    private PagedResponse<User> list;

    public PagedResponse(List<T> content, int totalPages, long totalElements, int numberOfElements) {
        this.content = content;
        this.totalPages = totalPages;
        this.totalElements = totalElements;
        this.numberOfElements = numberOfElements;
    }

	  public PagedResponse(int statusCode, String status, String message, List<T> list) {
			
		}

	  public PagedResponse(int i, String success, String string, PagedResponse<User> users) {
		  super();
			this.statusCode = statusCode;
			this.status = status;
			this.message = message;
			this.list = users;
	  }
  
	
    // getters and setters
}

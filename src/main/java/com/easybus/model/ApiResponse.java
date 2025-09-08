package com.easybus.model;



import java.util.List;

import lombok.Data;

@Data

public class ApiResponse<T> {
    private String status;   // "success" | "error"
    private String message;  // custom message
    private T data;         // actual response data
    private int statusCode;
    private List<?> list;
    public ApiResponse( String status, String message, List<?> data) {
		super();
		
		this.status = status;
		this.message = message;
		this.list = data;
	}
    public ApiResponse( String status, String message, T data) {
		super();
		
		this.status = status;
		this.message = message;
		this.data = data;
	}

	public ApiResponse(int statusCode, String status, String message, T data) {
		super();
		this.statusCode = statusCode;
		this.status = status;
		this.message = message;
		this.data = data;
	}
}

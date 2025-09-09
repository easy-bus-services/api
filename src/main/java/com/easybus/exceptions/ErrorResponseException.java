package com.easybus.exceptions;

public class ErrorResponseException extends RuntimeException { 
	private static final long serialVersionUID = 1L;

	public ErrorResponseException(String message) {
        super(message);
    }
}

package com.easybus.model;



import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApiResponse<T> {
    private String status;   // "success" | "error"
    private String message;  // custom message
    private T data;          // actual response data
}

package com.ncu.hospital.doctors.dto;

import org.springframework.http.ResponseEntity;

public class ApiResponse<T> {
    private String status;
    private String message;
    private T data;

    public ApiResponse() {
    }

    public ApiResponse(String status, String message, T data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }

    public static <T> ResponseEntity<ApiResponse<T>> ok(T data, String message) {
        ApiResponse<T> response = new ApiResponse<>("SUCCESS", message, data);
        return ResponseEntity.ok(response);
    }

    public static <T> ApiResponse<T> error(String message) {
        return new ApiResponse<>("ERROR", message, null);
    }

    // Getters and Setters
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
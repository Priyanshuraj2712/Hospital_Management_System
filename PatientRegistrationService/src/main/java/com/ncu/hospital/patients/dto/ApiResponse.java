package com.ncu.hospital.patients.dto;

import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

public class ApiResponse<T> {
    private T data;
    private String message;
    private String status;
    private int statusCode;

    public ApiResponse() {
    }

    public ApiResponse(T data, String message, String status, int statusCode) {
        this.data = data;
        this.message = message;
        this.status = status;
        this.statusCode = statusCode;
    }

    // Success response with data
    public static <T> ResponseEntity<ApiResponse<T>> ok(T data, String message) {
        ApiResponse<T> response = new ApiResponse<>(data, message, "SUCCESS", HttpStatus.OK.value());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // Error response
    public static <T> ResponseEntity<ApiResponse<T>> error(String message, HttpStatus status) {
        ApiResponse<T> response = new ApiResponse<>(null, message, "ERROR", status.value());
        return new ResponseEntity<>(response, status);
    }

    // Bad request response
    public static <T> ResponseEntity<ApiResponse<T>> badRequest(String message) {
        return error(message, HttpStatus.BAD_REQUEST);
    }

    // Not found response
    public static <T> ResponseEntity<ApiResponse<T>> notFound(String message) {
        return error(message, HttpStatus.NOT_FOUND);
    }

    // Server error response
    public static <T> ResponseEntity<ApiResponse<T>> serverError(String message) {
        return error(message, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // Getters and Setters
    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }
}
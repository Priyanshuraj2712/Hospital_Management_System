package com.ncu.hospital.doctors.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import com.ncu.hospital.doctors.dto.ApiResponse;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponse<Object>> handleResourceNotFoundException(
            ResourceNotFoundException ex, WebRequest request) {
        return new ResponseEntity<>(
            ApiResponse.error(ex.getMessage()),
            HttpStatus.NOT_FOUND
        );
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ApiResponse<Object>> handleBadRequestException(
            BadRequestException ex, WebRequest request) {
        return new ResponseEntity<>(
            ApiResponse.error(ex.getMessage()),
            HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(DatabaseException.class)
    public ResponseEntity<ApiResponse<Object>> handleDatabaseException(
            DatabaseException ex, WebRequest request) {
        return new ResponseEntity<>(
            ApiResponse.error("Database operation failed: " + ex.getMessage()),
            HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Object>> handleGlobalException(
            Exception ex, WebRequest request) {
        return new ResponseEntity<>(
            ApiResponse.error("An unexpected error occurred: " + ex.getMessage()),
            HttpStatus.INTERNAL_SERVER_ERROR
        );
    }
}
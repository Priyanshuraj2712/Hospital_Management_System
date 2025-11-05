package com.ncu.hospital.patients.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import com.ncu.hospital.patients.dto.ApiResponse;

@ControllerAdvice
public class GlobalExceptionHandler {
    
    @ExceptionHandler(PatientServiceException.class)
    public ResponseEntity<ApiResponse<Object>> handlePatientServiceException(PatientServiceException ex) {
        return ApiResponse.error(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
    
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponse<Object>> handleResourceNotFoundException(ResourceNotFoundException ex) {
        return ApiResponse.error(ex.getMessage(), HttpStatus.NOT_FOUND);
    }
    
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ApiResponse<Object>> handleBadRequestException(BadRequestException ex) {
        return ApiResponse.error(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }
    
    @ExceptionHandler(DatabaseException.class)
    public ResponseEntity<ApiResponse<Object>> handleDatabaseException(DatabaseException ex) {
        return ApiResponse.error(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
    
    @ExceptionHandler(DuplicateResourceException.class)
    public ResponseEntity<ApiResponse<Object>> handleDuplicateResourceException(DuplicateResourceException ex) {
        return ApiResponse.error(ex.getMessage(), HttpStatus.CONFLICT);
    }
    
    @ExceptionHandler(ServiceUnavailableException.class)
    public ResponseEntity<ApiResponse<Object>> handleServiceUnavailableException(ServiceUnavailableException ex) {
        return ApiResponse.error(ex.getMessage(), HttpStatus.SERVICE_UNAVAILABLE);
    }
    
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Object>> handleGenericException(Exception ex) {
        return ApiResponse.error("An unexpected error occurred: " + ex.getMessage(), 
            HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
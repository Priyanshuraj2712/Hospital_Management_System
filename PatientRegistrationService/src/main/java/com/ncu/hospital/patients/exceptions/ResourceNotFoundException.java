package com.ncu.hospital.patients.exceptions;

public class ResourceNotFoundException extends PatientServiceException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
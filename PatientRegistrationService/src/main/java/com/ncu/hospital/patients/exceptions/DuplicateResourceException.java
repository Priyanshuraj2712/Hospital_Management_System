package com.ncu.hospital.patients.exceptions;

public class DuplicateResourceException extends PatientServiceException {
    public DuplicateResourceException(String message) {
        super(message);
    }
}
package com.ncu.hospital.patients.exceptions;

public class PatientServiceException extends RuntimeException {
    public PatientServiceException(String message) {
        super(message);
    }
}
package com.ncu.hospital.patients.exceptions;

public class BadRequestException extends PatientServiceException {
    public BadRequestException(String message) {
        super(message);
    }
}
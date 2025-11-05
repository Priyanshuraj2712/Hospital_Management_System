package com.ncu.hospital.patients.exceptions;

public class ServiceUnavailableException extends PatientServiceException {
    public ServiceUnavailableException(String message) {
        super(message);
    }
}
package com.ncu.hospital.patients.exceptions;

public class DatabaseException extends PatientServiceException {
    public DatabaseException(String message) {
        super(message);
    }
}
package com.ncu.hospital.doctors.exceptions;

public class DoctorValidationException extends RuntimeException {
    public DoctorValidationException(String message) {
        super(message);
    }
}
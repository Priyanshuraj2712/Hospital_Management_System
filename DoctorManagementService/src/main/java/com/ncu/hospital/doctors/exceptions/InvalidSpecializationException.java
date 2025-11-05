package com.ncu.hospital.doctors.exceptions;

public class InvalidSpecializationException extends RuntimeException {
    public InvalidSpecializationException(String message) {
        super(message);
    }
}
package com.ncu.hospital.doctors.exceptions;

public class DuplicateDoctorException extends RuntimeException {
    public DuplicateDoctorException(String message) {
        super(message);
    }
}
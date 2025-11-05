package com.ncu.hospital.appointments.exceptions;

public class BadRequestException extends AppointmentException {
    public BadRequestException(String message) {
        super(message);
    }
}
package com.ncu.hospital.appointments.exceptions;

public class UnauthorizedException extends AppointmentException {
    public UnauthorizedException(String message) {
        super(message);
    }
}

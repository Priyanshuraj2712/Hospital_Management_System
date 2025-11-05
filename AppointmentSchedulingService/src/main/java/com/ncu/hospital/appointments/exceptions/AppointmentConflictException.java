package com.ncu.hospital.appointments.exceptions;

public class AppointmentConflictException extends AppointmentException {
    public AppointmentConflictException(String message) {
        super(message);
    }
}
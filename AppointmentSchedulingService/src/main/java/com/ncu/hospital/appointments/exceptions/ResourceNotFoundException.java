package com.ncu.hospital.appointments.exceptions;

public class ResourceNotFoundException extends AppointmentException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
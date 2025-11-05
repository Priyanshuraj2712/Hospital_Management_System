package com.ncu.hospital.receptions.exceptions;

public class BadRequestException extends ReceptionException {
    public BadRequestException(String message) {
        super(message);
    }
}

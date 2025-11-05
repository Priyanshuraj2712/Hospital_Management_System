package com.ncu.hospital.receptions.exceptions;

public class UnauthorizedException extends ReceptionException {
    public UnauthorizedException(String message) {
        super(message);
    }
}

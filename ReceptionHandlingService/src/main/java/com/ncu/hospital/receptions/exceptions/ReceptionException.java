package com.ncu.hospital.receptions.exceptions;

public class ReceptionException extends RuntimeException {
    public ReceptionException(String message) {
        super(message);
    }

    public ReceptionException(String message, Throwable cause) {
        super(message, cause);
    }
}

package com.ncu.hospital.billings.exceptions;

public class BadRequestException extends BillingException {
    public BadRequestException(String message) {
        super(message);
    }
}

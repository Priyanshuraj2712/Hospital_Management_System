package com.ncu.hospital.billings.exceptions;

public class UnauthorizedException extends BillingException {
    public UnauthorizedException(String message) {
        super(message);
    }
}

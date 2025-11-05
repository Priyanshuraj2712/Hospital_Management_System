package com.ncu.hospital.billings.exceptions;

public class ResourceNotFoundException extends BillingException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}

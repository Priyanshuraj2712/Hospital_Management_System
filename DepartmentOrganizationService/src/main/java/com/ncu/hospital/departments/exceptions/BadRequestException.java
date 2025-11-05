package com.ncu.hospital.departments.exceptions;

public class BadRequestException extends DepartmentException {
    public BadRequestException(String message) {
        super(message);
    }
}
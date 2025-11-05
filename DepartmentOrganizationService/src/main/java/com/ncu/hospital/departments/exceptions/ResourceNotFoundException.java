package com.ncu.hospital.departments.exceptions;

public class ResourceNotFoundException extends DepartmentException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
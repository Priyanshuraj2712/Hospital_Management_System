package com.ncu.hospital.doctors.utils;

import com.ncu.hospital.doctors.exceptions.DoctorValidationException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

public class DoctorValidationUtils {
    private static final Set<String> VALID_SPECIALIZATIONS = new HashSet<>(Arrays.asList(
        "CARDIOLOGY", "NEUROLOGY", "ORTHOPEDICS", "PEDIATRICS", "DERMATOLOGY",
        "ONCOLOGY", "GYNECOLOGY", "UROLOGY", "PSYCHIATRY", "GENERAL"
    ));

    private static final Pattern EMAIL_PATTERN = Pattern.compile(
        "^[A-Za-z0-9+_.-]+@(.+)$"
    );

    private static final Pattern PHONE_PATTERN = Pattern.compile(
        "^[0-9]{10}$"
    );

    public static void validateSpecialization(String specialization) {
        if (specialization == null || !VALID_SPECIALIZATIONS.contains(specialization.toUpperCase())) {
            throw new DoctorValidationException("Invalid specialization: " + specialization);
        }
    }

    public static void validateEmail(String email) {
        if (email == null || !EMAIL_PATTERN.matcher(email).matches()) {
            throw new DoctorValidationException("Invalid email format");
        }
    }

    public static void validatePhone(String phone) {
        if (phone == null || !PHONE_PATTERN.matcher(phone).matches()) {
            throw new DoctorValidationException("Invalid phone number format");
        }
    }

    public static void validateName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new DoctorValidationException("Doctor name cannot be empty");
        }
        if (!name.matches("^[a-zA-Z\\s.]{2,50}$")) {
            throw new DoctorValidationException("Invalid doctor name format");
        }
    }
}
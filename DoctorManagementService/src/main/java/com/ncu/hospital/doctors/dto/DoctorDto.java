package com.ncu.hospital.doctors.dto;
public class DoctorDto {
    private String name;
    private String specialty;
    private String phoneNumber;
    private String email;

    public DoctorDto() {}
    public DoctorDto(String name, String specialty, String phoneNumber, String email) {
        this.name = name;
        this.specialty = specialty;
        this.phoneNumber = phoneNumber;
        this.email = email;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getSpecialization() {
        return specialty;
    }
    public void setSpecialization(String specialty) {
        this.specialty = specialty;
    }
    public String getPhoneNumber() {
        return phoneNumber;
    }
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
}

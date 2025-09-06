package com.ncu.hospital.doctors.model;

public class Doctor {
    private int id;
    private String name;
    private String specialty;
    private String phoneNumber;
    private String email;

    public Doctor() {}
    public Doctor(int id, String name, String specialty, String phoneNumber, String email) {
        this.id = id;
        this.name = name;
        this.specialty = specialty;
        this.phoneNumber = phoneNumber;
        this.email = email;
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
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

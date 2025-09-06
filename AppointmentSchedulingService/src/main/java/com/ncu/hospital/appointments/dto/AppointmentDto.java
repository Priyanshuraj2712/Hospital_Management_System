package com.ncu.hospital.appointments.dto;

public class AppointmentDto {
    private int id;
    private int patientId;
    private int doctorId;
    private String appointmentDate;
    private String appointmentTime;

    public AppointmentDto() {}

    public AppointmentDto(int id, int patientId, int doctorId, String appointmentDate, String appointmentTime) {
        this.id = id;
        this.patientId = patientId;
        this.doctorId = doctorId;
        this.appointmentDate = appointmentDate;
        this.appointmentTime = appointmentTime;
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    
    public int getPatientId() {
        return patientId;
    }   
    public void setPatientId(int patientId) {
        this.patientId = patientId;
    }
    public int getDoctorId() {
        return doctorId;
    }
    public void setDoctorId(int doctorId) {
        this.doctorId = doctorId;
    }
    public String getAppointmentDate() {
        return appointmentDate;
    }
    public void setAppointmentDate(String appointmentDate) {
        this.appointmentDate = appointmentDate;
    }
    public String getAppointmentTime() {
        return appointmentTime;
    }
    public void setAppointmentTime(String appointmentTime) {
        this.appointmentTime = appointmentTime;
    }
}

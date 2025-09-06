package com.ncu.hospital.receptions.dto;

public class ReceptionDto {
    private int patientId;
    private String checkInTime;
    private String checkOutTime;
    private String status;

    public ReceptionDto() {}

    public ReceptionDto(int patientId, String checkInTime, String checkOutTime, String status) {
        this.patientId = patientId;
        this.checkInTime = checkInTime;
        this.checkOutTime = checkOutTime;
        this.status = status;
    }

    // Getters and Setters
    public int getPatientId() { 
        return patientId; 
    }
    public void setPatientId(int patientId) { 
        this.patientId = patientId; 
    }

    public String getCheckInTime() { 
        return checkInTime; 
    }
    public void setCheckInTime(String checkInTime) { 
        this.checkInTime = checkInTime; 
    }

    public String getCheckOutTime() { 
        return checkOutTime; 
    }
    public void setCheckOutTime(String checkOutTime) { 
        this.checkOutTime = checkOutTime; 
    }

    public String getStatus() { 
        return status; 
    }
    public void setStatus(String status) { 
        this.status = status; 
    }
}
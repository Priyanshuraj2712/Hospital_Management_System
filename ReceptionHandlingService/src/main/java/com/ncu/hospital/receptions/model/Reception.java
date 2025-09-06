package com.ncu.hospital.receptions.model;

public class Reception {
    private int id;
    private int patientId;
    private String checkInTime;
    private String checkOutTime;
    private String status;

    public Reception() {}

    public Reception(int id, int patientId, String checkInTime, String checkOutTime, String status) {
        this.id = id;
        this.patientId = patientId;
        this.checkInTime = checkInTime;
        this.checkOutTime = checkOutTime;
        this.status = status;
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
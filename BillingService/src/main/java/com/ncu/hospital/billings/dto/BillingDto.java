package com.ncu.hospital.billings.dto;

public class BillingDto {
    private int patientId;
    private double amount;
    private String billingDate;
    private String status;
    private String description;

    public BillingDto() {}

    public BillingDto(int patientId, double amount, String billingDate, String status, String description) {
        this.patientId = patientId;
        this.amount = amount;
        this.billingDate = billingDate;
        this.status = status;
        this.description = description;
    }

    public int getPatientId() {
        return patientId;
    }
    public void setPatientId(int patientId) { 
        this.patientId = patientId; 
    }

    public double getAmount() { 
        return amount; 
    }
    public void setAmount(double amount) { 
        this.amount = amount; 
    }

    public String getBillingDate() { 
        return billingDate; 
    }
    public void setBillingDate(String billingDate) { 
        this.billingDate = billingDate; 
    }

    public String getStatus() { 
        return status; 
    }
    public void setStatus(String status) { 
        this.status = status; 
    }

    public String getDescription() { 
        return description; 
    }
    public void setDescription(String description) { 
        this.description = description; 
    }
}

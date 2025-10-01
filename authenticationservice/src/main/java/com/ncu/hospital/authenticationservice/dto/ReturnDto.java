package com.ncu.hospital.authenticationservice.dto;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ReturnDto {
    @JsonProperty("status")
    private String Status;
    @JsonProperty("email")
    private String Email;

    public ReturnDto() {}
    public ReturnDto(String status, String email) {
        Status = status;
        Email = email;
    }
    public String getStatus() {
        return Status;
    }
    public void setStatus(String status) {
        Status = status;
    }
    public String getEmail() {
        return Email;
    }
    public void setEmail(String email) {
        Email = email;
    }

}

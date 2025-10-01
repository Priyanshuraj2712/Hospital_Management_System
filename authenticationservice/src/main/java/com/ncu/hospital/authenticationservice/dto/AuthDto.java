package com.ncu.hospital.authenticationservice.dto;
import com.fasterxml.jackson.annotation.JsonProperty;
public class AuthDto {
    @JsonProperty("email")
    private String Email;   
    @JsonProperty("password")
    private String Password;

    public AuthDto() {}

    public AuthDto(String email, String password) {
        Email = email;
        Password = password;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }
}

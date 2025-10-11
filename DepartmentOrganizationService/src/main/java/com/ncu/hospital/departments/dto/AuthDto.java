package com.ncu.hospital.departments.dto;
import com.fasterxml.jackson.annotation.JsonProperty;
public class AuthDto {
    @JsonProperty("email")
    private String Email;

    @JsonProperty("password")
    private String password;

    public AuthDto() {}

    public AuthDto(String email, String password) {
        this.Email = email;
        this.password = password;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

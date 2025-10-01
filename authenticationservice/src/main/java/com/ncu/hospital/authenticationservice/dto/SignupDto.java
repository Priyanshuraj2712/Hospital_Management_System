package com.ncu.hospital.authenticationservice.dto;
import com.fasterxml.jackson.annotation.JsonProperty;

public class SignupDto {
    @JsonProperty("email")
    private String Email;

    @JsonProperty("password")
    private String Password;

    @JsonProperty("namee")
    private String Name;

    public SignupDto() {}

    public SignupDto(String email, String password, String name) {
        Email = email;
        Password = password;
        Name = name;
    }

    public String getEmail() {
        return Email;
    }   
    public void setEmail(String email) {
        Email  = email;
    }
    public String getPassword() {
        return Password;
    }   
    public void setPassword(String password) {
        Password = password;
    }
    public String getName() {
        return Name;
    }   
    public void setName(String name) {
        Name = name;
    }
}

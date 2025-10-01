package com.ncu.hospital.authenticationservice.model;
public class User {
    String Name;
    String Email;
    String Password;

    public User() {}

    public User(String name, String email, String password) {
        Name = name;
        Email = email;
        Password = password;
    }
    public String getName() {
        return Name;
    }
    public void setName(String name) {
        Name = name;
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

package com.ncu.hospital.authenticationservice.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import com.ncu.hospital.authenticationservice.dto.SignupDto;
import com.ncu.hospital.authenticationservice.dto.AuthDto;

@Repository(value = "AuthRepository")
public class AuthRepository {

    JdbcTemplate _jdbcTemplate;

    @Autowired
    public AuthRepository(JdbcTemplate jdbcTemplate) {
        _jdbcTemplate = jdbcTemplate;
    }

    public boolean SignUp(SignupDto cred, StringBuffer error){
        try{
            String query = "INSERT INTO users (u_name, u_email, u_password) VALUES (?, ?, ?)";
            _jdbcTemplate.update(query, cred.getName(), cred.getEmail(), cred.getPassword());
        }catch (Exception e){
            error.append(e.getMessage());
            System.out.println("Error during signup: "+e.getMessage());
            return false;
        }
        return true;
    }

    public boolean getPasswordFromEmail(String email, StringBuffer password, StringBuffer error){
        try{
            String query = "SELECT u_password FROM users WHERE u_email = ?";
            password.append(_jdbcTemplate.queryForObject(query, new Object[]{email}, String.class));
        }catch (Exception e){
            error.append(e.getMessage());
            System.out.println("Error during fetching password: "+e.getMessage());
            return false;
        }
        return true;
    }
}

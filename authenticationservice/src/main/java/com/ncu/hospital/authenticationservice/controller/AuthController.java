package com.ncu.hospital.authenticationservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ncu.hospital.authenticationservice.service.AuthService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import com.ncu.hospital.authenticationservice.dto.AuthDto;
import com.ncu.hospital.authenticationservice.dto.ReturnDto;
import com.ncu.hospital.authenticationservice.dto.SignupDto;

@RequestMapping("/auth")
@RestController
public class AuthController {
    AuthService authService;

    @Autowired
    AuthController(AuthService authService){
        this.authService = authService;
    }

    @PostMapping("/signup")
    public ResponseEntity<ReturnDto> SignUp(@RequestBody SignupDto cred){
        ReturnDto response = new ReturnDto();
        boolean isSuccess = authService.SignUp(cred, response);
        if(isSuccess){
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @PostMapping("/authenticate")
    public ResponseEntity<String> Authenticate(@RequestBody AuthDto cred){
       boolean isAuthenticated = authService.Authenticate(cred);
       if(isAuthenticated){
           return ResponseEntity.ok("Authentication successful");
       }
       return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Authentication failed");
    }
}
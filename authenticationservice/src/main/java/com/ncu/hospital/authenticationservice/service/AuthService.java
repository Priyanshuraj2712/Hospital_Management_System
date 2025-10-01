package com.ncu.hospital.authenticationservice.service;
import org.springframework.stereotype.Service;
import com.ncu.hospital.authenticationservice.repository.AuthRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.ncu.hospital.authenticationservice.dto.AuthDto;
import com.ncu.hospital.authenticationservice.dto.ReturnDto;
import com.ncu.hospital.authenticationservice.dto.SignupDto;

@Service
public class AuthService {
    private final AuthRepository authRepository;
    private final PasswordEncoder passwordEncoder;
    
    @Autowired
    public AuthService(AuthRepository authRepository, PasswordEncoder passwordEncoder) {
        this.authRepository = authRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public boolean SignUp(SignupDto cred, ReturnDto response){
        cred.setPassword(passwordEncoder.encode(cred.getPassword()));
        StringBuffer status = new StringBuffer();
        response.setEmail(cred.getEmail());
        boolean isSuccess = authRepository.SignUp(cred, status);
        if(isSuccess){
            response.setStatus("Signup successful");
            return true;
        } else {
            response.setStatus("Signup failed: " + status.toString());
            return false;
        }
        
        // return isSuccess;
    }

    public boolean Authenticate(AuthDto cred){
        String email = cred.getEmail();
        String password = cred.getPassword();
        StringBuffer status = new StringBuffer();
        StringBuffer passwordFromDB = new StringBuffer();
        Boolean isSuccess = authRepository.getPasswordFromEmail(email, passwordFromDB, status);
        if(isSuccess && passwordEncoder.matches(password, passwordFromDB.toString())){
            return true;
        }
        return false;
    }

}


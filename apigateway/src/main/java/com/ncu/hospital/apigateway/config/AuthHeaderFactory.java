package com.ncu.hospital.apigateway.config;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class AuthHeaderFactory {

    @Autowired
    private ServiceCredentialsRepository credentialsRepository;

    public AuthHeaderFactory(ServiceCredentialsRepository credentialsRepository) {
        this.credentialsRepository = credentialsRepository;
    }

    @Value("${api.gateway.shared.secret}")
    String sharedSecret;

    public String BuildAuthHeader(String serviceName) {
        ServiceCredentials creds = credentialsRepository.getCredentialsByServiceName(serviceName);
        String auth = creds.getUsername() + ":" + creds.getPassword();
        return "Basic " + Base64.getEncoder().encodeToString(auth.getBytes(StandardCharsets.UTF_8));
    }

    String getSharedSecret() {
        return sharedSecret;
    }
}
package com.ncu.hospital.doctors.config;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.reactive.function.client.WebClient;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import reactor.core.publisher.Mono;

import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.ncu.hospital.doctors.dto.AuthDto; 

import io.micrometer.common.lang.NonNull;
@Component
public class DoctorManagementServiceFilter extends OncePerRequestFilter {

    private final WebClient.Builder webClientBuilder;
    @Value("${apigateway.shared.secret}")
    String sharedsecret;
    @Autowired
    public DoctorManagementServiceFilter(WebClient.Builder webClientBuilder) {
        this.webClientBuilder = webClientBuilder;
    }


    @Override
    protected void doFilterInternal(
        @NonNull HttpServletRequest request,
        @NonNull HttpServletResponse response,
        @NonNull FilterChain filterChain)
        throws ServletException, IOException {
        String username = request.getHeader("X-USERNAME");
        String password = request.getHeader("X-PASSWORD");

        if (username == null || password == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        AuthDto authDto = new AuthDto(username, password);

        WebClient client = webClientBuilder.build();
        Mono<ResponseEntity<Void>> authResponse = client.post()
                .uri("lb://authenticationservice/auth/authenticate") 
                .bodyValue(authDto)
                .retrieve()
                .toBodilessEntity();
        String secret = request.getHeader("X-API-GATEWAY-SECRET");
        if(sharedsecret == null || !sharedsecret.equals(secret)) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }
        ResponseEntity<Void> result = authResponse.block();

        if (result != null && result.getStatusCode().is2xxSuccessful()) {
            filterChain.doFilter(request, response);
        } else {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }
    }
}

package com.ncu.hospital.patients.config;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.reactive.function.client.WebClient;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import reactor.core.publisher.Mono;
import com.ncu.hospital.patients.dto.AuthDto;

@Component
public class PatientRegistrationServiceFilter extends OncePerRequestFilter {

    private final WebClient.Builder webClientBuilder;

    @Autowired
    public PatientRegistrationServiceFilter(WebClient.Builder webClientBuilder) {
        this.webClientBuilder = webClientBuilder;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
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

        ResponseEntity<Void> result = authResponse.block();

        if (result != null && result.getStatusCode().is2xxSuccessful()) {
            filterChain.doFilter(request, response);
        } else {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }
    }
}
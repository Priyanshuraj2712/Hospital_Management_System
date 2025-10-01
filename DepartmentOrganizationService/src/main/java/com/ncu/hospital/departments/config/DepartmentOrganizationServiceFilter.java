package com.ncu.hospital.departments.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.filter.OncePerRequestFilter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

import io.micrometer.common.lang.NonNull;

public class DepartmentOrganizationServiceFilter extends OncePerRequestFilter {

    @Value("${apigateway.shared.secret}")
    String sharedSecret;

    @Override
    protected void doFilterInternal(
        @NonNull HttpServletRequest request,
        @NonNull HttpServletResponse response,
        @NonNull FilterChain filterChain)
        throws ServletException, IOException {
        final String secret = request.getHeader("X-API-GATEWAY-SECRET");
        if(!sharedSecret.equals(secret)){
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }
        filterChain.doFilter(request, response);
    }
}

package com.ncu.hospital.apigateway.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Bean;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;

@Configuration
public class GatewayConfig {
    @Bean
    public RouteLocator routes(RouteLocatorBuilder builder) {
        return builder.routes()
        .route("patientregistrationservice", r->r.path("/patients/**")
                .uri("lb://patientregistrationservice"))
        .route("doctormanagementservice", r->r.path("/doctors/**")
                .uri("lb://doctormanagementservice"))
        .route("departmentorganizationservice", r->r.path("/departments/**")
                .uri("lb://departmentorganizationservice"))
        .route("appointmentschedulingservice", r->r.path("/appointments/**")
                .uri("lb://appointmentschedulingservice"))
        .route("billingservice", r->r.path("/billings/**")
                .uri("lb://billingservice"))
        .route("receptionhandlingservice", r->r.path("/reception/**")
                .uri("lb://receptionhandlingservice"))
        .build();
    }
}

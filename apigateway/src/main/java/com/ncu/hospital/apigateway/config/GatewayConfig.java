package com.ncu.hospital.apigateway.config;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Bean;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
@EnableWebFluxSecurity
public class GatewayConfig {
        GatewayConfig() {

        }
        @Bean
        public SecurityWebFilterChain  springSecurityFilterChain(ServerHttpSecurity http) {
                http
                .csrf(csrf -> csrf.disable())
                .authorizeExchange(exchange -> exchange
                        .pathMatchers("/auth/**").permitAll()
                        .anyExchange().permitAll()
                )
               .httpBasic(ServerHttpSecurity.HttpBasicSpec::disable)
               .formLogin(ServerHttpSecurity.FormLoginSpec::disable);
        return http.build();
        }

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
                .route("authservice", r->r.path("/auth/**")
                        .uri("lb://authservice"))
                .build();
        }

      @Bean
      @LoadBalanced
        public WebClient.Builder webClientBUilder() {
          return WebClient.builder();
        }
}

package com.ncu.hospital.apigateway.config;

import org.springframework.stereotype.Component;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain; 
import org.springframework.core.Ordered;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import com.ncu.hospital.apigateway.dto.AuthDto;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.cloud.gateway.route.Route;


@Component
public class AuthGlobalFilter implements GlobalFilter, Ordered {
    private final AuthHeaderFactory AuthFactory;
    private final WebClient.Builder webClientBUilder;

    @Autowired
    public AuthGlobalFilter(AuthHeaderFactory authFactory, WebClient.Builder webClientBuilder) {
        this.AuthFactory = authFactory;
        this.webClientBUilder = webClientBuilder;
    }
    
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String path = exchange.getRequest().getPath().toString();
        if (path.startsWith("/auth/")) {
            return chain.filter(exchange);
        }

        String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        if(authHeader == null || !authHeader.startsWith("Basic ")) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        String base64Credentials = authHeader.substring("Basic ".length()).trim();
        byte[] decodedByte = Base64.getDecoder().decode(base64Credentials);
        String decodedString = new String(decodedByte, StandardCharsets.UTF_8);

        String[] parts = decodedString.split(":", 2);
        String username = parts[0];
        String password = parts.length > 1 ? parts[1] : "";

        AuthDto request = new AuthDto(username, password);
        WebClient client = webClientBUilder.build();

        Mono<ResponseEntity<Void>> responseMono = client.post()
            .uri("lb://authservice/auth/authenticate")
            .bodyValue(request)
            .retrieve()
            .toBodilessEntity();

        return responseMono.flatMap(response -> {
            if (response.getStatusCode().is2xxSuccessful()) {
                Route route = exchange.getAttribute(ServerWebExchangeUtils.GATEWAY_ROUTE_ATTR);
                String routeId = (route != null) ? route.getId() : "default";

                ServerHttpRequest mutatedRequest = exchange.getRequest().mutate()
                    .header(HttpHeaders.AUTHORIZATION, AuthFactory.BuildAuthHeader(routeId))
                    .header("X-API-GATEWAY-SECRET", AuthFactory.getSharedSecret())
                    .build();
                return chain.filter(exchange.mutate().request(mutatedRequest).build());
                
            } else {
                return unauthorized(exchange);
            }
        })
            .onErrorResume(e -> unauthorized(exchange));
        }
        private Mono<Void> unauthorized(ServerWebExchange exchange) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
    }
    @Override
    public int getOrder() {
        return -1; // Set the order of the filter
    }
}

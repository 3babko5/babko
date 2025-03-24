package com.business.gateway.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.business.gateway.filter.JwtFilter;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class RouterConfig {

    private final JwtFilter jwtFilter;

    @Bean
    public RouteLocator routeLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                // Auth 서비스로 라우팅
                .route("auth-service", r -> r.path("/api/v1/auth/**")
                        .uri("lb://auth-service"))
                
                // User 서비스로 라우팅 (JWT 필터 적용)
                .route("user-service", r -> r.path("/api/v1/users/**")
                        .filters(f -> f.filter(jwtFilter.apply(new JwtFilter.Config())))
                        .uri("lb://user-service"))
                
                // Product 서비스로 라우팅 (JWT 필터 적용)
                .route("product-service", r -> r.path("/api/v1/products/**")
                        .filters(f -> f.filter(jwtFilter.apply(new JwtFilter.Config())))
                        .uri("lb://product-service"))
                
                // Order 서비스로 라우팅 (JWT 필터 적용)
                .route("order-service", r -> r.path("/api/v1/orders/**")
                        .filters(f -> f.filter(jwtFilter.apply(new JwtFilter.Config())))
                        .uri("lb://order-service"))

                // Hub 서비스로 라우팅 (JWT 필터 적용)
                .route("hub-service", r -> r.path("/api/v1/hubs/**")
                        .filters(f -> f.filter(jwtFilter.apply(new JwtFilter.Config())))
                        .uri("lb://hub-service"))

                // Delivery 서비스로 라우팅 (JWT 필터 적용)
                .route("delivery-service", r -> r.path("/api/v1/deliveries/**")
                        .filters(f -> f.filter(jwtFilter.apply(new JwtFilter.Config())))
                        .uri("lb://delivery-service"))

                .build();
    }
} 
package com.business.gateway.filter;

import java.util.List;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import com.business.gateway.jwt.JwtUtil;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class JwtFilter extends AbstractGatewayFilterFactory<JwtFilter.Config> {

    private final JwtUtil jwtUtil;
    
    private final List<String> excludedUrls = List.of(
            "/api/v1/auth/signup",
            "/api/v1/auth/login",
            "/api/v1/auth/refresh"
    );

    public JwtFilter() {
        super(Config.class);
        this.jwtUtil = null;
    }

    @Override
    public GatewayFilter apply(Config config) {
        return ((exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();
            
            // 인증이 필요 없는 URL인 경우 바로 통과
            if (isExcludedUrl(request.getPath().toString())) {
                return chain.filter(exchange);
            }
            
            // Authorization 헤더 확인
            if (!request.getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
                return onError(exchange, "Authorization 헤더가 없습니다.", HttpStatus.UNAUTHORIZED);
            }
            
            String authorizationHeader = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
            String token = authorizationHeader.replace("Bearer ", "");
            
            // 토큰 유효성 검사
            if (!jwtUtil.validateToken(token)) {
                return onError(exchange, "유효하지 않은 토큰입니다.", HttpStatus.UNAUTHORIZED);
            }
            
            // userId와 role 추출하여 헤더에 추가
            Long userId = jwtUtil.getUserId(token);
            String role = jwtUtil.getRole(token);
            
            // 헤더 추가
            ServerHttpRequest modifiedRequest = exchange.getRequest().mutate()
                    .header("X-client-userId", String.valueOf(userId))
                    .header("X-client-role", role)
                    .build();
            
            return chain.filter(exchange.mutate().request(modifiedRequest).build());
        });
    }

    private boolean isExcludedUrl(String url) {
        return excludedUrls.stream().anyMatch(url::contains);
    }

    private Mono<Void> onError(ServerWebExchange exchange, String errorMessage, HttpStatus httpStatus) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(httpStatus);
        return response.setComplete();
    }

    public static class Config {
        // 필요한 설정 정보가 있으면 여기에 추가
    }
} 
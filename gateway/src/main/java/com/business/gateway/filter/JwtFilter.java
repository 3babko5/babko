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
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtFilter extends AbstractGatewayFilterFactory<JwtFilter.Config> {

    private final JwtUtil jwtUtil;

    private final List<String> excludedUrls = List.of(
        "/api/v1/auth/signup",
        "/api/v1/auth/login",
        "/api/v1/auth/refresh"
    );

    // public JwtFilter() {
    //     super(Config.class);
    //     this.jwtUtil = null;
    // }

    @Override
    public GatewayFilter apply(Config config) {
        return ((exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();
            String path = request.getPath().toString();

            // 인증이 필요 없는 URL이면 필터 패스
            if (isExcludedUrl(path)) {
                log.debug("[JwtFilter] Excluded URL: {}", path);
                return chain.filter(exchange);
            }

            // Authorization 헤더 확인
            String authHeader = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return onError(exchange, "Authorization 헤더가 없습니다.", HttpStatus.UNAUTHORIZED);
            }

            String token = authHeader.replace("Bearer ", "");

            // JWT 검증
            if (!jwtUtil.validateToken(token)) {
                return onError(exchange, "유효하지 않은 토큰입니다.", HttpStatus.UNAUTHORIZED);
            }

            // userId, role 추출 (userId는 subject에서)
            Long userId = jwtUtil.getUserId(token);
            String role = jwtUtil.getRole(token);

            // 로그 출력
            log.info("[JwtFilter] userId: {}, role: {}", userId, role);

            // 헤더에 사용자 정보 추가
            ServerHttpRequest modifiedRequest = request.mutate()
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
        log.warn("[JwtFilter] Error: {}", errorMessage);
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(httpStatus);
        return response.setComplete();
    }

    public static class Config {
        // 설정이 필요하면 여기에 추가
    }
}

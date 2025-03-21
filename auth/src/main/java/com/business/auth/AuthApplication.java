package com.business.auth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * Auth 서비스의 메인 애플리케이션 클래스
 * - Spring Boot 애플리케이션 시작점
 * - Feign 클라이언트 활성화
 */
@SpringBootApplication(scanBasePackages = {"com.business.common", "com.business.auth"})
@EnableFeignClients(basePackages = "com.business.auth.infrastructure.client")
public class AuthApplication {
    public static void main(String[] args) {
        SpringApplication.run(AuthApplication.class, args);
    }
}

package com.business.auth.infrastructure.dto.response;

import lombok.Builder;
import lombok.Getter;
// User 서비스 → Auth 서비스로 유저 정보를 응답할 때 사용되는 DTO
@Getter
@Builder
public class UserResponseDto {
    private Long userId;
    private String username;
    private String password;
    private String email;
    private String slackId;
    private String role;
} 
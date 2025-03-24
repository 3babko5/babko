package com.business.auth.infrastructure.dto.request;

import lombok.Builder;
import lombok.Getter;
// Auth 서비스 → User 서비스로 회원가입 요청을 보낼 때 사용하는 DTO
@Getter
@Builder
public class CreateUserRequest {
    private String username;
    private String password;
    private String email;
    private String slackId;
    private String role;
} 
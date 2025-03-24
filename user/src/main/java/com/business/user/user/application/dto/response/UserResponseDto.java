package com.business.user.user.application.dto.response;

import com.business.user.user.domain.entity.User;

import lombok.Builder;
import lombok.Getter;
// 유저 정보를 응답할 때 사용하는 Response DTO
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
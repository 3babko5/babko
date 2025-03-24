package com.business.auth.infrastructure.client.dto;

import lombok.Getter;

@Getter
public class UserResponse {
    private Long userId;
    private String username;
    private String password;
    private String email;
    private String slackId;
    private String role;
} 
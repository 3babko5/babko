package com.business.auth.infrastructure.client.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CreateUserRequest {
    private String username;
    private String password;
    private String email;
    private String slackId;
    private String role;
} 
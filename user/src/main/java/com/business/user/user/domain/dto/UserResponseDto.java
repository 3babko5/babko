package com.business.user.user.domain.dto;

import com.business.user.user.domain.entity.User;
import com.business.user.user.domain.entity.UserType;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserResponseDto {
    private Long userId;
    private String username;
    private String password;
    private String email;
    private String slackId;
    private String role;
    
    public static UserResponseDto fromEntity(User user) {
        return UserResponseDto.builder()
                .userId(user.getUserId())
                .username(user.getUsername())
                .password(user.getPassword())
                .email(user.getEmail())
                .slackId(user.getSlackId())
                .role(user.getRole().name())
                .build();
    }
} 
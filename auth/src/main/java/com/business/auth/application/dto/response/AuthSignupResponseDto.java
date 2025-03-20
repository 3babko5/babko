package com.business.auth.application.dto.response;

import com.business.auth.domain.entity.UserType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
public class AuthSignupResponseDto {
	private Long userId;
	private String username;
	private String email;
	private String slackId;
	private UserType role;
}

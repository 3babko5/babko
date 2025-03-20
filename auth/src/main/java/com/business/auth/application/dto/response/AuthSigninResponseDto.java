package com.business.auth.application.dto.response;

import com.business.auth.domain.entity.UserType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
public class AuthSigninResponseDto {
	private String accessToken;
	private String refreshToken;
	private Long userId;
	private String username;
	private String email;
	private String slackId;
	private UserType role;
}

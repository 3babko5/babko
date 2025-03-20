package com.business.auth.application.dto.response;

import com.business.auth.domain.entity.UserType;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AuthUserSigninResponseDto { // User 서비스와 통신용
	private String accessToken;
	private String refreshToken;
	private Long userId;
	private String username;
	private String email;
	private String slackId;
	private UserType role;
}

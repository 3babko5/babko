package com.business.auth.application.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AuthSignupResponseDto {
	private String accessToken;
	private String refreshToken;
	private Long userId;
}

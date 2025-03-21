package com.business.auth.application.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
/**
 * 회원가입 로그인 응답 DTO
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthLoginResponseDto {
	private Long userId;
	private String accessToken;
	private String refreshToken;
}

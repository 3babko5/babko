package com.business.auth.application.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
/**
 * 토큰 재발급 응답 DTO
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthTokenResponseDto {
	private String accessToken;  // 새로 발급된 액세스 토큰
	private String refreshToken; // 기존 리프레시 토큰
}
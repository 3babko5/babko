package com.business.auth.application.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
/**
 * 엑세스 토큰 재발급 요청 DTO
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthRefreshRequestDto {
	private String refreshToken; // 리프레시 토큰 넣어야함!
}
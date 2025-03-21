package com.business.auth.application.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
/**
 * 로그인 요청 DTO
 *
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthLoginRequestDto {
	private String username;  // 사용자명
	private String password;  // 비밀번호 (평문)
}
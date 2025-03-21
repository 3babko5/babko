package com.business.auth.application.dto.request;

import com.business.auth.domain.entity.UserType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
/**
 * 회원가입 요청 DTO
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthRegisterRequestDto {
	private String username;  // 사용자명
	private String password;  // 비밀번호 (평문)
	private String email;     // 이메일
	private String slackId;   // 슬랙 ID
	private UserType role;    // 사용자 역할 (Enum)
}

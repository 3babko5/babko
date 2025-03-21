package com.business.auth.infrastructure.dto.request;

import com.business.auth.domain.entity.UserType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserCreateRequestDto {
	private String username;  // 사용자명
	private String password;  // 해싱된 비밀번호
	private String email;     // 이메일
	private String slackId;   // 슬랙 ID
	private UserType role;      // 역할
}
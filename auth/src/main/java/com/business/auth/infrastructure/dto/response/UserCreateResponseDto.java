package com.business.auth.infrastructure.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserCreateResponseDto {
	private Long userId;      // 사용자 ID
	private String username;  // 사용자명
	private String password;  // 해싱된 비밀번호
	private String email;     // 이메일
	private String role;      // 역할
	private String slackId;   // 슬랙 ID
}

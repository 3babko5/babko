package com.business.user.user.application.dto.response;

import com.business.user.user.domain.entity.User;

import lombok.Builder;
import lombok.Getter;

/**
 * 사용자 상세 조회 응답 DTO
 * (비밀번호 제외)
 */
@Getter
@Builder
public class UserDetailResponseDto {
	private Long userId;
	private String username;
	private String email;
	private String slackId;
	private String role;

	public static UserDetailResponseDto from(User user) {
		return UserDetailResponseDto.builder()
			.userId(user.getUserId())
			.username(user.getUsername())
			.email(user.getEmail())
			.slackId(user.getSlackId())
			.role(user.getRole().name())
			.build();
	}
}

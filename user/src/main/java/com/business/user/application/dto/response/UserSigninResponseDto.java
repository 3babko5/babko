package com.business.user.application.dto.response;

import com.business.user.domain.entity.UserType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserSigninResponseDto {
	private Long userId;
	private String username;
	private String email;
	private String slackId;
	private UserType role;
}
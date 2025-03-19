package com.business.user.application.dto.mapper;

import com.business.common.application.exception.BusinessLogicException;
import com.business.user.application.dto.response.UserSigninResponseDto;
import com.business.user.application.dto.response.UserSignupResponseDto;
import com.business.user.application.exception.UserExceptionCode;
import com.business.user.domain.entity.User;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserMapper {

	public static UserSignupResponseDto toSignupResponseDto(User user) {
		return UserSignupResponseDto.builder()
			.userId(user.getUserId())
			.username(user.getUsername())
			.email(user.getEmail())
			.slackId(user.getSlackId())
			.role(user.getRole())
			.build();
	}

	public static UserSigninResponseDto toSigninResponseDto(User user) {
		return UserSigninResponseDto.builder()
			.userId(user.getUserId())
			.username(user.getUsername())
			.email(user.getEmail())
			.slackId(user.getSlackId())
			.role(user.getRole())
			.build();
	}
}

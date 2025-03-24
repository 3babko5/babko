package com.business.user.user.application.mapper;

import com.business.user.user.application.dto.response.UserResponseDto;
import com.business.user.user.domain.entity.User;

public class UserMapper {

	// Entity → ResponseDto
	public static UserResponseDto toResponseDto(User user) {
		if (user == null) return null;

		return UserResponseDto.builder()
			.userId(user.getUserId())
			.username(user.getUsername())
			.password(user.getPassword())
			.email(user.getEmail())
			.slackId(user.getSlackId())
			.role(user.getRole().name())
			.build();
	}
}

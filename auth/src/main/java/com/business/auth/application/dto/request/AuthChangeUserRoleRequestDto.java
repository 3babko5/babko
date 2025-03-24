package com.business.auth.application.dto.request;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AuthChangeUserRoleRequestDto {
	private Long userId;
	private String  newRole;
}
package com.business.user.user.application.dto.request;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserChangeRoleRequest {
	private String newRole; // 문자열로 받음
}

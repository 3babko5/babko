package com.business.auth.application.dto.request;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ChangeUserRoleRequest {
	private Long userId;
	private String  newRole;
}
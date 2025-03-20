package com.business.user.user.domain.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum UserStatus {
	ACTIVE("활성화된 사용자 계정"),
	DELETED("삭제된 사용자 계정");

	private final String message;
}

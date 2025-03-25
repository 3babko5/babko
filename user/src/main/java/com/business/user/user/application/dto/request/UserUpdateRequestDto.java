package com.business.user.user.application.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Getter;

/**
 * 사용자 정보 수정 요청 DTO
 * - 마스터 관리자만 수정 가능
 * - 비밀번호는 제외
 */
@Getter
@Builder
public class UserUpdateRequestDto {

	@NotBlank(message = "이메일은 필수입니다.")
	@Email(message = "유효한 이메일 형식이 아닙니다.")
	private String email;

	@NotBlank(message = "슬랙 ID는 필수입니다.")
	private String slackId;

	@NotBlank(message = "역할은 필수입니다.")
	@Pattern(
		regexp = "ROLE_MASTER|ROLE_COMPANY|ROLE_HUB|ROLE_DELIVERY",
		message = "유효한 역할이 아닙니다. (ROLE_MASTER, ROLE_COMPANY, ROLE_HUB, ROLE_DELIVERY 중 하나여야 합니다.)"
	)
	private String role;
}


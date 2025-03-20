package com.business.auth.application.dto.request;

import com.business.auth.domain.entity.UserType;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AuthSignupRequestDto {

	@NotBlank
	private String username;

	@NotBlank
	private String password; // Auth 서비스에서 해싱 후 전달

	@Email
	@NotBlank
	private String email;

	@NotBlank
	private String slackId;

	@NotNull
	private UserType role;
}

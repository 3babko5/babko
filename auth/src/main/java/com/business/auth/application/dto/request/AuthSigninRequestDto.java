package com.business.auth.application.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AuthSigninRequestDto {
	@NotBlank
	private String username;

	@NotBlank
	private String password;
}

package com.business.user.application.dto.request;

import com.business.user.domain.entity.UserType;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserSignupRequestDto {

	@NotBlank(message = "사용자 이름은 필수 입력값입니다.")
	private String username;

	@NotBlank(message = "비밀번호는 필수 입력값입니다.")
	private String password;

	@NotBlank(message = "이메일은 필수 입력값입니다.")
	@Email(message = "유효한 이메일 형식이어야 합니다.")
	private String email;

	@NotBlank(message = "슬랙 ID는 필수 입력값입니다.")
	@Size(max = 50, message = "슬랙 ID는 최대 50자까지 가능합니다.")
	private String slackId;

	@NotNull(message = "역할(Role)은 필수 입력값입니다.")
	private UserType role;
}

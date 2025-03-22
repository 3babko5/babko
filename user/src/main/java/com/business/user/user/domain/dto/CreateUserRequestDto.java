package com.business.user.user.domain.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;

@Getter
public class CreateUserRequestDto {
    @NotBlank(message = "사용자명은 필수입니다.")
    private String username;
    
    @NotBlank(message = "비밀번호는 필수입니다.")
    private String password;
    
    @NotBlank(message = "이메일은 필수입니다.")
    @Email(message = "유효한 이메일 형식이 아닙니다.")
    private String email;
    
    @NotBlank(message = "슬랙 ID는 필수입니다.")
    private String slackId;
    
    @NotBlank(message = "역할은 필수입니다.")
    @Pattern(regexp = "ROLE_MASTER|ROLE_COMPANY|ROLE_HUB|ROLE_DELIVERY", message = "유효한 역할이 아닙니다. (ROLE_MASTER, ROLE_COMPANY, ROLE_HUB, ROLE_DELIVERY 중 하나여야 합니다.)")
    private String role;
} 
package com.business.auth.application.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class JwtTokenResponseDto {
    private String accessToken;
    private String refreshToken;
} 
package com.business.auth.presentation.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.business.auth.application.service.AuthService;
import com.business.auth.application.dto.response.JwtTokenResponseDto;
import com.business.auth.application.dto.request.LoginRequestDto;
import com.business.auth.application.dto.request.SignupRequestDto;
import com.business.auth.application.dto.request.TokenRefreshRequestDto;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<Void> signup(@Valid @RequestBody SignupRequestDto requestDto) {
        authService.signup(requestDto);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/login")
    public ResponseEntity<JwtTokenResponseDto> login(@Valid @RequestBody LoginRequestDto requestDto) {
        JwtTokenResponseDto tokenDto = authService.login(requestDto);
        return ResponseEntity.ok(tokenDto);
    }

    @PostMapping("/refresh")
    public ResponseEntity<JwtTokenResponseDto> refreshToken(@Valid @RequestBody TokenRefreshRequestDto requestDto) {
        JwtTokenResponseDto tokenDto = authService.refreshToken(requestDto);
        return ResponseEntity.ok(tokenDto);
    }
} 
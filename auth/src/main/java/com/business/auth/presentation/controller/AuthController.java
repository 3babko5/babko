package com.business.auth.presentation.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.business.auth.application.service.AuthService;
import com.business.auth.domain.dto.JwtTokenDto;
import com.business.auth.domain.dto.LoginRequestDto;
import com.business.auth.domain.dto.SignupRequestDto;
import com.business.auth.domain.dto.TokenRefreshRequestDto;

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
    public ResponseEntity<JwtTokenDto> login(@Valid @RequestBody LoginRequestDto requestDto) {
        JwtTokenDto tokenDto = authService.login(requestDto);
        return ResponseEntity.ok(tokenDto);
    }

    @PostMapping("/refresh")
    public ResponseEntity<JwtTokenDto> refreshToken(@Valid @RequestBody TokenRefreshRequestDto requestDto) {
        JwtTokenDto tokenDto = authService.refreshToken(requestDto);
        return ResponseEntity.ok(tokenDto);
    }
} 
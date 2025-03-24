package com.business.auth.presentation.controller;

import org.springframework.http.HttpStatus;
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
import com.business.common.aop.RoleCheck;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;

    /**
     * 회원가입
     */
    @PostMapping("/signup")
    public ResponseEntity<JwtTokenResponseDto> signup(@Valid @RequestBody SignupRequestDto requestDto) {
        JwtTokenResponseDto tokenDto = authService.signup(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(tokenDto); // 201 Created + 토큰 리턴
    }

    /**
     * 로그인 - JWT Access/Refresh 토큰 발급
     */
    @PostMapping("/login")
    public ResponseEntity<JwtTokenResponseDto> login(@Valid @RequestBody LoginRequestDto requestDto) {
        JwtTokenResponseDto tokenDto = authService.login(requestDto);
        return ResponseEntity.ok(tokenDto); // 200 OK
    }

    // // 토큰 재발급 부분 수정
    // @PostMapping("/refresh")
    // @RoleCheck(roles = {"ROLE_MASTER","ROLE_HUB", "ROLE_DELIVERY", "ROLE_COMPANY"})
    // public ResponseEntity<JwtTokenResponseDto> refreshToken(@Valid @RequestBody TokenRefreshRequestDto requestDto) {
    //     JwtTokenResponseDto tokenDto = authService.refreshToken(requestDto);
    //     return ResponseEntity.ok(tokenDto);
    //
    // }
}

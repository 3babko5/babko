package com.business.auth.presentation;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.business.auth.application.dto.request.AuthLoginRequestDto;
import com.business.auth.application.dto.request.AuthRefreshRequestDto;
import com.business.auth.application.dto.request.AuthRegisterRequestDto;
import com.business.auth.application.dto.response.AuthLoginResponseDto;
import com.business.auth.application.dto.response.AuthTokenResponseDto;
import com.business.auth.application.service.AuthService;

import lombok.RequiredArgsConstructor;

// AuthController: 인증 관련 요청(회원가입, 로그인, 토큰 재발급)을 처리하는 REST 컨트롤러
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;


    // 회원가입 요청을 받아 AuthService로 전달하고 응답 반환
    @PostMapping("/register")
    public ResponseEntity<AuthLoginResponseDto> register(@RequestBody AuthRegisterRequestDto requestDto) {
        AuthLoginResponseDto response = authService.register(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // 로그인 요청을 받아 AuthService로 전달하고 토큰 반환
    @PostMapping("/login")
    public ResponseEntity<AuthLoginResponseDto> login(@RequestBody AuthLoginRequestDto requestDto) {
        AuthLoginResponseDto response = authService.login(requestDto);
        return ResponseEntity.ok(response);
    }

    // 리프레시 토큰으로 새 액세스 토큰을 발급받는 요청 처리
    @PostMapping("/refresh")
    public ResponseEntity<AuthTokenResponseDto> refresh(@RequestBody AuthRefreshRequestDto requestDto) {
        AuthTokenResponseDto response = authService.refresh(requestDto);
        return ResponseEntity.ok(response);
    }
}
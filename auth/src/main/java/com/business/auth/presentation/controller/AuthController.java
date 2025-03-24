package com.business.auth.presentation.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.business.auth.application.dto.request.AuthChangeUserRoleRequestDto;
import com.business.auth.application.dto.response.AuthLoginResponseDto;
import com.business.auth.application.dto.response.AuthSignupResponseDto;
import com.business.auth.application.service.AuthService;
import com.business.auth.application.dto.request.AuthLoginRequestDto;
import com.business.auth.application.dto.request.AuthSignupRequestDto;
import com.business.common.aop.RoleCheck;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;

    /**
     * 회원가입 (모두 가능)
     */
    @PostMapping("/signup")
    public ResponseEntity<AuthSignupResponseDto> signup(@Valid @RequestBody AuthSignupRequestDto requestDto) {
        AuthSignupResponseDto responseDto = authService.signup(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto); // 201 Created + 응답 DTO
    }

    /**
     * 로그인 - JWT Access/Refresh 토큰 발급 (모두 가능)
     */
    @PostMapping("/login")
    public ResponseEntity<AuthLoginResponseDto> login(@Valid @RequestBody AuthLoginRequestDto requestDto) {
        AuthLoginResponseDto responseDto = authService.login(requestDto);
        return ResponseEntity.ok(responseDto); // 200 OK
    }

    /**
     * 역할 변경 - (마스터 전용)
     */
    @PutMapping("/users/role")
    @RoleCheck(roles = {"ROLE_MASTER"}) // 마스터만 역할 변경 가능
    public ResponseEntity<Void> changeUserRole(@RequestBody AuthChangeUserRoleRequestDto request) {
        authService.changeUserRole(request);
        return ResponseEntity.ok().build(); // 200 OK
    }
}
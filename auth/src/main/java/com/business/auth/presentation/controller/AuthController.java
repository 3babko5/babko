package com.business.auth.presentation.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.business.auth.application.dto.request.ChangeUserRoleRequest;
import com.business.auth.application.service.AuthService;
import com.business.auth.application.dto.response.JwtTokenResponseDto;
import com.business.auth.application.dto.request.LoginRequestDto;
import com.business.auth.application.dto.request.SignupRequestDto;
import com.business.auth.infrastructure.client.UserClient;
import com.business.auth.infrastructure.util.JwtTokenUtil;
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
    /**
     * 역할 변경 - 마스터 전용
     */
    @PutMapping("/users/role")
    @RoleCheck(roles = {"ROLE_MASTER"}) // 마스터만 역할 변경 가능
    public ResponseEntity<Void> changeUserRole(@RequestBody ChangeUserRoleRequest request) {
        authService.changeUserRole(request);
        return ResponseEntity.ok().build();
    }
}

    // // 토큰 재발급 부분 수정
    // @PostMapping("/refresh")
    // @RoleCheck(roles = {"ROLE_MASTER","ROLE_HUB", "ROLE_DELIVERY", "ROLE_COMPANY"})
    // public ResponseEntity<JwtTokenResponseDto> refreshToken(@Valid @RequestBody TokenRefreshRequestDto requestDto) {
    //     JwtTokenResponseDto tokenDto = authService.refreshToken(requestDto);
    //     return ResponseEntity.ok(tokenDto);
    //
    // }


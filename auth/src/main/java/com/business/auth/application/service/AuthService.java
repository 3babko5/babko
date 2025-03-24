package com.business.auth.application.service;

import com.business.auth.application.dto.request.AuthChangeUserRoleRequestDto;
import com.business.auth.application.dto.response.AuthLoginResponseDto;
import com.business.auth.application.dto.response.AuthSignupResponseDto;
import com.business.auth.application.exception.AuthExceptionCode;
import com.business.auth.infrastructure.util.JwtTokenUtil;

import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.business.auth.application.dto.request.AuthLoginRequestDto;
import com.business.auth.application.dto.request.AuthSignupRequestDto;
import com.business.auth.domain.entity.Token;
import com.business.auth.domain.repository.TokenRepository;
import com.business.auth.infrastructure.client.UserClient;
import com.business.auth.infrastructure.dto.request.AuthCreateUserRequestDto;
import com.business.auth.infrastructure.dto.response.UserResponseDto;
import com.business.common.application.exception.BusinessLogicException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserClient userClient; // User 서비스와 통신하기 위한 Client
    private final TokenRepository tokenRepository; // RefreshToken을 저장하기 위한 Repository
    private final PasswordEncoder passwordEncoder; // 비밀번호 암호화를 위한 Encoder
    private final JwtTokenUtil jwtTokenUtil; // JWT 토큰을 생성하기 위한 유틸리티 클래스

    /**
     * 회원가입
     * - 사용자가 입력한 정보를 바탕으로 회원 가입을 처리합니다.
     * - 비밀번호는 암호화하고, User 서비스를 통해 사용자를 생성합니다.
     * - 회원가입 후 JWT 토큰을 생성하여 반환합니다.
     */
    @Transactional
    public AuthSignupResponseDto signup(AuthSignupRequestDto requestDto) {
        try {
            // 1. 비밀번호 암호화 (plain text 비밀번호를 암호화하여 저장)
            String hashedPassword = passwordEncoder.encode(requestDto.getPassword());

            // 2. 유저 생성 요청 DTO 구성 (회원가입 요청 정보)
            AuthCreateUserRequestDto createUserRequest = AuthCreateUserRequestDto.builder()
                .username(requestDto.getUsername()) // 사용자 이름
                .password(hashedPassword) // 암호화된 비밀번호
                .email(requestDto.getEmail()) // 이메일
                .slackId(requestDto.getSlackId()) // 슬랙 ID
                .role(requestDto.getRole()) // 역할 (예: ROLE_USER)
                .build();

            // 3. 회원 생성 요청 (user-service로 회원 생성 요청)
            userClient.createUser(createUserRequest);

            // 4. 회원가입 후, 로그인 정보 조회 (회원가입 후 생성된 사용자 정보 조회)
            ResponseEntity<UserResponseDto> response = userClient.getUserByUsername(requestDto.getUsername());
            UserResponseDto user = response.getBody();

            // 5. 유저 정보가 없으면 예외 처리
            if (user == null) {
                throw new BusinessLogicException(AuthExceptionCode.USER_NOT_FOUND);
            }

            // 6. JWT 토큰 발급 (Access token과 Refresh token 생성)
            String accessToken = jwtTokenUtil.generateAccessToken(user.getUserId(), user.getRole());
            String refreshToken = jwtTokenUtil.generateRefreshToken(user.getUserId());

            // 7. RefreshToken 저장 (기존에 존재하는 토큰이 있으면 업데이트, 없으면 새로 저장)
            tokenRepository.findByUserId(user.getUserId())
                .ifPresentOrElse(
                    token -> token.updateRefreshToken(refreshToken), // 기존 토큰이 있으면 업데이트
                    () -> { // 기존 토큰이 없으면 새로 생성하여 저장
                        Token newToken = Token.create(user.getUserId(), refreshToken);
                        newToken.createdBy(0L); // 관리자 ID로 감사 로그용
                        tokenRepository.save(newToken);
                    }
                );

            // 8. JWT 토큰을 응답으로 반환
            return AuthSignupResponseDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .userId(user.getUserId()) // 사용자 ID 포함
                .build();

        } catch (Exception e) {
            // 예외 발생 시 로그를 남기고 사용자 생성 실패 예외 던짐
            System.out.println("e.getMessage() = " + e.getMessage());
            throw new BusinessLogicException(AuthExceptionCode.USER_CREATION_FAILED);
        }
    }

    /**
     * 로그인 → accessToken + refreshToken 발급
     * - 사용자가 입력한 정보로 로그인하여 JWT 토큰을 발급합니다.
     * - 비밀번호를 검증하고, 유효한 사용자일 경우 토큰을 반환합니다.
     */
    @Transactional
    public AuthLoginResponseDto login(AuthLoginRequestDto requestDto) {

        // 1. 유저 정보 조회 (사용자 이름으로 유저 조회)
        ResponseEntity<UserResponseDto> response = userClient.getUserByUsername(requestDto.getUsername());
        UserResponseDto user = response.getBody();

        // 2. 유저가 없으면 예외 처리
        if (user == null) {
            throw new BusinessLogicException(AuthExceptionCode.USER_NOT_FOUND);
        }

        // 3. 비밀번호 검증 (입력된 비밀번호와 저장된 비밀번호 비교)
        if (!passwordEncoder.matches(requestDto.getPassword(), user.getPassword())) {
            throw new BusinessLogicException(AuthExceptionCode.PASSWORD_MISMATCH); // 비밀번호 불일치 예외
        }

        // 4. Access / Refresh 토큰 생성
        String accessToken = jwtTokenUtil.generateAccessToken(user.getUserId(), user.getRole());
        String refreshToken = jwtTokenUtil.generateRefreshToken(user.getUserId());

        // 5. Refresh 토큰 저장 또는 업데이트
        tokenRepository.findByUserId(user.getUserId())
            .ifPresentOrElse(
                token -> token.updateRefreshToken(refreshToken), // 기존 토큰이 있으면 업데이트
                () -> { // 기존 토큰이 없으면 새로 생성하여 저장
                    Token newToken = Token.create(user.getUserId(), refreshToken);
                    newToken.createdBy(0L); // 감사 로그용
                    tokenRepository.save(newToken);
                }
            );

        // 6. 토큰 응답 반환
        return AuthLoginResponseDto.builder()
            .accessToken(accessToken)
            .refreshToken(refreshToken)
            .userId(user.getUserId()) // 사용자 ID 포함
            .build();
    }
    // 역할 변경 (마스터 가능)
    public void changeUserRole(AuthChangeUserRoleRequestDto request) {
        userClient.changeUserRole(request.getUserId(), request);
    }
   }
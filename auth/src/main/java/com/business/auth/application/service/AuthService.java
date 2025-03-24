package com.business.auth.application.service;

import com.business.auth.application.exception.AuthExceptionCode;
import com.business.auth.infrastructure.util.JwtTokenUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.business.auth.application.dto.response.JwtTokenResponseDto;
import com.business.auth.application.dto.request.LoginRequestDto;
import com.business.auth.application.dto.request.SignupRequestDto;
import com.business.auth.domain.entity.Token;
import com.business.auth.domain.repository.TokenRepository;
import com.business.auth.infrastructure.client.UserClient;
import com.business.auth.infrastructure.dto.request.CreateUserRequest;
import com.business.auth.infrastructure.dto.UserResponse;
import com.business.common.application.exception.BusinessLogicException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserClient userClient;
    private final TokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenUtil jwtTokenUtil;

    /**
     * 회원가입
     */
    @Transactional
    public JwtTokenResponseDto signup(SignupRequestDto requestDto) {
        try {
            // 1. 비밀번호 암호화
            String hashedPassword = passwordEncoder.encode(requestDto.getPassword());

            // 2. 유저 생성 요청 DTO 구성
            CreateUserRequest createUserRequest = CreateUserRequest.builder()
                .username(requestDto.getUsername())
                .password(hashedPassword)
                .email(requestDto.getEmail())
                .slackId(requestDto.getSlackId())
                .role(requestDto.getRole())
                .build();

            // 3. 회원 생성 요청 (user-service)
            userClient.createUser(createUserRequest);

            // 4. 회원가입 후, 로그인 정보 조회
            ResponseEntity<UserResponse> response = userClient.getUserByUsername(requestDto.getUsername());
            UserResponse user = response.getBody();

            if (user == null) {
                throw new BusinessLogicException(AuthExceptionCode.USER_NOT_FOUND);
            }

            // 5. 토큰 발급
            String accessToken = jwtTokenUtil.generateAccessToken(user.getUserId(), user.getRole());
            String refreshToken = jwtTokenUtil.generateRefreshToken(user.getUserId());

            // 6. RefreshToken 저장
            tokenRepository.findByUserId(user.getUserId())
                .ifPresentOrElse(
                    token -> token.updateRefreshToken(refreshToken),
                    () -> {
                        Token newToken = Token.create(user.getUserId(), refreshToken);
                        newToken.createdBy(0L);
                        tokenRepository.save(newToken);
                    }
                );

            // 7. 토큰 응답
            return JwtTokenResponseDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .userId(user.getUserId())
                .build();

        } catch (Exception e) {
            System.out.println("e.getMessage() = " + e.getMessage());
            throw new BusinessLogicException(AuthExceptionCode.USER_CREATION_FAILED);
            
        }
    }


    /**
     * 로그인 → accessToken + refreshToken 발급
     */
    @Transactional
    public JwtTokenResponseDto login(LoginRequestDto requestDto) {

        // 1. 유저 정보 조회
        ResponseEntity<UserResponse> response = userClient.getUserByUsername(requestDto.getUsername());
        UserResponse user = response.getBody();

        if (user == null) {
            throw new BusinessLogicException(AuthExceptionCode.USER_NOT_FOUND);
        }

        // 2. 비밀번호 검증
        if (!passwordEncoder.matches(requestDto.getPassword(), user.getPassword())) {
            throw new BusinessLogicException(AuthExceptionCode.PASSWORD_MISMATCH);
        }

        // 3. Access / Refresh 토큰 생성
        String accessToken = jwtTokenUtil.generateAccessToken(user.getUserId(), user.getRole());
        String refreshToken = jwtTokenUtil.generateRefreshToken(user.getUserId());

        // 4. Refresh 토큰 저장 또는 업데이트
        tokenRepository.findByUserId(user.getUserId())
            .ifPresentOrElse(
                token -> token.updateRefreshToken(refreshToken),
                () -> {
                    Token newToken = Token.create(user.getUserId(), refreshToken);
                    newToken.createdBy(0L); // 감사 로그용
                    tokenRepository.save(newToken);
                }
            );

        // 5. 토큰 응답 반환
        return JwtTokenResponseDto.builder()
            .accessToken(accessToken)
            .refreshToken(refreshToken)
            .userId(user.getUserId())
            .build();
    }
    // @Transactional
    // public JwtTokenResponseDto refreshToken(TokenRefreshRequestDto requestDto) {
    //     String refreshToken = requestDto.getRefreshToken();
    //
    //     // 리프레시 토큰 검증
    //     if (!jwtTokenUtil.validateToken(refreshToken)) {
    //         throw new RuntimeException("유효하지 않은 리프레시 토큰입니다.");
    //     }
    //
    //     // DB에서 리프레시 토큰 조회
    //     Token token = tokenRepository.findByRefreshToken(refreshToken)
    //         .orElseThrow(() -> new RuntimeException("존재하지 않는 리프레시 토큰입니다."));
    //
    //     // 토큰에서 사용자 ID 추출
    //     Long userId = jwtTokenUtil.extractUserId(refreshToken);
    //
    //     // 사용자 정보 조회 (역할 확인을 위해)
    //     ResponseEntity<UserResponse> response = userClient.getUserById(userId);
    //     UserResponse user = response.getBody();
    //
    //     if (user == null) {
    //         throw new RuntimeException("존재하지 않는 사용자입니다.");
    //     }
    //
    //     // 새로운 토큰 생성
    //     String newAccessToken = jwtTokenUtil.generateAccessToken(userId, user.getRole());
    //     String newRefreshToken = jwtTokenUtil.generateRefreshToken(userId);
    //
    //     JwtTokenResponseDto newTokenDto = JwtTokenResponseDto.builder()
    //         .accessToken(newAccessToken)
    //         .refreshToken(newRefreshToken)
    //         .build();
    //
    //     // 리프레시 토큰 업데이트
    //     token.updateRefreshToken(newRefreshToken);
    //
    //     return newTokenDto;
    // }
}




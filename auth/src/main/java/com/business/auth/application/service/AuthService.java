package com.business.auth.application.service;

import com.business.auth.infrastructure.util.JwtTokenUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.business.auth.application.dto.response.JwtTokenResponseDto;
import com.business.auth.application.dto.request.LoginRequestDto;
import com.business.auth.application.dto.request.SignupRequestDto;
import com.business.auth.application.dto.request.TokenRefreshRequestDto;
import com.business.auth.domain.entity.Token;
import com.business.auth.domain.repository.TokenRepository;
import com.business.auth.infrastructure.client.UserClient;
import com.business.auth.infrastructure.client.dto.CreateUserRequest;
import com.business.auth.infrastructure.client.dto.UserResponse;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserClient userClient;
    private final TokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenUtil jwtTokenUtil;

    @Transactional
    public void signup(SignupRequestDto requestDto) {
        // 비밀번호 해싱
        String hashedPassword = passwordEncoder.encode(requestDto.getPassword());

        // User 서비스에 회원 정보 저장 요청
        CreateUserRequest createUserRequest = CreateUserRequest.builder()
            .username(requestDto.getUsername())
            .password(hashedPassword)
            .email(requestDto.getEmail())
            .slackId(requestDto.getSlackId())
            .role("ROLE_COMPANY") // 기본 역할은 ROLE_COMPANY
            .build();

        userClient.createUser(createUserRequest);
    }
    @Transactional
    public JwtTokenResponseDto login(LoginRequestDto requestDto) {
        // User 서비스에서 회원 정보 조회
        ResponseEntity<UserResponse> response = userClient.getUserByUsername(requestDto.getUsername());
        UserResponse user = response.getBody();
        
        if (user == null) {
            throw new RuntimeException("존재하지 않는 사용자입니다.");
        }
        
        // 비밀번호 검증
        if (!passwordEncoder.matches(requestDto.getPassword(), user.getPassword())) {
            throw new RuntimeException("비밀번호가 일치하지 않습니다.");
        }
        
        // JWT 토큰 생성
        String accessToken = jwtTokenUtil.generateAccessToken(user.getUserId(), user.getRole());
        String refreshToken = jwtTokenUtil.generateRefreshToken(user.getUserId());
        
        JwtTokenResponseDto tokenDto = JwtTokenResponseDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
        
        // Refresh 토큰 저장 또는 업데이트
        tokenRepository.findByUserId(user.getUserId())
                .ifPresentOrElse(
                        token -> token.updateRefreshToken(refreshToken),
                        () -> {
                            Token newToken = Token.create(user.getUserId(), refreshToken);
                            // 시스템 사용자(ID: 0)로 감사 필드 설정
                            newToken.createdBy(0L);
                            tokenRepository.save(newToken);
                        }
                );
        
        return tokenDto;
    }

    @Transactional
    public JwtTokenResponseDto refreshToken(TokenRefreshRequestDto requestDto) {
        String refreshToken = requestDto.getRefreshToken();
        
        // 리프레시 토큰 검증
        if (!jwtTokenUtil.validateToken(refreshToken)) {
            throw new RuntimeException("유효하지 않은 리프레시 토큰입니다.");
        }
        
        // DB에서 리프레시 토큰 조회
        Token token = tokenRepository.findByRefreshToken(refreshToken)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 리프레시 토큰입니다."));
        
        // 토큰에서 사용자 ID 추출
        Long userId = jwtTokenUtil.extractUserId(refreshToken);
        
        // 사용자 정보 조회 (역할 확인을 위해)
        ResponseEntity<UserResponse> response = userClient.getUserById(userId);
        UserResponse user = response.getBody();
        
        if (user == null) {
            throw new RuntimeException("존재하지 않는 사용자입니다.");
        }
        
        // 새로운 토큰 생성
        String newAccessToken = jwtTokenUtil.generateAccessToken(userId, user.getRole());
        String newRefreshToken = jwtTokenUtil.generateRefreshToken(userId);
        
        JwtTokenResponseDto newTokenDto = JwtTokenResponseDto.builder()
                .accessToken(newAccessToken)
                .refreshToken(newRefreshToken)
                .build();
        
        // 리프레시 토큰 업데이트
        token.updateRefreshToken(newRefreshToken);
        
        return newTokenDto;
    }
} 

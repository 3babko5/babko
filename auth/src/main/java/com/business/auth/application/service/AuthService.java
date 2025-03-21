package com.business.auth.application.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.business.auth.application.dto.request.AuthLoginRequestDto;
import com.business.auth.application.dto.request.AuthRefreshRequestDto;
import com.business.auth.application.dto.request.AuthRegisterRequestDto;
import com.business.auth.application.dto.response.AuthLoginResponseDto;
import com.business.auth.application.dto.response.AuthTokenResponseDto;
import com.business.auth.application.exception.AuthExceptionCode;

import com.business.auth.infrastructure.client.UserServiceClient;
import com.business.auth.infrastructure.config.JwtTokenProvider;
import com.business.auth.infrastructure.dto.request.UserCreateRequestDto;
import com.business.auth.infrastructure.dto.response.UserCreateResponseDto;
import com.business.common.application.exception.BusinessLogicException;


// AuthService: 회원가입, 로그인, 토큰 재발급의 인증 로직을 처리
@Service
public class AuthService {
	private final UserServiceClient userServiceClient;
	private final PasswordEncoder passwordEncoder;
	private final JwtTokenProvider jwtTokenProvider;

	public AuthService(UserServiceClient userServiceClient, PasswordEncoder passwordEncoder, JwtTokenProvider jwtTokenProvider) {
		this.userServiceClient = userServiceClient;
		this.passwordEncoder = passwordEncoder;
		this.jwtTokenProvider = jwtTokenProvider;
	}

	// 회원가입: 비밀번호 해싱 후 User 서비스에 저장 요청, 성공 시 토큰과 userId 반환
	public AuthLoginResponseDto register(AuthRegisterRequestDto requestDto) {
		String hashedPassword = passwordEncoder.encode(requestDto.getPassword());
		UserCreateRequestDto userCreateRequestDto = new UserCreateRequestDto(requestDto.getUsername(), hashedPassword, requestDto.getEmail(), requestDto.getSlackId(), requestDto.getRole());
		UserCreateResponseDto userResponse = userServiceClient.createUser(userCreateRequestDto);
		String accessToken = jwtTokenProvider.createAccessToken(userResponse.getUserId(), userResponse.getRole());
		String refreshToken = jwtTokenProvider.createRefreshToken(userResponse.getUserId());
		return AuthLoginResponseDto.builder()
			.userId(userResponse.getUserId())
			.accessToken(accessToken)
			.refreshToken(refreshToken)
			.build();
	}

	// 로그인: 사용자 정보 확인 후 실패 시 예외 발생, 성공 시 토큰과 userId 반환
	public AuthLoginResponseDto login(AuthLoginRequestDto requestDto) {
		UserCreateResponseDto user = userServiceClient.getUserByUsername(requestDto.getUsername());
		if (user == null) {
			throw new BusinessLogicException(AuthExceptionCode.USER_NOT_FOUND); // 사용자가 없으면 예외
		}
		if (!passwordEncoder.matches(requestDto.getPassword(), user.getPassword())) {
			throw new BusinessLogicException(AuthExceptionCode.PASSWORD_MISMATCH); // 비밀번호 불일치 시 예외
		}
		String accessToken = jwtTokenProvider.createAccessToken(user.getUserId(), user.getRole());
		String refreshToken = jwtTokenProvider.createRefreshToken(user.getUserId());
		return AuthLoginResponseDto.builder()
			.userId(user.getUserId())
			.accessToken(accessToken)
			.refreshToken(refreshToken)
			.build();
	}

	// 토큰 재발급: 리프레시 토큰 검증 후 실패 시 예외 발생, 성공 시 새 토큰 반환
	public AuthTokenResponseDto refresh(AuthRefreshRequestDto requestDto) {
		if (!jwtTokenProvider.validateToken(requestDto.getRefreshToken())) {
			throw new BusinessLogicException(AuthExceptionCode.INVALID_TOKEN); // 유효하지 않은 토큰이면 예외
		}
		Long userId = jwtTokenProvider.getUserIdFromToken(requestDto.getRefreshToken());
		UserCreateResponseDto user = userServiceClient.getUserById(userId);
		if (user == null) {
			throw new BusinessLogicException(AuthExceptionCode.USER_NOT_FOUND); // 사용자 없으면 예외
		}
		String newAccessToken = jwtTokenProvider.createAccessToken(user.getUserId(), user.getRole());
		return new AuthTokenResponseDto(newAccessToken, requestDto.getRefreshToken());
	}
}
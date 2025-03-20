package com.business.auth.infrastructure.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component // Spring 빈으로 등록
public class JwtUtil {

	@Value("${jwt.secret}") // 설정 파일에서 비밀키 가져오기
	private String secret;

	@Value("${jwt.access-token-expiration}") // Access Token 만료 시간
	private long accessTokenExpiration;

	@Value("${jwt.refresh-token-expiration}") // Refresh Token 만료 시간
	private long refreshTokenExpiration;

	public String generateAccessToken(Long userId, String role) { // Access Token 생성
		return Jwts.builder()
			.setSubject(userId.toString()) // 사용자 ID 설정
			.claim("role", role)  // 역할 정보 추가
			.setIssuedAt(new Date()) // 발급 시간
			.setExpiration(new Date(System.currentTimeMillis() + accessTokenExpiration)) // 만료 시간
			.signWith(SignatureAlgorithm.HS512, secret) // HS512로 서명
			.compact(); // 토큰 문자열 생성
	}

	public String generateRefreshToken(Long userId) { // Refresh Token 생성
		return Jwts.builder()
			.setSubject(userId.toString()) // 사용자 ID 설정
			.setIssuedAt(new Date()) // 발급 시간
			.setExpiration(new Date(System.currentTimeMillis() + refreshTokenExpiration)) // 만료 시간
			.signWith(SignatureAlgorithm.HS512, secret) // HS512로 서명
			.compact(); // 토큰 문자열 생성
	}

	public Claims extractClaims(String token) { // 토큰에서 클레임 추출
		return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
	}

	public Long extractUserId(String token) { // 사용자 ID 추출
		return Long.parseLong(extractClaims(token).getSubject());
	}

	public String extractRole(String token) { // 역할 추출
		return extractClaims(token).get("role", String.class);
	}

	public boolean validateToken(String token) { // 토큰 유효성 검증
		try {
			Jwts.parser().setSigningKey(secret).parseClaimsJws(token); // 파싱 성공 시 유효
			return true;
		} catch (Exception e) { // 예외 발생 시 유효하지 않음
			return false;
		}
	}
}

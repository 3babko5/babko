package com.business.auth.infrastructure.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

// JwtTokenProvider: JWT 토큰 생성 및 검증을 담당
@Component
public class JwtTokenProvider {
	@Value("${jwt.secret}")
	private String secretKey;
	private final long ACCESS_TOKEN_VALIDITY = 30 * 60 * 1000; // 30분
	private final long REFRESH_TOKEN_VALIDITY = 7 * 24 * 60 * 60 * 1000; // 7일

	// 액세스 토큰 생성: 사용자 ID와 역할을 포함
	public String createAccessToken(Long userId, String role) {
		return Jwts.builder()
			.setSubject(userId.toString())
			.claim("role", role)
			.setIssuedAt(new java.util.Date())
			.setExpiration(new java.util.Date(System.currentTimeMillis() + ACCESS_TOKEN_VALIDITY))
			.signWith(SignatureAlgorithm.HS256, secretKey)
			.compact();
	}

	// 리프레시 토큰 생성: 사용자 ID만 포함
	public String createRefreshToken(Long userId) {
		return Jwts.builder()
			.setSubject(userId.toString())
			.setIssuedAt(new java.util.Date())
			.setExpiration(new java.util.Date(System.currentTimeMillis() + REFRESH_TOKEN_VALIDITY))
			.signWith(SignatureAlgorithm.HS256, secretKey)
			.compact();
	}

	// 토큰 유효성 검증
	public boolean validateToken(String token) {
		try {
			Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	// 토큰에서 사용자 ID 추출
	public Long getUserIdFromToken(String token) {
		return Long.parseLong(Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject());
	}

	// 토큰에서 역할 추출 (이 부분 다시 변경)
	public String getRoleFromToken(String token) {
		return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().get("role", String.class);
	}
}
package com.business.user.user.application.exception;

import org.springframework.http.HttpStatus;

import com.business.common.application.exception.ExceptionCode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum UserExceptionCode implements ExceptionCode {

	// 사용자 관련 예외
	USER_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 사용자를 찾을 수 없습니다."),
	USERNAME_ALREADY_EXISTS(HttpStatus.CONFLICT, "이미 존재하는 사용자명입니다."),
	EMAIL_ALREADY_EXISTS(HttpStatus.CONFLICT, "이미 존재하는 이메일입니다."),
	SLACKID_ALREADY_EXISTS(HttpStatus.CONFLICT, "이미 존재하는 슬랙 ID입니다."),

	// 역할(Role) 관련 예외
	USER_ACCESS_DENIED(HttpStatus.FORBIDDEN, "해당 리소스에 접근할 권한이 없습니다."),

	// 계정 상태 관련 예외
	ACCOUNT_EXPIRED(HttpStatus.FORBIDDEN, "계정이 만료되었습니다."),

	// 로그인 및 인증 관련 예외
	PASSWORD_MISMATCH(HttpStatus.UNAUTHORIZED, "비밀번호가 일치하지 않습니다."),

	// 데이터 유효성 검사
	INVALID_USER_DATA(HttpStatus.BAD_REQUEST, "입력한 사용자 데이터가 올바르지 않습니다.");

	private final HttpStatus status;
	private final String message;
	private final String code = this.name();
}
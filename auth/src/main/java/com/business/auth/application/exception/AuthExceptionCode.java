package com.business.auth.application.exception;

import org.springframework.http.HttpStatus;

import com.business.common.application.exception.ExceptionCode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AuthExceptionCode implements ExceptionCode {
	USER_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 사용자를 찾을 수 없습니다."),
	PASSWORD_MISMATCH(HttpStatus.UNAUTHORIZED, "비밀번호가 일치하지 않습니다."),
	INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "유효하지 않은 토큰입니다."),
	USER_CREATION_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "사용자 생성에 실패했습니다."),
	INVALID_USER_ID(HttpStatus.INTERNAL_SERVER_ERROR, "유효하지 않은 사용자 ID입니다.");


	private final HttpStatus status;
	private final String message;
	private final String code = this.name();
}
package com.business.common.exception;

import com.business.common.dto.CommonDto;
import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.naming.AuthenticationException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    private ResponseEntity<CommonDto<Object>> buildErrorResponse(HttpStatus status, String message, Object data) {
        return new ResponseEntity<>(
                CommonDto.builder()
                        .statusCode(status.value())
                        .message(message)
                        .data(data)
                        .build(),
                status
        );
    }

    @ExceptionHandler(BusinessLogicException.class)
    public ResponseEntity<CommonDto<Object>> handleBusinessLogicException(BusinessLogicException e) {
        log.warn("비즈니스 로직 예외 발생: {}", e.getMessage());
        return buildErrorResponse(HttpStatus.BAD_REQUEST, e.getMessage(), null);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<CommonDto<Object>> handleValidationException(MethodArgumentNotValidException e) {
        Map<String, String> validationErrors = new HashMap<>();
        e.getBindingResult().getFieldErrors().forEach(error ->
                validationErrors.put(error.getField(), error.getDefaultMessage())
        );
        log.warn("유효성 검사 실패: {}", validationErrors);
        return buildErrorResponse(HttpStatus.BAD_REQUEST, "유효성 검사 실패", validationErrors);
    }

    @ExceptionHandler(FeignException.class)
    public ResponseEntity<CommonDto<Object>> handleFeignClientException(FeignException e) {
        log.error("FeignClient 예외 발생: {}", e.getMessage(), e);
        return buildErrorResponse(HttpStatus.BAD_GATEWAY, "외부 서비스 호출 중 오류가 발생했습니다.", null);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<CommonDto<Object>> handleAuthenticationException(AuthenticationException e) {
        log.warn("인증 실패: {}", e.getMessage());
        return buildErrorResponse(HttpStatus.UNAUTHORIZED, "인증에 실패했습니다.", null);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<CommonDto<Object>> handleConstraintViolationException(ConstraintViolationException e) {
        log.warn("@RequestParam 유효성 검사 실패: {}", e.getMessage());
        return buildErrorResponse(HttpStatus.BAD_REQUEST, "잘못된 요청입니다.", e.getMessage());
    }

    @ExceptionHandler(org.springframework.dao.DataAccessException.class)
    public ResponseEntity<CommonDto<Object>> handleDatabaseException(org.springframework.dao.DataAccessException e) {
        log.error("데이터베이스 오류 발생: {}", e.getMessage(), e);
        return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "데이터베이스 처리 중 오류가 발생했습니다.", null);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<CommonDto<Object>> handleGeneralException(Exception e) {
        log.error("서버 내부 오류 발생: {}", e.getMessage(), e);
        return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "서버 내부 오류가 발생했습니다.", null);
    }
}

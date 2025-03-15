package com.business.common.exception;

import com.business.common.dto.CommonDto;
import feign.FeignException;
import org.hibernate.exception.ConstraintViolationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import javax.naming.AuthenticationException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler globalExceptionHandler;

    @BeforeEach
    void setUp() {
        globalExceptionHandler = new GlobalExceptionHandler();
    }

    @Test
    @DisplayName("BusinessLogicException 예외 처리 테스트")
    void handleBusinessLogicException() {
        BusinessLogicException exception = new BusinessLogicException("비즈니스 로직 예외 발생");
        ResponseEntity<CommonDto<Object>> response = globalExceptionHandler.handleBusinessLogicException(exception);

        assertEquals(400, response.getStatusCode().value());
        assertEquals("비즈니스 로직 예외 발생", response.getBody().getMessage());
    }

    @Test
    @DisplayName("FeignClient 예외 처리 테스트")
    void handleFeignClientException() {
        FeignException exception = mock(FeignException.class);
        ResponseEntity<CommonDto<Object>> response = globalExceptionHandler.handleFeignClientException(exception);

        assertEquals(502, response.getStatusCode().value()); // BAD_GATEWAY
        assertEquals("외부 서비스 호출 중 오류가 발생했습니다.", response.getBody().getMessage());
    }

    @Test
    @DisplayName("AuthenticationException 예외 처리 테스트")
    void handleAuthenticationException() {
        AuthenticationException exception = new AuthenticationException("인증 실패");
        ResponseEntity<CommonDto<Object>> response = globalExceptionHandler.handleAuthenticationException(exception);

        assertEquals(401, response.getStatusCode().value()); // UNAUTHORIZED
        assertEquals("인증에 실패했습니다.", response.getBody().getMessage());
    }

    @Test
    @DisplayName("DataAccessException 예외 처리 테스트")
    void handleDatabaseException() {
        org.springframework.dao.DataAccessException exception = mock(org.springframework.dao.DataAccessException.class);
        ResponseEntity<CommonDto<Object>> response = globalExceptionHandler.handleDatabaseException(exception);

        assertEquals(500, response.getStatusCode().value()); // INTERNAL_SERVER_ERROR
        assertEquals("데이터베이스 처리 중 오류가 발생했습니다.", response.getBody().getMessage());
    }

    @Test
    @DisplayName("Exception 예외 처리 테스트")
    void handleGeneralException() {
        Exception exception = new Exception("서버 오류");
        ResponseEntity<CommonDto<Object>> response = globalExceptionHandler.handleGeneralException(exception);

        assertEquals(500, response.getStatusCode().value()); // INTERNAL_SERVER_ERROR
        assertEquals("서버 내부 오류가 발생했습니다.", response.getBody().getMessage());
    }
}

package com.business.common.exception;

import com.business.common.exception.dto.ExceptionResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.naming.AuthenticationException;
import java.nio.file.AccessDeniedException;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(BusinessLogicException.class)
    public final ResponseEntity<ExceptionResponse> handleBusinessLogicException(
            BusinessLogicException exc, HttpServletRequest request) {
        ExceptionCode exceptionCode = exc.getExceptionCode();

        ExceptionResponse response = createErrorResponse(request, exceptionCode, null);
        logError(exc, exceptionCode);
        return ResponseEntity.status(exceptionCode.getStatus()).body(response);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ExceptionResponse> handleRuntimeException(
            RuntimeException exc, HttpServletRequest request) {
        ExceptionCode exceptionCode = GlobalExceptionCode.INTERNAL_ERROR;

        ExceptionResponse response = createErrorResponse(request, exceptionCode, null);

        logError(exc, exceptionCode);

        return ResponseEntity.status(exceptionCode.getStatus()).body(response);
    }

    @ExceptionHandler(AuthenticationException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseEntity<ExceptionResponse> handleAuthenticationException(
            AuthenticationException exc, HttpServletRequest request) {
        ExceptionCode exceptionCode = GlobalExceptionCode.UNAUTHORIZED;
        ExceptionResponse response = createErrorResponse(request, exceptionCode, null);

        logError(exc, exceptionCode);
        return ResponseEntity.status(exceptionCode.getStatus()).body(response);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException exc,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request) {
        HttpServletRequest servletRequest = ((ServletWebRequest) request).getRequest();
        ExceptionCode exceptionCode = GlobalExceptionCode.INVALID_INPUT;
        Map<String, String> details =
                exc.getBindingResult().getFieldErrors().stream()
                        .collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage));

        ExceptionResponse response = createErrorResponse(servletRequest, exceptionCode, details);

        logError(exc, exceptionCode);

        return ResponseEntity.status(exceptionCode.getStatus()).body(response);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(
            HttpMessageNotReadableException exc,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request) {
        HttpServletRequest servletRequest = ((ServletWebRequest) request).getRequest();
        ExceptionCode exceptionCode = GlobalExceptionCode.NOT_READABLE;

        ExceptionResponse response =
                createErrorResponse(servletRequest, exceptionCode, Map.of("message", exc.getMessage()));

        logError(exc, exceptionCode);

        return ResponseEntity.status(exceptionCode.getStatus()).body(response);
    }

    @Override
    protected ResponseEntity<Object> handleHandlerMethodValidationException(
            HandlerMethodValidationException exc,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request) {
        HttpServletRequest servletRequest = ((ServletWebRequest) request).getRequest();
        ExceptionCode exceptionCode = GlobalExceptionCode.INVALID_REQUEST;
        Map<String, String> details = createMethodValidationErrors(exc);

        ExceptionResponse response = createErrorResponse(servletRequest, exceptionCode, details);

        logError(exc, exceptionCode);

        return ResponseEntity.status(exceptionCode.getStatus()).body(response);
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ResponseEntity<ExceptionResponse> handleAccessDeniedException(
            AccessDeniedException exc, HttpServletRequest request) {
        ExceptionCode exceptionCode = GlobalExceptionCode.FORBIDDEN;

        ExceptionResponse response = createErrorResponse(request, exceptionCode, null);

        logError(exc, exceptionCode);

        return ResponseEntity.status(exceptionCode.getStatus()).body(response);
    }

    private Map<String, String> createMethodValidationErrors(HandlerMethodValidationException exc) {
        return exc.getParameterValidationResults().stream()
                .collect(
                        Collectors.toMap(
                                result -> result.getResolvableErrors().get(0).getCodes()[1],
                                result -> result.getResolvableErrors().get(0).getDefaultMessage(),
                                (existing, replacement) -> existing));
    }

    private ExceptionResponse createErrorResponse(
            HttpServletRequest request, ExceptionCode exceptionCode, Map<String, String> details) {
        return ExceptionResponse.builder()
                .httpMethod(request.getMethod())
                .httpStatus(exceptionCode.getStatus().value())
                .errorCode(exceptionCode.getCode())
                .message(exceptionCode.getMessage())
                .path(request.getRequestURI())
                .details(details)
                .build();
    }

    private void logError(Exception exception, ExceptionCode exceptionCode) {
        log.error(
                "Exception occurred - Message: {}, Code: {}",
                exception.getMessage(),
                exceptionCode.getCode());
    }
}

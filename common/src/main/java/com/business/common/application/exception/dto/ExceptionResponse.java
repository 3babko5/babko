package com.business.common.application.exception.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.Map;

@Builder
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@ToString
public class ExceptionResponse {
    private String httpMethod;
    private int httpStatus;
    private String errorCode;
    @Builder.Default private String timestamp = LocalDateTime.now().toString();
    private String message;
    private Map<String, String> details;
    private String path;
}

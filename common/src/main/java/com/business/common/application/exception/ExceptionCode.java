package com.business.common.application.exception;

import org.springframework.http.HttpStatusCode;

public interface ExceptionCode {
    HttpStatusCode getStatus();

    String getMessage();

    String getCode();
}

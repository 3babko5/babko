package com.business.user.application.exception;

import com.business.common.application.exception.ExceptionCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum DeliveryDriverErrorCode implements ExceptionCode {

  DRIVER_ALREADY_EXISTS(HttpStatus.CONFLICT, "해당 사용자는 이미 배송 담당자로 등록되어 있습니다."),
  HUB_NOT_FOUND(HttpStatus.NOT_FOUND, "요청한 소속 허브를 찾을 수 없습니다."),
  USER_NOT_FOUND(HttpStatus.NOT_FOUND, "요청한 사용자 정보를 찾을 수 없습니다."),
  INVALID_DRIVER_TYPE(HttpStatus.BAD_REQUEST, "허브 배송 담당자는 허브 ID가 필요합니다."),
  INVALID_REQUEST(HttpStatus.BAD_REQUEST, "입력 데이터가 올바르지 않습니다.");

  private final HttpStatus status;
  private final String message;
  private final String code = this.name();
}

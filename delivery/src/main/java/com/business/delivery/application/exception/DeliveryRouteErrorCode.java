package com.business.delivery.application.exception;

import com.business.common.application.exception.ExceptionCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum DeliveryRouteErrorCode implements ExceptionCode {

  ROUTE_NOT_FOUND(HttpStatus.NOT_FOUND, "배송 경로를 찾을 수 없습니다."),
  INVALID_ROUTE_SEQUENCE(HttpStatus.BAD_REQUEST, "배송 경로 순서가 올바르지 않습니다."),
  DUPLICATE_DELIVERY_ROUTE(HttpStatus.CONFLICT, "이미 존재하는 배송 경로입니다."),
  INVALID_DELIVERY_STATUS(HttpStatus.BAD_REQUEST, "배송 상태가 올바르지 않습니다."),
  NO_AVAILABLE_ROUTE(HttpStatus.NOT_FOUND, "사용 가능한 배송 경로가 없습니다.");

  private final HttpStatus status;
  private final String message;
  private final String code = this.name();
}

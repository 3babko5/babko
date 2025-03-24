package com.business.user.deliverydriver.application.exception;

import com.business.common.application.exception.ExceptionCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum DeliveryDriverErrorCode implements ExceptionCode {

  // 배송 담당자
  DRIVER_ALREADY_EXISTS(HttpStatus.CONFLICT, "해당 사용자는 이미 배송 담당자로 등록되어 있습니다."),
  MAX_HUB_DRIVERS_EXCEEDED(HttpStatus.BAD_REQUEST, "허브 배송 담당자는 최대 10명까지만 등록 가능합니다."),
  MAX_COMPANY_DRIVERS_PER_HUB_EXCEEDED(HttpStatus.BAD_REQUEST, "해당 허브에는 최대 10명의 업체 배송 담당자만 추가할 수 있습니다."),
  INVALID_DRIVER_TYPE(HttpStatus.BAD_REQUEST, "허브 배송 담당자는 허브 ID가 없어야 합니다."),
  INVALID_HUB_FOR_COMPANY_DRIVER(HttpStatus.BAD_REQUEST, "업체 배송 담당자는 허브 ID가 필요합니다."),
  HUB_NOT_FOUND(HttpStatus.NOT_FOUND, "요청한 소속 허브를 찾을 수 없습니다."),
  USER_NOT_FOUND(HttpStatus.NOT_FOUND, "요청한 사용자 정보를 찾을 수 없습니다."),
  INVALID_REQUEST(HttpStatus.BAD_REQUEST, "입력 데이터가 올바르지 않습니다."),
  DELIVERY_ROUTE_NOT_ASSIGNED(HttpStatus.NOT_FOUND, "배송 경로가 아직 할당되지 않았습니다."),
  DELIVERY_ROUTE_NOT_FOUND(HttpStatus.NOT_FOUND, "배송 담당자에게 할당된 배송 경로 정보를 찾을 수 없습니다."),
  NO_AVAILABLE_DRIVER(HttpStatus.NOT_FOUND, "배정할 담당자가 없습니다."),
  DELIVERY_DRIVER_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 배송 담당자입니다."),
  DRIVER_CANCEL_ERROR(HttpStatus.SERVICE_UNAVAILABLE, "배송 담당자 취소 상태 업데이트에 실패했습니다."),

  // 외부 API
  EXTERNAL_DELIVERY_NOT_FOUND(HttpStatus.NOT_FOUND, "외부 배송 정보를 찾을 수 없습니다."),
  EXTERNAL_DELIVERY_ROUTE_NOT_FOUND(HttpStatus.NOT_FOUND, "외부 배송 경로 정보를 찾을 수 없습니다."),
  EXTERNAL_HUB_NOT_FOUND(HttpStatus.NOT_FOUND, "외부 허브 정보를 찾을 수 없습니다.");

  private final HttpStatus status;
  private final String message;
  private final String code = this.name();
}
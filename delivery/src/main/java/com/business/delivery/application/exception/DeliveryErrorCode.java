package com.business.delivery.application.exception;

import com.business.common.application.exception.ExceptionCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum DeliveryErrorCode implements ExceptionCode {

  // 배송
  DELIVERY_NOT_FOUND(HttpStatus.NOT_FOUND, "배송 정보를 찾을 수 없습니다."),
  DELIVERY_ALREADY_DELETED(HttpStatus.BAD_REQUEST, "이미 삭제된 배송입니다."),

  // 배송 경로
  DELIVERY_ROUTE_NOT_FOUND(HttpStatus.NOT_FOUND, "배송 경로를 찾을 수 없습니다."),
  INVALID_ROUTE_SEQUENCE(HttpStatus.BAD_REQUEST, "배송 경로 순서가 올바르지 않습니다."),
  DUPLICATE_DELIVERY_ROUTE(HttpStatus.CONFLICT, "이미 존재하는 배송 경로입니다."),
  INVALID_DELIVERY_STATUS(HttpStatus.BAD_REQUEST, "배송 상태가 올바르지 않습니다."),
  NO_AVAILABLE_ROUTE(HttpStatus.NOT_FOUND, "사용 가능한 배송 경로가 없습니다."),

  // 외부 API
  HUB_MOVEMENT_NOT_AVAILABLE(HttpStatus.NOT_FOUND, "허브 간 이동 정보를 가져올 수 없습니다."),
  HUB_COORDINATE_NOT_AVAILABLE(HttpStatus.NOT_FOUND, "허브 좌표 정보를 가져올 수 없습니다."),
  NAVER_API_ERROR(HttpStatus.SERVICE_UNAVAILABLE, "네이버 API 호출 중 오류가 발생했습니다."),
  DRIVER_CANCEL_ERROR(HttpStatus.SERVICE_UNAVAILABLE, "배송 담당자 취소 서비스와의 통신 중 오류가 발생했습니다.");


  private final HttpStatus status;
  private final String message;
  private final String code = this.name();
}

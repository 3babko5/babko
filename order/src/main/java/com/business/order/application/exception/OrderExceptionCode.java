package com.business.order.application.exception;

import com.business.common.application.exception.ExceptionCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum OrderExceptionCode implements ExceptionCode {

    DELIVERY_REQUEST_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "배송 생성 요청 실패"),
    DELIVERY_STATUS_NOT_FOUND(HttpStatus.NOT_FOUND, "배송 상태를 찾을 수 없습니다."),

    PRODUCT_QUANTITY_EXCEEDED(HttpStatus.BAD_REQUEST, "해당상품이 품절되었습니다."),
    ORDER_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 주문은 없는 주문입니다");



    private final HttpStatus status;
    private final String message;
    private final String code = this.name();
}

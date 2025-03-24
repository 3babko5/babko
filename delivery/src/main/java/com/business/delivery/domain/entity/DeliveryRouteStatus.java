package com.business.delivery.domain.entity;

import com.business.common.application.exception.BusinessLogicException;
import com.business.delivery.application.exception.DeliveryErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum DeliveryRouteStatus {

  PENDING(0, "기본 상태"),
  IN_TRANSIT_TO_HUB(10, "출고 확인 후, 해당 구간 출발"),
  ARRIVED_AT_HUB(20, "허브 도착"),
  OUT_FOR_DELIVERY(30, "최종 목적지로 이동 중"),
  DELIVERED(40, "해당 구간 배송 완료"),
  CANCELED(-1, "배송 취소");

  private final int step;
  private final String description;

  public boolean isTerminal() {

    return this == DELIVERED || this == CANCELED;
  }

  public void validateTransition(DeliveryRouteStatus newStatus) {
    if (newStatus == DELIVERED || newStatus == CANCELED) {
      if (this.isTerminal()) {
        throw new BusinessLogicException(DeliveryErrorCode.INVALID_STATUS_LASTCHANGE);
      }
      return;
    }

    if (!newStatus.canTransitionFrom(this)) {
      throw new BusinessLogicException(DeliveryErrorCode.INVALID_ROUTE_STATUS_TRANSITION);
    }
  }

  public boolean canTransitionFrom(DeliveryRouteStatus currentStatus) {

    if (this == CANCELED || this == DELIVERED) {
      return false;
    }
    return this.step > currentStatus.step;
  }
}


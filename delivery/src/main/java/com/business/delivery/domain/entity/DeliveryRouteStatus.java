package com.business.delivery.domain.entity;

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
  CANCELED(-1, "취소됨");

  private final int step;
  private final String description;

  public boolean isProgressableFrom(DeliveryRouteStatus currentStatus) {
    if (this == CANCELED || this == DELIVERED) {
      return false;
    }
    return this.step > currentStatus.step;
  }

  public boolean isTerminal() {
    return this == DELIVERED || this == CANCELED;
  }
}


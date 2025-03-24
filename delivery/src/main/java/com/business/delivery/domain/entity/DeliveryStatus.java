package com.business.delivery.domain.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum DeliveryStatus {

  CANCELED,
  WAITING_AT_HUB,
  OUT_FOR_DELIVERY,
  DELIVERED;

  public boolean isTerminal() {

    return this == DELIVERED || this == CANCELED;
  }
}

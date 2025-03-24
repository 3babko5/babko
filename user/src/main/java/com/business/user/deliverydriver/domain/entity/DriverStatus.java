package com.business.user.deliverydriver.domain.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum DriverStatus {

    ASSIGNED(0, "배송지 할당"),
    IN_TRANSIT_TO_HUB(10, "허브 이동 중"),
    OUT_FOR_DELIVERY(20, "최종 목적지 이동 중"),
    DELIVERED(30, "배달 완료"),
    CANCELED(-1, "취소됨");

    private final int step;
    private final String description;

    public boolean isProgressableFrom(DriverStatus currentStatus) {
        if (this == DELIVERED || this == CANCELED) {
            return false;
        }
        return this.step > currentStatus.step;
    }

    public boolean isTerminal() {
        return this == DELIVERED || this == CANCELED;
    }
}

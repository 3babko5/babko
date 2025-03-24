package com.business.user.deliverydriver.domain.entity;

import com.business.common.application.exception.BusinessLogicException;
import com.business.user.deliverydriver.application.exception.DeliveryDriverErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum DriverStatus {

    ASSIGNED(0, "배송 담당자 배정 완료"),
    IN_TRANSIT_TO_HUB(10, "허브 이동 중"),
    OUT_FOR_DELIVERY(20, "최종 목적지 이동 중"),
    DELIVERED(30, "배달 완료"),
    CANCELED(-1, "배송 취소");

    private final int step;
    private final String description;

    public boolean isTerminal() {
        return this == DELIVERED || this == CANCELED;
    }

    public void validateTransition(DriverStatus newStatus) {

        if (!newStatus.canTransitionFrom(this)) {
            throw new BusinessLogicException(DeliveryDriverErrorCode.INVALID_DRIVER_STATUS_TRANSITION);
        }
    }

    public boolean canTransitionFrom(DriverStatus currentStatus) {

        if (this == CANCELED || this == DELIVERED) {
            return false;
        }
        return this.step > currentStatus.step;
    }
}

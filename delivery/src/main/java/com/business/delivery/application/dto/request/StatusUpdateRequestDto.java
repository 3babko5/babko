package com.business.delivery.application.dto.request;

import com.business.delivery.domain.entity.DeliveryRouteStatus;
import com.business.delivery.domain.entity.DeliveryStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StatusUpdateRequestDto {

    private DeliveryStatus deliveryStatus;
    private DeliveryRouteStatus deliveryRouteStatus;
}

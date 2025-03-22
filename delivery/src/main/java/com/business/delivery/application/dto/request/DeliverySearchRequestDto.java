package com.business.delivery.application.dto.request;

import com.business.delivery.domain.entity.DeliveryRouteStatus;
import com.business.delivery.domain.entity.DeliveryStatus;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DeliverySearchRequestDto {

    private Integer page = 1;
    private Integer size = 10;
    private String orderBy = "createdAt";
    private String sort = "desc";

    private UUID orderId;
    private UUID startHubId;
    private UUID endHubId;
    private UUID originHubId;
    private UUID destinationHubId;
    private DeliveryStatus deliveryStatus;
    private DeliveryRouteStatus deliveryRouteStatus;
}

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
public class SearchRequestDto {

    @Builder.Default
    private Integer page = 1;

    @Builder.Default
    private Integer size = 10;

    @Builder.Default
    private String orderBy = "createdAt";

    @Builder.Default
    private String sort = "desc";

    private UUID orderId;
    private UUID startHubId;
    private UUID endHubId;
    private UUID originHubId;
    private UUID destinationHubId;
    private DeliveryStatus deliveryStatus;
    private DeliveryRouteStatus deliveryRouteStatus;
}

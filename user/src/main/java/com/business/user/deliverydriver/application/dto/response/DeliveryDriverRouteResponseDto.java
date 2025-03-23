package com.business.user.deliverydriver.application.dto.response;

import java.math.BigDecimal;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DeliveryDriverRouteResponseDto {

    private UUID originHubId;
    private UUID destinationHubId;
    private String deliveryAddress;
    private Long routeSequence;
    private String deliveryRouteStatus;
    private BigDecimal estimatedDistance;
    private Long estimatedTime;
    private BigDecimal actualDistance;
    private Long actualTime;
}

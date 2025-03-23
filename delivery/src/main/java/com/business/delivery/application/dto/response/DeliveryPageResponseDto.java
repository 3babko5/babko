package com.business.delivery.application.dto.response;

import com.business.delivery.domain.entity.DeliveryRouteStatus;
import com.business.delivery.domain.entity.DeliveryStatus;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DeliveryPageResponseDto {

    private UUID deliveryId;
    private UUID orderId;
    private DeliveryStatus deliveryStatus;
    private UUID startHubId;
    private UUID endHubId;
    private String deliveryAddress;
    private UUID recipientId;
    private UUID recipientSlackId;
    private String createdAt;
    private String updatedAt;
    private List<DeliveryRoutePageResponseDto> deliveryRoutes;

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class DeliveryRoutePageResponseDto {

        private UUID deliveryRouteId;
        private Long routeSequence;
        private UUID originHubId;
        private UUID destinationHubId;
        private BigDecimal estimatedDistance;
        private Long estimatedTime;
        private BigDecimal actualDistance;
        private Long actualTime;
        private DeliveryRouteStatus deliveryRouteStatus;
        private String createdAt;
        private String updatedAt;
    }
}

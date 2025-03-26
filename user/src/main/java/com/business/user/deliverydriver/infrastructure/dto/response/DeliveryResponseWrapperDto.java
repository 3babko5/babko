package com.business.user.deliverydriver.infrastructure.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Getter
@NoArgsConstructor
public class DeliveryResponseWrapperDto {
    private UUID deliveryId;
    private String deliveryStatus;
    private List<DeliveryClientResponseDto> deliveryRoutes;
}

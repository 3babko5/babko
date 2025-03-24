package com.business.order.application.dto.mapper;

import com.business.order.infrastructure.dto.request.CreateDeliveryRequestDto;
import com.business.order.infrastructure.dto.request.OrderDeliveryRequestDto;

public class DeliveryMapper {

    public static CreateDeliveryRequestDto toCreateDeliveryRequestDto(OrderDeliveryRequestDto dto) {
        return CreateDeliveryRequestDto.builder()
                .orderId(dto.getOrderId())
                .startHubId(dto.getOriginHubId())
                .endHubId(dto.getDestinationHubId())
                .deliveryAddress(dto.getDeliveryAddress())
                .recipientId(dto.getReceiverId())
                .build();
    }
}

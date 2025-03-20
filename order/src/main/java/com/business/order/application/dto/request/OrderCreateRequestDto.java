package com.business.order.application.dto.request;

import com.business.order.domain.entity.Order;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderCreateRequestDto {

    private UUID receiverId;
    private String deliveryAddress;
    private List<OrderItemRequestDto> items;

    public Order createOrder(Long userId, UUID originHubId, UUID destinationHubId) {
        return Order.create(
                userId,
                receiverId,
                deliveryAddress,
                originHubId,
                destinationHubId,
                25000
        );
    }

}

package com.business.order.infrastructure.dto.request;

import com.business.order.domain.entity.Order;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder
@AllArgsConstructor
public class OrderDeliveryRequestDto {

    private UUID orderId; //주문id
    private Long userId; //수령인id
    private UUID receiverId; //수령업체id
    private String deliveryAddress; //배송지주소
    private UUID originHubId; //출발허브id
    private UUID destinationHubId; //도착허브id

    public static OrderDeliveryRequestDto fromOrder(Order order) {
        return OrderDeliveryRequestDto.builder()
                .orderId(order.getOrderId())
                .userId(order.getUserId())
                .receiverId(order.getReceiverId())
                .deliveryAddress(order.getDeliveryAddress())
                .originHubId(order.getOriginHubId())
                .destinationHubId(order.getDestinationHubId())
                .build();
    }

}

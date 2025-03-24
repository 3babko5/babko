package com.business.order.infrastructure.dto.request;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class CreateDeliveryRequestDto {

    private UUID orderId;
    private UUID startHubId;       // 출발 허브
    private UUID endHubId;         // 도착 허브
    private String deliveryAddress;
    private UUID recipientId;      // 수령인 (receiverId에 해당)
}

package com.business.order.infrastructure.dto.request;

import lombok.*;

import java.util.UUID;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class DeliverySearchForOrderRequestDto {

    @Builder.Default
    private Integer page = 1;

    @Builder.Default
    private Integer size = 10;

    @Builder.Default
    private String orderBy = "createdAt";

    @Builder.Default
    private String sort = "desc";

    private UUID orderId;

    public static DeliverySearchForOrderRequestDto fromOrderId(UUID orderId) {
        return DeliverySearchForOrderRequestDto.builder()
                .orderId(orderId)
                .build();
    }
}

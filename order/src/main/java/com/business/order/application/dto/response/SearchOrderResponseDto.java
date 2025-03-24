package com.business.order.application.dto.response;

import com.business.order.domain.entity.OrderItem;
import com.business.order.domain.entity.OrderStatus;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.UUID;

@Getter
@Builder
public class SearchOrderResponseDto {
    private final String message;
    private final int stateCode;
    private final List<OrderData> order;
    private final PageInfo pageInfo;

    @Getter
    @Builder
    public static class OrderData {
        private UUID orderId;
        private Long userId;
        private UUID receiverId;
        private String deliveryAddress;
        private Integer totalPrice;
        private OrderStatus orderStatus;
//        private List<OrderItem> orderItems;
    }

    @Getter
    @Builder
    public static class PageInfo {
        private final int page;
        private final int size;
        private final long totalElements;
        private final int totalPages;
        private final boolean isLast;
        private final String sort;
        private final String direction;
    }
}

package com.business.order.application.dto.request;

import com.business.order.domain.entity.Order;
import com.business.order.domain.entity.OrderItem;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemRequestDto {
    private UUID productId;
    private UUID supplierId;
    private Integer orderItemAmount;
    private Long orderItemPrice;

    public void setSupplierId(UUID supplierId) {
        this.supplierId = supplierId;
    }

    public void setOrderItemPrice(Long orderItemPrice) {
        this.orderItemPrice = orderItemPrice;
    }

    public OrderItem createOrderItem(Order order) {
        return OrderItem.create(order, productId, supplierId, orderItemAmount, orderItemPrice);
    }
}

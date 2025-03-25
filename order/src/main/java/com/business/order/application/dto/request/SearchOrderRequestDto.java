package com.business.order.application.dto.request;

import com.business.order.domain.entity.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SearchOrderRequestDto {
    private UUID orderId;
    private OrderStatus orderStatus;
    private Integer page = 1;
    private Integer size = 10;
    private String orderBy = "CREATED";
    private String sort = "desc";
}

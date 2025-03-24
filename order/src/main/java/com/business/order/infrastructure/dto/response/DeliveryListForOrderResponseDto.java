package com.business.order.infrastructure.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DeliveryListForOrderResponseDto<T> {
    private long totalElements;
    private int totalPages;
    private int currentPage;
    private int size;
    private List<T> data;
}

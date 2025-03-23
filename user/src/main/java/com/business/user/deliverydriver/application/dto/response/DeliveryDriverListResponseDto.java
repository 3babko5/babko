package com.business.user.deliverydriver.application.dto.response;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DeliveryDriverListResponseDto<T> {

    private long totalElements;
    private int totalPages;
    private int currentPage;
    private int size;
    private List<T> data;
}

package com.business.delivery.application.dto.response;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DeliveryPageListResponseDto<T> {

    private long totalElements;
    private int totalPages;
    private int currentPage;
    private int size;
    private List<T> data;
}

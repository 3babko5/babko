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
public class DeliveryPageWrapperResponseDto<T> {

    private long totalElements;
    private int totalPages;
    private int currentPage;
    private int size;
    private List<T> data;

    public static <T> DeliveryPageWrapperResponseDto<T> fromPage(Page<T> page) {

        return DeliveryPageWrapperResponseDto.<T>builder()
            .totalElements(page.getTotalElements())
            .totalPages(page.getTotalPages())
            .currentPage(page.getNumber() + 1)
            .size(page.getSize())
            .data(page.getContent())
            .build();
    }
}

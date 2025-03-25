package com.business.product.product.application.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.UUID;

@Getter
@Builder
public class SearchProductResponseDto {
    private final String message;
    private final int stateCode;
    private final List<ProductData> product;
    private final PageInfo pageInfo;

    @Getter
    @Builder
    public static class ProductData {
        private final UUID productId;
        private final String productName;
        private final Integer productPrice;
        private final UUID companyId;
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

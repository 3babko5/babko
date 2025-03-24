package com.business.order.infrastructure.dto.queryDto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SearchProductQueryDto {
    private UUID productId;
    private String productName;
    private UUID companyId;
    private Integer productQuantity;
    private Integer page;
    private Integer size;
    private String orderBy;
    private String sort;
}

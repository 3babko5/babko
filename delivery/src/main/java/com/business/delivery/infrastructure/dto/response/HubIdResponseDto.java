package com.business.delivery.infrastructure.dto.response;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HubIdResponseDto {

    private BigDecimal latitude;
    private BigDecimal longitude;
}

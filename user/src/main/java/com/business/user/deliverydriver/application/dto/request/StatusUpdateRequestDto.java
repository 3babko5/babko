package com.business.user.deliverydriver.application.dto.request;

import com.business.user.deliverydriver.domain.entity.DriverStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StatusUpdateRequestDto {

    private String deliveryRouteStatus;
}

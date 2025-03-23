package com.business.user.deliverydriver.application.dto.mapper;

import com.business.common.infrastructure.util.JpaUtil;
import com.business.user.deliverydriver.application.dto.request.CreateDeliveryDriverRequestDto;
import com.business.user.deliverydriver.application.dto.request.DeliveryDriverSearchRequestDto;
import com.business.user.deliverydriver.domain.entity.DeliveryDriver;
import org.springframework.data.domain.Pageable;

public class DeliveryDriverRequestMapper {

    public static DeliveryDriver createRequestToEntity(CreateDeliveryDriverRequestDto request, Long newSequence) {

        return DeliveryDriver.deliveryDriverCreateBuilder()
            .deliveryDriverId(request.getDeliveryDriverId())
            .hubId(request.getHubId())
            .slackId(request.getSlackId())
            .driverType(request.getDriverType())
            .deliverySequence(newSequence)
            .build();
    }

    public static Pageable SearchRequestDtoToPageable(DeliveryDriverSearchRequestDto request) {

        return JpaUtil.getNormalPageable(
            request.getPage(),
            request.getSize(),
            request.getOrderBy(),
            request.getSort()
        );
    }
}

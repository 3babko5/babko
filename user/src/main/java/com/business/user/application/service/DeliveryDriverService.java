package com.business.user.application.service;

import com.business.common.application.exception.BusinessLogicException;
import com.business.user.application.dto.mapper.DeliveryDriverMapper;
import com.business.user.application.dto.request.CreateDeliveryDriverRequestDto;
import com.business.user.application.dto.response.DeliveryDriverResponseDto;
import com.business.user.application.exception.DeliveryDriverErrorCode;
import com.business.user.domain.entity.DeliveryDriver;
import com.business.user.domain.entity.DriverType;
import com.business.user.domain.repository.DeliveryDriverRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class DeliveryDriverService {

  private final DeliveryDriverRepository deliveryDriverRepository;

  public DeliveryDriverResponseDto createDeliveryDriver(CreateDeliveryDriverRequestDto request) {

    if (deliveryDriverRepository.existsById(request.getDeliveryDriverId())) {
      throw new BusinessLogicException(DeliveryDriverErrorCode.DRIVER_ALREADY_EXISTS);
    }

    Long lastSequence = deliveryDriverRepository.findLastDeliverySequence().orElse(0L);
    Long newSequence = lastSequence + 1;

    if (request.getDriverType() == DriverType.HUB && request.getHubId() == null) {
      throw new BusinessLogicException(DeliveryDriverErrorCode.INVALID_DRIVER_TYPE);
    }

    DeliveryDriver deliveryDriver = DeliveryDriver.create(
        request.getDeliveryDriverId(),
        request.getHubId(),
        request.getSlackId(),
        request.getDriverType(),
        newSequence
    );

    deliveryDriverRepository.save(deliveryDriver);

    return DeliveryDriverMapper.toDto(deliveryDriver);
  }
}

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

    if (request.getDriverType() == DriverType.HUB && request.getHubId() != null) {
      throw new BusinessLogicException(DeliveryDriverErrorCode.INVALID_DRIVER_TYPE);
    }

    if (request.getDriverType() == DriverType.COMPANY && request.getHubId() == null) {
      throw new BusinessLogicException(DeliveryDriverErrorCode.INVALID_HUB_FOR_COMPANY_DRIVER);
    }

    if (request.getDriverType() == DriverType.HUB) {
      long hubDriverCount = deliveryDriverRepository.countByDriverType(DriverType.HUB);
      if (hubDriverCount >= 10) {
        throw new BusinessLogicException(DeliveryDriverErrorCode.MAX_HUB_DRIVERS_EXCEEDED);
      }
    }

    if (request.getDriverType() == DriverType.COMPANY) {
      long companyDriverCount = deliveryDriverRepository.countByHubIdAndDriverType(request.getHubId(), DriverType.COMPANY);
      if (companyDriverCount >= 10) {
        throw new BusinessLogicException(DeliveryDriverErrorCode.MAX_COMPANY_DRIVERS_PER_HUB_EXCEEDED);
      }
    }

    Long newSequence = switch (request.getDriverType()) {
      case HUB -> deliveryDriverRepository.findLastDeliverySequenceForHubDrivers().orElse(0L) + 1;
      case COMPANY -> deliveryDriverRepository.findLastDeliverySequenceForCompanyDrivers(request.getHubId()).orElse(0L) + 1;
    };

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

package com.business.user.deliverydriver.application.service;

import com.business.common.application.exception.BusinessLogicException;
import com.business.user.deliverydriver.application.dto.mapper.DeliveryDriverMapper;
import com.business.user.deliverydriver.application.dto.request.CreateDeliveryDriverRequestDto;
import com.business.user.deliverydriver.application.dto.response.AssignDeliveryDriverResponseDto;
import com.business.user.deliverydriver.application.dto.response.DeliveryDriverResponseDto;
import com.business.user.deliverydriver.application.exception.DeliveryDriverErrorCode;
import com.business.user.deliverydriver.domain.entity.DeliveryDriver;
import com.business.user.deliverydriver.domain.entity.DriverType;
import com.business.user.deliverydriver.domain.repository.DeliveryDriverRepository;
import com.business.user.deliverydriver.infrastructure.client.DeliveryClient;
import com.business.user.deliverydriver.infrastructure.client.HubClient;
import com.business.user.deliverydriver.infrastructure.dto.response.DeliveryClientResponseDto;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class DeliveryDriverService {

  private final DeliveryDriverRepository deliveryDriverRepository;
  private final HubClient hubClient;
  private final DeliveryClient deliveryClient;

  public DeliveryDriverResponseDto createDeliveryDriver(CreateDeliveryDriverRequestDto request) {

    if (deliveryDriverRepository.existsById(request.getDeliveryDriverId())) {
      throw new BusinessLogicException(DeliveryDriverErrorCode.DRIVER_ALREADY_EXISTS);
    }

    if (request.getDriverType() == DriverType.HUB && request.getHubId() != null) {
      throw new BusinessLogicException(DeliveryDriverErrorCode.INVALID_DRIVER_TYPE);
    }

    if (request.getDriverType() == DriverType.COMPANY) {
      if (request.getHubId() == null || !hubClient.existsByHubId(request.getHubId().toString())) {
        throw new BusinessLogicException(DeliveryDriverErrorCode.HUB_NOT_FOUND);
      }
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

    DeliveryDriver deliveryDriver =
        DeliveryDriverMapper.createRequestToEntity(request, newSequence);

    deliveryDriverRepository.save(deliveryDriver);

    return DeliveryDriverMapper.toDto(deliveryDriver);
  }

  public List<AssignDeliveryDriverResponseDto> assignDriversForDelivery(UUID deliveryId) {

    List<DeliveryClientResponseDto> routes = deliveryClient.getRoutesByDeliveryId(deliveryId);

    List<AssignDeliveryDriverResponseDto> assignedDrivers = new ArrayList<>();

    for (DeliveryClientResponseDto route : routes) {
      Long assignedDriverId;

      if (isLastDestination(route)) {
        assignedDriverId = assignNextDeliveryDriver(DriverType.COMPANY, route.getDestinationHubId());
      } else {
        assignedDriverId = assignNextDeliveryDriver(DriverType.HUB, route.getOriginHubId());
      }

      DeliveryDriver assignedDriver = saveAssignedDriver(assignedDriverId, route.getDeliveryRouteId(), route.getRouteSequence());
      assignedDrivers.add(DeliveryDriverMapper.toAssignedDto(assignedDriver));
    }
    return assignedDrivers;
  }

  private DeliveryDriver saveAssignedDriver(Long deliveryDriverId, UUID deliveryRouteId, Long routeSequence) {
    DeliveryDriver driver = deliveryDriverRepository.findById(deliveryDriverId)
        .orElseThrow(() -> new BusinessLogicException(DeliveryDriverErrorCode.NO_AVAILABLE_DRIVER));

    driver.assignToRoute(deliveryRouteId, routeSequence);

    return deliveryDriverRepository.save(driver);
  }

  private Long assignNextDeliveryDriver(DriverType driverType, UUID hubId) {
    Optional<DeliveryDriver> lastAssignedDriver =
        driverType == DriverType.COMPANY
            ? deliveryDriverRepository.findLastAssignedDriverByTypeAndHub(driverType, hubId)
            : deliveryDriverRepository.findLastAssignedDriver();

    if (lastAssignedDriver.isPresent()) {
      Long currentSequence = lastAssignedDriver.get().getDeliverySequence();

      Optional<DeliveryDriver> nextDriver =
          driverType == DriverType.COMPANY
              ? deliveryDriverRepository.findNextAvailableDriverByTypeAndHub(currentSequence, driverType, hubId)
              : deliveryDriverRepository.findNextAvailableDriver(currentSequence);

      if (nextDriver.isPresent()) {
        return updateAssignedDriver(nextDriver.get()).getDeliveryDriverId();
      }
    }

    return deliveryDriverRepository.findFirstAvailableDriver()
        .map(this::updateAssignedDriver)
        .map(DeliveryDriver::getDeliveryDriverId)
        .orElseThrow(() -> new BusinessLogicException(DeliveryDriverErrorCode.NO_AVAILABLE_DRIVER));
  }

  private DeliveryDriver updateAssignedDriver(DeliveryDriver driver) {
    driver.updateAssignAt(LocalDateTime.now());
    return driver;
  }

  private boolean isLastDestination(DeliveryClientResponseDto route) {
    return route.getDeliveryAddress() != null;
  }
}


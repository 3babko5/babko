package com.business.user.deliverydriver.application.service;

import com.business.common.application.exception.BusinessLogicException;
import com.business.user.deliverydriver.application.dto.mapper.DeliveryDriverMapper;
import com.business.user.deliverydriver.application.dto.request.CreateDeliveryDriverRequestDto;
import com.business.user.deliverydriver.application.dto.response.AssignDeliveryDriverResponseDto;
import com.business.user.deliverydriver.application.dto.response.DeliveryDriverResponseDto;
import com.business.user.deliverydriver.application.exception.DeliveryDriverErrorCode;
import com.business.user.deliverydriver.domain.entity.DeliveryDriver;
import com.business.user.deliverydriver.domain.entity.DriverType;
import com.business.user.deliverydriver.infrastructure.client.DeliveryRouteClient;
import com.business.user.deliverydriver.infrastructure.client.HubClientService;
import com.business.user.deliverydriver.infrastructure.dto.response.DeliveryRouteClientResponseDto;
import com.business.user.deliverydriver.infrastructure.repository.DeliveryDriverJpaRepository;
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

  private final DeliveryDriverJpaRepository deliveryDriverJpaRepository;
  private final HubClientService hubClientService;
  private final DeliveryRouteClient deliveryRouteClient;

  /**
   * 🚀 배송 담당자 생성
   */
  public DeliveryDriverResponseDto createDeliveryDriver(CreateDeliveryDriverRequestDto request) {

    if (deliveryDriverJpaRepository.existsById(request.getDeliveryDriverId())) {
      throw new BusinessLogicException(DeliveryDriverErrorCode.DRIVER_ALREADY_EXISTS);
    }

    if (request.getDriverType() == DriverType.HUB && request.getHubId() != null) {
      throw new BusinessLogicException(DeliveryDriverErrorCode.INVALID_DRIVER_TYPE);
    }

    if (request.getDriverType() == DriverType.COMPANY) {
      hubClientService.validateHubId(request.getHubId());
    }

    if (request.getDriverType() == DriverType.HUB) {
      long hubDriverCount = deliveryDriverJpaRepository.countByDriverType(DriverType.HUB);
      if (hubDriverCount >= 10) {
        throw new BusinessLogicException(DeliveryDriverErrorCode.MAX_HUB_DRIVERS_EXCEEDED);
      }
    }

    if (request.getDriverType() == DriverType.COMPANY) {
      long companyDriverCount = deliveryDriverJpaRepository.countByHubIdAndDriverType(request.getHubId(), DriverType.COMPANY);
      if (companyDriverCount >= 10) {
        throw new BusinessLogicException(DeliveryDriverErrorCode.MAX_COMPANY_DRIVERS_PER_HUB_EXCEEDED);
      }
    }

    Long newSequence = switch (request.getDriverType()) {
      case HUB -> deliveryDriverJpaRepository.findLastDeliverySequenceForHubDrivers().orElse(0L) + 1;
      case COMPANY -> deliveryDriverJpaRepository.findLastDeliverySequenceForCompanyDrivers(request.getHubId()).orElse(0L) + 1;
    };

    DeliveryDriver deliveryDriver = DeliveryDriver.create(
        request.getDeliveryDriverId(),
        request.getHubId(),
        request.getSlackId(),
        request.getDriverType(),
        newSequence,
        null,
        null
    );

    deliveryDriverJpaRepository.save(deliveryDriver);
    return DeliveryDriverMapper.toDto(deliveryDriver);
  }

  /**
   * 특정 배송 ID에 대해 전체 배송 경로 조회 후, 배송 담당자 배정
   */
  public List<AssignDeliveryDriverResponseDto> assignDriversForDelivery(UUID deliveryId) {

    List<DeliveryRouteClientResponseDto> routes = deliveryRouteClient.getRoutesByDeliveryId(deliveryId);

    List<AssignDeliveryDriverResponseDto> assignedDrivers = new ArrayList<>();

    for (DeliveryRouteClientResponseDto route : routes) {
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

  /**
   * 배정된 담당자를 배송 담당자 테이블에 저장하고 반환
   */
  private DeliveryDriver saveAssignedDriver(Long deliveryDriverId, UUID deliveryRouteId, Long routeSequence) {
    DeliveryDriver driver = deliveryDriverJpaRepository.findById(deliveryDriverId)
        .orElseThrow(() -> new BusinessLogicException(DeliveryDriverErrorCode.NO_AVAILABLE_DRIVER));

    driver.assignToRoute(deliveryRouteId, routeSequence);

    return deliveryDriverJpaRepository.save(driver);
  }

  /**
   * 허브 담당자 & 업체 담당자 배정 (순차적 로테이션)
   */
  private Long assignNextDeliveryDriver(DriverType driverType, UUID hubId) {
    Optional<DeliveryDriver> lastAssignedDriver =
        driverType == DriverType.COMPANY
            ? deliveryDriverJpaRepository.findLastAssignedDriverByTypeAndHub(driverType, hubId)
            : deliveryDriverJpaRepository.findLastAssignedDriver();

    if (lastAssignedDriver.isPresent()) {
      Long currentSequence = lastAssignedDriver.get().getDeliverySequence();

      Optional<DeliveryDriver> nextDriver =
          driverType == DriverType.COMPANY
              ? deliveryDriverJpaRepository.findNextAvailableDriverByTypeAndHub(currentSequence, driverType, hubId)
              : deliveryDriverJpaRepository.findNextAvailableDriver(currentSequence);

      if (nextDriver.isPresent()) {
        return updateAssignedDriver(nextDriver.get()).getDeliveryDriverId();
      }
    }

    return deliveryDriverJpaRepository.findFirstAvailableDriver()
        .map(this::updateAssignedDriver)
        .map(DeliveryDriver::getDeliveryDriverId)
        .orElseThrow(() -> new BusinessLogicException(DeliveryDriverErrorCode.NO_AVAILABLE_DRIVER));
  }

  /**
   * 담당자 할당한 시간 업데이트
   */
  private DeliveryDriver updateAssignedDriver(DeliveryDriver driver) {
    driver.updateAssignAt(LocalDateTime.now());
    return driver;
  }

  /**
   * 마지막 배송 경로인지 확인 (배송지면 업체 담당자 배정)
   */
  private boolean isLastDestination(DeliveryRouteClientResponseDto route) {
    return route.getDeliveryAddress() != null;
  }
}


package com.business.user.deliverydriver.domain.repository;

import com.business.user.deliverydriver.application.dto.request.DeliveryDriverSearchRequestDto;
import com.business.user.deliverydriver.domain.entity.DeliveryDriver;
import com.business.user.deliverydriver.domain.entity.DriverType;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface DeliveryDriverRepository {

  boolean existsById(Long id);
  DeliveryDriver save(DeliveryDriver deliveryDriver);
  long countByDriverType(DriverType driverType);
  long countByHubIdAndDriverType(UUID hubId, DriverType driverType);
  Optional<Long> findLastDeliverySequenceForHubDrivers();
  Optional<Long> findLastDeliverySequenceForCompanyDrivers(UUID hubId);
  Optional<DeliveryDriver> findById(Long id);
  Optional<DeliveryDriver> findLastAssignedDriver();
  Optional<DeliveryDriver> findNextAvailableDriver(Long currentSequence);
  Optional<DeliveryDriver> findFirstAvailableDriver();
  Optional<DeliveryDriver> findLastAssignedDriverByTypeAndHub(DriverType driverType, UUID hubId);
  Optional<DeliveryDriver> findNextAvailableDriverByTypeAndHub(Long currentSequence, DriverType driverType, UUID hubId);
  Page<DeliveryDriver> findDeliveryDrivers(DeliveryDriverSearchRequestDto request, Pageable pageable);
}
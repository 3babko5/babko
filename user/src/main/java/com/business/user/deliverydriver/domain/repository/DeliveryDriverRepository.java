package com.business.user.deliverydriver.domain.repository;

import com.business.user.deliverydriver.domain.entity.DeliveryDriver;
import com.business.user.deliverydriver.domain.entity.DriverType;
import java.util.Optional;
import java.util.UUID;

public interface DeliveryDriverRepository {

  Optional<Long> findLastDeliverySequenceForHubDrivers();

  Optional<Long> findLastDeliverySequenceForCompanyDrivers(UUID hubId);

  long countByDriverType(DriverType driverType);

  long countByHubIdAndDriverType(UUID hubId, DriverType driverType);

  Optional<DeliveryDriver> findLastAssignedDriver();

  Optional<DeliveryDriver> findNextAvailableDriver(Long currentSequence);

  Optional<DeliveryDriver> findFirstAvailableDriver();

  Optional<DeliveryDriver> findLastAssignedDriverByTypeAndHub(DriverType driverType, UUID hubId);

  Optional<DeliveryDriver> findNextAvailableDriverByTypeAndHub(Long currentSequence, DriverType driverType, UUID hubId);
}
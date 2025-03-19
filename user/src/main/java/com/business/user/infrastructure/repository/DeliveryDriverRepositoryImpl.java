package com.business.user.infrastructure.repository;

import com.business.user.domain.entity.DeliveryDriver;
import com.business.user.domain.entity.DriverType;
import com.business.user.domain.repository.DeliveryDriverRepository;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Repository;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class DeliveryDriverRepositoryImpl implements DeliveryDriverRepository {

  private final DeliveryDriverJpaRepository deliveryDriverJpaRepository;

  @Override
  public Optional<Long> findLastDeliverySequenceForHubDrivers() {
    return deliveryDriverJpaRepository.findLastDeliverySequenceForHubDrivers();
  }

  @Override
  public Optional<Long> findLastDeliverySequenceForCompanyDrivers(UUID hubId) {
    return deliveryDriverJpaRepository.findLastDeliverySequenceForCompanyDrivers(hubId);
  }

  @Override
  public long countByDriverType(DriverType driverType) {
    return deliveryDriverJpaRepository.countByDriverType(driverType);
  }

  @Override
  public long countByHubIdAndDriverType(UUID hubId, DriverType driverType) {
    return deliveryDriverJpaRepository.countByHubIdAndDriverType(hubId, driverType);
  }

  @Override
  public Optional<DeliveryDriver> findLastAssignedDriver() {
    return deliveryDriverJpaRepository.findLastAssignedDriver();
  }

  @Override
  public Optional<DeliveryDriver> findNextAvailableDriver(Long currentSequence) {
    return deliveryDriverJpaRepository.findNextAvailableDriver(currentSequence);
  }

  @Override
  public Optional<DeliveryDriver> findFirstAvailableDriver() {
    return deliveryDriverJpaRepository.findFirstAvailableDriver();
  }

  @Override
  public Optional<DeliveryDriver> findLastAssignedDriverByTypeAndHub(DriverType driverType, UUID hubId) {
    return deliveryDriverJpaRepository.findLastAssignedDriverByTypeAndHub(driverType, hubId);
  }

  @Override
  public Optional<DeliveryDriver> findNextAvailableDriverByTypeAndHub(Long currentSequence, DriverType driverType, UUID hubId) {
    return deliveryDriverJpaRepository.findNextAvailableDriverByTypeAndHub(currentSequence, driverType, hubId);
  }
}

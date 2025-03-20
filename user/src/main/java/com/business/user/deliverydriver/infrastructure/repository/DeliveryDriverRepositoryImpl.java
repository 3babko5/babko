package com.business.user.deliverydriver.infrastructure.repository;

import com.business.user.deliverydriver.domain.entity.DeliveryDriver;
import com.business.user.deliverydriver.domain.entity.DriverType;
import com.business.user.deliverydriver.domain.repository.DeliveryDriverRepository;
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

  @Override
  public boolean existsById(Long id) {
    return deliveryDriverJpaRepository.existsById(id);
  }

  @Override
  public Optional<DeliveryDriver> findById(Long id) {
    return deliveryDriverJpaRepository.findById(id);
  }

  @Override
  public DeliveryDriver save(DeliveryDriver deliveryDriver) {
    return deliveryDriverJpaRepository.save(deliveryDriver);
  }
}

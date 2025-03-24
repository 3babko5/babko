package com.business.user.deliverydriver.infrastructure.repository;

import com.business.user.deliverydriver.domain.entity.DeliveryDriver;
import com.business.user.deliverydriver.domain.entity.DriverType;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface DeliveryDriverJpaRepository extends JpaRepository<DeliveryDriver, Long> {

  @Query("SELECT MAX(d.deliverySequence) FROM DeliveryDriver d WHERE d.driverType = 'HUB'")
  Optional<Long> findLastDeliverySequenceForHubDrivers();

  @Query("SELECT MAX(d.deliverySequence) FROM DeliveryDriver d WHERE d.hubId = :hubId AND d.driverType = 'COMPANY'")
  Optional<Long> findLastDeliverySequenceForCompanyDrivers(@Param("hubId") UUID hubId);

  Optional<DeliveryDriver> findFirstByAssignAtIsNotNullAndDeletedAtIsNullOrderByAssignAtDesc();

  Optional<DeliveryDriver> findFirstByDeliverySequenceGreaterThanAndDeletedAtIsNullOrderByDeliverySequenceAsc(Long currentSequence);

  Optional<DeliveryDriver> findFirstByDeletedAtIsNullOrderByDeliverySequenceAsc();

  Optional<DeliveryDriver> findFirstByAssignAtIsNotNullAndDeletedAtIsNullAndDriverTypeAndHubIdOrderByAssignAtDesc(DriverType driverType, UUID hubId);

  Optional<DeliveryDriver> findFirstByDeliverySequenceGreaterThanAndDeletedAtIsNullAndDriverTypeAndHubIdOrderByDeliverySequenceAsc(Long currentSequence, DriverType driverType, UUID hubId);

  long countByDriverType(DriverType driverType);

  long countByHubIdAndDriverType(UUID hubId, DriverType driverType);

  boolean existsByDeliveryDriverIdAndDeletedAtIsNull(Long deliveryDriverId);

  Optional<DeliveryDriver> findByDeliveryDriverIdAndDeletedAtIsNull(Long deliveryDriverId);

  Optional<DeliveryDriver> findByDeliveryRouteIdAndDeletedAtIsNull(UUID deliveryRouteId);
}

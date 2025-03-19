package com.business.user.infrastructure.repository;

import com.business.user.domain.entity.DeliveryDriver;
import com.business.user.domain.entity.DriverType;
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

  @Query("""
        SELECT d FROM DeliveryDriver d
        WHERE d.assignAt IS NOT NULL 
        AND d.deletedAt IS NULL
        ORDER BY d.assignAt DESC 
        LIMIT 1
    """)
  Optional<DeliveryDriver> findLastAssignedDriver();

  @Query("""
        SELECT d FROM DeliveryDriver d
        WHERE d.deliverySequence > :currentSequence 
        AND d.deletedAt IS NULL
        ORDER BY d.deliverySequence ASC 
        LIMIT 1
    """)
  Optional<DeliveryDriver> findNextAvailableDriver(@Param("currentSequence") Long currentSequence);

  @Query("""
        SELECT d FROM DeliveryDriver d
        WHERE d.deletedAt IS NULL
        ORDER BY d.deliverySequence ASC 
        LIMIT 1
    """)
  Optional<DeliveryDriver> findFirstAvailableDriver();

  @Query("""
      SELECT d FROM DeliveryDriver d
      WHERE d.assignAt IS NOT NULL 
      AND d.deletedAt IS NULL
      AND d.driverType = :driverType
      AND (:hubId IS NULL OR d.hubId = :hubId)
      ORDER BY d.assignAt DESC 
      LIMIT 1
  """)
  Optional<DeliveryDriver> findLastAssignedDriverByTypeAndHub(@Param("driverType") DriverType driverType, @Param("hubId") UUID hubId);

  @Query("""
      SELECT d FROM DeliveryDriver d
      WHERE d.deliverySequence > :currentSequence 
      AND d.deletedAt IS NULL
      AND d.driverType = :driverType
      AND (:hubId IS NULL OR d.hubId = :hubId)
      ORDER BY d.deliverySequence ASC 
      LIMIT 1
  """)
  Optional<DeliveryDriver> findNextAvailableDriverByTypeAndHub(@Param("currentSequence") Long currentSequence, @Param("driverType") DriverType driverType, @Param("hubId") UUID hubId);

  long countByDriverType(DriverType driverType);

  long countByHubIdAndDriverType(UUID hubId, DriverType driverType);
}

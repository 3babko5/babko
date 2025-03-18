package com.business.user.domain.repository;

import com.business.user.domain.entity.DeliveryDriver;
import com.business.user.domain.entity.DriverType;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface DeliveryDriverRepository extends JpaRepository<DeliveryDriver, Long> {

  @Query("SELECT MAX(d.deliverySequence) FROM DeliveryDriver d WHERE d.driverType = 'HUB'")
  Optional<Long> findLastDeliverySequenceForHubDrivers();

  @Query("SELECT MAX(d.deliverySequence) FROM DeliveryDriver d WHERE d.hubId = :hubId AND d.driverType = 'COMPANY'")
  Optional<Long> findLastDeliverySequenceForCompanyDrivers(@Param("hubId") UUID hubId);

  long countByDriverType(DriverType driverType);

  long countByHubIdAndDriverType(UUID hubId, DriverType driverType);

}

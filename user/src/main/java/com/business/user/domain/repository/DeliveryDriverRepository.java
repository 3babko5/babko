package com.business.user.domain.repository;

import com.business.user.domain.entity.DeliveryDriver;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface DeliveryDriverRepository extends JpaRepository<DeliveryDriver, Long> {

  @Query("SELECT MAX(d.deliverySequence) FROM DeliveryDriver d WHERE d.deletedAt IS NULL")
  Optional<Long> findLastDeliverySequence();
}

package com.business.delivery.infrastructure.repository;

import com.business.delivery.domain.entity.DeliveryRoute;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeliveryRouteJpaRepository extends JpaRepository<DeliveryRoute, UUID> {


}

package com.business.delivery.infrastructure.repository;

import com.business.delivery.domain.entity.DeliveryRoute;
import com.business.delivery.domain.repository.DeliveryRouteRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class DeliveryRouteRepositoryImpl implements DeliveryRouteRepository {

  private final DeliveryRouteJpaRepository deliveryRouteJpaRepository;

  @Override
  public List<DeliveryRoute> saveAll(List<DeliveryRoute> routes) {
    return deliveryRouteJpaRepository.saveAll(routes);
  }
}

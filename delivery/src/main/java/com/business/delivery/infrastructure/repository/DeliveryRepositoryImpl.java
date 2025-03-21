package com.business.delivery.infrastructure.repository;

import com.business.delivery.domain.entity.Delivery;
import com.business.delivery.domain.repository.DeliveryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class DeliveryRepositoryImpl implements DeliveryRepository {

  private final DeliveryJpaRepository deliveryJpaRepository;

  @Override
  public Delivery save(Delivery delivery) {
    return deliveryJpaRepository.save(delivery);
  }
}
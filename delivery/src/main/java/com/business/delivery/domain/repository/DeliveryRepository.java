package com.business.delivery.domain.repository;

import com.business.delivery.domain.entity.Delivery;

public interface DeliveryRepository {

  Delivery save(Delivery delivery);
}

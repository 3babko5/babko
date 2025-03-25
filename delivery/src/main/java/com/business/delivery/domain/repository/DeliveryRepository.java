package com.business.delivery.domain.repository;

import com.business.delivery.application.dto.request.SearchRequestDto;
import com.business.delivery.domain.entity.Delivery;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface DeliveryRepository {

  Delivery save(Delivery delivery);

  Delivery saveAndFlush(Delivery delivery);

  Page<Delivery> findDeliveries(SearchRequestDto request, Pageable pageable);

  Delivery findByDeliveryId(UUID deliveryId);

  void deleteByDeliveryId(UUID deliveryId, Long deletedBy);
}

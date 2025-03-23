package com.business.delivery.domain.repository;

import com.business.delivery.application.dto.request.DeliverySearchRequestDto;
import com.business.delivery.domain.entity.Delivery;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface DeliveryRepository {

  Delivery save(Delivery delivery);

  Page<Delivery> findDeliveries(DeliverySearchRequestDto request, Pageable pageable);

  Delivery findByDeliveryId(UUID deliveryId);

  void deleteByDeliveryId(UUID deliveryId, Long deletedBy);
}

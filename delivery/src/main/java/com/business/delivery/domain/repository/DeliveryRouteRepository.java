package com.business.delivery.domain.repository;

import com.business.delivery.domain.entity.DeliveryRoute;
import java.util.List;

public interface DeliveryRouteRepository {

  List<DeliveryRoute> saveAll(List<DeliveryRoute> routes);
}

package com.business.delivery.application.service;

import com.business.delivery.application.dto.mapper.DeliveryRouteMapper;
import com.business.delivery.application.dto.request.CreateDeliveryRouteRequestDto;
import com.business.delivery.application.dto.response.DeliveryRouteResponseDto;
import com.business.delivery.domain.entity.DeliveryRoute;
import com.business.delivery.domain.entity.DeliveryRouteStatus;
import com.business.delivery.domain.repository.DeliveryRouteJpaRepository;
import com.business.delivery.infrastructure.client.HubMovementClient;
import com.business.delivery.infrastructure.dto.mapper.HubMovementMapper;
import com.business.delivery.infrastructure.dto.response.HubMovementResponseDto;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
@Transactional
public class DeliveryRouteService {

  private final DeliveryRouteJpaRepository deliveryRouteJpaRepository;
  private final HubMovementClient hubMovementClient;

  public DeliveryRouteResponseDto createDeliveryRoute(CreateDeliveryRouteRequestDto request) {

    List<HubMovementResponseDto> hubMovements =
        hubMovementClient.getRoutesByStartAndEndHub(
            request.getStartHubId(),
            request.getEndHubId()
        );

    List<DeliveryRoute> deliveryRoutes = IntStream.range(0, hubMovements.size())
        .mapToObj(i -> HubMovementMapper.toEntity
            (hubMovements.get(i),
                request.getDeliveryId(), (long) i + 1))
        .toList();

    List<DeliveryRoute> savedRoutes = deliveryRouteJpaRepository.saveAll(deliveryRoutes);

    return DeliveryRouteMapper.toDto(savedRoutes);
  }
}

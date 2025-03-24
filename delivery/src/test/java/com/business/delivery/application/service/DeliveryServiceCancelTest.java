package com.business.delivery.application.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.business.common.application.exception.BusinessLogicException;
import com.business.delivery.domain.entity.Delivery;
import com.business.delivery.domain.entity.DeliveryRoute;
import com.business.delivery.domain.entity.DeliveryRouteStatus;
import com.business.delivery.domain.entity.DeliveryStatus;
import com.business.delivery.domain.repository.DeliveryRepository;
import com.business.delivery.infrastructure.client.UserClient;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.Mockito.lenient;

@ExtendWith(MockitoExtension.class)
class DeliveryServiceCancelTest {

  @Mock
  private DeliveryRepository deliveryRepository;

  @Mock
  private UserClient mockUserClient;

  @InjectMocks
  private DeliveryService deliveryService;

  private UUID deliveryId;
  private UUID routeId;
  private Delivery delivery;
  private DeliveryRoute deliveryRoute;

  @BeforeEach
  void setUp() throws Exception {
    deliveryId = UUID.randomUUID();
    routeId = UUID.randomUUID();

    lenient().doNothing().when(mockUserClient).cancelDriverStatus(any(UUID.class));

    deliveryRoute = DeliveryRoute.deliveryRouteCreateBuilder()
        .delivery(null)
        .routeSequence(1L)
        .originHubId(UUID.randomUUID())
        .destinationHubId(UUID.randomUUID())
        .estimatedDistance(BigDecimal.TEN)
        .estimatedTime(10L)
        .deliveryRouteStatus(DeliveryRouteStatus.PENDING)
        .deliveryAddress("Test Address")
        .build();

    Field routeIdField = DeliveryRoute.class.getDeclaredField("deliveryRouteId");
    routeIdField.setAccessible(true);
    routeIdField.set(deliveryRoute, routeId);

    delivery = createDeliveryInstance();

    setDeliveryStatus(delivery, DeliveryStatus.WAITING_AT_HUB);

    setDeliveryRoutes(delivery, List.of(deliveryRoute));

    associateRouteWithDelivery(deliveryRoute, delivery);
  }

  @Test
  void cancelDelivery_Success() throws Exception {

    when(deliveryRepository.findByDeliveryId(deliveryId)).thenReturn(delivery);

    deliveryService.cancelDelivery(deliveryId);

    assertEquals(DeliveryStatus.CANCELED, getDeliveryStatus(delivery));

    assertEquals(DeliveryRouteStatus.CANCELED, deliveryRoute.getDeliveryRouteStatus());

    verify(mockUserClient, times(1)).cancelDriverStatus(routeId);
  }

  @Test
  void cancelDelivery_WhenNoAssignedRoute_ThrowsException() throws Exception {

    setDeliveryRoutes(delivery, Collections.emptyList());
    when(deliveryRepository.findByDeliveryId(deliveryId)).thenReturn(delivery);

    BusinessLogicException ex = assertThrows(BusinessLogicException.class,
        () -> deliveryService.cancelDelivery(deliveryId)
    );
  }

  private Delivery createDeliveryInstance() throws Exception {
    Class<Delivery> clazz = Delivery.class;
    Constructor<Delivery> cons = clazz.getDeclaredConstructor();
    cons.setAccessible(true);
    return cons.newInstance();
  }

  private void setDeliveryStatus(Delivery delivery, DeliveryStatus status) throws Exception {
    Field statusField = Delivery.class.getDeclaredField("deliveryStatus");
    statusField.setAccessible(true);
    statusField.set(delivery, status);
  }

  private void setDeliveryRoutes(Delivery delivery, List<DeliveryRoute> routes) throws Exception {
    Field routesField = Delivery.class.getDeclaredField("deliveryRoutes");
    routesField.setAccessible(true);
    routesField.set(delivery, routes);
  }

  private void associateRouteWithDelivery(DeliveryRoute route, Delivery delivery) throws Exception {
    Field deliveryField = DeliveryRoute.class.getDeclaredField("delivery");
    deliveryField.setAccessible(true);
    deliveryField.set(route, delivery);
  }

  private DeliveryStatus getDeliveryStatus(Delivery delivery) throws Exception {
    Field statusField = Delivery.class.getDeclaredField("deliveryStatus");
    statusField.setAccessible(true);
    return (DeliveryStatus) statusField.get(delivery);
  }
}

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
class DeliveryServiceTest {

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

    // lenient stubbing: 불필요한 stubbing 경고를 피하기 위해 lenient() 사용
    lenient().doNothing().when(mockUserClient).cancelDriverStatus(any(UUID.class));

    // DeliveryRoute 인스턴스 생성 (Reflection 필요 없으면 Builder 사용)
    deliveryRoute = DeliveryRoute.deliveryRouteCreateBuilder()
        .delivery(null) // 나중에 Reflection으로 연결
        .routeSequence(1L)
        .originHubId(UUID.randomUUID())
        .destinationHubId(UUID.randomUUID())
        .estimatedDistance(BigDecimal.TEN)
        .estimatedTime(10L)
        .deliveryRouteStatus(DeliveryRouteStatus.WAITING_AT_HUB)
        .deliveryAddress("Test Address")
        .build();

    // Reflection으로 deliveryRouteId 설정
    Field routeIdField = DeliveryRoute.class.getDeclaredField("deliveryRouteId");
    routeIdField.setAccessible(true);
    routeIdField.set(deliveryRoute, routeId);

    // ===== Delivery 인스턴스 생성 (protected 생성자) =====
    delivery = createDeliveryInstance();

    // DeliveryStatus 초기값: WAITING_AT_HUB
    setDeliveryStatus(delivery, DeliveryStatus.WAITING_AT_HUB);

    // deliveryRoutes 필드에 1개의 route를 설정
    setDeliveryRoutes(delivery, List.of(deliveryRoute));

    // deliveryRoute와 delivery 연결 (양방향 관계 시 필요)
    associateRouteWithDelivery(deliveryRoute, delivery);
  }

  @Test
  void cancelDelivery_Success() throws Exception {
    // given
    when(deliveryRepository.findByDeliveryId(deliveryId)).thenReturn(delivery);

    // when
    deliveryService.cancelDelivery(deliveryId);

    // then
    // 1) Delivery 상태가 CANCELED로 바뀌었는지 확인
    assertEquals(DeliveryStatus.CANCELED, getDeliveryStatus(delivery));

    // 2) DeliveryRoute 상태가 CANCELED로 바뀌었는지 확인
    assertEquals(DeliveryRouteStatus.CANCELED, deliveryRoute.getDeliveryRouteStatus());

    // 3) MockUserClient가 cancelDriverStatus(routeId)를 호출했는지 확인
    verify(mockUserClient, times(1)).cancelDriverStatus(routeId);
  }

  @Test
  void cancelDelivery_WhenNoAssignedRoute_ThrowsException() throws Exception {
    // given
    // deliveryRoutes를 빈 리스트로 세팅
    setDeliveryRoutes(delivery, Collections.emptyList());
    when(deliveryRepository.findByDeliveryId(deliveryId)).thenReturn(delivery);

    // when & then
    BusinessLogicException ex = assertThrows(BusinessLogicException.class,
        () -> deliveryService.cancelDelivery(deliveryId)
    );
    // 여기서는 예외 발생 여부만 확인합니다.
  }

  // ===== 아래는 Reflection 헬퍼 메서드들 =====

  // protected 생성자인 Delivery 인스턴스를 Reflection으로 생성
  private Delivery createDeliveryInstance() throws Exception {
    Class<Delivery> clazz = Delivery.class;
    Constructor<Delivery> cons = clazz.getDeclaredConstructor();
    cons.setAccessible(true); // 접근 권한 허용
    return cons.newInstance();
  }

  // Delivery 엔티티의 deliveryStatus 필드에 값을 주입
  private void setDeliveryStatus(Delivery delivery, DeliveryStatus status) throws Exception {
    Field statusField = Delivery.class.getDeclaredField("deliveryStatus");
    statusField.setAccessible(true);
    statusField.set(delivery, status);
  }

  // Delivery 엔티티의 deliveryRoutes 필드에 값을 주입
  private void setDeliveryRoutes(Delivery delivery, List<DeliveryRoute> routes) throws Exception {
    Field routesField = Delivery.class.getDeclaredField("deliveryRoutes");
    routesField.setAccessible(true);
    routesField.set(delivery, routes);
  }

  // DeliveryRoute와 Delivery를 연결 (양방향 매핑 시 필요)
  private void associateRouteWithDelivery(DeliveryRoute route, Delivery delivery) throws Exception {
    Field deliveryField = DeliveryRoute.class.getDeclaredField("delivery");
    deliveryField.setAccessible(true);
    deliveryField.set(route, delivery);
  }

  // Delivery 엔티티의 현재 상태를 Reflection으로 가져오기
  private DeliveryStatus getDeliveryStatus(Delivery delivery) throws Exception {
    Field statusField = Delivery.class.getDeclaredField("deliveryStatus");
    statusField.setAccessible(true);
    return (DeliveryStatus) statusField.get(delivery);
  }
}

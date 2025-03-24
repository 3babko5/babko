package com.business.user.deliverydriver.application.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.business.common.application.exception.BusinessLogicException;
import com.business.user.deliverydriver.domain.entity.DeliveryDriver;
import com.business.user.deliverydriver.domain.entity.DriverStatus;
import com.business.user.deliverydriver.domain.repository.DeliveryDriverRepository;
import java.lang.reflect.Field;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class DeliveryDriverServiceTest {

    @Mock
    private DeliveryDriverRepository deliveryDriverRepository;

    @InjectMocks
    private DeliveryDriverService deliveryDriverService;

    private UUID deliveryRouteId;
    private DeliveryDriver deliveryDriver;

    @BeforeEach
    void setUp() throws Exception {
        deliveryRouteId = UUID.randomUUID();

        // DeliveryDriver 인스턴스 생성 (빌더 사용)
        deliveryDriver = DeliveryDriver.deliveryDriverCreateBuilder()
            .deliveryDriverId(1L)
            .hubId(UUID.randomUUID())
            .slackId(UUID.randomUUID())
            .driverType(null)  // 필요에 따라 타입 지정 (예: DriverType.HUB)
            .deliverySequence(1L)
            .driverStatus(DriverStatus.ASSIGNED)
            .build();

        // DeliveryDriver에 배정된 배송 경로 ID 설정 (Reflection 사용)
        Field routeField = DeliveryDriver.class.getDeclaredField("deliveryRouteId");
        routeField.setAccessible(true);
        routeField.set(deliveryDriver, deliveryRouteId);
    }

    @Test
    void cancelDriverStatus_Success() {
        // given: 해당 배송 경로에 배정된 배송 담당자가 존재하는 경우
        when(deliveryDriverRepository.findByDeliveryRouteId(deliveryRouteId))
            .thenReturn(Optional.of(deliveryDriver));

        // when: 배송 담당자 취소 호출
        deliveryDriverService.cancelDriverStatus(deliveryRouteId);

        // then: 배송 담당자의 상태가 CANCELED로 업데이트되었는지 검증
        assertEquals(DriverStatus.CANCELED, deliveryDriver.getDriverStatus());
        // 그리고 변경된 엔티티가 저장되었는지 검증
        verify(deliveryDriverRepository, times(1)).save(deliveryDriver);
    }

    @Test
    void cancelDriverStatus_WhenDriverNotFound_ThrowsException() {
        // given: 해당 배송 경로에 배정된 배송 담당자가 존재하지 않는 경우
        when(deliveryDriverRepository.findByDeliveryRouteId(deliveryRouteId))
            .thenReturn(Optional.empty());

        // when & then: BusinessLogicException이 발생하는지 검증
        assertThrows(BusinessLogicException.class,
            () -> deliveryDriverService.cancelDriverStatus(deliveryRouteId));
    }
}

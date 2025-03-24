package com.business.user.deliverydriver.infrastructure.repository;

import static org.junit.jupiter.api.Assertions.*;

import com.business.user.deliverydriver.domain.entity.DeliveryDriver;
import com.business.user.deliverydriver.domain.entity.DriverType;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class DeliveryDriverRepositoryTest {

    @Autowired
    private DeliveryDriverJpaRepository repository;

    @BeforeEach
    public void setUp() {
        repository.deleteAll();
    }

    /**
     * 1. testFindFirstByAssignAtIsNotNullAndDeletedAtIsNullOrderByAssignAtDesc
     *
     * 설명:
     * - assignAt 값이 설정되어 있고(deletedAt이 null인) 모든 드라이버 중,
     *   assignAt이 가장 늦은(최신) 드라이버를 반환하는지 검증합니다.
     * - 여기서는 허브 타입(HUB) 드라이버를 대상으로 테스트합니다.
     */
    @Test
    public void testFindFirstByAssignAtIsNotNullAndDeletedAtIsNullOrderByAssignAtDesc() {
        // driver1: 할당 시각이 현재 (assignAt이 현재 시각)
        DeliveryDriver driver1 = DeliveryDriver.deliveryDriverCreateBuilder()
            .deliveryDriverId(1L)
            .hubId(null) // 허브 타입은 hubId가 필요 없음
            .slackId(UUID.randomUUID())
            .driverType(DriverType.HUB)
            .deliverySequence(1L)
            .build();
        driver1.createdBy(1L); // BaseDataEntity 필수 필드 설정
        driver1.assignToRoute(UUID.randomUUID(), 1L);  // assignAt이 설정됨.
        repository.save(driver1);

        // driver2: driver1보다 나중에 할당된 것으로 설정 (assignAt 10분 후)
        DeliveryDriver driver2 = DeliveryDriver.deliveryDriverCreateBuilder()
            .deliveryDriverId(2L)
            .hubId(null)
            .slackId(UUID.randomUUID())
            .driverType(DriverType.HUB)
            .deliverySequence(2L)
            .build();
        driver2.createdBy(2L);
        driver2.assignToRoute(UUID.randomUUID(), 2L);
        driver2.updateAssignAt(LocalDateTime.now().plusMinutes(10));
        repository.save(driver2);

        Optional<DeliveryDriver> result = repository.findFirstByAssignAtIsNotNullAndDeletedAtIsNullOrderByAssignAtDesc();
        assertTrue(result.isPresent());
        // driver2가 최신 할당 시각이므로 반환되어야 합니다.
        assertEquals(2L, result.get().getDeliveryDriverId());
    }

    /**
     * 2. testFindFirstByDeliverySequenceGreaterThanAndDeletedAtIsNullOrderByDeliverySequenceAsc
     *
     * 설명:
     * - currentSequence 값보다 큰 deliverySequence 값을 가진 드라이버 중에서,
     *   가장 작은(오름차순으로 정렬한 첫 번째) deliverySequence를 가진 드라이버를 반환하는지 검증합니다.
     * - 여기서는 허브 타입(HUB) 드라이버를 대상으로 합니다.
     */
    @Test
    public void testFindFirstByDeliverySequenceGreaterThanAndDeletedAtIsNullOrderByDeliverySequenceAsc() {
        // driver1: sequence 1, driver2: sequence 3, driver3: sequence 5 삽입
        DeliveryDriver driver1 = DeliveryDriver.deliveryDriverCreateBuilder()
            .deliveryDriverId(1L)
            .hubId(null)
            .slackId(UUID.randomUUID())
            .driverType(DriverType.HUB)
            .deliverySequence(1L)
            .build();
        driver1.createdBy(1L);
        repository.save(driver1);

        DeliveryDriver driver2 = DeliveryDriver.deliveryDriverCreateBuilder()
            .deliveryDriverId(2L)
            .hubId(null)
            .slackId(UUID.randomUUID())
            .driverType(DriverType.HUB)
            .deliverySequence(3L)
            .build();
        driver2.createdBy(2L);
        repository.save(driver2);

        DeliveryDriver driver3 = DeliveryDriver.deliveryDriverCreateBuilder()
            .deliveryDriverId(3L)
            .hubId(null)
            .slackId(UUID.randomUUID())
            .driverType(DriverType.HUB)
            .deliverySequence(5L)
            .build();
        driver3.createdBy(3L);
        repository.save(driver3);

        // currentSequence가 1이면, 다음 deliverySequence는 3인 driver2가 나와야 합니다.
        Optional<DeliveryDriver> result = repository.findFirstByDeliverySequenceGreaterThanAndDeletedAtIsNullOrderByDeliverySequenceAsc(1L);
        assertTrue(result.isPresent());
        assertEquals(3L, result.get().getDeliverySequence());
    }

    /**
     * 3. testFindFirstByDeletedAtIsNullOrderByDeliverySequenceAsc
     *
     * 설명:
     * - 삭제되지 않은(drivers with deletedAt == null) 드라이버들 중,
     *   deliverySequence가 가장 낮은 값을 가진 드라이버를 반환하는지 검증합니다.
     */
    @Test
    public void testFindFirstByDeletedAtIsNullOrderByDeliverySequenceAsc() {
        // driver1: sequence 2, driver2: sequence 4 삽입
        DeliveryDriver driver1 = DeliveryDriver.deliveryDriverCreateBuilder()
            .deliveryDriverId(1L)
            .hubId(null)
            .slackId(UUID.randomUUID())
            .driverType(DriverType.HUB)
            .deliverySequence(2L)
            .build();
        driver1.createdBy(1L);
        repository.save(driver1);

        DeliveryDriver driver2 = DeliveryDriver.deliveryDriverCreateBuilder()
            .deliveryDriverId(2L)
            .hubId(null)
            .slackId(UUID.randomUUID())
            .driverType(DriverType.HUB)
            .deliverySequence(4L)
            .build();
        driver2.createdBy(2L);
        repository.save(driver2);

        // driver1을 삭제했다고 가정. BaseDataEntity의 setDeletedBy()를 이용합니다.
        driver1.deletedBy(999L); // 임의의 사용자 ID로 삭제 처리
        repository.save(driver1);

        // 삭제되지 않은 드라이버 중 가장 낮은 deliverySequence는 driver2가 되어야 합니다.
        Optional<DeliveryDriver> result = repository.findFirstByDeletedAtIsNullOrderByDeliverySequenceAsc();
        assertTrue(result.isPresent());
        assertEquals(4L, result.get().getDeliverySequence());
    }

    /**
     * 4. testFindFirstByAssignAtIsNotNullAndDeletedAtIsNullAndDriverTypeAndHubIdOrderByAssignAtDesc
     *
     * 설명:
     * - 특정 드라이버 타입(여기서는 COMPANY) 및 hubId를 가진 드라이버 중에서,
     *   assignAt 값이 null이 아닌 드라이버들 중 가장 늦게 할당된(driver assignAt이 가장 큰) 드라이버를 반환하는지 검증합니다.
     */
    @Test
    public void testFindFirstByAssignAtIsNotNullAndDeletedAtIsNullAndDriverTypeAndHubIdOrderByAssignAtDesc() {
        // 동일한 hubId, 드라이버 타입 COMPANY로 드라이버 2명을 생성
        UUID hubId = UUID.randomUUID();

        // driver1: 할당 시각은 현재
        DeliveryDriver driver1 = DeliveryDriver.deliveryDriverCreateBuilder()
            .deliveryDriverId(10L)
            .hubId(hubId)
            .slackId(UUID.randomUUID())
            .driverType(DriverType.COMPANY)
            .deliverySequence(1L)
            .build();
        driver1.createdBy(1L);
        driver1.assignToRoute(UUID.randomUUID(), 1L);
        repository.save(driver1);

        // driver2: driver1보다 나중에 할당된 것으로 설정 (assignAt 15분 후)
        DeliveryDriver driver2 = DeliveryDriver.deliveryDriverCreateBuilder()
            .deliveryDriverId(11L)
            .hubId(hubId)
            .slackId(UUID.randomUUID())
            .driverType(DriverType.COMPANY)
            .deliverySequence(2L)
            .build();
        driver2.createdBy(2L);
        driver2.assignToRoute(UUID.randomUUID(), 2L);
        driver2.updateAssignAt(LocalDateTime.now().plusMinutes(15));
        repository.save(driver2);

        // driver2의 assignAt이 driver1보다 늦으므로 driver2가 반환되어야 합니다.
        Optional<DeliveryDriver> result = repository.findFirstByAssignAtIsNotNullAndDeletedAtIsNullAndDriverTypeAndHubIdOrderByAssignAtDesc(DriverType.COMPANY, hubId);
        assertTrue(result.isPresent());
        assertEquals(11L, result.get().getDeliveryDriverId());
    }

    /**
     * 5. testFindFirstByDeliverySequenceGreaterThanAndDeletedAtIsNullAndDriverTypeAndHubIdOrderByDeliverySequenceAsc
     *
     * 설명:
     * - 특정 드라이버 타입(여기서는 COMPANY) 및 hubId를 가진 드라이버 중에서,
     *   currentSequence 값보다 큰 deliverySequence를 가진 드라이버들 중,
     *   가장 작은(오름차순 정렬의 첫 번째) deliverySequence를 가진 드라이버를 반환하는지 검증합니다.
     */
    @Test
    public void testFindFirstByDeliverySequenceGreaterThanAndDeletedAtIsNullAndDriverTypeAndHubIdOrderByDeliverySequenceAsc() {
        // 동일한 hubId, 드라이버 타입 COMPANY로 드라이버 3명을 생성
        UUID hubId = UUID.randomUUID();

        DeliveryDriver driver1 = DeliveryDriver.deliveryDriverCreateBuilder()
            .deliveryDriverId(20L)
            .hubId(hubId)
            .slackId(UUID.randomUUID())
            .driverType(DriverType.COMPANY)
            .deliverySequence(10L)
            .build();
        driver1.createdBy(1L);
        repository.save(driver1);

        DeliveryDriver driver2 = DeliveryDriver.deliveryDriverCreateBuilder()
            .deliveryDriverId(21L)
            .hubId(hubId)
            .slackId(UUID.randomUUID())
            .driverType(DriverType.COMPANY)
            .deliverySequence(20L)
            .build();
        driver2.createdBy(2L);
        repository.save(driver2);

        DeliveryDriver driver3 = DeliveryDriver.deliveryDriverCreateBuilder()
            .deliveryDriverId(22L)
            .hubId(hubId)
            .slackId(UUID.randomUUID())
            .driverType(DriverType.COMPANY)
            .deliverySequence(30L)
            .build();
        driver3.createdBy(3L);
        repository.save(driver3);

        // currentSequence가 15이면, deliverySequence가 20인 driver2가 가장 작은 값이므로 반환되어야 합니다.
        Optional<DeliveryDriver> result = repository
            .findFirstByDeliverySequenceGreaterThanAndDeletedAtIsNullAndDriverTypeAndHubIdOrderByDeliverySequenceAsc(15L, DriverType.COMPANY, hubId);
        assertTrue(result.isPresent());
        assertEquals(20L, result.get().getDeliverySequence());
    }
}
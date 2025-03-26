package com.business.user.deliverydriver.domain.entity;

import com.business.common.application.exception.BusinessLogicException;
import com.business.common.domain.entity.BaseDataEntity;
import com.business.user.deliverydriver.application.exception.DeliveryDriverErrorCode;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Getter
@Slf4j
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "p_delivery_drivers")
@Comment("배송 담당자")
public class DeliveryDriver extends BaseDataEntity {

  @Id
  @Comment("배달 담당자 ID")
  private Long deliveryDriverId;

  @Comment("소속 허브 ID")
  private UUID hubId;

  @JdbcTypeCode(SqlTypes.UUID)
  @Comment("슬랙 ID")
  private UUID slackId;

  @NotNull
  @Column(nullable = false, length = 10)
  @Enumerated(EnumType.STRING)
  @Comment("배송 담당자 타입 (허브, 업체)")
  private DriverType driverType;

  @NotNull
  @Column(nullable = false)
  @Comment("배송 담당자 배정 순서")
  private Long deliverySequence;

  @Comment("배정 할당 시간")
  private LocalDateTime assignAt;

  @Comment("배정된 배송 경로 ID")
  private UUID deliveryRouteId;

  @Comment("배정된 배송 경로 순서")
  private Long routeSequence;

  @Column
  @Enumerated(EnumType.STRING)
  @Comment("배송 담당자 상태")
  private DriverStatus driverStatus;

  private DeliveryDriver(Long userId, UUID hubId, UUID slackId, DriverType driverType, Long deliverySequence, DriverStatus driverStatus) {

    this.deliveryDriverId = userId;
    this.hubId = hubId;
    this.slackId = slackId;
    this.driverType = driverType;
    this.deliverySequence = deliverySequence;
    this.driverStatus = driverStatus;
  }

  @Builder(builderMethodName = "deliveryDriverCreateBuilder")
  public static DeliveryDriver create(Long deliveryDriverId, UUID hubId, UUID slackId, DriverType driverType, Long deliverySequence, DriverStatus driverStatus) {

    return new DeliveryDriver(deliveryDriverId, hubId, slackId, driverType, deliverySequence, driverStatus);
  }

  public void assignToRoute(UUID deliveryRouteId, Long routeSequence) {

    this.deliveryRouteId = deliveryRouteId;
    this.routeSequence = routeSequence;
    this.assignAt = LocalDateTime.now();
  }

  public void updateAssignAt(LocalDateTime assignAt) {

    this.assignAt = assignAt;
  }

  public void updateStatus(DriverStatus newStatus) {
    //this.driverStatus.validateTransition(newStatus);
    this.driverStatus = newStatus;

  }

  public void updateCancelStatus() {

    this.driverStatus = DriverStatus.CANCELED;
  }
}



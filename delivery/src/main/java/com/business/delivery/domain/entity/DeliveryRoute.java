package com.business.delivery.domain.entity;

import com.business.common.domain.entity.BaseDataEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UuidGenerator;
import org.hibernate.type.SqlTypes;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "p_delivery_routes")
@Comment("배송 경로 기록")
public class DeliveryRoute extends BaseDataEntity {

  @Id
  @UuidGenerator
  @JdbcTypeCode(SqlTypes.UUID)
  @Comment("배송 경로 기록 ID")
  private UUID deliveryRouteId;

  @JoinColumn(name = "delivery_id")
  @ManyToOne(fetch = FetchType.LAZY)
  @Comment("배송")
  private Delivery delivery;

  @NotNull
  @Column(nullable = false, length = 20)
  @Enumerated(EnumType.STRING)
  @Comment("배송 경로 기록 상태 (배송 취소, 허브 대기 중, 허브 이동 중, 목적지 허브 도착, 배송 중, 배송 완료, 준비 중)")
  private DeliveryRouteStatus deliveryRouteStatus;

  @NotNull
  @Column(nullable = false)
  @Comment("배송 경로 상 허브의 순서")
  private Long routeSequence;

  @NotNull
  @Column(nullable = false)
  @JdbcTypeCode(SqlTypes.UUID)
  @Comment("출발 허브 ID")
  private UUID originHubId;

  @Column
  @JdbcTypeCode(SqlTypes.UUID)
  @Comment("도착 허브 ID")
  private UUID destinationHubId;

  @Comment("배송지 주소")
  private String deliveryAddress;

  @NotNull
  @Column(nullable = false, precision = 10, scale = 2)
  @Comment("예상 거리")
  private BigDecimal estimatedDistance;

  @NotNull
  @Column(nullable = false)
  @Comment("예상 시간")
  private Long estimatedTime;

  @Column(precision = 10, scale = 2)
  @Comment("실제 거리")
  private BigDecimal actualDistance;

  @Comment("실제 시간")
  private Long actualTime;

  private DeliveryRoute(Delivery delivery, Long routeSequence, UUID originHubId, UUID destinationHubId,
      BigDecimal estimatedDistance, Long estimatedTime, DeliveryRouteStatus deliveryRouteStatus, String deliveryAddress) {

    this.delivery = delivery;
    this.routeSequence = routeSequence;
    this.originHubId = originHubId;
    this.destinationHubId = destinationHubId;
    this.estimatedDistance = estimatedDistance;
    this.estimatedTime = estimatedTime;
    this.deliveryRouteStatus = deliveryRouteStatus;
    this.deliveryAddress = deliveryAddress;
  }

  @Builder(builderMethodName = "deliveryRouteCreateBuilder")
  public static DeliveryRoute create(Delivery delivery, Long routeSequence, UUID originHubId, UUID destinationHubId,
      BigDecimal estimatedDistance, Long estimatedTime, DeliveryRouteStatus deliveryRouteStatus, String deliveryAddress) {

    return new DeliveryRoute(delivery, routeSequence, originHubId, destinationHubId, estimatedDistance, estimatedTime, deliveryRouteStatus, deliveryAddress);
  }

  public void associateDelivery(Delivery delivery) {
      this.delivery = delivery;
  }

  public void updateCancelStatus() {

    this.deliveryRouteStatus = DeliveryRouteStatus.CANCELED;
  }
}

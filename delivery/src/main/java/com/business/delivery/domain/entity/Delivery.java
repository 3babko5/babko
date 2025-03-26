package com.business.delivery.domain.entity;

import com.business.common.application.exception.BusinessLogicException;
import com.business.common.domain.entity.BaseDataEntity;
import com.business.delivery.application.exception.DeliveryErrorCode;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
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
@Table(name = "p_deliveries")
@Comment("배송")
public class Delivery extends BaseDataEntity {

  @Id
  @UuidGenerator
  @JdbcTypeCode(SqlTypes.UUID)
  @Comment("배달 ID")
  private UUID deliveryId;

  @NotNull
  @Column(nullable = false)
  @JdbcTypeCode(SqlTypes.UUID)
  @Comment("주문 ID")
  private UUID orderId;

  @NotNull
  @Column(nullable = false)
  @JdbcTypeCode(SqlTypes.UUID)
  @Comment("출발 허브 ID")
  private UUID startHubId;

  @NotNull
  @Column(nullable = false)
  @JdbcTypeCode(SqlTypes.UUID)
  @Comment("도착 허브 ID")
  private UUID endHubId;

  @NotNull
  @Column(nullable = false)
  @Comment("배송지 주소")
  private String deliveryAddress;

  @NotNull
  @Column(nullable = false, length = 20)
  @Enumerated(EnumType.STRING)
  @Comment("배송 상태 (배송 취소, 허브 대기 중, 허브 이동 중, 목적지 허브 도착, 배송 중, 배송 완료)")
  private DeliveryStatus deliveryStatus;

  @NotNull
  @Column(nullable = false)
  @JdbcTypeCode(SqlTypes.UUID)
  @Comment("수령인 ID")
  private UUID recipientId;

  @JdbcTypeCode(SqlTypes.UUID)
  @Comment("수령인 슬랙 ID")
  private UUID recipientSlackId;

  @OneToMany(
      mappedBy = "delivery",
      fetch = FetchType.LAZY,
      cascade = CascadeType.ALL
  )
  @Comment("배송 경로 리스트")
  private List<DeliveryRoute> deliveryRoutes = new ArrayList<>();

  public Delivery(UUID orderId, UUID startHubId, UUID endHubId,
      String deliveryAddress, UUID recipientId, UUID recipientSlackId, DeliveryStatus deliveryStatus) {

    this.orderId = orderId;
    this.startHubId = startHubId;
    this.endHubId = endHubId;
    this.deliveryAddress = deliveryAddress;
    this.recipientId = recipientId;
    this.recipientSlackId = recipientSlackId;
    this.deliveryStatus = deliveryStatus;

  }

  @Builder(builderMethodName = "deliveryCreateBuilder")
  public static Delivery create(UUID orderId, UUID startHubId, UUID endHubId, String deliveryAddress, UUID recipientId) {
    return new Delivery(orderId, startHubId, endHubId, deliveryAddress, recipientId, null, DeliveryStatus.WAITING_AT_HUB);
  }

  public void addDeliveryRoute(List<DeliveryRoute> deliveryRoutes) {
    for (DeliveryRoute deliveryRoute : deliveryRoutes) {
      this.deliveryRoutes.add(deliveryRoute);
      deliveryRoute.associateDelivery(this);
    }
  }

  public void updateStatus(DeliveryStatus newStatus) {

    if (this.deliveryStatus.isTerminal()) {
      throw new BusinessLogicException(DeliveryErrorCode.INVALID_STATUS_LASTCHANGE);
    }

    if (this.deliveryStatus == newStatus) {
      return;
    }

    this.deliveryStatus = newStatus;
  }

  public void updateCancelStatus() {

    this.deliveryStatus = DeliveryStatus.CANCELED;
    for (DeliveryRoute route : deliveryRoutes) {
      route.updateCancelStatus();
    }
  }

  public void softDelete(Long userId) {

    this.deletedBy(userId);
  }
}


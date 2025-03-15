package com.business.delivery.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UuidGenerator;
import org.hibernate.type.SqlTypes;

@Entity
@Getter
@Builder(access = AccessLevel.PUBLIC)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "p_deliveries")
@Comment("배송")
public class Delivery {

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
  private UUID originHubId;

  @NotNull
  @Column(nullable = false)
  @JdbcTypeCode(SqlTypes.UUID)
  @Comment("도착 허브 ID")
  private UUID destinationHubId;

  @NotNull
  @Column(nullable = false)
  @JdbcTypeCode(SqlTypes.UUID)
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
  @Comment("배송 담당자 ID")
  private Long deliveryDriverId;

  @NotNull
  @Column(nullable = false)
  @JdbcTypeCode(SqlTypes.UUID)
  @Comment("수령인 ID")
  private UUID recipientId;

  @JdbcTypeCode(SqlTypes.UUID)
  @Comment("수령인 슬랙 ID")
  private UUID recipientSlackId;
}


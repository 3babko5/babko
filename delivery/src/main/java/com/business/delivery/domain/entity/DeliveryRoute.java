package com.business.delivery.domain.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UuidGenerator;
import org.hibernate.type.SqlTypes;

@Entity
@Getter
@Builder(access = AccessLevel.PUBLIC)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "p_delivery_routes")
@Comment("배송 경로 기록")
public class DeliveryRoute {

  @Id
  @UuidGenerator
  @JdbcTypeCode(SqlTypes.UUID)
  @Comment("배송 경로 기록 ID")
  private UUID deliveryRouteId;

  @NotNull
  @JdbcTypeCode(SqlTypes.UUID)
  @Column(nullable = false)
  @Comment("배송 ID")
  private UUID deliveryId;

  @NotNull
  @Column(nullable = false, length = 20)
  @Enumerated(EnumType.STRING)
  @Comment("배송 경로 기록 상태 (배송 취소, 허브 대기 중, 허브 이동 중, 목적지 허브 도착, 배송 중, 배송 완료)")
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

  @NotNull
  @Column(nullable = false)
  @JdbcTypeCode(SqlTypes.UUID)
  @Comment("도착 허브 ID")
  private UUID destinationHubId;

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

  @NotNull
  @Column(nullable = false)
  @JdbcTypeCode(SqlTypes.UUID)
  @Comment("허브 배송 담당자 ID")
  private UUID hubDriverId;

  @NotNull
  @Column(nullable = false)
  @JdbcTypeCode(SqlTypes.UUID)
  @Comment("업체 배송 담당자 ID")
  private UUID companyDriverId;
}

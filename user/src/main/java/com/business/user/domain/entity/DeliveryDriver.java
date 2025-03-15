package com.business.user.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
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
@Table(name = "p_delivery_drivers")
@Comment("배송 담당자")
public class DeliveryDriver {

  @Id
  @UuidGenerator
  @JdbcTypeCode(SqlTypes.UUID)
  @Comment("배달 담당자 ID")
  private UUID deliveryDriverId;

  @NotNull
  @Comment("소속 허브 ID")
  private UUID hubId;

  @NotNull
  @Column(nullable = false)
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
}

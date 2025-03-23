package com.business.hub.domain.entity;

import com.business.common.domain.entity.BaseDataEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "p_hub_movements")
public class HubMovement extends BaseDataEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "hub_movement_id")
    private UUID hubMovementId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "departure_hub_id", referencedColumnName = "hub_id", nullable = false)
    private Hub departureHub;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "arrival_hub_id", referencedColumnName = "hub_id", nullable = false)
    private Hub arrivalHub;

    @Column(name = "duration_time", nullable = false)
    private int durationTime;

    @Column(name = "distance", nullable = false, precision = 10, scale = 2)
    private BigDecimal distance;

    public static HubMovement create(
            Hub departureHub,
            Hub arrivalHub,
            int durationTime,
            BigDecimal distance,
            Long createdBy) {
        return HubMovement.builder()
                .departureHub(departureHub)
                .arrivalHub(arrivalHub)
                .durationTime(durationTime)
                .distance(distance)
                .createdBy(createdBy)
                .build();
    }

    public void update(
            Hub departureHub,
            Hub arrivalHub,
            BigDecimal distance,
            int durationTime,
            Long updatedBy) {
        this.departureHub = departureHub;
        this.arrivalHub = arrivalHub;
        this.distance = distance;
        this.durationTime = durationTime;
        this.updatedBy(updatedBy);
    }
}

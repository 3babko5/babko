package com.business.hub.domain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "p_hub_monvements")
public class HubMovement {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "hub_movement_id")
    private UUID movementId;

    @ManyToOne
    @JoinColumn(name = "departure_hub_id", nullable = false)
    private Hub departureHub;

    @ManyToOne
    @JoinColumn(name = "arrival_hub_id", nullable = false)
    private Hub arrivalHub;

    @Column(name = "duration_time", nullable = false)
    private int durationTime;

    @Column(name = "distance", nullable = false)
    private BigDecimal distance;


}

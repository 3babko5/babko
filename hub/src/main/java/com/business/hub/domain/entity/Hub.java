package com.business.hub.domain.entity;

import com.business.common.domain.entity.BaseDataEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


@Getter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "p_hubs")
public class Hub extends BaseDataEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "hub_id")
    private UUID hubId;

    @Column(name = "hub_name", nullable = false, length = 255)
    private String hubName;

    @Column(name = "hub_address", nullable = false, length = 255)
    private String hubAddress;

    @Column(name = "hub_latitude", nullable = false, precision = 11, scale = 8)
    private BigDecimal hubLatitude;

    @Column(name = "hub_longitude", nullable = false, precision = 12, scale = 8)
    private BigDecimal hubLongitude;

    @Column(name = "hub_manager_id", nullable = false)
    private UUID hubManagerId;


    @OneToMany(mappedBy = "departureHub")
    private List<HubMovement> departureMovements = new ArrayList<>();

    @OneToMany(mappedBy = "arrivalHub")
    private List<HubMovement> arrivalMovements = new ArrayList<>();

}

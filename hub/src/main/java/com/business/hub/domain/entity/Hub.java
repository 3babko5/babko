package com.business.hub.domain.entity;

import com.business.common.domain.entity.BaseDataEntity;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
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
    private Long hubManagerId;

    @OneToMany(mappedBy = "departureHub")
    private List<HubMovement> departureMovements = new ArrayList<>();

    @OneToMany(mappedBy = "arrivalHub")
    private List<HubMovement> arrivalMovements = new ArrayList<>();

    @Builder
    public Hub(
            LocalDateTime createdAt,
            Long createdBy,
            String hubName,
            String hubAddress,
            BigDecimal hubLatitude,
            BigDecimal hubLongitude,
            Long hubManagerId) {
        this.setCreatedBy(createdBy);
        this.hubName = hubName;
        this.hubAddress = hubAddress;
        this.hubLatitude = hubLatitude;
        this.hubLongitude = hubLongitude;
        this.hubManagerId = hubManagerId;
    }


    public void update(
            String hubName,
            String hubAddress,
            BigDecimal hubLatitude,
            BigDecimal hubLongitude,
            Long hubManagerId,
            Long updatedBy) {
        this.hubName = hubName;
        this.hubAddress = hubAddress;
        this.hubLatitude = hubLatitude;
        this.hubLongitude = hubLongitude;
        this.hubManagerId = hubManagerId;
        this.setUpdatedBy(updatedBy);
    }

}

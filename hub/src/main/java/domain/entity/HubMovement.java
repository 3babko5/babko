package domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "p_hub_monvements")
public class HubMovement extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "hub_movement_id")
    private UUID movementId;

    @ManyToOne
    @JoinColumn(name = "departure_hub_id", nullable = false)
    private Hub departureHubId;

    @ManyToOne
    @JoinColumn(name = "arrival_hub_id", nullable = false)
    private Hub arrivalHubId;

    @Column(name = "duration_time", nullable = false)
    private int durationTime;

    @Column(name = "distance", nullable = false)
    private BigDecimal distance;


}

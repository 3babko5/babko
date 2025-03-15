package domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@MappedSuperclass
public abstract class BaseEntity {


    @Column(name = "created_at", updatable = false, nullable = false)
    protected LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "created_by", updatable = false, nullable = false)
    protected UUID createdBy;

    @Column(name = "updated_at")
    protected LocalDateTime updatedAt;

    @Column(name = "updated_by")
    protected UUID updatedBy;

    @Column(name = "deleted_at")
    protected LocalDateTime deletedAt;

    @Column(name = "deleted_by")
    protected UUID deletedBy;
}

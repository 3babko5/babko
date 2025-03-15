package com.business.common.Entity;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseDataEntity {

    @Column(name = "created_by", nullable = false, updatable = false)
    private Integer createdBy;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_by")
    private Integer updatedBy;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "deleted_by")
    private Integer deletedBy;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    public void setCreatedBy(Integer userId) {
        this.createdBy = userId;
        this.createdAt = LocalDateTime.now();
    }

    public void setUpdatedBy(Integer userId) {
        this.updatedBy = userId;
        this.updatedAt = LocalDateTime.now();
    }

    public void setDeletedBy(Integer userId) {
        this.deletedBy = userId;
        this.deletedAt = LocalDateTime.now();
    }

}

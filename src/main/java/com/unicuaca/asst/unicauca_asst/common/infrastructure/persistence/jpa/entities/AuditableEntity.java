package com.unicuaca.asst.unicauca_asst.common.infrastructure.persistence.jpa.entities;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.Getter;

/**
 * Clase base para entidades que requieren auditoría temporal.
 * Registra automáticamente la fecha de creación y actualización.
 *
 * <p>Se utiliza como superclase para heredar estos campos en las entidades JPA.</p>
 */
@Getter
@MappedSuperclass
public abstract class AuditableEntity {

    @Column(name = "fecha_creacion", nullable = false, updatable = false)
    protected LocalDateTime createdAt;

    @Column(name = "fecha_actualizacion")
    protected LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}

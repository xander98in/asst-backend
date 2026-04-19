package com.unicuaca.asst.unicauca_asst.common.infrastructure.persistence.jpa.entities;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.Getter;

/**
 * Clase base para entidades que requieren auditoría temporal.
 *
 * <p>Registra automáticamente la fecha de creación y actualización mediante callbacks JPA.
 * Se utiliza como superclase para heredar estos campos en las entidades JPA.</p>
 */
@Getter
@MappedSuperclass
public abstract class AuditableEntity {

    /**
     * Fecha y hora en que se creó el registro. Se asigna automáticamente en {@link #onCreate()}
     * y no se actualiza nunca.
     */
    @Column(name = "fecha_creacion", nullable = false, updatable = false)
    protected LocalDateTime createdAt;

    /**
     * Fecha y hora de la última actualización del registro. Se refresca automáticamente en
     * {@link #onUpdate()} cada vez que la entidad se modifica.
     */
    @Column(name = "fecha_actualizacion")
    protected LocalDateTime updatedAt;

    /**
     * Callback JPA ejecutado antes de persistir por primera vez la entidad.
     * Asigna la fecha de creación con el instante actual.
     */
    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    /**
     * Callback JPA ejecutado antes de cada actualización de la entidad.
     * Refresca la fecha de actualización con el instante actual.
     */
    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}

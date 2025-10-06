package com.unicuaca.asst.unicauca_asst.core.batteries_management.infrastructure.adapters.output.persistence.jpa.entities;

import com.unicuaca.asst.unicauca_asst.common.infrastructure.persistence.jpa.entities.AuditableEntity;
import jakarta.persistence.*;
import lombok.*;

/**
 * Entidad que representa un registro de gestión de batería.
 * Cada registro está asociado a una persona evaluada y tiene un estado.
 * Mapea la tabla "registros_gestion_baterias".
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "registros_gestion_baterias")
public class BatteryManagementRecordEntity extends AuditableEntity {

    /**
     * Identificador único del registro de gestión de batería.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_registro_gestion_bateria")
    private Long id;

    /**
     * Persona evaluada asociada al registro.
     * Una persona puede tener múltiples registros de gestión de baterías.
     */
    @ManyToOne
    @JoinColumn(name = "id_persona_evaluada", nullable = false)
    private PersonEvaluatedEntity personEvaluated;

    /**
     * Estado actual del registro de gestión de batería.
     */
    @ManyToOne
    @JoinColumn(name = "id_estado_registro_gestion_bateria", nullable = false)
    private BatteryManagementRecordStatusEntity status;
}

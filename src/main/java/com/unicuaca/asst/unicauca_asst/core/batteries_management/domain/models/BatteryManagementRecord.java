package com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

/**
 * Representa un registro de gestión de batería.
 *
 * <p>Cada registro está asociado a una persona evaluada y mantiene un estado
 * que refleja la fase actual del proceso (creado, en diligenciamiento,
 * diligenciado o cerrado).</p>
 */
@Getter
@Setter
@AllArgsConstructor
@Builder
@ToString
public class BatteryManagementRecord {

    /** Identificador único del registro de gestión de batería. */
    private Long id;

    /** Persona evaluada asociada a este registro. */
    private PersonEvaluated personEvaluated;

    /** Fecha y hora de creación del registro. */
    private LocalDateTime createdAt;

    /** Fecha y hora de la última actualización del registro. */
    private LocalDateTime updatedAt;

    /** Estado actual del registro de gestión de batería. */
    private BatteryManagementRecordStatus status;
}

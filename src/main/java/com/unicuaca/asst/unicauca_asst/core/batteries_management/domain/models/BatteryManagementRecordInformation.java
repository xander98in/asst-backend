package com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.models;

import lombok.*;

import java.time.LocalDateTime;

/**
 * Modelo de dominio que representa la información agregada de un registro de gestión de baterías.
 *
 * <p>Consolida el registro principal, sus fechas de auditoría y los datos
 * completos de la persona evaluada junto con sus detalles demográficos y laborales.</p>
 */
@Getter
@Setter
@AllArgsConstructor
@Builder
@ToString
public class BatteryManagementRecordInformation {

    /** Identificador único del registro de gestión de batería. */
    private Long id;

    /** Fecha y hora de creación del registro. */
    private LocalDateTime createdAt;

    /** Fecha y hora de la última actualización del registro. */
    private LocalDateTime updatedAt;

    /** Estado actual del registro de gestión de batería. */
    private BatteryManagementRecordStatus status;

    /** Persona evaluada asociada al registro. */
    private PersonEvaluated personEvaluated;

    /** Detalles demográficos, laborales y socioeconómicos de la persona evaluada. */
    private PersonEvaluatedDetails personEvaluatedDetails;
}

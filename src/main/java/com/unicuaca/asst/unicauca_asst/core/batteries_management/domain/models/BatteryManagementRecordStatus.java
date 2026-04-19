package com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Representa el estado de un registro de gestión de baterías.
 *
 * <p>Incluye un identificador único y un nombre descriptivo del estado
 * (por ejemplo: "Creado", "En diligenciamiento", "Diligenciado", "Cerrado").</p>
 */
@Getter
@Setter
@AllArgsConstructor
@Builder
@ToString
public class BatteryManagementRecordStatus {

    /** Identificador único del estado. */
    private Long id;

    /** Nombre descriptivo del estado. */
    private String name;
}

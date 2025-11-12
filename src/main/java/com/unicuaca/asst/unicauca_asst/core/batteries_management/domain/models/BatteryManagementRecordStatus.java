package com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Representa el estado de un registro de gestión de baterías.
 * Incluye un identificador único y un nombre descriptivo del estado.
 */
@Getter
@Setter
@AllArgsConstructor
@Builder
@ToString
public class BatteryManagementRecordStatus {

    private Long id;
    private String name;
}

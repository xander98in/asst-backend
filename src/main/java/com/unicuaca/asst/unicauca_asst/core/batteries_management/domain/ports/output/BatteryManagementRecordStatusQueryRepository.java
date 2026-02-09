package com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.ports.output;

import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.models.BatteryManagementRecordStatus;

import java.util.Optional;

/**
 * Puerto de salida para consultas relacionadas con los estados de los registros de gestión de baterías.
 */
public interface BatteryManagementRecordStatusQueryRepository {

    /**
     * Obtiene un estado de registro de gestión de baterías por su nombre.
     *
     * @param name nombre del estado a buscar.
     * @return un {@link Optional} con el estado encontrado, o vacío si no existe.
     */
    Optional<BatteryManagementRecordStatus> getStatusByName(String name);
}

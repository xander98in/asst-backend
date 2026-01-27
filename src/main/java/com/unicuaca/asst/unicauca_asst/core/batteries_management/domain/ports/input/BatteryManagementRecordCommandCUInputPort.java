package com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.ports.input;

import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.models.BatteryManagementRecord;

public interface BatteryManagementRecordCommandCUInputPort {

    /**
     * Crea un nuevo registro de gestión de baterías para la persona evaluada indicada.
     *
     * @param personEvaluatedId ID de la persona evaluada para la cual se crea el registro.
     * @return El registro de gestión de baterías creado.
     */
    BatteryManagementRecord createBatteryManagementRecord(Long personEvaluatedId);

    /**
     * Elimina un registro de gestión de baterías por su ID.
     *
     * @param id ID del registro de gestión de baterías a eliminar.
     */
    void deleteBatteryManagementRecord(Long id);
}

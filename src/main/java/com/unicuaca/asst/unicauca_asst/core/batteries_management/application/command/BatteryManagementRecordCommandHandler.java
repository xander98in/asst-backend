package com.unicuaca.asst.unicauca_asst.core.batteries_management.application.command;

import com.unicuaca.asst.unicauca_asst.core.batteries_management.application.dto.response.BatteryManagementRecordResponseDTO;

public interface BatteryManagementRecordCommandHandler {

    /**
     * Crea un nuevo registro de gestión de baterías para la persona evaluada indicada.
     *
     * @param personEvaluatedId ID de la persona evaluada para la cual se crea el registro.
     * @return El DTO del registro de gestión de baterías creado.
     */
    BatteryManagementRecordResponseDTO createBatteryManagementRecord(Long personEvaluatedId);

    /**
     * Elimina un registro de gestión de baterías por su ID.
     *
     * @param recordId ID del registro de gestión de baterías a eliminar.
     */
    void deleteBatteryManagementRecord(Long recordId);
}

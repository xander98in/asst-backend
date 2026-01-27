package com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.ports.input;

import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.models.PersonEvaluatedDetails;

/**
 * Puerto de entrada para consultas sobre PersonEvaluatedDetails.
 */
public interface PersonEvaluatedDetailsQueryCUInputPort {

    /**
     * Obtiene los detalles (modelo dominio) asociados a un registro de gestión de batería.
     * En este flujo se usará solo para exponer id/batteryManagementRecordId/createdAt/updatedAt.
     *
     * @param batteryManagementRecordId ID del registro de gestión de batería
     * @return PersonEvaluatedDetails (con id, createdAt, updatedAt)
     */
    PersonEvaluatedDetails getMetaByBatteryManagementRecordId(Long batteryManagementRecordId);
}

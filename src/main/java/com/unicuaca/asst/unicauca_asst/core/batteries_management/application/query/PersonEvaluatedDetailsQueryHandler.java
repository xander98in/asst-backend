package com.unicuaca.asst.unicauca_asst.core.batteries_management.application.query;

import com.unicuaca.asst.unicauca_asst.core.batteries_management.application.dto.response.PersonEvaluatedDetailsMetaResponseDTO;

/**
 * Manejador de consultas para detalles de personas evaluadas.
 */
public interface PersonEvaluatedDetailsQueryHandler {

    /**
     * Obtiene metadata del detalle de una persona evaluada asociado a un registro de gestión de bateria.
     *
     * @param batteryManagementRecordId ID del registro de gestión de batería
     */
    PersonEvaluatedDetailsMetaResponseDTO getMetaByBatteryManagementRecordId(Long batteryManagementRecordId);
}

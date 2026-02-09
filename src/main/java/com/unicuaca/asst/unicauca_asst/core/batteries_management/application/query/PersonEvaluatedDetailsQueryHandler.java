package com.unicuaca.asst.unicauca_asst.core.batteries_management.application.query;

import com.unicuaca.asst.unicauca_asst.core.batteries_management.application.dto.response.PersonEvaluatedDetailsMetaResponseDTO;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.application.dto.response.PersonEvaluatedDetailsResponseDTO;

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

    /**
     * Obtiene los detalles completos de una persona evaluada por su ID.
     *
     * @param personEvaluatedDetailsId ID del detalle de la persona evaluada
     * @return DTO con los detalles de la persona evaluada
     */
    PersonEvaluatedDetailsResponseDTO getPersonEvaluatedDetailsById(Long personEvaluatedDetailsId);
}

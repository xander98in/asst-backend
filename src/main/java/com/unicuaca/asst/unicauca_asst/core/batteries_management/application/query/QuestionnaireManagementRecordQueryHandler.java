package com.unicuaca.asst.unicauca_asst.core.batteries_management.application.query;

import com.unicuaca.asst.unicauca_asst.core.batteries_management.application.dto.response.QuestionnaireManagementRecordResponseDTO;

/**
 * Manejador de consultas para registros de gestión de cuestionarios.
 */
public interface QuestionnaireManagementRecordQueryHandler {

    /**
     * Obtiene un registro de gestión de cuestionario por el ID del registro de gestión de batería y la abreviatura del cuestionario.
     *
     * @param batteryManagementRecordId ID del registro de gestión de batería
     * @param questionnaireAbbreviation Abreviatura del cuestionario
     * @return DTO con los detalles del registro de gestión del cuestionario
     */
    QuestionnaireManagementRecordResponseDTO getByBatteryRecordIdAndQuestionnaireAbbreviation(
        Long batteryManagementRecordId,
        String questionnaireAbbreviation
    );
}

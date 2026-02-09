package com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.ports.input;

import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.models.QuestionnaireManagementRecord;

/**
 * Puerto de entrada para consultas sobre QuestionnaireManagementRecord.
 */
public interface QuestionnaireManagementRecordQueryCUInputPort {

    /**
     * Obtiene un registro de gestión de cuestionario por el ID del registro de gestión de batería
     * y la abreviatura del cuestionario.
     *
     * @param batteryManagementRecordId ID del registro de gestión de batería
     * @param questionnaireAbbreviation abreviatura del cuestionario
     * @return QuestionnaireManagementRecord (modelo dominio)
     */
    QuestionnaireManagementRecord getByBatteryRecordIdAndQuestionnaireAbbreviation(
        Long batteryManagementRecordId,
        String questionnaireAbbreviation
    );
}

package com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.ports.input;

import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.models.QuestionnaireManagementRecord;

/**
 * Puerto de entrada para los casos de uso de consulta sobre registros de gestión de cuestionarios.
 *
 * <p>Define las operaciones de tipo "Query" del modelo {@link QuestionnaireManagementRecord},
 * permitiendo que los adaptadores de entrada interactúen con la lógica de negocio sin acoplarse
 * directamente a su implementación.</p>
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

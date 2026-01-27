package com.unicuaca.asst.unicauca_asst.core.batteries_management.application.command;

import com.unicuaca.asst.unicauca_asst.core.batteries_management.application.dto.request.QuestionnaireManagementRecordCreateRequestDTO;

/**
 * Manejador de comandos para operaciones sobre
 * registros de gestión de cuestionarios (QuestionnaireManagementRecord).
 */
public interface QuestionnaireManagementRecordCommandHandler {

    /**
     * Crea un nuevo registro de gestión de cuestionario.
     *
     * @param dto datos de entrada validados para la creación
     */
    void createQuestionnaireManagementRecord(QuestionnaireManagementRecordCreateRequestDTO dto);

}

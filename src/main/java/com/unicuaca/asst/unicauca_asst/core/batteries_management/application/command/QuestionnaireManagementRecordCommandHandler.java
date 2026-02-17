package com.unicuaca.asst.unicauca_asst.core.batteries_management.application.command;

import com.unicuaca.asst.unicauca_asst.core.batteries_management.application.dto.request.QuestionnaireManagementRecordCreateRequestDTO;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.application.dto.response.QuestionnaireManagementRecordResponseDTO;

/**
 * Manejador de comandos para operaciones sobre
 * registros de gestión de cuestionarios (QuestionnaireManagementRecord).
 */
public interface QuestionnaireManagementRecordCommandHandler {

    /**
     * Crea un nuevo registro de gestión de cuestionario.
     *
     * @param dto datos de entrada validados para la creación
     * @return el DTO de respuesta que representa el registro de gestión de cuestionario creado
     */
    QuestionnaireManagementRecordResponseDTO createQuestionnaireManagementRecord(QuestionnaireManagementRecordCreateRequestDTO dto);

}

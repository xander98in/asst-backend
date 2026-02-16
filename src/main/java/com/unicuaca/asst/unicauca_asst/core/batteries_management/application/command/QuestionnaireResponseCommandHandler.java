package com.unicuaca.asst.unicauca_asst.core.batteries_management.application.command;

import com.unicuaca.asst.unicauca_asst.core.batteries_management.application.dto.request.QuestionnaireResponseBatchCreateRequestDTO;

/**
 * Manejador de comandos para operaciones sobre las respuestas de los cuestionarios.
 */
public interface QuestionnaireResponseCommandHandler {

    /**
     * Procesa la creación masiva de respuestas de un cuestionario.
     *
     * @param dto datos de entrada validados que contienen el ID del registro y la lista de respuestas.
     */
    void createQuestionnaireResponseBatch(QuestionnaireResponseBatchCreateRequestDTO dto);
}

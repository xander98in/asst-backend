package com.unicuaca.asst.unicauca_asst.core.batteries_management.application.query;

import com.unicuaca.asst.unicauca_asst.core.batteries_management.application.dto.response.QuestionnaireResponseBatchResponseDTO;

/**
 * Manejador de consultas para respuestas de cuestionarios.
 * Este manejador se encarga de procesar las consultas relacionadas con las respuestas de los cuestionarios.
 */
public interface QuestionnaireResponseQueryHandler {

    /**
     * Consulta las respuestas de un registro de gestión de cuestionario y las retorna en un formato estructurado.
     *
     * @param recordId ID del registro de gestión de cuestionario.
     * @return DTO con la información del registro y sus respuestas.
     */
    QuestionnaireResponseBatchResponseDTO getResponsesByRecordId(Long recordId);
}

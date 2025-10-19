package com.unicuaca.asst.unicauca_asst.core.batteries_management.application.query;

import com.unicuaca.asst.unicauca_asst.core.batteries_management.application.dto.response.QuestionnaireResponseDTO;

import java.util.List;

/**
 * Manejador de consultas (Application Layer) para cuestionarios.
 */
public interface QuestionnaireQueryHandler {

    /**
     * Lista todos los cuestionarios disponibles.
     *
     * @return lista de {@link QuestionnaireResponseDTO}.
     */
    List<QuestionnaireResponseDTO> getAllQuestionnaires();

    /**
     * Obtiene un cuestionario por su abreviatura exacta.
     *
     * @param abbreviation abreviatura a buscar (p. ej., "ILA", "ILB", "EXT", "EST").
     * @return {@link QuestionnaireResponseDTO} correspondiente.
     */
    QuestionnaireResponseDTO getByAbbreviation(String abbreviation);
}

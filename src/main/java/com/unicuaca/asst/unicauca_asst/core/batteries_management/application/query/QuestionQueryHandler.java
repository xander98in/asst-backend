package com.unicuaca.asst.unicauca_asst.core.batteries_management.application.query;

import java.util.List;

import com.unicuaca.asst.unicauca_asst.core.batteries_management.application.dto.response.QuestionResponseDTO;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.application.dto.response.QuestionWithQuestionnaireResponseDTO;

/**
 * Manejador de consultas (Application Layer) para preguntas de cuestionarios.
 */
public interface QuestionQueryHandler {

    /**
     * Lista todas las preguntas sin información del cuestionario asociado.
     *
     * @return lista de {@link QuestionResponseDTO}.
     */
    List<QuestionResponseDTO> getAllQuestions();

    /**
     * Lista todas las preguntas con información del cuestionario asociado.
     *
     * @return lista de {@link QuestionWithQuestionnaireResponseDTO}.
     */
    List<QuestionWithQuestionnaireResponseDTO> getAllQuestionsWithQuestionnaire();

    /**
     * Obtiene una pregunta por su ID sin información del cuestionario.
     *
     * @param id identificador de la pregunta.
     * @return {@link QuestionResponseDTO} correspondiente.
     */
    QuestionResponseDTO getById(Long id);

    /**
     * Obtiene una pregunta por su ID con información del cuestionario asociado.
     *
     * @param id identificador de la pregunta.
     * @return {@link QuestionWithQuestionnaireResponseDTO} correspondiente.
     */
    QuestionWithQuestionnaireResponseDTO getByIdWithQuestionnaire(Long id);

    /**
     * Obtiene una pregunta por su orden y el ID del cuestionario,
     * con información del cuestionario asociado.
     *
     * @param order           orden de la pregunta dentro del cuestionario.
     * @param questionnaireId identificador del cuestionario.
     * @return {@link QuestionWithQuestionnaireResponseDTO} correspondiente.
     */
    QuestionWithQuestionnaireResponseDTO getByOrderAndQuestionnaireIdWithQuestionnaire(Integer order, Long questionnaireId);

    /**
     * Obtiene las preguntas asociadas a un cuestionario por su ID.
     *
     * @param questionnaireId ID del cuestionario.
     * @return Lista de preguntas (sin info anidada del cuestionario).
     */
    List<QuestionResponseDTO> getByQuestionnaireId(Long questionnaireId);

    /**
     * Obtiene las preguntas asociadas a un cuestionario por su Abreviatura.
     *
     * @param abbreviation Abreviatura del cuestionario (ej: "ILA").
     * @return Lista de preguntas (sin info anidada del cuestionario).
     */
    List<QuestionResponseDTO> getByQuestionnaireAbbreviation(String abbreviation);
}

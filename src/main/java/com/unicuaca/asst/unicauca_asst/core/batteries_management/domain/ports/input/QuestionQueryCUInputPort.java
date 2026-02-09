package com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.ports.input;

import java.util.List;

import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.models.Question;

/**
 * Puerto de entrada (Command/Query Use Cases) para operaciones de consulta
 * relacionadas con preguntas de cuestionarios.
 *
 * Expone operaciones de solo lectura para que los adaptadores de entrada (p. ej. controladores REST)
 * interactúen con la lógica de negocio sin acoplarse a su implementación.
 */
public interface QuestionQueryCUInputPort {

    /**
     * Consulta una pregunta por su identificador único,
     * sin requerir explícitamente la carga del cuestionario.
     *
     * @param id identificador de la pregunta.
     * @return la pregunta encontrada.
     */
    Question getQuestionById(Long id);

    /**
     * Consulta una pregunta por su identificador único,
     * cargando explícitamente la información del cuestionario asociado.
     *
     * @param id identificador de la pregunta.
     * @return la pregunta encontrada (incluyendo su cuestionario).
     */
    Question getQuestionByIdWithQuestionnaire(Long id);

    /**
     * Obtiene todas las preguntas, sin requerir explícitamente la carga del cuestionario.
     *
     * @return lista de preguntas (posiblemente vacía si no hay resultados).
     */
    List<Question> getAllQuestions();

    /**
     * Obtiene todas las preguntas, cargando explícitamente la información
     * del cuestionario asociado a cada una.
     *
     * @return lista de preguntas con sus cuestionarios (posiblemente vacía si no hay resultados).
     */
    List<Question> getAllQuestionsWithQuestionnaire();

    /**
     * Obtiene una pregunta por su orden y el identificador del cuestionario,
     * cargando explícitamente la información del cuestionario asociado.
     *
     * @param order           orden de la pregunta dentro del cuestionario.
     * @param questionnaireId identificador del cuestionario.
     * @return la pregunta encontrada (incluyendo su cuestionario).
     */
    Question getQuestionByOrderAndQuestionnaireIdWithQuestionnaire(Integer order, Long questionnaireId);

    /**
     * Obtiene las preguntas asociadas a un cuestionario específico por su ID.
     *
     * @param questionnaireId ID del cuestionario.
     * @return Lista de preguntas.
     */
    List<Question> getQuestionsByQuestionnaireId(Long questionnaireId);

    /**
     * Obtiene las preguntas asociadas a un cuestionario específico por su abreviatura.
     *
     * @param abbreviation Abreviatura del cuestionario.
     * @return Lista de preguntas.
     */
    List<Question> getQuestionsByQuestionnaireAbbreviation(String abbreviation);
}

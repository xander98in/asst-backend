package com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.ports.output;

import java.util.List;
import java.util.Optional;

import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.models.Question;

/**
 * Puerto de salida para operaciones de consulta sobre preguntas asociadas a cuestionarios.
 *
 * Define las firmas de los métodos que deben implementar los adaptadores de infraestructura 
 * encargados de recuperar información desde fuentes externas (p. ej., base de datos relacional).
 */
public interface QuestionQueryRepository {

    /**
     * Consulta una pregunta por su identificador único,
     * sin requerir explícitamente la carga del cuestionario.
     *
     * @param id identificador de la pregunta.
     * @return un {@link Optional} con la pregunta encontrada o vacío si no existe.
     */
    Optional<Question> getQuestionById(Long id);

    /**
     * Consulta una pregunta por su identificador único,
     * cargando explícitamente la información del cuestionario asociado.
     *
     * @param id identificador de la pregunta.
     * @return un {@link Optional} con la pregunta encontrada (incluyendo su cuestionario) o vacío si no existe.
     */
    Optional<Question> getQuestionByIdWithQuestionnaire(Long id);

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
     * @return un {@link Optional} con la pregunta encontrada (incluyendo su cuestionario) o vacío si no existe.
     */
    Optional<Question> getQuestionByOrderAndQuestionnaireIdWithQuestionnaire(Integer order, Long questionnaireId);
}

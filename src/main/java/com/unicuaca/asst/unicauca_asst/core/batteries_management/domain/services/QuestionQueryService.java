package com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.services;

import java.util.Collections;
import java.util.List;

import com.unicuaca.asst.unicauca_asst.common.application.output.ResultFormatterOutputPort;
import com.unicuaca.asst.unicauca_asst.common.exceptions.structure.ErrorCode;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.models.Question;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.ports.input.QuestionQueryCUInputPort;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.ports.output.QuestionQueryRepository;

import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.ports.output.QuestionnaireQueryRepository;
import lombok.RequiredArgsConstructor;

/**
 * Implementación del caso de uso para operaciones de consulta (query)
 * sobre preguntas de cuestionarios.
 *
 * Encapsula la lógica de negocio de lectura utilizando el puerto de salida
 * {@link QuestionQueryRepository}.
 */
@RequiredArgsConstructor
public class QuestionQueryService implements QuestionQueryCUInputPort {

    private final QuestionQueryRepository questionQueryRepository;
    private final QuestionnaireQueryRepository questionnaireQueryRepository;
    private final ResultFormatterOutputPort resultFormatter;

    /**
     * Consulta una pregunta por su identificador único,
     * sin requerir explícitamente la carga del cuestionario.
     *
     * @param id identificador de la pregunta.
     * @return la pregunta encontrada.
     */
    @Override
    public Question getQuestionById(Long id) {
        return questionQueryRepository.getQuestionById(id)
                .orElseGet(() -> {
                    resultFormatter.throwEntityNotFound(
                            ErrorCode.ENTITY_NOT_FOUND.getCode(),
                            String.format("La pregunta con ID %d no fue encontrada.", id)
                    );
                    return null; // requerido por el compilador
                });
    }

    /**
     * Consulta una pregunta por su identificador único,
     * cargando explícitamente la información del cuestionario asociado.
     *
     * @param id identificador de la pregunta.
     * @return la pregunta encontrada (incluyendo su cuestionario).
     */
    @Override
    public Question getQuestionByIdWithQuestionnaire(Long id) {
        return questionQueryRepository.getQuestionByIdWithQuestionnaire(id)
                .orElseGet(() -> {
                    resultFormatter.throwEntityNotFound(
                            ErrorCode.ENTITY_NOT_FOUND.getCode(),
                            String.format("La pregunta con ID %d no fue encontrada.", id)
                    );
                    return null;
                });
    }

    /**
     * Obtiene todas las preguntas, sin requerir explícitamente la carga del cuestionario.
     *
     * @return lista de preguntas (posiblemente vacía si no hay resultados).
     */
    @Override
    public List<Question> getAllQuestions() {
        return questionQueryRepository.getAllQuestions();
    }

    /**
     * Obtiene todas las preguntas, cargando explícitamente la información
     * del cuestionario asociado a cada una.
     *
     * @return lista de preguntas con sus cuestionarios (posiblemente vacía si no hay resultados).
     */
    @Override
    public List<Question> getAllQuestionsWithQuestionnaire() {
        return questionQueryRepository.getAllQuestionsWithQuestionnaire();
    }

    /**
     * Obtiene una pregunta por su orden y el identificador del cuestionario,
     * cargando explícitamente la información del cuestionario asociado.
     *
     * @param order           orden de la pregunta dentro del cuestionario.
     * @param questionnaireId identificador del cuestionario.
     * @return la pregunta encontrada (incluyendo su cuestionario).
     */
    @Override
    public Question getQuestionByOrderAndQuestionnaireIdWithQuestionnaire(Integer order, Long questionnaireId) {
        return questionQueryRepository.getQuestionByOrderAndQuestionnaireIdWithQuestionnaire(order, questionnaireId)
                .orElseGet(() -> {
                    resultFormatter.throwEntityNotFound(
                            ErrorCode.ENTITY_NOT_FOUND.getCode(),
                            String.format(
                                "No se encontró una pregunta con orden %d en el cuestionario con ID %d.",
                                order,
                                questionnaireId
                            )
                    );
                    return null;
                });
    }

    /**
     * Obtiene las preguntas asociadas a un cuestionario específico por su ID.
     *
     * @param questionnaireId ID del cuestionario.
     * @return Lista de preguntas.
     */
    @Override
    public List<Question> getQuestionsByQuestionnaireId(Long questionnaireId) {
        if (!questionnaireQueryRepository.existsById(questionnaireId)) {
            resultFormatter.throwEntityNotFound(
                ErrorCode.ENTITY_NOT_FOUND.getCode(),
                String.format("El cuestionario con ID %d no existe.", questionnaireId)
            );
            return null; // requerido por el compilador, aunque no se alcanzará debido a la excepción lanzada
        }
        return questionQueryRepository.getByQuestionnaireId(questionnaireId);
    }

    /**
     * Obtiene las preguntas asociadas a un cuestionario específico por su abreviatura.
     *
     * @param abbreviation Abreviatura del cuestionario.
     * @return Lista de preguntas.
     */
    @Override
    public List<Question> getQuestionsByQuestionnaireAbbreviation(String abbreviation) {
        if (!questionnaireQueryRepository.existsByAbbreviation(abbreviation)) {
            resultFormatter.throwEntityNotFound(
                ErrorCode.ENTITY_NOT_FOUND.getCode(),
                String.format("El cuestionario con abreviatura '%s' no existe.", abbreviation)
            );
            return null; // requerido por el compilador, aunque no se alcanzará debido a la excepción lanzada
        }
        return questionQueryRepository.getByQuestionnaireAbbreviation(abbreviation);
    }

}

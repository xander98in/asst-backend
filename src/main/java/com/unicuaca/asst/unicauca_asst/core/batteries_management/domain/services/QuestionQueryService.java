package com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.services;

import java.util.List;

import com.unicuaca.asst.unicauca_asst.common.application.output.ResultFormatterOutputPort;
import com.unicuaca.asst.unicauca_asst.common.exceptions.structure.ErrorCode;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.models.Question;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.ports.input.QuestionQueryCUInputPort;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.ports.output.QuestionQueryRepository;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.ports.output.QuestionnaireQueryRepository;

import lombok.RequiredArgsConstructor;

/**
 * Servicio de dominio para la consulta de preguntas de cuestionarios.
 *
 * <p>Implementa la lógica de lectura para recuperar preguntas individuales o en lote,
 * permitiendo filtrar por identificador, orden dentro del cuestionario o pertenencia
 * a un cuestionario específico (por ID o abreviatura). Valida la existencia de las
 * entidades referenciadas antes de ejecutar la consulta y centraliza la gestión de
 * errores mediante i18n para mensajes técnicos y de usuario.</p>
 */
@RequiredArgsConstructor
public class QuestionQueryService implements QuestionQueryCUInputPort {

    private final QuestionQueryRepository questionQueryRepository;
    private final QuestionnaireQueryRepository questionnaireQueryRepository;
    private final ResultFormatterOutputPort resultFormatter;

    /**
     * Obtiene una pregunta por su identificador único.
     *
     * @param id identificador de la pregunta
     * @return la pregunta encontrada
     */
    @Override
    public Question getQuestionById(Long id) {
        return questionQueryRepository.getQuestionById(id)
                .orElseGet(() -> {
                    resultFormatter.throwEntityNotFound(
                        ErrorCode.QUESTION_NOT_FOUND,
                        "user.question.not_found",
                        id
                    );
                    return null;
                });
    }

    /**
     * Obtiene una pregunta por su identificador, incluyendo la información
     * del cuestionario al que pertenece.
     *
     * @param id identificador de la pregunta
     * @return la pregunta encontrada con su cuestionario cargado
     */
    @Override
    public Question getQuestionByIdWithQuestionnaire(Long id) {
        return questionQueryRepository.getQuestionByIdWithQuestionnaire(id)
                .orElseGet(() -> {
                    resultFormatter.throwEntityNotFound(
                        ErrorCode.QUESTION_NOT_FOUND,
                        "user.question.query_details_not_found",
                        id
                    );
                    return null;
                });
    }

    /**
     * Obtiene todas las preguntas registradas en el sistema.
     *
     * @return lista de preguntas (posiblemente vacía si no hay registros)
     */
    @Override
    public List<Question> getAllQuestions() {
        return questionQueryRepository.getAllQuestions();
    }

    /**
     * Obtiene todas las preguntas registradas, incluyendo la información
     * del cuestionario asociado a cada una.
     *
     * @return lista de preguntas con sus cuestionarios cargados (posiblemente vacía)
     */
    @Override
    public List<Question> getAllQuestionsWithQuestionnaire() {
        return questionQueryRepository.getAllQuestionsWithQuestionnaire();
    }

    /**
     * Obtiene una pregunta por su posición ordinal dentro de un cuestionario específico,
     * incluyendo la información del cuestionario asociado.
     *
     * @param order           número de orden de la pregunta dentro del cuestionario
     * @param questionnaireId identificador del cuestionario al que pertenece
     * @return la pregunta encontrada con su cuestionario cargado
     */
    @Override
    public Question getQuestionByOrderAndQuestionnaireIdWithQuestionnaire(Integer order, Long questionnaireId) {
        return questionQueryRepository.getQuestionByOrderAndQuestionnaireIdWithQuestionnaire(order, questionnaireId)
                .orElseGet(() -> {
                    resultFormatter.throwEntityNotFound(
                        ErrorCode.QUESTION_NOT_FOUND,
                        "user.question.by_order_not_found",
                        order
                    );
                    return null;
                });
    }

    /**
     * Obtiene las preguntas asociadas a un cuestionario específico por su identificador.
     *
     * <p>Valida previamente que el cuestionario exista antes de ejecutar la consulta.</p>
     *
     * @param questionnaireId identificador del cuestionario
     * @return lista de preguntas pertenecientes al cuestionario
     */
    @Override
    public List<Question> getQuestionsByQuestionnaireId(Long questionnaireId) {
        if (!questionnaireQueryRepository.existsById(questionnaireId)) {
            resultFormatter.throwEntityNotFound(
                ErrorCode.QUESTIONNAIRE_NOT_FOUND_BY_REF,
                "user.question.questionnaire_not_found",
                questionnaireId
            );
            return null;
        }
        return questionQueryRepository.getByQuestionnaireId(questionnaireId);
    }

    /**
     * Obtiene las preguntas asociadas a un cuestionario específico por su abreviatura.
     *
     * <p>Valida previamente que el cuestionario exista antes de ejecutar la consulta.</p>
     *
     * @param abbreviation abreviatura del cuestionario (ej: "EXT", "EST", "ILA")
     * @return lista de preguntas pertenecientes al cuestionario
     */
    @Override
    public List<Question> getQuestionsByQuestionnaireAbbreviation(String abbreviation) {
        if (!questionnaireQueryRepository.existsByAbbreviation(abbreviation)) {
            resultFormatter.throwEntityNotFound(
                ErrorCode.QUESTIONNAIRE_NOT_FOUND_BY_REF,
                "user.question.questionnaire_not_found",
                abbreviation
            );
            return null;
        }
        return questionQueryRepository.getByQuestionnaireAbbreviation(abbreviation);
    }
}

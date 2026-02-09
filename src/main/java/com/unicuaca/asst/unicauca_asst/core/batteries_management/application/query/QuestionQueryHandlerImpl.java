package com.unicuaca.asst.unicauca_asst.core.batteries_management.application.query;

import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.unicuaca.asst.unicauca_asst.core.batteries_management.application.dto.response.QuestionResponseDTO;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.application.dto.response.QuestionWithQuestionnaireResponseDTO;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.application.mappers.QuestionMapper;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.ports.input.QuestionQueryCUInputPort;

import lombok.RequiredArgsConstructor;

/**
 * Implementación del manejador de consultas para preguntas de cuestionarios.
 */
@RequiredArgsConstructor
@Component
@Transactional(readOnly = true)
public class QuestionQueryHandlerImpl implements QuestionQueryHandler {

    private final QuestionQueryCUInputPort questionQueryCUInputPort;
    private final QuestionMapper questionMapper;

    /**
     * Lista todas las preguntas sin información del cuestionario asociado.
     *
     * @return lista de {@link QuestionResponseDTO}.
     */
    @Override
    public List<QuestionResponseDTO> getAllQuestions() {
        return questionMapper.toQuestionResponseDTOList(
            questionQueryCUInputPort.getAllQuestions()
        );
    }

    /**
     * Lista todas las preguntas con información del cuestionario asociado.
     *
     * @return lista de {@link QuestionWithQuestionnaireResponseDTO}.
     */
    @Override
    public List<QuestionWithQuestionnaireResponseDTO> getAllQuestionsWithQuestionnaire() {
        return questionMapper.toQuestionWithQuestionnaireResponseDTOList(
            questionQueryCUInputPort.getAllQuestionsWithQuestionnaire()
        );
    }

    /**
     * Obtiene una pregunta por su ID sin información del cuestionario.
     *
     * @param id identificador de la pregunta.
     * @return {@link QuestionResponseDTO} correspondiente.
     */
    @Override
    public QuestionResponseDTO getById(Long id) {
        return questionMapper.toQuestionResponseDTO(
            questionQueryCUInputPort.getQuestionById(id)
        );
    }

    /**
     * Obtiene una pregunta por su ID con información del cuestionario asociado.
     *
     * @param id identificador de la pregunta.
     * @return {@link QuestionWithQuestionnaireResponseDTO} correspondiente.
     */
    @Override
    public QuestionWithQuestionnaireResponseDTO getByIdWithQuestionnaire(Long id) {
        return questionMapper.toQuestionWithQuestionnaireResponseDTO(
            questionQueryCUInputPort.getQuestionByIdWithQuestionnaire(id)
        );
    }

    /**
     * Obtiene una pregunta por su orden y el ID del cuestionario,
     * con información del cuestionario asociado.
     *
     * @param order           orden de la pregunta dentro del cuestionario.
     * @param questionnaireId identificador del cuestionario.
     * @return {@link QuestionWithQuestionnaireResponseDTO} correspondiente.
     */
    @Override
    public QuestionWithQuestionnaireResponseDTO getByOrderAndQuestionnaireIdWithQuestionnaire(Integer order, Long questionnaireId) {
        return questionMapper.toQuestionWithQuestionnaireResponseDTO(
            questionQueryCUInputPort.getQuestionByOrderAndQuestionnaireIdWithQuestionnaire(order, questionnaireId)
        );
    }

    /**
     * Obtiene las preguntas asociadas a un cuestionario por su ID.
     *
     * @param questionnaireId ID del cuestionario.
     * @return Lista de preguntas (sin info anidada del cuestionario).
     */
    @Override
    public List<QuestionResponseDTO> getByQuestionnaireId(Long questionnaireId) {
        return questionMapper.toQuestionResponseDTOList(
            questionQueryCUInputPort.getQuestionsByQuestionnaireId(questionnaireId)
        );
    }

    /**
     * Obtiene las preguntas asociadas a un cuestionario por su Abreviatura.
     *
     * @param abbreviation Abreviatura del cuestionario (ej: "ILA").
     * @return Lista de preguntas (sin info anidada del cuestionario).
     */
    @Override
    public List<QuestionResponseDTO> getByQuestionnaireAbbreviation(String abbreviation) {
        return questionMapper.toQuestionResponseDTOList(
            questionQueryCUInputPort.getQuestionsByQuestionnaireAbbreviation(abbreviation)
        );
    }
}

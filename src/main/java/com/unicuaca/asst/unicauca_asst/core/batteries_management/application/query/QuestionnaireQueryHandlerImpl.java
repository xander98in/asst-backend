package com.unicuaca.asst.unicauca_asst.core.batteries_management.application.query;

import com.unicuaca.asst.unicauca_asst.core.batteries_management.application.dto.response.QuestionnaireResponseDTO;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.application.mappers.QuestionnaireMapper;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.ports.input.QuestionnaireQueryCUInputPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Implementación del manejador de consultas para cuestionarios.
 */
@RequiredArgsConstructor
@Component
@Transactional(readOnly = true)
public class QuestionnaireQueryHandlerImpl implements QuestionnaireQueryHandler {

    private final QuestionnaireQueryCUInputPort questionnaireQueryCUInputPort;
    private final QuestionnaireMapper questionnaireMapper;

    /**
     * Recupera todos los cuestionarios disponibles en el sistema.
     *
     * @return una lista de DTOs de respuesta de cuestionarios.
     */
    @Override
    public List<QuestionnaireResponseDTO> getAllQuestionnaires() {
        return questionnaireMapper.toResponseDTO(
            questionnaireQueryCUInputPort.getAllQuestionnaires()
        );
    }

    /**
     * Obtiene un cuestionario por su abreviatura exacta.
     *
     * @param abbreviation abreviatura del cuestionario.
     * @return el DTO de respuesta del cuestionario correspondiente.
     */
    @Override
    public QuestionnaireResponseDTO getByAbbreviation(String abbreviation) {
        return questionnaireMapper.toResponseDTO(
            questionnaireQueryCUInputPort.getQuestionnaireByAbbreviation(abbreviation)
        );
    }
}

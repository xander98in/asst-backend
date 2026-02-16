package com.unicuaca.asst.unicauca_asst.core.batteries_management.application.command;

import com.unicuaca.asst.unicauca_asst.core.batteries_management.application.dto.request.QuestionnaireResponseBatchCreateRequestDTO;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.application.mappers.QuestionnaireResponseMapper;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.models.QuestionnaireResponse;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.ports.input.QuestionnaireResponseCommandCUInputPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Implementación del manejador de comandos para respuestas de cuestionario.
 * Se encarga de orquestar la conversión de datos y la llamada a los puertos de entrada.
 */
@RequiredArgsConstructor
@Component
@Transactional
public class QuestionnaireResponseCommandHandlerImpl implements QuestionnaireResponseCommandHandler{

    private final QuestionnaireResponseCommandCUInputPort questionnaireResponseCommandCUInputPort;
    private final QuestionnaireResponseMapper questionnaireResponseMapper;

    /**
     * Procesa la creación masiva de respuestas de un cuestionario.
     *
     * @param dto datos de entrada validados que contienen el ID del registro y la lista de respuestas.
     */
    @Override
    public void createQuestionnaireResponseBatch(QuestionnaireResponseBatchCreateRequestDTO dto) {
        List<QuestionnaireResponse> domainResponses = questionnaireResponseMapper.toDomainList(dto);
        questionnaireResponseCommandCUInputPort.createQuestionnaireResponseBatch(domainResponses);
    }
}

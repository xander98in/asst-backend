package com.unicuaca.asst.unicauca_asst.core.batteries_management.application.query;

import com.unicuaca.asst.unicauca_asst.core.batteries_management.application.dto.response.QuestionnaireResponseBatchResponseDTO;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.application.mappers.QuestionnaireResponseMapper;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.models.QuestionnaireResponse;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.ports.input.QuestionnaireResponseQueryCUInputPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Implementación del manejador de consultas para respuestas de cuestionarios.
 *
 * Delega la lógica de negocio al puerto de entrada y transforma el modelo de dominio en un DTO para la respuesta.
 */
@RequiredArgsConstructor
@Component
@Transactional(readOnly = true)
public class QuestionnaireResponseQueryHandlerImpl implements QuestionnaireResponseQueryHandler {

    private final QuestionnaireResponseQueryCUInputPort queryInputPort;
    private final QuestionnaireResponseMapper mapper;

    /**
     * Consulta las respuestas de un registro de gestión de cuestionario y las retorna en un formato estructurado.
     *
     * @param recordId ID del registro de gestión de cuestionario.
     * @return DTO con la información del registro y sus respuestas.
     */
    @Override
    public QuestionnaireResponseBatchResponseDTO getResponsesByRecordId(Long recordId) {
        List<QuestionnaireResponse> domainList = queryInputPort.getResponsesByQuestionnaireManagementRecordId(recordId);
        return mapper.toBatchResponseDTO(domainList);
    }
}

package com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.services;

import com.unicuaca.asst.unicauca_asst.common.application.output.ResultFormatterOutputPort;
import com.unicuaca.asst.unicauca_asst.common.exceptions.structure.ErrorCode;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.models.QuestionnaireResponse;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.ports.input.QuestionnaireResponseQueryCUInputPort;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.ports.output.QuestionnaireManagementRecordQueryRepository;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.ports.output.QuestionnaireResponseQueryRepository;
import lombok.RequiredArgsConstructor;

import java.util.List;

/**
 * Implementación del caso de uso para consultas sobre respuestas de cuestionarios.
 */
@RequiredArgsConstructor
public class QuestionnaireResponseQueryService implements QuestionnaireResponseQueryCUInputPort {

    private final QuestionnaireManagementRecordQueryRepository questionnaireManagementRecordQueryRepository;
    private final QuestionnaireResponseQueryRepository questionnaireResponseQueryRepository;
    private final ResultFormatterOutputPort resultFormatter;

    /**
     * Obtiene la lista completa de respuestas asociadas a un registro de gestión de cuestionario.
     *
     * @param recordId ID del registro de gestión de cuestionario.
     * @return Lista de respuestas del dominio.
     */
    @Override
    public List<QuestionnaireResponse> getResponsesByQuestionnaireManagementRecordId(Long recordId) {

        // Validar que el registro de gestión exista
        if (!questionnaireManagementRecordQueryRepository.existsById(recordId)) {
            questionnaireManagementRecordQueryRepository.findById(recordId)
                .orElseThrow(() -> {
                    resultFormatter.throwEntityNotFound(
                        ErrorCode.ENTITY_NOT_FOUND.getCode(),
                        String.format(ErrorCode.ENTITY_NOT_FOUND.getMessageKey(), "El registro de gestión de cuestionario con ID " + recordId + " no existe.")
                    );
                    return null;
                });
        }
        // Obtener las respuestas asociadas al registro de gestión de cuestionario
        List<QuestionnaireResponse> responses = questionnaireResponseQueryRepository.findAllByRecordIdWithAllRelations(recordId);
        if (responses.isEmpty()) {
            return List.of();
        }
        return responses;
    }
}

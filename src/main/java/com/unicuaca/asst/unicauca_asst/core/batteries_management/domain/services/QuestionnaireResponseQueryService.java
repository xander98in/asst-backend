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
 * Servicio de dominio para la consulta de respuestas de cuestionarios.
 * 
 * <p>Implementa los casos de uso para recuperar las respuestas registradas en un proceso
 * de evaluación. Garantiza que el registro de gestión exista antes de intentar recuperar
 * la información mediante i18n.</p>
 */
@RequiredArgsConstructor
public class QuestionnaireResponseQueryService implements QuestionnaireResponseQueryCUInputPort {

    private final QuestionnaireManagementRecordQueryRepository questionnaireManagementRecordQueryRepository;
    private final QuestionnaireResponseQueryRepository questionnaireResponseQueryRepository;
    private final ResultFormatterOutputPort resultFormatter;

    /**
     * Recupera todas las respuestas asociadas a un registro de gestión de cuestionario.
     *
     * @param recordId identificador del registro de gestión
     * @return lista de respuestas encontradas (puede ser vacía)
     */
    @Override
    public List<QuestionnaireResponse> getResponsesByQuestionnaireManagementRecordId(Long recordId) {

        // Validación de existencia del registro antes de la consulta
        if (!questionnaireManagementRecordQueryRepository.existsById(recordId)) {
            resultFormatter.throwEntityNotFound(
                ErrorCode.QUESTIONNAIRE_MGMT_RECORD_NOT_FOUND,
                "user.responses.query_not_found",
                recordId
            );
        }

        // Recuperación de respuestas con relaciones hidratadas
        return questionnaireResponseQueryRepository.findAllByRecordIdWithAllRelations(recordId);
    }
}

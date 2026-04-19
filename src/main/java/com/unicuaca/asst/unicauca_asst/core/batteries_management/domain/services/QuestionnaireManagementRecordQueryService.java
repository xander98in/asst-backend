package com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.services;

import com.unicuaca.asst.unicauca_asst.common.domain.ports.output.ResultFormatterOutputPort;
import com.unicuaca.asst.unicauca_asst.common.exceptions.structure.ErrorCode;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.models.QuestionnaireManagementRecord;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.ports.input.QuestionnaireManagementRecordQueryCUInputPort;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.ports.output.QuestionnaireManagementRecordQueryRepository;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

/**
 * Servicio de dominio para la consulta de registros de gestión de cuestionarios.
 * 
 * <p>Proporciona acceso a la información de vinculación entre baterías y cuestionarios.
 * Implementa i18n para reportar fallos de búsqueda de forma detallada y profesional.</p>
 */
@RequiredArgsConstructor
public class QuestionnaireManagementRecordQueryService implements QuestionnaireManagementRecordQueryCUInputPort {

    private final QuestionnaireManagementRecordQueryRepository questionnaireManagementRecordQueryRepository;
    private final ResultFormatterOutputPort resultFormatter;

    /**
     * Obtiene un registro de gestión de cuestionario específico dentro de una batería.
     *
     * @param batteryManagementRecordId identificador del registro de gestión de batería (BMR)
     * @param questionnaireAbbreviation abreviatura del cuestionario solicitado
     * @return el registro de gestión de cuestionario encontrado
     */
    @Override
    public QuestionnaireManagementRecord getByBatteryRecordIdAndQuestionnaireAbbreviation(Long batteryManagementRecordId, String questionnaireAbbreviation) {
        Optional<QuestionnaireManagementRecord> recordOpt = questionnaireManagementRecordQueryRepository
            .findByBatteryManagementRecordIdAndQuestionnaireAbbreviationWithAll(batteryManagementRecordId, questionnaireAbbreviation);
        if (recordOpt.isEmpty()) {
            resultFormatter.throwEntityNotFound(
                ErrorCode.QUESTIONNAIRE_MGMT_RECORD_NOT_FOUND,
                "user.questionnaire_management.query_not_found",
                questionnaireAbbreviation
            );
        }
        return recordOpt.get();
    }
}

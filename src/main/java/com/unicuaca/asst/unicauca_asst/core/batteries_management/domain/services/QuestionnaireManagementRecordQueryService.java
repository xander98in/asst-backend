package com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.services;

import com.unicuaca.asst.unicauca_asst.common.application.output.ResultFormatterOutputPort;
import com.unicuaca.asst.unicauca_asst.common.exceptions.structure.ErrorCode;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.models.QuestionnaireManagementRecord;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.ports.input.QuestionnaireManagementRecordQueryCUInputPort;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.ports.output.QuestionnaireManagementRecordQueryRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class QuestionnaireManagementRecordQueryService implements QuestionnaireManagementRecordQueryCUInputPort {

    private final QuestionnaireManagementRecordQueryRepository questionnaireManagementRecordQueryRepository;
    private final ResultFormatterOutputPort resultFormatter;

    /**
     * Obtiene un registro de gestión de cuestionario por el ID del registro de gestión de batería
     * y la abreviatura del cuestionario.
     *
     * @param batteryManagementRecordId ID del registro de gestión de batería.
     * @param questionnaireAbbreviation Abreviatura del cuestionario.
     * @return El registro de gestión de cuestionario correspondiente.
     */
    @Override
    public QuestionnaireManagementRecord getByBatteryRecordIdAndQuestionnaireAbbreviation(Long batteryManagementRecordId, String questionnaireAbbreviation) {
        return questionnaireManagementRecordQueryRepository
            .findByBatteryManagementRecordIdAndQuestionnaireAbbreviationWithAll(batteryManagementRecordId, questionnaireAbbreviation)
            .orElseGet(() -> {
                resultFormatter.throwEntityNotFound(
                    ErrorCode.ENTITY_NOT_FOUND.getCode(),
                    String.format(
                        ErrorCode.ENTITY_NOT_FOUND.getMessageKey(),
                        "No se encontró un registro de gestión de cuestionario para el recordId "
                            + batteryManagementRecordId + " y el cuestionario " + questionnaireAbbreviation
                    )
                );
                return null;
            });
    }
}

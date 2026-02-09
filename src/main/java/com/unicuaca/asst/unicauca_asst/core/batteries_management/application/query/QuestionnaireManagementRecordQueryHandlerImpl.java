package com.unicuaca.asst.unicauca_asst.core.batteries_management.application.query;

import com.unicuaca.asst.unicauca_asst.core.batteries_management.application.dto.response.QuestionnaireManagementRecordResponseDTO;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.application.mappers.QuestionnaireManagementRecordMapper;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.models.QuestionnaireManagementRecord;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.ports.input.QuestionnaireManagementRecordQueryCUInputPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementación del manejador de consultas para registros de gestión de cuestionarios.
 * Delega la lógica de negocio al puerto de entrada y transforma
 * el modelo de dominio en un DTO para la respuesta.
 */
@RequiredArgsConstructor
@Component
@Transactional
public class QuestionnaireManagementRecordQueryHandlerImpl implements QuestionnaireManagementRecordQueryHandler{

    private final QuestionnaireManagementRecordQueryCUInputPort questionnaireManagementRecordQueryCUInputPort;
    private final QuestionnaireManagementRecordMapper questionnaireManagementRecordMapper;

    /**
     * Obtiene un registro de gestión de cuestionario por el ID del registro de gestión de batería y la abreviatura del cuestionario.
     *
     * @param batteryManagementRecordId ID del registro de gestión de batería
     * @param questionnaireAbbreviation Abreviatura del cuestionario
     * @return DTO con los detalles del registro de gestión del cuestionario
     */
    @Override
    public QuestionnaireManagementRecordResponseDTO getByBatteryRecordIdAndQuestionnaireAbbreviation(Long batteryManagementRecordId, String questionnaireAbbreviation) {
        QuestionnaireManagementRecord record =
            questionnaireManagementRecordQueryCUInputPort.getByBatteryRecordIdAndQuestionnaireAbbreviation(
                batteryManagementRecordId,
                questionnaireAbbreviation
            );

        return questionnaireManagementRecordMapper.toResponseDTO(record);
    }
}

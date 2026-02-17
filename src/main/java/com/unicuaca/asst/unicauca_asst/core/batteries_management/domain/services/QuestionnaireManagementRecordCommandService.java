package com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.services;

import com.unicuaca.asst.unicauca_asst.common.application.output.ResultFormatterOutputPort;
import com.unicuaca.asst.unicauca_asst.common.exceptions.structure.ErrorCode;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.models.BatteryManagementRecord;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.models.Questionnaire;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.models.QuestionnaireManagementRecord;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.models.QuestionnaireManagementRecordStatus;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.models.enums.QuestionnaireManagementRecordStatusEnum;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.ports.input.QuestionnaireManagementRecordCommandCUInputPort;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.ports.output.*;
import lombok.RequiredArgsConstructor;

/**
 * Servicio de dominio para la gestión de registros de cuestionarios.
 * Implementa los casos de uso relacionados con la creación, actualización y eliminación de registros de gestión de cuestionarios.
 * Orquesta la lógica de negocio y las reglas de dominio para estas operaciones.
 */
@RequiredArgsConstructor
public class QuestionnaireManagementRecordCommandService implements QuestionnaireManagementRecordCommandCUInputPort {

    private final BatteryManagementRecordQueryRepository batteryManagementRecordQueryRepository;
    private final QuestionnaireQueryRepository questionnaireQueryRepository;
    private final QuestionnaireManagementRecordStatusQueryRepository questionnaireManagementRecordStatusQueryRepository;
    private final QuestionnaireManagementRecordQueryRepository questionnaireManagementRecordQueryRepository;
    private final QuestionnaireManagementRecordCommandRepository questionnaireManagementRecordCommandRepository;
    private final ResultFormatterOutputPort resultFormatter;

    /**
     * Crea un nuevo registro de gestión de cuestionario.
     *
     * @param record modelo de dominio con los datos necesarios (IDs de batería y cuestionario)
     * @return el registro creado con sus datos completos (ID generado, fechas, estado, etc.)
     */
    @Override
    public QuestionnaireManagementRecord createQuestionnaireManagementRecord(QuestionnaireManagementRecord record) {

        // Obtener y Validar el Registro de Gestión de Batería
        Long batteryId = record.getBatteryManagementRecord().getId();

        BatteryManagementRecord batteryRecord = batteryManagementRecordQueryRepository.getBatteryManagementRecordById(batteryId)
            .orElseGet(() -> {
                resultFormatter.throwEntityNotFound(
                    ErrorCode.ENTITY_NOT_FOUND.getCode(),
                    String.format(ErrorCode.ENTITY_NOT_FOUND.getMessageKey(),
                        "El registro de gestión de batería con ID " + batteryId + " no existe.")
                );
                return null;
            });

        // Obtener y Validar el Cuestionario
        Long questionnaireId = record.getQuestionnaire().getId();

        Questionnaire questionnaire = questionnaireQueryRepository.getById(questionnaireId)
            .orElseGet(() -> {
                resultFormatter.throwEntityNotFound(
                    ErrorCode.ENTITY_NOT_FOUND.getCode(),
                    String.format(ErrorCode.ENTITY_NOT_FOUND.getMessageKey(),
                        "El cuestionario con ID " + questionnaireId + " no existe.")
                );
                return null;
            });

        // Validar si YA EXISTE el registro (Regla de Negocio)
        if (questionnaireManagementRecordQueryRepository.existsByBatteryManagementRecordIdAndQuestionnaireId(batteryId, questionnaireId)) {
            resultFormatter.throwEntityAlreadyExists(
                ErrorCode.QUESTIONNAIRE_RECORD_ALREADY_EXISTS.getCode(),
                String.format(ErrorCode.QUESTIONNAIRE_RECORD_ALREADY_EXISTS.getMessageKey(), batteryId, questionnaire.getName())
            );
        }

        // Obtener el Estado Inicial (CREADO) usando el Enum
        String initialStatusName = QuestionnaireManagementRecordStatusEnum.CREADO.getName();

        QuestionnaireManagementRecordStatus initialStatus = questionnaireManagementRecordStatusQueryRepository
            .getQuestionnaireManagementRecordStatusByName(initialStatusName)
            .orElseGet(() -> {
                resultFormatter.throwEntityNotFound(
                    ErrorCode.ENTITY_NOT_FOUND.getCode(),
                    String.format(ErrorCode.ENTITY_NOT_FOUND.getMessageKey(),
                        "El estado inicial '" + initialStatusName + "' no se encuentra configurado en el sistema.")
                );
                return null;
            });

        // Hydration (Completar el objeto de dominio)
        record.setBatteryManagementRecord(batteryRecord);
        record.setQuestionnaire(questionnaire);
        record.setStatus(initialStatus);

        // Persistencia. Al guardar, JPA retorna la entidad con ID generado.
        // Como 'record' ya tenía las relaciones completas, el resultado también las tendrá.
        return questionnaireManagementRecordCommandRepository.save(record);
    }
}

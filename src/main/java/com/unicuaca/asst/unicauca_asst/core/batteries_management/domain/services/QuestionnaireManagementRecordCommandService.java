package com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.services;

import com.unicuaca.asst.unicauca_asst.common.application.output.ResultFormatterOutputPort;
import com.unicuaca.asst.unicauca_asst.common.exceptions.structure.ErrorCode;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.models.*;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.models.enums.BatteryManagementRecordStatusCode;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.models.enums.QuestionnaireEnum;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.models.enums.QuestionnaireManagementRecordStatusEnum;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.ports.input.QuestionnaireManagementRecordCommandCUInputPort;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.ports.output.*;
import lombok.RequiredArgsConstructor;

import java.util.List;

/**
 * Servicio de dominio para la gestión de registros de gestión de cuestionarios.
 * 
 * <p>Esta clase orquesta la vinculación de cuestionarios específicos a un proceso de evaluación (Batería).
 * Maneja la creación inicial de registros de cuestionario, su eliminación controlada y la sincronización
 * automática del estado de la batería basada en el progreso de sus cuestionarios.</p>
 */
@RequiredArgsConstructor
public class QuestionnaireManagementRecordCommandService implements QuestionnaireManagementRecordCommandCUInputPort {

    private final BatteryManagementRecordQueryRepository batteryManagementRecordQueryRepository;
    private final QuestionnaireQueryRepository questionnaireQueryRepository;
    private final QuestionnaireManagementRecordStatusQueryRepository questionnaireManagementRecordStatusQueryRepository;
    private final QuestionnaireManagementRecordQueryRepository questionnaireManagementRecordQueryRepository;
    private final BatteryManagementRecordStatusQueryRepository batteryManagementRecordStatusQueryRepository;
    private final QuestionnaireResponseCommandRepository questionnaireResponseCommandRepository;
    private final QuestionnaireManagementRecordCommandRepository questionnaireManagementRecordCommandRepository;
    private final BatteryManagementRecordCommandRepository batteryManagementRecordCommandRepository;
    private final ResultFormatterOutputPort resultFormatter;

    /**
     * Crea un nuevo registro de gestión de cuestionario.
     *
     * @param record modelo de dominio con los datos necesarios (IDs de batería y cuestionario)
     * @return el registro creado con sus datos completos (ID generado, fechas, estado, etc.)
     */
    @Override
    public QuestionnaireManagementRecord createQuestionnaireManagementRecord(QuestionnaireManagementRecord record) {

        Long batteryId = record.getBatteryManagementRecord().getId();

        // Validación de existencia de la batería contenedora
        BatteryManagementRecord batteryRecord = batteryManagementRecordQueryRepository.getBatteryManagementRecordById(batteryId)
            .orElseGet(() -> {
                resultFormatter.throwEntityNotFound(
                    ErrorCode.BATTERY_RECORD_NOT_FOUND,
                    "user.questionnaire_management.battery_not_found",
                    batteryId
                );
                return null;
            });

        // Validación de existencia del catálogo de cuestionario
        Long questionnaireId = record.getQuestionnaire().getId();
        Questionnaire questionnaire = questionnaireQueryRepository.getById(questionnaireId)
            .orElseGet(() -> {
                resultFormatter.throwEntityNotFound(
                    ErrorCode.QUESTIONNAIRE_NOT_FOUND_BY_REF,
                    "user.questionnaire_management.not_found",
                    questionnaireId
                );
                return null;
            });

        // Regla de Negocio: Evitar duplicidad de asignación
        if (questionnaireManagementRecordQueryRepository
            .existsByBatteryManagementRecordIdAndQuestionnaireId(batteryId, questionnaireId)) {
            resultFormatter.throwEntityAlreadyExists(
                ErrorCode.QUESTIONNAIRE_ALREADY_ASSIGNED,
                "user.questionnaire_management.already_assigned",
                questionnaire.getName()
            );
        }

        // Resolución del estado inicial 'Creado' para el cuestionario
        String initialStatusName = QuestionnaireManagementRecordStatusEnum.CREADO.getName();
        QuestionnaireManagementRecordStatus initialStatus = questionnaireManagementRecordStatusQueryRepository
            .getQuestionnaireManagementRecordStatusByName(initialStatusName)
            .orElseGet(() -> {
                resultFormatter.throwEntityNotFound(
                    ErrorCode.QUESTIONNAIRE_MGMT_STATUS_NOT_FOUND,
                    "user.questionnaire_management.config_error",
                    initialStatusName
                );
                return null;
            });

        record.setBatteryManagementRecord(batteryRecord);
        record.setQuestionnaire(questionnaire);
        record.setStatus(initialStatus);

        return questionnaireManagementRecordCommandRepository.save(record);
    }

    /**
     * Elimina una vinculación de cuestionario y sincroniza el estado general de la batería.
     *
     * @param id identificador del registro de gestión de cuestionario a eliminar
     */
    @Override
    public void deleteQuestionnaireManagementRecord(Long id) {

        QuestionnaireManagementRecord recordToDelete = questionnaireManagementRecordQueryRepository.findByIdWithAll(id)
            .orElseGet(() -> {
                resultFormatter.throwEntityNotFound(
                    ErrorCode.QUESTIONNAIRE_MGMT_RECORD_NOT_FOUND,
                    "user.questionnaire_management.delete_not_found",
                    id
                );
                return null;
            });

        // Regla de Negocio: No eliminar cuestionarios finalizados
        if (QuestionnaireManagementRecordStatusEnum.CERRADO.getName().equals(recordToDelete.getStatus().getName())) {
            resultFormatter.throwBusinessRuleViolation(
                ErrorCode.QUESTIONNAIRE_RECORD_DELETE_NOT_ALLOWED,
                "user.questionnaire_management.delete_not_allowed",
                recordToDelete.getStatus().getName(), id
            );
        }

        Long batteryId = recordToDelete.getBatteryManagementRecord().getId();
        questionnaireResponseCommandRepository.deleteByQuestionnaireManagementRecordId(id);
        questionnaireManagementRecordCommandRepository.deleteById(id);

        // Recalculo dinámico del estado de la batería contenedora
        syncBatteryStatus(batteryId);
    }

    /**
     * Sincroniza el estado de la batería basado en el progreso de sus cuestionarios vinculados.
     *
     * @param batteryId identificador de la batería a sincronizar
     */
    private void syncBatteryStatus(Long batteryId) {
        String statusDiligenciado = QuestionnaireManagementRecordStatusEnum.DILIGENCIADO.getName();

        List<String> completedAbbreviations = questionnaireManagementRecordQueryRepository
            .findAbbreviationsByBatteryIdAndStatusName(batteryId, statusDiligenciado);

        // Reglas de negocio para determinar si la batería está completa (EXT + EST + (ILA ó ILB))
        boolean hasExt = completedAbbreviations.contains(QuestionnaireEnum.EXT.getAbbreviation());
        boolean hasEst = completedAbbreviations.contains(QuestionnaireEnum.EST.getAbbreviation());
        boolean hasIla = completedAbbreviations.contains(QuestionnaireEnum.ILA.getAbbreviation());
        boolean hasIlb = completedAbbreviations.contains(QuestionnaireEnum.ILB.getAbbreviation());

        boolean isBatteryComplete = hasExt && hasEst && (hasIla || hasIlb) && completedAbbreviations.size() == 3;

        String targetBatteryStatusName = isBatteryComplete 
            ? BatteryManagementRecordStatusCode.COMPLETED.getDescription() 
            : BatteryManagementRecordStatusCode.IN_PROCESSING.getDescription();

        BatteryManagementRecord batteryRecord = batteryManagementRecordQueryRepository.getBatteryManagementRecordById(batteryId)
            .orElseGet(() -> {
                resultFormatter.throwEntityNotFound(
                    ErrorCode.BATTERY_RECORD_NOT_FOUND,
                    "user.questionnaire_management.sync_battery_failed",
                    batteryId
                );
                return null;
            });

        if (!batteryRecord.getStatus().getName().equals(targetBatteryStatusName)) {
            BatteryManagementRecordStatus newStatus = batteryManagementRecordStatusQueryRepository
                .getStatusByName(targetBatteryStatusName)
                .orElseGet(() -> {
                    resultFormatter.throwEntityNotFound(
                        ErrorCode.BATTERY_STATUS_NOT_FOUND,
                        "user.questionnaire_management.sync_status_not_found",
                        targetBatteryStatusName
                    );
                    return null;
                });

            batteryRecord.setStatus(newStatus);
            batteryManagementRecordCommandRepository.updateBatteryManagementRecord(batteryRecord);
        }
    }
}

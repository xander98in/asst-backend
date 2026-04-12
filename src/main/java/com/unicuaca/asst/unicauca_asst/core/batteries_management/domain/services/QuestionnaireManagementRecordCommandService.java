package com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.services;

import com.unicuaca.asst.unicauca_asst.common.application.output.ResultFormatterOutputPort;
import com.unicuaca.asst.unicauca_asst.common.exceptions.structure.ErrorCode;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.models.BatteryManagementRecord;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.models.BatteryManagementRecordStatus;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.models.Questionnaire;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.models.QuestionnaireManagementRecord;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.models.QuestionnaireManagementRecordStatus;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.models.enums.BatteryManagementRecordStatusCode;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.models.enums.QuestionnaireEnum;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.models.enums.QuestionnaireManagementRecordStatusEnum;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.ports.input.QuestionnaireManagementRecordCommandCUInputPort;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.ports.output.BatteryManagementRecordCommandRepository;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.ports.output.BatteryManagementRecordQueryRepository;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.ports.output.BatteryManagementRecordStatusQueryRepository;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.ports.output.QuestionnaireManagementRecordCommandRepository;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.ports.output.QuestionnaireManagementRecordQueryRepository;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.ports.output.QuestionnaireManagementRecordStatusQueryRepository;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.ports.output.QuestionnaireQueryRepository;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.ports.output.QuestionnaireResponseCommandRepository;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

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
        Optional<BatteryManagementRecord> batteryOpt = batteryManagementRecordQueryRepository.getBatteryManagementRecordById(batteryId);
        if (batteryOpt.isEmpty()) {
            resultFormatter.throwEntityNotFound(
                ErrorCode.BATTERY_RECORD_NOT_FOUND,
                "user.questionnaire_management.battery_not_found",
                batteryId
            );
        }
        BatteryManagementRecord batteryRecord = batteryOpt.get();

        // Validación de existencia del catálogo de cuestionario
        Long questionnaireId = record.getQuestionnaire().getId();
        Optional<Questionnaire> questionnaireOpt = questionnaireQueryRepository.getById(questionnaireId);
        if (questionnaireOpt.isEmpty()) {
            resultFormatter.throwEntityNotFound(
                ErrorCode.QUESTIONNAIRE_NOT_FOUND_BY_REF,
                "user.questionnaire_management.not_found",
                questionnaireId
            );
        }
        Questionnaire questionnaire = questionnaireOpt.get();

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
        Optional<QuestionnaireManagementRecordStatus> initialStatusOpt = questionnaireManagementRecordStatusQueryRepository
            .getQuestionnaireManagementRecordStatusByName(initialStatusName);
        if (initialStatusOpt.isEmpty()) {
            resultFormatter.throwEntityNotFound(
                ErrorCode.QUESTIONNAIRE_MGMT_STATUS_NOT_FOUND,
                "user.questionnaire_management.config_error",
                initialStatusName
            );
        }
        QuestionnaireManagementRecordStatus initialStatus = initialStatusOpt.get();

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

        Optional<QuestionnaireManagementRecord> recordOpt = questionnaireManagementRecordQueryRepository.findByIdWithAll(id);
        if (recordOpt.isEmpty()) {
            resultFormatter.throwEntityNotFound(
                ErrorCode.QUESTIONNAIRE_MGMT_RECORD_NOT_FOUND,
                "user.questionnaire_management.delete_not_found",
                id
            );
        }
        QuestionnaireManagementRecord recordToDelete = recordOpt.get();

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

        Optional<BatteryManagementRecord> batteryOpt = batteryManagementRecordQueryRepository.getBatteryManagementRecordById(batteryId);
        if (batteryOpt.isEmpty()) {
            resultFormatter.throwEntityNotFound(
                ErrorCode.BATTERY_RECORD_NOT_FOUND,
                "user.questionnaire_management.sync_battery_failed",
                batteryId
            );
        }
        BatteryManagementRecord batteryRecord = batteryOpt.get();

        if (!batteryRecord.getStatus().getName().equals(targetBatteryStatusName)) {
            Optional<BatteryManagementRecordStatus> statusOpt = batteryManagementRecordStatusQueryRepository
                .getStatusByName(targetBatteryStatusName);
            if (statusOpt.isEmpty()) {
                resultFormatter.throwEntityNotFound(
                    ErrorCode.BATTERY_STATUS_NOT_FOUND,
                    "user.questionnaire_management.sync_status_not_found",
                    targetBatteryStatusName
                );
            }
            BatteryManagementRecordStatus newStatus = statusOpt.get();

            batteryRecord.setStatus(newStatus);
            batteryManagementRecordCommandRepository.updateBatteryManagementRecord(batteryRecord);
        }
    }
}

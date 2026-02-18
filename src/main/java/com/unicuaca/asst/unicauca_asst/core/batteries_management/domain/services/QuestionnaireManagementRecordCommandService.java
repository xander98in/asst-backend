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

    /**
     * Elimina un registro de gestión de cuestionario por su ID.
     * También se encarga de eliminar en cascada las respuestas asociadas a este registro de gestión de cuestionario, si existen.
     *
     * @param id identificador del registro a eliminar.
     */
    @Override
    public void deleteQuestionnaireManagementRecord(Long id) {

        // Buscar el registro completo (necesitamos la batería asociada)
        QuestionnaireManagementRecord recordToDelete = questionnaireManagementRecordQueryRepository.findByIdWithAll(id)
            .orElseGet(() -> {
                resultFormatter.throwEntityNotFound(
                    ErrorCode.ENTITY_NOT_FOUND.getCode(),
                    String.format(ErrorCode.ENTITY_NOT_FOUND.getMessageKey(), "El registro de gestión de cuestionario con ID " + id + " no existe.")
                );
                return null;
            });

        // Validar Estado (Regla de Negocio): Si está CERRADO, no se puede eliminar.
        if (QuestionnaireManagementRecordStatusEnum.CERRADO.getName().equals(recordToDelete.getStatus().getName())) {
            resultFormatter.throwBusinessRuleViolation(
                ErrorCode.QUESTIONNAIRE_RECORD_DELETE_NOT_ALLOWED.getCode(),
                String.format(ErrorCode.QUESTIONNAIRE_RECORD_DELETE_NOT_ALLOWED.getMessageKey(), recordToDelete.getStatus().getName())
            );
        }

        Long batteryId = recordToDelete.getBatteryManagementRecord().getId();
        //  Eliminar respuestas asociadas (Cascada)
        questionnaireResponseCommandRepository.deleteByQuestionnaireManagementRecordId(id);
        // Eliminar el registro de gestión de cuestionario
        questionnaireManagementRecordCommandRepository.deleteById(id);

        // =================================================================================
        // Recalcular y Actualizar Estado de la Batería
        // =================================================================================

        // Al borrar un cuestionario, la batería podría dejar de estar "Diligenciada".
        String statusDiligenciado = QuestionnaireManagementRecordStatusEnum.DILIGENCIADO.getName();

        // Obtenemos la lista de cuestionarios completados que QUEDAN
        List<String> completedAbbreviations = questionnaireManagementRecordQueryRepository
            .findAbbreviationsByBatteryIdAndStatusName(batteryId, statusDiligenciado);

        // Evaluar Reglas de Negocio (Misma lógica que en create/update)
        boolean hasExt = completedAbbreviations.contains(QuestionnaireEnum.EXT.getAbbreviation());
        boolean hasEst = completedAbbreviations.contains(QuestionnaireEnum.EST.getAbbreviation());
        boolean hasIla = completedAbbreviations.contains(QuestionnaireEnum.ILA.getAbbreviation());
        boolean hasIlb = completedAbbreviations.contains(QuestionnaireEnum.ILB.getAbbreviation());

        boolean isBatteryComplete = hasExt && hasEst && (hasIla || hasIlb) && completedAbbreviations.size() == 3;

        String targetBatteryStatusName;
        if (isBatteryComplete) {
            targetBatteryStatusName = BatteryManagementRecordStatusCode.COMPLETED.getDescription();
        } else {
            targetBatteryStatusName = BatteryManagementRecordStatusCode.IN_PROCESSING.getDescription();
        }

        // Actualizar Batería
        BatteryManagementRecord batteryRecord = batteryManagementRecordQueryRepository.getBatteryManagementRecordById(batteryId)
            .orElseThrow(() -> new RuntimeException("Error de integridad: Batería no encontrada"));

        // Optimización: Solo actualizar si el estado cambia
        if (!batteryRecord.getStatus().getName().equals(targetBatteryStatusName)) {
            BatteryManagementRecordStatus newStatus = batteryManagementRecordStatusQueryRepository
                .getStatusByName(targetBatteryStatusName)
                .orElseGet(() -> {
                    resultFormatter.throwEntityNotFound(
                        ErrorCode.ENTITY_NOT_FOUND.getCode(),
                        "El estado de batería '" + targetBatteryStatusName + "' no existe."
                    );
                    return null;
                });

            batteryRecord.setStatus(newStatus);
            batteryManagementRecordCommandRepository.updateBatteryManagementRecord(batteryRecord);
        }
    }
}

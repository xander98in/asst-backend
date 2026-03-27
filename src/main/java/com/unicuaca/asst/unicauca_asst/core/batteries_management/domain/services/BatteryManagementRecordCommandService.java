package com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.services;

import com.unicuaca.asst.unicauca_asst.common.application.output.ResultFormatterOutputPort;
import com.unicuaca.asst.unicauca_asst.common.exceptions.structure.ErrorCode;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.models.*;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.models.enums.BatteryManagementRecordStatusCode;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.models.enums.QuestionnaireManagementRecordStatusEnum;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.models.enums.StatusPersonEvaluatedEnum;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.ports.input.BatteryManagementRecordCommandCUInputPort;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.ports.output.*;
import lombok.RequiredArgsConstructor;

import java.util.List;

/**
 * Servicio de dominio para la gestión de comandos de registros de batería.
 * 
 * <p>Esta clase orquesta el ciclo de vida completo de un proceso de evaluación (Batería),
 * incluyendo su apertura, transición de estados, eliminación controlada y cierre definitivo.
 * Garantiza la integridad referencial y la sincronización de estados con la persona evaluada.</p>
 */
@RequiredArgsConstructor
public class BatteryManagementRecordCommandService implements BatteryManagementRecordCommandCUInputPort {

    private final BatteryManagementRecordCommandRepository batteryManagementRecordCommandRepository;
    private final BatteryManagementRecordQueryRepository batteryManagementRecordQueryRepository;
    private final PersonEvaluatedQueryRepository personEvaluatedQueryRepository;
    private final PersonEvaluatedCommandRepository personEvaluatedCommandRepository;
    private final QuestionnaireManagementRecordCommandRepository questionnaireManagementRecordCommandRepository;
    private final QuestionnaireManagementRecordQueryRepository questionnaireManagementRecordQueryRepository;
    private final QuestionnaireManagementRecordStatusQueryRepository questionnaireManagementRecordStatusQueryRepository;
    private final ResultFormatterOutputPort resultFormatterOutputPort;

    /**
     * Crea un nuevo registro de gestión de baterías para la persona evaluada indicada.
     *
     * @param personEvaluatedId identificador único de la persona evaluada
     * @return el nuevo registro de batería persistido
     */
    @Override
    public BatteryManagementRecord createBatteryManagementRecord(Long personEvaluatedId) {

        // Se crea una nueva instancia de BatteryManagementRecord
        BatteryManagementRecord record = BatteryManagementRecord.builder()
            .id(null)
            .status(null)
            .personEvaluated(null)
            .build();

        // Resolución del estado inicial 'Creado'
        BatteryManagementRecordStatus initialStatus = batteryManagementRecordQueryRepository
            .getBatteryManagementRecordStatudByName(BatteryManagementRecordStatusCode.CREATED.getDescription())
            .orElseGet(() -> {
                this.resultFormatterOutputPort.throwEntityNotFound(
                    ErrorCode.BATTERY_STATUS_NOT_FOUND,
                    "user.battery.config_error",
                    BatteryManagementRecordStatusCode.CREATED.getDescription()
                );
                return null;
            });
        record.setStatus(initialStatus);

        // Validación y vinculación de la persona evaluada
        if(personEvaluatedQueryRepository.existsById(personEvaluatedId)) {
            PersonEvaluated personEvaluated = personEvaluatedQueryRepository.getPersonEvaluatedById(personEvaluatedId)
                .orElseGet(() -> {
                    this.resultFormatterOutputPort.throwEntityNotFound(
                        ErrorCode.PERSON_NOT_FOUND,
                        "user.battery.person_not_found",
                        personEvaluatedId
                    );
                    return null;
                });

            // Actualización de estado de la persona para reflejar vinculación a proceso
            StatusPersonEvaluated withRecordStatus = personEvaluatedQueryRepository
                .getStatusPersonEvaluatedByName(StatusPersonEvaluatedEnum.WITH_RECORD.getDescription())
                .orElseGet(() -> {
                    this.resultFormatterOutputPort.throwEntityNotFound(
                        ErrorCode.PERSON_STATUS_NOT_FOUND,
                        "user.battery.sync_status_not_found",
                        StatusPersonEvaluatedEnum.WITH_RECORD.getDescription()
                    );
                    return null;
                });

            personEvaluated.setStatus(withRecordStatus);
            personEvaluated = personEvaluatedCommandRepository.updatePersonEvaluated(personEvaluated)
                .orElseGet(() -> {
                    this.resultFormatterOutputPort.throwEntityCreationFailed(
                        ErrorCode.ENTITY_UPDATE_ERROR,
                        "user.battery.status_update_failed",
                        personEvaluatedId
                    );
                    return null;
                });
            record.setPersonEvaluated(personEvaluated);
        } else {
            this.resultFormatterOutputPort.throwEntityNotFound(
                ErrorCode.PERSON_NOT_FOUND,
                "user.person.selected_not_exists",
                personEvaluatedId
            );
        }

        // Regla de Negocio: No permitir múltiples procesos activos simultáneos
        if (batteryManagementRecordQueryRepository.existsByPersonEvaluatedId(personEvaluatedId)) {
            this.resultFormatterOutputPort.throwEntityAlreadyExists(
                ErrorCode.BATTERY_RECORD_ALREADY_EXISTS,
                "user.battery.already_exists",
                personEvaluatedId
            );
        }

        // Guarda y retorna el nuevo registro de gestión de baterías
        return batteryManagementRecordCommandRepository.saveBatteryManagementRecord(record)
            .orElseGet(() -> {
                this.resultFormatterOutputPort.throwEntityCreationFailed(
                    ErrorCode.ENTITY_CREATION_ERROR,
                    "user.battery.creation_failed",
                    personEvaluatedId
                );
                return null;
            });
    }

    /**
     * Elimina un registro de gestión de batería siempre que el proceso no haya avanzado.
     *
     * @param id identificador del registro de batería a eliminar
     */
    @Override
    public void deleteBatteryManagementRecord(Long id) {

        BatteryManagementRecord batteryRecord = this.batteryManagementRecordQueryRepository
            .getBatteryManagementRecordById(id)
            .orElseGet(() -> {
                this.resultFormatterOutputPort.throwEntityNotFound(
                    ErrorCode.BATTERY_RECORD_NOT_FOUND,
                    "user.battery.delete_not_found",
                    id
                );
                return null;
            });

        BatteryManagementRecordStatus status = batteryRecord.getStatus();

        // Validación de estado habilitado para eliminación
        if (status.getName().equals(BatteryManagementRecordStatusCode.CREATED.getDescription())) {
            batteryManagementRecordCommandRepository.deleteBatteryManagementRecordById(id);

            PersonEvaluated personEvaluated = batteryRecord.getPersonEvaluated();
            if (personEvaluated != null) {
                syncPersonStatusAfterDelete(personEvaluated);
            }
        } else {
            this.resultFormatterOutputPort.throwBusinessRuleViolation(
                ErrorCode.DELETE_BATTERY_MANAGEMENT_RECORD,
                "user.battery.delete_not_allowed",
                status.getName(), id
            );
        }
    }

    /**
     * Finaliza y cierra un registro de batería de forma definitiva.
     *
     * @param recordId identificador del registro de batería
     * @return el registro actualizado en estado final 'Cerrado'
     */
    @Override
    public BatteryManagementRecord closeBatteryManagementRecord(Long recordId) {
        BatteryManagementRecord record = batteryManagementRecordQueryRepository.getBatteryManagementRecordById(recordId)
            .orElseGet(() -> {
                this.resultFormatterOutputPort.throwEntityNotFound(
                    ErrorCode.BATTERY_RECORD_NOT_FOUND,
                    "user.battery.close_not_found",
                    recordId
                );
                return null;
            });

        // Verificación de integridad del proceso antes del cierre
        if (!record.getStatus().getName().equals(BatteryManagementRecordStatusCode.COMPLETED.getDescription())) {
            this.resultFormatterOutputPort.throwBusinessRuleViolation(
                ErrorCode.CLOSE_BATTERY_MANAGEMENT_RECORD,
                "user.battery.close_not_allowed",
                record.getStatus().getName(), recordId
            );
        }

        // Cierre en cascada de los cuestionarios asociados
        QuestionnaireManagementRecordStatus closedStatus = questionnaireManagementRecordStatusQueryRepository
            .getQuestionnaireManagementRecordStatusByName(QuestionnaireManagementRecordStatusEnum.CERRADO.getName())
            .orElseGet(() -> {
                this.resultFormatterOutputPort.throwEntityNotFound(
                    ErrorCode.QUESTIONNAIRE_MGMT_STATUS_NOT_FOUND,
                    "user.battery.close_config_error",
                    QuestionnaireManagementRecordStatusEnum.CERRADO.getName()
                );
                return null;
            });

        List<QuestionnaireManagementRecord> questionnaires = questionnaireManagementRecordQueryRepository.findAllByBatteryManagementRecordId(recordId);
        questionnaires.forEach(q -> {
            q.setStatus(closedStatus);
            questionnaireManagementRecordCommandRepository.save(q);
        });

        // Actualización final del registro de batería
        BatteryManagementRecordStatus closedBatteryStatus = batteryManagementRecordQueryRepository
            .getBatteryManagementRecordStatudByName(BatteryManagementRecordStatusCode.CLOSED.getDescription())
            .orElseGet(() -> {
                this.resultFormatterOutputPort.throwEntityNotFound(
                    ErrorCode.BATTERY_STATUS_NOT_FOUND,
                    "user.battery.close_status_failed",
                    BatteryManagementRecordStatusCode.CLOSED.getDescription()
                );
                return null;
            });

        record.setStatus(closedBatteryStatus);
        return batteryManagementRecordCommandRepository.saveBatteryManagementRecord(record)
            .orElseGet(() -> {
                this.resultFormatterOutputPort.throwEntityCreationFailed(
                    ErrorCode.ENTITY_UPDATE_ERROR,
                    "user.battery.close_failed",
                    recordId
                );
                return null;
            });
    }

    /**
     * Coordina la actualización del estado de la persona evaluada tras la remoción de un proceso.
     * 
     * <p>Analiza si la persona cuenta con otros registros activos para decidir si el estado
     * debe mantenerse en 'Con Registro' o restaurarse a 'Sin Registro'.</p>
     *
     * @param personEvaluated la persona evaluada a sincronizar
     */
    private void syncPersonStatusAfterDelete(PersonEvaluated personEvaluated) {
        boolean hasOtherActiveRecords = batteryManagementRecordQueryRepository.existsByPersonEvaluatedIdAndStatusNameIn(
            personEvaluated.getId(),
            List.of(
                BatteryManagementRecordStatusCode.CREATED.getDescription(),
                BatteryManagementRecordStatusCode.IN_PROCESSING.getDescription(),
                BatteryManagementRecordStatusCode.COMPLETED.getDescription()
            )
        );

        String targetStatusName = hasOtherActiveRecords
            ? StatusPersonEvaluatedEnum.WITH_RECORD.getDescription()
            : StatusPersonEvaluatedEnum.WITHOUT_RECORD.getDescription();

        if (!personEvaluated.getStatus().getName().equals(targetStatusName)) {
            StatusPersonEvaluated newStatus = personEvaluatedQueryRepository.getStatusPersonEvaluatedByName(targetStatusName)
                .orElseGet(() -> {
                    this.resultFormatterOutputPort.throwEntityNotFound(
                        ErrorCode.PERSON_STATUS_NOT_FOUND,
                        "user.battery.sync_delete_error",
                        targetStatusName
                    );
                    return null;
                });

            personEvaluated.setStatus(newStatus);
            personEvaluatedCommandRepository.updatePersonEvaluated(personEvaluated);
        }
    }
}

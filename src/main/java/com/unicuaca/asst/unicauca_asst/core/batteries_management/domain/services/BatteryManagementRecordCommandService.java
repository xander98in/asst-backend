package com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.services;

import java.util.List;
import java.util.Optional;

import com.unicuaca.asst.unicauca_asst.common.domain.ports.output.ResultFormatterOutputPort;
import com.unicuaca.asst.unicauca_asst.common.exceptions.structure.ErrorCode;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.models.BatteryManagementRecord;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.models.BatteryManagementRecordStatus;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.models.PersonEvaluated;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.models.QuestionnaireManagementRecord;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.models.QuestionnaireManagementRecordStatus;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.models.StatusPersonEvaluated;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.models.enums.BatteryManagementRecordStatusCode;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.models.enums.QuestionnaireManagementRecordStatusEnum;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.models.enums.StatusPersonEvaluatedEnum;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.ports.input.BatteryManagementRecordCommandCUInputPort;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.ports.output.BatteryManagementRecordCommandRepository;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.ports.output.BatteryManagementRecordQueryRepository;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.ports.output.PersonEvaluatedCommandRepository;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.ports.output.PersonEvaluatedQueryRepository;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.ports.output.QuestionnaireManagementRecordCommandRepository;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.ports.output.QuestionnaireManagementRecordQueryRepository;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.ports.output.QuestionnaireManagementRecordStatusQueryRepository;

import lombok.RequiredArgsConstructor;

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
        Optional<BatteryManagementRecordStatus> optionalInitialStatus = batteryManagementRecordQueryRepository
            .getBatteryManagementRecordStatudByName(BatteryManagementRecordStatusCode.CREATED.getDescription());
        if (optionalInitialStatus.isEmpty()) {
            this.resultFormatterOutputPort.throwEntityNotFound(
                ErrorCode.BATTERY_STATUS_NOT_FOUND,
                "user.battery.config_error",
                BatteryManagementRecordStatusCode.CREATED.getDescription()
            );
        }
        BatteryManagementRecordStatus initialStatus = optionalInitialStatus.get();
        record.setStatus(initialStatus);

        // Validación y vinculación de la persona evaluada
        Optional<PersonEvaluated> optionalPerson = personEvaluatedQueryRepository.getPersonEvaluatedById(personEvaluatedId);
        if (optionalPerson.isEmpty()) {
            this.resultFormatterOutputPort.throwEntityNotFound(
                ErrorCode.PERSON_NOT_FOUND,
                "user.battery.person_not_found",
                personEvaluatedId
            );
        }
        PersonEvaluated personEvaluated = optionalPerson.get();

        // Actualización de estado de la persona para reflejar vinculación a proceso
        Optional<StatusPersonEvaluated> optionalWithRecordStatus = personEvaluatedQueryRepository
            .getStatusPersonEvaluatedByName(StatusPersonEvaluatedEnum.WITH_RECORD.getDescription());
        if (optionalWithRecordStatus.isEmpty()) {
            this.resultFormatterOutputPort.throwEntityNotFound(
                ErrorCode.PERSON_STATUS_NOT_FOUND,
                "user.battery.sync_status_not_found",
                StatusPersonEvaluatedEnum.WITH_RECORD.getDescription()
            );
        }
        StatusPersonEvaluated withRecordStatus = optionalWithRecordStatus.get();

        personEvaluated.setStatus(withRecordStatus);
        Optional<PersonEvaluated> optionalUpdatedPerson = personEvaluatedCommandRepository.updatePersonEvaluated(personEvaluated);
        if (optionalUpdatedPerson.isEmpty()) {
            this.resultFormatterOutputPort.throwEntityCreationFailed(
                ErrorCode.ENTITY_UPDATE_ERROR,
                "user.battery.status_update_failed",
                personEvaluatedId
            );
        }
        personEvaluated = optionalUpdatedPerson.get();
        record.setPersonEvaluated(personEvaluated);

        // Regla de Negocio: No permitir múltiples procesos activos simultáneos
        if (batteryManagementRecordQueryRepository.existsByPersonEvaluatedId(personEvaluatedId)) {
            this.resultFormatterOutputPort.throwEntityAlreadyExists(
                ErrorCode.BATTERY_RECORD_ALREADY_EXISTS,
                "user.battery.already_exists",
                personEvaluatedId
            );
        }

        // Guarda y retorna el nuevo registro de gestión de baterías
        Optional<BatteryManagementRecord> optionalSaved = batteryManagementRecordCommandRepository.saveBatteryManagementRecord(record);
        if (optionalSaved.isEmpty()) {
            this.resultFormatterOutputPort.throwEntityCreationFailed(
                ErrorCode.ENTITY_CREATION_ERROR,
                "user.battery.creation_failed",
                personEvaluatedId
            );
        }
        return optionalSaved.get();
    }

    /**
     * Elimina un registro de gestión de batería siempre que el proceso no haya avanzado.
     *
     * @param id identificador del registro de batería a eliminar
     */
    @Override
    public void deleteBatteryManagementRecord(Long id) {

        Optional<BatteryManagementRecord> optionalRecord = this.batteryManagementRecordQueryRepository
            .getBatteryManagementRecordById(id);
        if (optionalRecord.isEmpty()) {
            this.resultFormatterOutputPort.throwEntityNotFound(
                ErrorCode.BATTERY_RECORD_NOT_FOUND,
                "user.battery.delete_not_found",
                id
            );
        }
        BatteryManagementRecord batteryRecord = optionalRecord.get();

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
        Optional<BatteryManagementRecord> optionalRecord = batteryManagementRecordQueryRepository.getBatteryManagementRecordById(recordId);
        if (optionalRecord.isEmpty()) {
            this.resultFormatterOutputPort.throwEntityNotFound(
                ErrorCode.BATTERY_RECORD_NOT_FOUND,
                "user.battery.close_not_found",
                recordId
            );
        }
        BatteryManagementRecord record = optionalRecord.get();

        // Verificación de integridad del proceso antes del cierre
        if (!record.getStatus().getName().equals(BatteryManagementRecordStatusCode.COMPLETED.getDescription())) {
            this.resultFormatterOutputPort.throwBusinessRuleViolation(
                ErrorCode.CLOSE_BATTERY_MANAGEMENT_RECORD,
                "user.battery.close_not_allowed",
                record.getStatus().getName(), recordId
            );
        }

        // Cierre en cascada de los cuestionarios asociados
        Optional<QuestionnaireManagementRecordStatus> optionalClosedQStatus = questionnaireManagementRecordStatusQueryRepository
            .getQuestionnaireManagementRecordStatusByName(QuestionnaireManagementRecordStatusEnum.CERRADO.getName());
        if (optionalClosedQStatus.isEmpty()) {
            this.resultFormatterOutputPort.throwEntityNotFound(
                ErrorCode.QUESTIONNAIRE_MGMT_STATUS_NOT_FOUND,
                "user.battery.close_config_error",
                QuestionnaireManagementRecordStatusEnum.CERRADO.getName()
            );
        }
        QuestionnaireManagementRecordStatus closedStatus = optionalClosedQStatus.get();

        List<QuestionnaireManagementRecord> questionnaires = questionnaireManagementRecordQueryRepository.findAllByBatteryManagementRecordId(recordId);
        questionnaires.forEach(q -> {
            q.setStatus(closedStatus);
            questionnaireManagementRecordCommandRepository.save(q);
        });

        // Actualización final del registro de batería
        Optional<BatteryManagementRecordStatus> optionalClosedBatteryStatus = batteryManagementRecordQueryRepository
            .getBatteryManagementRecordStatudByName(BatteryManagementRecordStatusCode.CLOSED.getDescription());
        if (optionalClosedBatteryStatus.isEmpty()) {
            this.resultFormatterOutputPort.throwEntityNotFound(
                ErrorCode.BATTERY_STATUS_NOT_FOUND,
                "user.battery.close_status_failed",
                BatteryManagementRecordStatusCode.CLOSED.getDescription()
            );
        }
        BatteryManagementRecordStatus closedBatteryStatus = optionalClosedBatteryStatus.get();

        record.setStatus(closedBatteryStatus);
        Optional<BatteryManagementRecord> optionalSaved = batteryManagementRecordCommandRepository.saveBatteryManagementRecord(record);
        if (optionalSaved.isEmpty()) {
            this.resultFormatterOutputPort.throwEntityCreationFailed(
                ErrorCode.ENTITY_UPDATE_ERROR,
                "user.battery.close_failed",
                recordId
            );
        }
        return optionalSaved.get();
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
            Optional<StatusPersonEvaluated> optionalNewStatus = personEvaluatedQueryRepository.getStatusPersonEvaluatedByName(targetStatusName);
            if (optionalNewStatus.isEmpty()) {
                this.resultFormatterOutputPort.throwEntityNotFound(
                    ErrorCode.PERSON_STATUS_NOT_FOUND,
                    "user.battery.sync_delete_error",
                    targetStatusName
                );
            }
            StatusPersonEvaluated newStatus = optionalNewStatus.get();

            personEvaluated.setStatus(newStatus);
            personEvaluatedCommandRepository.updatePersonEvaluated(personEvaluated);
        }
    }
}

package com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.services;

import com.unicuaca.asst.unicauca_asst.common.application.output.ResultFormatterOutputPort;
import com.unicuaca.asst.unicauca_asst.common.exceptions.structure.ErrorCode;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.models.BatteryManagementRecord;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.models.BatteryManagementRecordStatus;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.models.PersonEvaluated;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.models.StatusPersonEvaluated;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.models.enums.BatteryManagementRecordStatusCode;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.ports.input.BatteryManagementRecordCommandCUInputPort;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.ports.output.BatteryManagementRecordCommandRepository;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.ports.output.BatteryManagementRecordQueryRepository;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.ports.output.PersonEvaluatedCommandRepository;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.ports.output.PersonEvaluatedQueryRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class BatteryManagementRecordCommandService implements BatteryManagementRecordCommandCUInputPort {

    private final BatteryManagementRecordCommandRepository batteryManagementRecordCommandRepository;
    private final BatteryManagementRecordQueryRepository batteryManagementRecordQueryRepository;
    private final PersonEvaluatedQueryRepository personEvaluatedQueryRepository;
    private final PersonEvaluatedCommandRepository personEvaluatedCommandRepository;
    private final ResultFormatterOutputPort resultFormatterOutputPort;

    /**
     * Crea un nuevo registro de gestión de baterías para la persona evaluada indicada.
     *
     * @param personEvaluatedId ID de la persona evaluada para la cual se crea el registro.
     * @return El registro de gestión de baterías creado.
     */
    @Override
    public BatteryManagementRecord createBatteryManagementRecord(Long personEvaluatedId) {

        // Se crea una nueva instancia de BatteryManagementRecord
        BatteryManagementRecord record = BatteryManagementRecord.builder()
            .id(null)
            .status(null)
            .personEvaluated(null)
            .build();

        // Se busca el estado "Creado" para asignarlo al registro de gestión de baterías
        BatteryManagementRecordStatus batteryManagementRecordStatus = batteryManagementRecordQueryRepository.getBatteryManagementRecordStatudByName(BatteryManagementRecordStatusCode.CREATED.getDescription())
            .orElseGet(() -> {
                this.resultFormatterOutputPort.throwEntityNotFound(
                    ErrorCode.ENTITY_NOT_FOUND.getCode(),
                    String.format(ErrorCode.ENTITY_NOT_FOUND.getMessageKey(), "El estado 'Creado' no fue encontrado")
                );
                return null;
            });
        record.setStatus(batteryManagementRecordStatus);

        // Se busca la persona evaluada por su ID se actualiza su estado a "Con registro" y se asigna al registro de gestión de baterías
        PersonEvaluated personEvaluated = null;
        if(personEvaluatedQueryRepository.existsById(personEvaluatedId)) {
            personEvaluated = personEvaluatedQueryRepository.getPersonEvaluatedById(personEvaluatedId)
                .orElseGet(() -> {
                    this.resultFormatterOutputPort.throwEntityNotFound(
                        ErrorCode.ENTITY_NOT_FOUND.getCode(),
                        String.format(ErrorCode.ENTITY_NOT_FOUND.getMessageKey(), "La persona evaluada con ID " + personEvaluatedId + " no fue encontrada")
                    );
                    return null;
                });
            StatusPersonEvaluated statusPersonEvaluated = personEvaluatedQueryRepository.getStatusPersonEvaluatedByName("Con registro")
                .orElseGet(() -> {
                    this.resultFormatterOutputPort.throwEntityNotFound(
                        ErrorCode.ENTITY_NOT_FOUND.getCode(),
                        String.format(ErrorCode.ENTITY_NOT_FOUND.getMessageKey(), "El estado 'Con registro' no fue encontrado.")
                    );
                    return null;
                });

            if(personEvaluated != null) {
                personEvaluated.setStatus(statusPersonEvaluated);
                String identificationNumber = personEvaluated.getIdentificationNumber();
                personEvaluated = personEvaluatedCommandRepository.updatePersonEvaluated(personEvaluated)
                    .orElseGet(() -> {
                        this.resultFormatterOutputPort.throwEntityCreationFailed(
                            ErrorCode.ENTITY_UPDATE_ERROR.getCode(),
                            String.format(ErrorCode.ENTITY_UPDATE_ERROR.getMessageKey(), "La persona con ID " + identificationNumber + " no se actualizó correctamente.")
                        );
                        return null;
                    });
            }
            System.out.println("Persona evaluada encontrada: " + personEvaluated);
            record.setPersonEvaluated(personEvaluated);
        }
        else {
            this.resultFormatterOutputPort.throwEntityNotFound(
                ErrorCode.ENTITY_NOT_FOUND.getCode(),
                String.format(ErrorCode.ENTITY_NOT_FOUND.getMessageKey(), "La persona evaluada con ID " + personEvaluatedId + " no fue encontrada")
            );
        }

        // Verifica si la persona evaluada ya tiene un registro de gestión de baterías
        if (batteryManagementRecordQueryRepository.existsByPersonEvaluatedId(personEvaluatedId)) {
            this.resultFormatterOutputPort.throwEntityAlreadyExists(
                ErrorCode.ENTITY_ALREADY_EXISTS.getCode(),
                String.format(ErrorCode.ENTITY_ALREADY_EXISTS.getMessageKey(), "La persona evaluada con ID " + personEvaluatedId + " ya tiene un registro de gestión de baterías.")
            );
        }

        // Guarda y retorna el nuevo registro de gestión de baterías
        return batteryManagementRecordCommandRepository.saveBatteryManagementRecord(record)
            .orElseGet(() -> {
                this.resultFormatterOutputPort.throwEntityCreationFailed(
                    ErrorCode.ENTITY_CREATION_ERROR.getCode(),
                    String.format(ErrorCode.ENTITY_CREATION_ERROR.getMessageKey(), "No fue posible crear el registro de gestión de baterías.")
                );
                return null;
            });
    }

    /**
     * Elimina un registro de gestión de baterías por su ID.
     *
     * @param id ID del registro de gestión de baterías a eliminar.
     */
    @Override
    public void deleteBatteryManagementRecord(Long id) {

        BatteryManagementRecord batteryManagementRecord = this.batteryManagementRecordQueryRepository
            .getBatteryManagementRecordById(id)
            .orElseGet(() -> {
                this.resultFormatterOutputPort.throwEntityNotFound(
                    ErrorCode.ENTITY_NOT_FOUND.getCode(),
                    String.format(ErrorCode.ENTITY_NOT_FOUND.getMessageKey(), "El registro de gestión de baterías con ID " + id + " no fue encontrado.")
                );
                return null;
            });

        System.out.println("Battery management record eliminado: " + batteryManagementRecord);
        BatteryManagementRecordStatus status = batteryManagementRecord.getStatus();
        System.out.println("Estado del registro: " + status.getName());

        if(status.getName().equals(BatteryManagementRecordStatusCode.CREATED.getDescription())) {
            batteryManagementRecordCommandRepository.deleteBatteryManagementRecordById(id);
            return;
        }
        else {
            this.resultFormatterOutputPort.throwBusinessRuleViolation(
                ErrorCode.DELETE_BATTERY_MANAGEMENT_RECORD.getCode(),
                String.format(ErrorCode.DELETE_BATTERY_MANAGEMENT_RECORD.getMessageKey(), "'Creado'")
            );
        }
    }
}

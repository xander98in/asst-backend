package com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.services;

import com.unicuaca.asst.unicauca_asst.common.application.output.ResultFormatterOutputPort;
import com.unicuaca.asst.unicauca_asst.common.exceptions.structure.ErrorCode;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.models.BatteryManagementRecord;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.models.BatteryManagementRecordStatus;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.models.PersonEvaluated;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.models.enums.BatteryManagementRecordStatusCode;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.ports.input.BatteryManagementRecordCommandCUInputPort;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.ports.output.BatteryManagementRecordCommandRepository;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.ports.output.BatteryManagementRecordQueryRepository;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.ports.output.PersonEvaluatedQueryRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class BatteryManagementRecordCommandService implements BatteryManagementRecordCommandCUInputPort {

    private final BatteryManagementRecordCommandRepository batteryManagementRecordCommandRepository;
    private final BatteryManagementRecordQueryRepository batteryManagementRecordQueryRepository;
    private final PersonEvaluatedQueryRepository personEvaluatedQueryRepository;
    private final ResultFormatterOutputPort resultFormatterOutputPort;

    @Override
    public BatteryManagementRecord createBatteryManagementRecord(Long personEvaluatedId) {
        BatteryManagementRecord record = BatteryManagementRecord.builder()
            .id(null)
            .status(null)
            .personEvaluated(null)
            .build();

        BatteryManagementRecordStatus batteryManagementRecordStatus = batteryManagementRecordQueryRepository.getBatteryManagementRecordStatudByName(BatteryManagementRecordStatusCode.CREATED.getDescription())
            .orElseGet(() -> {
                this.resultFormatterOutputPort.throwEntityNotFound(
                    ErrorCode.ENTITY_NOT_FOUND.getCode(),
                    String.format(ErrorCode.ENTITY_NOT_FOUND.getMessageKey(), "El estado 'Creado' no fue encontrado")
                );
                return null;
            });

        record.setStatus(batteryManagementRecordStatus);
        PersonEvaluated personEvaluated;
        if(personEvaluatedQueryRepository.existsById(personEvaluatedId)) {
            personEvaluated = personEvaluatedQueryRepository.getPersonEvaluatedById(personEvaluatedId)
                .orElseGet(() -> {
                    this.resultFormatterOutputPort.throwEntityNotFound(
                        ErrorCode.ENTITY_NOT_FOUND.getCode(),
                        String.format(ErrorCode.ENTITY_NOT_FOUND.getMessageKey(), "La persona evaluada con ID " + personEvaluatedId + " no fue encontrada")
                    );
                    return null;
                });
            record.setPersonEvaluated(personEvaluated);
        }
        else {
            this.resultFormatterOutputPort.throwEntityNotFound(
                ErrorCode.ENTITY_NOT_FOUND.getCode(),
                String.format(ErrorCode.ENTITY_NOT_FOUND.getMessageKey(), "La persona evaluada con ID " + personEvaluatedId + " no fue encontrada")
            );
        }

        if (batteryManagementRecordQueryRepository.existsByPersonEvaluatedId(personEvaluatedId)) {
            this.resultFormatterOutputPort.throwEntityAlreadyExists(
                ErrorCode.ENTITY_ALREADY_EXISTS.getCode(),
                String.format(ErrorCode.ENTITY_ALREADY_EXISTS.getMessageKey(), "La persona evaluada con ID " + personEvaluatedId + " ya tiene un registro de gestión de baterías.")
            );
        }

        return batteryManagementRecordCommandRepository.saveBatteryManagementRecord(record)
            .orElseGet(() -> {
                this.resultFormatterOutputPort.throwEntityCreationFailed(
                    ErrorCode.ENTITY_CREATION_ERROR.getCode(),
                    String.format(ErrorCode.ENTITY_CREATION_ERROR.getMessageKey(), "No fue posible crear el registro de gestión de baterías.")
                );
                return null;
            });
    }
}

package com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.services;

import com.unicuaca.asst.unicauca_asst.common.application.output.ResultFormatterOutputPort;
import com.unicuaca.asst.unicauca_asst.common.exceptions.structure.ErrorCode;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.models.BatteryManagementRecord;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.models.PersonEvaluatedDetails;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.ports.input.PersonEvaluatedDetailsQueryCUInputPort;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.ports.output.BatteryManagementRecordQueryRepository;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.ports.output.PersonEvaluatedDetailsQueryRepository;
import lombok.RequiredArgsConstructor;

/**
 * Servicio de consulta para detalles de personas evaluadas.
 */
@RequiredArgsConstructor
public class PersonEvaluatedDetailsQueryService implements PersonEvaluatedDetailsQueryCUInputPort {

    private final PersonEvaluatedDetailsQueryRepository personEvaluatedDetailsQueryRepository;
    private final BatteryManagementRecordQueryRepository batteryManagementRecordQueryRepository;
    private final ResultFormatterOutputPort resultFormatter;

    /**
     * Obtiene metadata del detalle de una persona evaluada asociado a un registro de gestión de bateria.
     *
     * @param batteryManagementRecordId ID del registro de gestión de batería
     * @return instancia de {@link PersonEvaluatedDetails}
     */
    @Override
    public PersonEvaluatedDetails getMetaByBatteryManagementRecordId(Long batteryManagementRecordId) {

        System.out.println("[PersonEvaluatedDetailsQueryService] getMetaByBatteryManagementRecordId -> recordId=" + batteryManagementRecordId);

        BatteryManagementRecord record = batteryManagementRecordQueryRepository
            .getBatteryManagementRecordById(batteryManagementRecordId)
            .orElseGet(() -> {
                this.resultFormatter.throwEntityNotFound(
                    ErrorCode.ENTITY_NOT_FOUND.getCode(),
                    String.format(
                        ErrorCode.ENTITY_NOT_FOUND.getMessageKey(),
                        "El registro de gestión de baterías con ID " + batteryManagementRecordId + " no fue encontrado."
                    )
                );
                return null;
            });

        PersonEvaluatedDetails details = personEvaluatedDetailsQueryRepository
            .getByBatteryManagementRecordId(record.getId())
            .orElseGet(() -> {
                this.resultFormatter.throwEntityNotFound(
                    ErrorCode.PERSON_EVALUATED_DETAIL_NOT_FOUNDS.getCode(),
                    String.format(ErrorCode.PERSON_EVALUATED_DETAIL_NOT_FOUNDS.getMessageKey(), batteryManagementRecordId)
                );
                return null;
            });

        return PersonEvaluatedDetails.builder()
            .id(details.getId())
            .batteryManagementRecord(record)
            .createdAt(details.getCreatedAt())
            .updatedAt(details.getUpdatedAt())
            .build();
    }
}

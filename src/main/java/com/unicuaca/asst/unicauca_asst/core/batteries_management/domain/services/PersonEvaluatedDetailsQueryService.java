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
 * Servicio de dominio para la consulta de información sociodemográfica de personas evaluadas.
 * 
 * <p>Esta clase permite recuperar la metadata y los detalles completos asociados a un proceso
 * de evaluación. Utiliza internacionalización (i18n) para reportar fallos de búsqueda de forma 
 * consistente y profesional.</p>
 */
@RequiredArgsConstructor
public class PersonEvaluatedDetailsQueryService implements PersonEvaluatedDetailsQueryCUInputPort {

    private final PersonEvaluatedDetailsQueryRepository personEvaluatedDetailsQueryRepository;
    private final BatteryManagementRecordQueryRepository batteryManagementRecordQueryRepository;
    private final ResultFormatterOutputPort resultFormatter;

    /**
     * Recupera la metadata básica del detalle sociodemográfico asociado a una batería.
     * 
     * <p>Se utiliza principalmente para verificar la existencia de información previa y 
     * obtener identificadores necesarios para el flujo de navegación.</p>
     *
     * @param batteryManagementRecordId identificador del registro de gestión de batería (BMR)
     * @return objeto con la metadata esencial (ID, fechas, tipos de cargo)
     */
    @Override
    public PersonEvaluatedDetails getMetaByBatteryManagementRecordId(Long batteryManagementRecordId) {

        // Validación de existencia de la batería
        BatteryManagementRecord record = batteryManagementRecordQueryRepository
            .getBatteryManagementRecordById(batteryManagementRecordId)
            .orElseGet(() -> {
                this.resultFormatter.throwEntityNotFound(
                    ErrorCode.BATTERY_RECORD_NOT_FOUND,
                    "user.person_evaluated_details.battery_not_found",
                    batteryManagementRecordId
                );
                return null;
            });

        // Consulta de detalles vinculados a la batería
        PersonEvaluatedDetails details = personEvaluatedDetailsQueryRepository
            .getByBatteryManagementRecordId(record.getId())
            .orElseGet(() -> {
                this.resultFormatter.throwEntityNotFound(
                    ErrorCode.PERSON_DETAILS_NOT_FOUND,
                    "user.person_evaluated_details.query_by_record_not_found",
                    batteryManagementRecordId
                );
                return null;
            });

        return PersonEvaluatedDetails.builder()
            .id(details.getId())
            .batteryManagementRecord(record)
            .jobPositionType(details.getJobPositionType())
            .createdAt(details.getCreatedAt())
            .updatedAt(details.getUpdatedAt())
            .build();
    }

    /**
     * Obtiene los detalles sociodemográficos completos por su identificador único.
     *
     * @param personEvaluatedDetailsId identificador único de los detalles
     * @return objeto con la información sociodemográfica completa e hidratada
     */
    @Override
    public PersonEvaluatedDetails getPersonEvaluatedDetailsById(Long personEvaluatedDetailsId) {
        return personEvaluatedDetailsQueryRepository
            .getByIdWithAll(personEvaluatedDetailsId)
            .orElseGet(() -> {
                this.resultFormatter.throwEntityNotFound(
                    ErrorCode.PERSON_DETAILS_NOT_FOUND,
                    "user.person_evaluated_details.query_not_found",
                    personEvaluatedDetailsId
                );
                return null;
            });
    }
}

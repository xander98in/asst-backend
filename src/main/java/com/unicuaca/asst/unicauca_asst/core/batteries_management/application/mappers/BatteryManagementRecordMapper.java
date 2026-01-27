package com.unicuaca.asst.unicauca_asst.core.batteries_management.application.mappers;

import com.unicuaca.asst.unicauca_asst.core.batteries_management.application.dto.response.BatteryManagementRecordInformationResponseDTO;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.application.dto.response.BatteryManagementRecordResponseDTO;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.models.BatteryManagementRecord;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.models.BatteryManagementRecordInformation;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper que convierte un objeto del modelo de dominio {@link BatteryManagementRecord}
 * a un DTO de respuesta {@link BatteryManagementRecordResponseDTO}.
 *
 * Utiliza MapStruct para mapear automáticamente los campos, incluyendo
 * atributos anidados como persona evaluada y su tipo de identificación.
 */
@Mapper(componentModel = "spring")
public interface BatteryManagementRecordMapper {

    @Mapping(target = "id", source = "id")
    @Mapping(target = "createdAt", source = "createdAt")
    @Mapping(target = "updatedAt", source = "updatedAt")
    @Mapping(target = "status", source = "status.name")
    @Mapping(target = "personEvaluatedId", source = "personEvaluated.id")
    @Mapping(target = "identificationType", source = "personEvaluated.identificationType.name")
    @Mapping(target = "identificationAbbreviation", source = "personEvaluated.identificationType.abbreviation")
    @Mapping(target = "identificationNumber", source = "personEvaluated.identificationNumber")
    @Mapping(target = "firstName", source = "personEvaluated.firstName")
    @Mapping(target = "lastName", source = "personEvaluated.lastName")
    BatteryManagementRecordResponseDTO toResponseDTO(BatteryManagementRecord batteryManagementRecord);

    /**
     * Mapea un objeto de dominio {@link BatteryManagementRecordInformation}
     * a un DTO de respuesta {@link BatteryManagementRecordInformationResponseDTO}.
     *
     * @param information el objeto de dominio a mapear
     * @return el DTO de respuesta mapeado
     */
    @Mapping(target = "id", source = "id")
    @Mapping(target = "personEvaluatedId", source = "personEvaluated.id")
    @Mapping(target = "identificationType", source = "personEvaluated.identificationType.name")
    @Mapping(target = "identificationAbbreviation", source = "personEvaluated.identificationType.abbreviation")
    @Mapping(target = "identificationNumber", source = "personEvaluated.identificationNumber")
    @Mapping(target = "firstName", source = "personEvaluated.firstName")
    @Mapping(target = "lastName", source = "personEvaluated.lastName")
    @Mapping(target = "batteryManagementRecordStatus", source = "status.name")
    @Mapping(target = "createdAt", source = "createdAt")
    @Mapping(target =  "area", source = "personEvaluatedDetails.workAreaName")
    BatteryManagementRecordInformationResponseDTO toInformationResponseDTO(BatteryManagementRecordInformation information);
}

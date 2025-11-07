package com.unicuaca.asst.unicauca_asst.core.batteries_management.application.mappers;

import com.unicuaca.asst.unicauca_asst.core.batteries_management.application.dto.response.BatteryManagementRecordResponseDTO;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.models.BatteryManagementRecord;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

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
}

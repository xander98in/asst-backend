package com.unicuaca.asst.unicauca_asst.core.batteries_management.application.mappers;

import com.unicuaca.asst.unicauca_asst.core.batteries_management.application.dto.response.QuestionnaireManagementRecordResponseDTO;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.models.QuestionnaireManagementRecord;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper para convertir entre el dominio de registro de gestión de cuestionario y su DTO de respuesta.
 */
@Mapper(componentModel = "spring")
public interface QuestionnaireManagementRecordMapper {

    /**
     * Mapea un dominio de registro de gestión de cuestionario a un DTO de respuesta.
     *
     * @param domain El dominio del registro de gestión de cuestionario
     * @return El DTO de respuesta correspondiente
     */
    @Mapping(target = "id", source = "id")
    @Mapping(target = "statusId", source = "status.id")
    @Mapping(target = "statusName", source = "status.name")
    @Mapping(target = "batteryManagementRecordId", source = "batteryManagementRecord.id")
    @Mapping(target = "questionnaireId", source = "questionnaire.id")
    @Mapping(target = "questionnaireAbbreviation", source = "questionnaire.abbreviation")
    @Mapping(target = "questionnaireName", source = "questionnaire.name")
    @Mapping(target = "createdAt", source = "createdAt")
    @Mapping(target = "updatedAt", source = "updatedAt")
    QuestionnaireManagementRecordResponseDTO toResponseDTO(QuestionnaireManagementRecord domain);
}

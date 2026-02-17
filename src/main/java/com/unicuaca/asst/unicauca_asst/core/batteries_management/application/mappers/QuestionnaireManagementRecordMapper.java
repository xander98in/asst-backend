package com.unicuaca.asst.unicauca_asst.core.batteries_management.application.mappers;

import com.unicuaca.asst.unicauca_asst.core.batteries_management.application.dto.request.QuestionnaireManagementRecordCreateRequestDTO;
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
    @Mapping(target = "batteryManagementRecordId", source = "batteryManagementRecord.id")
    @Mapping(target = "statusId", source = "status.id")
    @Mapping(target = "statusName", source = "status.name")
    @Mapping(target = "questionnaireId", source = "questionnaire.id")
    @Mapping(target = "questionnaireName", source = "questionnaire.name")
    @Mapping(target = "questionnaireAbbreviation", source = "questionnaire.abbreviation")
    @Mapping(target = "questionnaireDescription", source = "questionnaire.description")
    @Mapping(target = "createdAt", source = "createdAt")
    @Mapping(target = "updatedAt", source = "updatedAt")
    QuestionnaireManagementRecordResponseDTO toResponseDTO(QuestionnaireManagementRecord domain);

    /**
     * Convierte el DTO de creación al modelo de Dominio.
     * Mapeamos los IDs a objetos anidados vacíos (solo con ID) para que el dominio los use.
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "status", ignore = true) // El estado se asigna en el servicio
    @Mapping(target = "batteryManagementRecord.id", source = "batteryManagementRecordId")
    @Mapping(target = "questionnaire.id", source = "questionnaireId")
    QuestionnaireManagementRecord toDomain(QuestionnaireManagementRecordCreateRequestDTO dto);
}

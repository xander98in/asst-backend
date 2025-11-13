package com.unicuaca.asst.unicauca_asst.core.batteries_management.application.mappers;

import java.util.List;

import org.mapstruct.Mapper;

import com.unicuaca.asst.unicauca_asst.core.batteries_management.application.dto.response.QuestionnaireManagementRecordStatusResponseDTO;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.models.QuestionnaireManagementRecordStatus;

/**
 * Mapper de la capa de aplicación para convertir modelos de dominio de estados
 * de registro de gestión de cuestionarios a DTOs de respuesta.
 *
 * Usa MapStruct para generar el mapeo en tiempo de compilación.
 */
@Mapper(componentModel = "spring")
public interface QuestionnaireManagementRecordStatusMapper {

    /**
     * Convierte un modelo de dominio a su DTO de respuesta.
     *
     * @param domain modelo de dominio {@link QuestionnaireManagementRecordStatus}.
     * @return {@link QuestionnaireManagementRecordStatusResponseDTO}.
     */
    QuestionnaireManagementRecordStatusResponseDTO toResponseDTO(QuestionnaireManagementRecordStatus domain);

    /**
     * Convierte una lista de modelos de dominio a una lista de DTOs.
     *
     * @param domainList lista de {@link QuestionnaireManagementRecordStatus}.
     * @return lista de {@link QuestionnaireManagementRecordStatusResponseDTO}.
     */
    List<QuestionnaireManagementRecordStatusResponseDTO> toResponseDTO(List<QuestionnaireManagementRecordStatus> domainList);
}

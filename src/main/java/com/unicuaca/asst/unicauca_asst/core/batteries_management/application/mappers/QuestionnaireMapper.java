package com.unicuaca.asst.unicauca_asst.core.batteries_management.application.mappers;

import com.unicuaca.asst.unicauca_asst.core.batteries_management.application.dto.response.QuestionnaireResponseDTO;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.models.Questionnaire;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * Mapper de la capa de aplicación para convertir modelos de dominio de cuestionario a DTOs.
 * <p>
 * Usa MapStruct para generar el mapeo en tiempo de compilación.
 */
@Mapper(componentModel = "spring")
public interface QuestionnaireMapper {

    /**
     * Convierte un modelo de dominio a su DTO de respuesta.
     *
     * @param domain modelo de dominio {@link Questionnaire}.
     * @return {@link QuestionnaireResponseDTO}.
     */
    QuestionnaireResponseDTO toResponseDTO(Questionnaire domain);

    /**
     * Convierte una lista de modelos de dominio a una lista de DTOs.
     *
     * @param domainList lista de {@link Questionnaire}.
     * @return lista de {@link QuestionnaireResponseDTO}.
     */
    List<QuestionnaireResponseDTO> toResponseDTO(List<Questionnaire> domainList);
}

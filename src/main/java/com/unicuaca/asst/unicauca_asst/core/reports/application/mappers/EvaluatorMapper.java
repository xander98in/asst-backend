package com.unicuaca.asst.unicauca_asst.core.reports.application.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.unicuaca.asst.unicauca_asst.core.reports.application.dto.request.EvaluatorCreateRequestDTO;
import com.unicuaca.asst.unicauca_asst.core.reports.application.dto.request.EvaluatorUpdateRequestDTO;
import com.unicuaca.asst.unicauca_asst.core.reports.application.dto.response.EvaluatorResponseDTO;
import com.unicuaca.asst.unicauca_asst.core.reports.domain.models.Evaluator;

/**
 * Mapper MapStruct para la conversión entre modelos de dominio y DTOs de evaluadores.
 */
@Mapper(componentModel = "spring")
public interface EvaluatorMapper {

    /**
     * Convierte un modelo de dominio de evaluador a DTO de respuesta.
     *
     * @param evaluator modelo de dominio a convertir
     * @return DTO de respuesta equivalente
     */
    EvaluatorResponseDTO toResponseDTO(Evaluator evaluator);

    /**
     * Convierte un DTO de creación de evaluador a modelo de dominio, ignorando
     * los campos autogenerados (id, creatorUserId, createdAt).
     *
     * @param dto DTO de creación a convertir
     * @return modelo de dominio equivalente sin campos autogenerados
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "creatorUserId", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    Evaluator toDomain(EvaluatorCreateRequestDTO dto);

    /**
     * Convierte un DTO de actualización de evaluador a modelo de dominio, ignorando
     * los campos autogenerados (id, creatorUserId, createdAt).
     *
     * @param dto DTO de actualización a convertir
     * @return modelo de dominio equivalente sin campos autogenerados
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "creatorUserId", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    Evaluator toDomain(EvaluatorUpdateRequestDTO dto);
}

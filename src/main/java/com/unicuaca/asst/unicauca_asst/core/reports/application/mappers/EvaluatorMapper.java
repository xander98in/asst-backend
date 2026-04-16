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

    EvaluatorResponseDTO toResponseDTO(Evaluator evaluator);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "creatorUserId", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    Evaluator toDomain(EvaluatorCreateRequestDTO dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "creatorUserId", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    Evaluator toDomain(EvaluatorUpdateRequestDTO dto);
}

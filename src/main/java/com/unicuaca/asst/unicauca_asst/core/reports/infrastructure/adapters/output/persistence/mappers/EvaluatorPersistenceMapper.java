package com.unicuaca.asst.unicauca_asst.core.reports.infrastructure.adapters.output.persistence.mappers;

import org.mapstruct.Mapper;

import com.unicuaca.asst.unicauca_asst.core.reports.domain.models.Evaluator;
import com.unicuaca.asst.unicauca_asst.core.reports.infrastructure.adapters.output.persistence.jpa.entities.EvaluatorEntity;

/**
 * Mapper MapStruct entre entidad JPA y modelo de dominio de evaluadores.
 */
@Mapper(componentModel = "spring", builder = @org.mapstruct.Builder(disableBuilder = true))
public interface EvaluatorPersistenceMapper {

    Evaluator toDomain(EvaluatorEntity entity);

    EvaluatorEntity toEntity(Evaluator domain);
}

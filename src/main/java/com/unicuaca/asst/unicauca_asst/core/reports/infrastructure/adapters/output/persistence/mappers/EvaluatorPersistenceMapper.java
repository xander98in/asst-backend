package com.unicuaca.asst.unicauca_asst.core.reports.infrastructure.adapters.output.persistence.mappers;

import org.mapstruct.Mapper;

import com.unicuaca.asst.unicauca_asst.core.reports.domain.models.Evaluator;
import com.unicuaca.asst.unicauca_asst.core.reports.infrastructure.adapters.output.persistence.jpa.entities.EvaluatorEntity;

/**
 * Mapper MapStruct entre entidad JPA y modelo de dominio de evaluadores.
 */
@Mapper(componentModel = "spring", builder = @org.mapstruct.Builder(disableBuilder = true))
public interface EvaluatorPersistenceMapper {

    /**
     * Convierte una entidad JPA de evaluador a modelo de dominio.
     *
     * @param entity entidad JPA a convertir
     * @return modelo de dominio equivalente
     */
    Evaluator toDomain(EvaluatorEntity entity);

    /**
     * Convierte un modelo de dominio de evaluador a entidad JPA.
     *
     * @param domain modelo de dominio a convertir
     * @return entidad JPA equivalente
     */
    EvaluatorEntity toEntity(Evaluator domain);
}

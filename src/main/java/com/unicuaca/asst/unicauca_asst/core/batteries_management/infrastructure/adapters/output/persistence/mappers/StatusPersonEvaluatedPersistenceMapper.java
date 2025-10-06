package com.unicuaca.asst.unicauca_asst.core.batteries_management.infrastructure.adapters.output.persistence.mappers;

import org.mapstruct.Mapper;

import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.models.StatusPersonEvaluated;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.infrastructure.adapters.output.persistence.jpa.entities.StatusPersonEvaluatedEntity;

/**
 * Mapper MapStruct entre:
 * - StatusPersonEvaluatedEntity (JPA/infraestructura)
 * - StatusPersonEvaluated (dominio)
 */
@Mapper(componentModel = "spring")
public interface StatusPersonEvaluatedPersistenceMapper {

    /**
     * Convierte una entidad de JPA a un modelo de dominio.
     *
     * @param entity la entidad de JPA
     * @return el modelo de dominio correspondiente
     */
    StatusPersonEvaluated toDomain(StatusPersonEvaluatedEntity entity);

    /**
     * Convierte un modelo de dominio a una entidad de JPA.
     *
     * @param domain el modelo de dominio
     * @return la entidad de JPA correspondiente
     */
    StatusPersonEvaluatedEntity toEntity(StatusPersonEvaluated domain);
}

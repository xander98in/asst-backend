package com.unicuaca.asst.unicauca_asst.common.infrastructure.adapters.output.persistence.mappers;

import com.unicuaca.asst.unicauca_asst.common.domain.models.Gender;
import com.unicuaca.asst.unicauca_asst.common.infrastructure.adapters.output.persistence.jpa.entities.GenderEntity;
import org.mapstruct.Mapper;

/**
 * Mapper MapStruct entre:
 * - GenderEntity (JPA/infraestructura)
 * - Gender (dominio)
 */
@Mapper(componentModel = "spring")
public interface GenderPersistenceMapper {

    /**
     * Convierte una entidad JPA de género a un modelo de dominio de género.
     * @param entity la entidad JPA de género
     * @return el modelo de dominio de género
     */
    Gender toDomain(GenderEntity entity);

    /**
     * Convierte un modelo de dominio de género a una entidad JPA de género.
     * @param domain el modelo de dominio de género
     * @return la entidad JPA de género
     */
    GenderEntity toEntity(Gender domain);
}

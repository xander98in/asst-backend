package com.unicuaca.asst.unicauca_asst.core.auth.infrastructure.adapters.output.persistence.mappers;

import org.mapstruct.Mapper;

import com.unicuaca.asst.unicauca_asst.core.auth.domain.models.Role;
import com.unicuaca.asst.unicauca_asst.core.auth.infrastructure.adapters.output.persistence.jpa.entities.RoleEntity;

/**
 * Mapper MapStruct entre:
 * - {@link RoleEntity} (JPA/infraestructura)
 * - {@link Role} (dominio)
 */
@Mapper(componentModel = "spring")
public interface RolePersistenceMapper {

    /**
     * Convierte una entidad de JPA a un modelo de dominio.
     *
     * @param entity la entidad de JPA
     * @return el modelo de dominio correspondiente
     */
    Role toDomain(RoleEntity entity);

    /**
     * Convierte un modelo de dominio a una entidad de JPA.
     *
     * @param domain el modelo de dominio
     * @return la entidad de JPA correspondiente
     */
    RoleEntity toEntity(Role domain);
}

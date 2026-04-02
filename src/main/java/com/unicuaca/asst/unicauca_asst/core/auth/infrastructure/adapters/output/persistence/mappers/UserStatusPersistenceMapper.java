package com.unicuaca.asst.unicauca_asst.core.auth.infrastructure.adapters.output.persistence.mappers;

import org.mapstruct.Mapper;

import com.unicuaca.asst.unicauca_asst.core.auth.domain.models.UserStatus;
import com.unicuaca.asst.unicauca_asst.core.auth.infrastructure.adapters.output.persistence.jpa.entities.UserStatusEntity;

/**
 * Mapper MapStruct entre:
 * - {@link UserStatusEntity} (JPA/infraestructura)
 * - {@link UserStatus} (dominio)
 */
@Mapper(componentModel = "spring")
public interface UserStatusPersistenceMapper {

    /**
     * Convierte una entidad de JPA a un modelo de dominio.
     *
     * @param entity la entidad de JPA
     * @return el modelo de dominio correspondiente
     */
    UserStatus toDomain(UserStatusEntity entity);

    /**
     * Convierte un modelo de dominio a una entidad de JPA.
     *
     * @param domain el modelo de dominio
     * @return la entidad de JPA correspondiente
     */
    UserStatusEntity toEntity(UserStatus domain);
}

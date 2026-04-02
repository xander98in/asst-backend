package com.unicuaca.asst.unicauca_asst.core.auth.infrastructure.adapters.output.persistence.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.unicuaca.asst.unicauca_asst.core.auth.domain.models.SystemUser;
import com.unicuaca.asst.unicauca_asst.core.auth.infrastructure.adapters.output.persistence.jpa.entities.SystemUserEntity;

/**
 * Mapper MapStruct entre:
 * - {@link SystemUserEntity} (JPA/infraestructura)
 * - {@link SystemUser} (dominio)
 *
 * <p>Utiliza submappers para status ({@link UserStatusPersistenceMapper})
 * y roles ({@link RolePersistenceMapper}). La relación personEvaluated
 * se mapea como ID en el dominio y se gestiona manualmente en el repositorio.</p>
 */
@Mapper(
    componentModel = "spring",
    uses = {
        RolePersistenceMapper.class,
        UserStatusPersistenceMapper.class
    },
    builder = @org.mapstruct.Builder(disableBuilder = true)
)
public interface SystemUserPersistenceMapper {

    /**
     * Convierte una entidad JPA en un modelo de dominio.
     * El campo personEvaluated (entidad) se mapea a personEvaluatedId (Long).
     *
     * @param entity entidad de base de datos
     * @return modelo de dominio
     */
    @Mapping(target = "personEvaluatedId", source = "personEvaluated.id")
    SystemUser toDomain(SystemUserEntity entity);

    /**
     * Convierte un modelo de dominio en una entidad JPA.
     * El campo personEvaluated se ignora y se gestiona manualmente en el repositorio.
     *
     * @param domain modelo de dominio
     * @return entidad de base de datos
     */
    @Mapping(target = "personEvaluated", ignore = true)
    SystemUserEntity toEntity(SystemUser domain);
}

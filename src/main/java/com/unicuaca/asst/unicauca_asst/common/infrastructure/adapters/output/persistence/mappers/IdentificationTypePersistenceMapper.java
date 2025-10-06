package com.unicuaca.asst.unicauca_asst.common.infrastructure.adapters.output.persistence.mappers;

import com.unicuaca.asst.unicauca_asst.common.domain.models.IdentificationType;
import com.unicuaca.asst.unicauca_asst.common.infrastructure.adapters.output.persistence.jpa.entities.IdentificationTypeEntity;
import org.mapstruct.Mapper;

/**
 * Mapper MapStruct entre:
 * - IdentificationTypeEntity (JPA/infraestructura)
 * - IdentificationType (dominio)
 */
@Mapper(componentModel = "spring")
public interface IdentificationTypePersistenceMapper {

    /**
     * Convierte una entidad JPA en el modelo del dominio.
     * Mapea un objeto {@link IdentificationTypeEntity} a {@link IdentificationType}.
     *
     * @param entity Entidad de tipo de identificación a mapear.
     * @return Objeto de tipo de identificación mapeado.
     */
    IdentificationType toDomain(IdentificationTypeEntity entity);

    /**
     * Convierte un modelo del dominio en su entidad JPA equivalente.
     * Mapea un objeto {@link IdentificationType} a {@link IdentificationTypeEntity}.
     *
     * @param domain Objeto de tipo de identificación a mapear.
     * @return Entidad de tipo de identificación mapeada.
     */
    IdentificationTypeEntity toEntity(IdentificationType domain);
}

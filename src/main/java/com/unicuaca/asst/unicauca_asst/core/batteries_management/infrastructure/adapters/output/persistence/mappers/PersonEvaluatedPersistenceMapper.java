package com.unicuaca.asst.unicauca_asst.core.batteries_management.infrastructure.adapters.output.persistence.mappers;

import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.models.PersonEvaluated;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.infrastructure.adapters.output.persistence.jpa.entities.PersonEvaluatedEntity;

/**
 * Mapper para convertir entre entidades JPA y modelos del dominio relacionados con personas evaluadas.
 * Utiliza MapStruct para generar el código automáticamente.
 */
@Mapper(componentModel = "spring")
public interface PersonEvaluatedPersistenceMapper {

    /**
     * Convierte una entidad JPA en un modelo del dominio.
     *
     * @param entity entidad de base de datos
     * @return modelo de dominio
     */
    @Mappings({
        @Mapping(target = "email", source = "email"),
        @Mapping(target = "gender.id", source = "gender.id"),
        @Mapping(target = "gender.name", source = "gender.name"),
        @Mapping(target = "identificationType.id", source = "identificationType.id"),
        @Mapping(target = "identificationType.name", source = "identificationType.name")
    })
    PersonEvaluated toDomain(PersonEvaluatedEntity entity);

    /**
     * Convierte un modelo del dominio en una entidad JPA.
     *
     * @param domain modelo del dominio
     * @return entidad persistente
     */
    @InheritInverseConfiguration
    PersonEvaluatedEntity toEntity(PersonEvaluated domain);
}

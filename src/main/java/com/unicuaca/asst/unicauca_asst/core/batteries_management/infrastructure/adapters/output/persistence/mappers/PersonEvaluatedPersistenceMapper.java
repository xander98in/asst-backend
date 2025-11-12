package com.unicuaca.asst.unicauca_asst.core.batteries_management.infrastructure.adapters.output.persistence.mappers;

import com.unicuaca.asst.unicauca_asst.common.infrastructure.adapters.output.persistence.mappers.IdentificationTypePersistenceMapper;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.models.PersonEvaluated;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.infrastructure.adapters.output.persistence.jpa.entities.PersonEvaluatedEntity;

/**
 * Mapper para convertir entre entidades JPA y modelos del dominio relacionados con personas evaluadas.
 * Utiliza MapStruct para generar el código automáticamente.
 */
@Mapper(
    componentModel = "spring",
    uses = {
        IdentificationTypePersistenceMapper.class,
        StatusPersonEvaluatedPersistenceMapper.class
    }
)
public interface PersonEvaluatedPersistenceMapper {

    /**
     * Convierte una entidad JPA en un modelo del dominio.
     *
     * @param entity entidad de base de datos
     * @return modelo de dominio
     */
    @Mapping(target = "id", source = "id")
    @Mapping(target = "identificationType", source = "identificationType")
    @Mapping(target = "identificationNumber", source = "identificationNumber")
    @Mapping(target = "firstName", source = "firstName")
    @Mapping(target = "lastName", source = "lastName")
    @Mapping(target = "birthYear", source = "birthYear")
    @Mapping(target = "email", source = "email")
    @Mapping(target = "status", source = "status")
    PersonEvaluated toDomain(PersonEvaluatedEntity entity);

    /**
     * Convierte un modelo del dominio en una entidad JPA.
     *
     * @param domain modelo de dominio
     * @return entidad de base de datos
     */
    @InheritInverseConfiguration
    PersonEvaluatedEntity toEntity(PersonEvaluated domain);
}

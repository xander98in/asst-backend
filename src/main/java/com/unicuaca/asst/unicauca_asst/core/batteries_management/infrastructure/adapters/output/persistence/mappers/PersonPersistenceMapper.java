package com.unicuaca.asst.unicauca_asst.core.batteries_management.infrastructure.adapters.output.persistence.mappers;

import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.models.Person;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.infrastructure.adapters.output.persistence.jpa.entities.PersonEntity;

/**
 * Mapper para convertir entre entidades JPA y modelos del dominio relacionados con personas.
 * Utiliza MapStruct para generar el código automáticamente.
 */
@Mapper(componentModel = "spring")
public interface PersonPersistenceMapper {

    /**
     * Convierte una instancia de {@link PersonEntity} en un objeto de dominio {@link Person}.
     *
     * Se mapean explícitamente los campos anidados `gender` e `identificationType`, extrayendo sus
     * propiedades `id` y `description` para construir los objetos del modelo de dominio.
     *
     * @param entity entidad persistente que representa una persona
     * @return instancia del modelo de dominio {@link Person}
     */
    @Mappings({
        @Mapping(target = "gender.id", source = "gender.id"),
        @Mapping(target = "gender.description", source = "gender.description"),
        @Mapping(target = "identificationType.id", source = "identificationType.id"),
        @Mapping(target = "identificationType.description", source = "identificationType.description")
    })
    Person toDomain(PersonEntity entity);

    /**
     * Convierte una instancia del modelo de dominio {@link Person} en una entidad {@link PersonEntity}.
     *
     * Utiliza la configuración inversa del método {@code toDomain} para mapear los mismos campos.
     *
     * @param domain instancia del modelo de dominio
     * @return entidad JPA lista para persistencia
     */
    @InheritInverseConfiguration
    PersonEntity toEntity(Person domain);
}

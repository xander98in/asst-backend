package com.unicuaca.asst.unicauca_asst.core.batteries_management.application.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import com.unicuaca.asst.unicauca_asst.core.batteries_management.application.dto.request.PersonCreateRequestDTO;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.application.dto.response.PersonResponseDTO;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.models.Person;

/**
 * Mapper que convierte un objeto del modelo de dominio {@link Person}
 * a un DTO de respuesta {@link PersonResponseDTO}.
 *
 * Utiliza MapStruct para mapear automáticamente los campos, incluyendo
 * atributos anidados como identificación y género.
 */
@Mapper(componentModel = "spring")
public interface PersonMapper {

    /**
     * Convierte una entidad de dominio {@link Person} en su representación
     * para la capa de aplicación {@link PersonResponseDTO}.
     *
     * @param person objeto del dominio a convertir
     * @return DTO con los datos listos para respuesta
     */
    @Mapping(target = "identificationType", source = "identificationType.description")
    @Mapping(target = "gender", source = "gender.description")
    PersonResponseDTO toResponseDTO(Person person);

    /**
     * Convierte un DTO de creación de persona {@link PersonCreateRequestDTO} en un modelo del dominio {@link Person}.
     *
     * @param dto DTO recibido desde la capa de entrada HTTP
     * @return modelo de dominio {@link Person} listo para usar en la lógica de negocio
     */
    @Mappings({
        @Mapping(target = "id", ignore = true),
        @Mapping(target = "identificationType", expression = "java(new IdentificationType(dto.getIdentificationTypeId(), null))"),
        @Mapping(target = "gender", expression = "java(new Gender(dto.getGenderId(), null))")
    })
    Person toDomain(PersonCreateRequestDTO dto);

}

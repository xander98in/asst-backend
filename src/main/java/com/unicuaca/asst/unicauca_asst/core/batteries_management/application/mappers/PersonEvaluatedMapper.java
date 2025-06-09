package com.unicuaca.asst.unicauca_asst.core.batteries_management.application.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import com.unicuaca.asst.unicauca_asst.core.batteries_management.application.dto.request.PersonEvaluatedCreateRequestDTO;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.application.dto.request.PersonEvaluatedUpdateRequestDTO;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.application.dto.response.PersonEvaluatedResponseDTO;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.models.PersonEvaluated;

/**
 * Mapper que convierte un objeto del modelo de dominio {@link PersonEvaluated}
 * a un DTO de respuesta {@link PersonEvaluatedResponseDTO}.
 *
 * Utiliza MapStruct para mapear automáticamente los campos, incluyendo
 * atributos anidados como identificación y género.
 */
@Mapper(componentModel = "spring")
public interface PersonEvaluatedMapper {

    /**
     * Convierte un modelo de dominio {@link PersonEvaluated} en un DTO de respuesta {@link PersonEvaluatedResponseDTO}.
     *
     * Mapea los campos anidados 'identificationType' y 'gender' como sus descripciones.
     */
    @Mappings({
        @Mapping(target = "identificationType", source = "identificationType.description"),
        @Mapping(target = "gender", source = "gender.description"),
        @Mapping(target = "email", source = "email")
    })
    PersonEvaluatedResponseDTO toResponseDTO(PersonEvaluated person);

    /**
     * Convierte un DTO de creación en un modelo de dominio {@link PersonEvaluated}.
     *
     * Construye objetos anidados con solo sus IDs.
     */
    @Mappings({
        @Mapping(target = "id", ignore = true),
        @Mapping(target = "email", source = "email"),
        @Mapping(target = "identificationType", expression = "java(new IdentificationType(dto.getIdentificationTypeId(), null))"),
        @Mapping(target = "gender", expression = "java(new Gender(dto.getGenderId(), null))")
    })
    PersonEvaluated toDomain(PersonEvaluatedCreateRequestDTO dto);

    /**
     * Convierte un DTO de actualización en un modelo de dominio con ID.
     *
     * @param id identificador de la persona a actualizar
     * @param dto objeto con los datos nuevos
     * @return modelo de dominio completo
     */
    @Mappings({
        @Mapping(target = "id", source = "id"),
        @Mapping(target = "identificationType", ignore = true), // no se modifica
        @Mapping(target = "identificationNumber", ignore = true), // no se modifica
        @Mapping(target = "gender", expression = "java(new Gender(dto.getGenderId(), null))")
    })
    PersonEvaluated toDomain(Long id, PersonEvaluatedUpdateRequestDTO dto);
}

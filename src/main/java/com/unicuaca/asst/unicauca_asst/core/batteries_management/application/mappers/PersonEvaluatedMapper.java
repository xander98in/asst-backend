package com.unicuaca.asst.unicauca_asst.core.batteries_management.application.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.unicuaca.asst.unicauca_asst.core.batteries_management.application.dto.request.PersonEvaluatedCreateRequestDTO;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.application.dto.request.PersonEvaluatedUpdateRequestDTO;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.application.dto.response.PersonEvaluatedInformationResponseDTO;
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
   
    @Mapping(target = "identificationType", source = "identificationType.name")
    @Mapping(target = "email", source = "email")
    @Mapping(target = "identificacionAbbreviation", source = "identificationType.abbreviation")
    PersonEvaluatedResponseDTO toResponseDTO(PersonEvaluated person);

    /**
     * Convierte un DTO de creación en un modelo de dominio {@link PersonEvaluated}.
     *
     * Construye objetos anidados con solo sus IDs.
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "email", source = "email")
    @Mapping(target = "identificationType", expression = "java(new IdentificationType(null, null, dto.getIdentificationType()))")
    @Mapping(target = "status", expression = "java(null)")
    PersonEvaluated toDomain(PersonEvaluatedCreateRequestDTO dto);

    /**
     * Convierte un DTO de actualización en un modelo de dominio con ID.
     *
     * @param id  identificador de la persona a actualizar
     * @param dto objeto con los datos nuevos
     * @return modelo de dominio completo
     */
    @Mapping(target = "id", source = "id")
    @Mapping(target = "identificationType", expression = "java(new IdentificationType(null, null, dto.getIdentificationType()))")
    @Mapping(target = "identificationNumber", source = "dto.identificationNumber")
    @Mapping(target = "firstName", source = "dto.firstName")
    @Mapping(target = "lastName", source = "dto.lastName")
    @Mapping(target = "birthYear", source = "dto.birthYear")
    @Mapping(target = "email", source = "dto.email")
    @Mapping(target = "status", ignore = true)
    PersonEvaluated toDomain(Long id, PersonEvaluatedUpdateRequestDTO dto);

    /**
     * Convierte un modelo de dominio {@link PersonEvaluated} en un DTO de respuesta para listas {@link PersonEvaluatedInformationResponseDTO}.
     * 
     * @param personEvaluated el modelo de dominio a convertir
     * @return el DTO de respuesta correspondiente
     */
    @Mapping(target = "id", source = "id")
    @Mapping(target = "identificationType", source = "identificationType.name")
    @Mapping(target = "identificacionAbbreviation", source = "identificationType.abbreviation")
    @Mapping(target = "identificationNumber", source = "identificationNumber")
    @Mapping(target = "firstName", source = "firstName")
    @Mapping(target = "lastName", source = "lastName")
    @Mapping(target = "birthYear", source = "birthYear")
    @Mapping(target = "status", source = "status.name")
    PersonEvaluatedInformationResponseDTO toInformationListResponseDTO(PersonEvaluated personEvaluated);
}

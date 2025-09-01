package com.unicuaca.asst.unicauca_asst.common.application.mappers;

import org.mapstruct.Mapper;

import com.unicuaca.asst.unicauca_asst.common.application.dto.response.IdentificationTypeResponseDTO;
import com.unicuaca.asst.unicauca_asst.common.domain.models.IdentificationType;

/**
 * Mapper para convertir entre objetos de dominio y DTOs.
 */
@Mapper(componentModel = "spring")
public interface CatalogMapper {

    /**
     * Convierte un objeto IdentificationType en un objeto IdentificationTypeResponseDTO.
     *
     * @param identificationType el objeto IdentificationType a convertir
     * @return el objeto IdentificationTypeResponseDTO resultante
     */
    IdentificationTypeResponseDTO toIdentificationTypeResponseDTO(IdentificationType identificationType);

}

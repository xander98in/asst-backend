package com.unicuaca.asst.unicauca_asst.common.infrastructure.adapters.output.persistence.mappers;

import org.mapstruct.Mapper;

import com.unicuaca.asst.unicauca_asst.common.domain.models.IdentificationType;
import com.unicuaca.asst.unicauca_asst.common.infrastructure.adapters.output.persistence.jpa.entities.IdentificationTypeEntity;

@Mapper(componentModel = "spring")
public interface CatalogPersistenceMapper {

    /**
     * Convierte un objeto IdentificationType en un objeto IdentificationTypeEntity.
     *
     * @param identificationType el objeto IdentificationType a convertir
     * @return el objeto IdentificationTypeEntity resultante
     */
    IdentificationTypeEntity toIdentificationTypeEntity(IdentificationType identificationType);

    /**
     * Convierte un objeto IdentificationTypeEntity en un objeto IdentificationType.
     *
     * @param identificationTypeEntity el objeto IdentificationTypeEntity a convertir
     * @return el objeto IdentificationType resultante
     */
    IdentificationType toIdentificationTypeDomain(IdentificationTypeEntity identificationTypeEntity);

}

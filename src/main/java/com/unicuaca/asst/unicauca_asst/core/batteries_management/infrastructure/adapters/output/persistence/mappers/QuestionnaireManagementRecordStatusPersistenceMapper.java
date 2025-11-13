package com.unicuaca.asst.unicauca_asst.core.batteries_management.infrastructure.adapters.output.persistence.mappers;

import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.models.QuestionnaireManagementRecordStatus;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.infrastructure.adapters.output.persistence.jpa.entities.QuestionnaireManagementRecordStatusEntity;

@Mapper(
    componentModel = "spring",
    builder = @Builder(disableBuilder = true)
)
public interface QuestionnaireManagementRecordStatusPersistenceMapper {

     /**
     * Convierte una entidad JPA a un modelo de dominio.
     *
     * @param entity la entidad JPA
     * @return el modelo de dominio
     */
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    QuestionnaireManagementRecordStatus toDomain(QuestionnaireManagementRecordStatusEntity entity);

    /**
     * Convierte un modelo de dominio a una entidad JPA.
     *
     * @param domain el modelo de dominio
     * @return la entidad JPA
     */
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    QuestionnaireManagementRecordStatusEntity toEntity(QuestionnaireManagementRecordStatus domain);

}

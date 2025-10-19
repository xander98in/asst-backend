package com.unicuaca.asst.unicauca_asst.core.batteries_management.infrastructure.adapters.output.persistence.mappers;

import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.models.Questionnaire;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.infrastructure.adapters.output.persistence.jpa.entities.QuestionnaireEntity;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper para convertir entre entidades JPA y modelos del dominio relacionados con cuestionarios.
 * Utiliza MapStruct para generar el código automáticamente.
 */
@Mapper(componentModel = "spring")
public interface QuestionnairePersistenceMapper {

    /**
     * Convierte una entidad JPA en un modelo del dominio.
     *
     * @param entity entidad de base de datos
     * @return modelo de dominio
     */
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    @Mapping(target = "abbreviation", source = "abbreviation")
    @Mapping(target = "description", source = "description")
    Questionnaire toDomain(QuestionnaireEntity entity);

    /**
     * Convierte un modelo del dominio en una entidad JPA.
     *
     * @param domain modelo de dominio
     * @return entidad de base de datos
     */
    @InheritInverseConfiguration
    QuestionnaireEntity toEntity(Questionnaire domain);
}

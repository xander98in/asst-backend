package com.unicuaca.asst.unicauca_asst.core.batteries_management.infrastructure.adapters.output.persistence.mappers;

import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.models.Question;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.infrastructure.adapters.output.persistence.jpa.entities.QuestionEntity;

@Mapper(
    componentModel = "spring",
    uses = {
        QuestionnairePersistenceMapper.class
    },
    builder = @Builder(disableBuilder = true)
)
public interface QuestionPersistenceMapper {

    /**
     * Convierte una entidad JPA a un modelo de dominio.
     *
     * @param entity la entidad JPA
     * @return el modelo de dominio
     */
    @Mapping(target = "id", source = "id")
    @Mapping(target = "text", source = "text")
    @Mapping(target = "order", source = "order")
    @Mapping(target = "questionnaire", source = "questionnaire")
    Question toDomain(QuestionEntity entity);

    /**
     * Convierte un modelo de dominio a una entidad JPA.
     *
     * @param domain el modelo de dominio
     * @return la entidad JPA
     */
    @Mapping(target = "id", source = "id")
    @Mapping(target = "text", source = "text")
    @Mapping(target = "order", source = "order")
    @Mapping(target = "questionnaire", source = "questionnaire")
    QuestionEntity toEntity(Question domain);

}

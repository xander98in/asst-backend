package com.unicuaca.asst.unicauca_asst.core.batteries_management.infrastructure.adapters.output.persistence.mappers;

import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.models.QuestionnaireResponse;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.infrastructure.adapters.output.persistence.jpa.entities.QuestionnaireResponseEntity;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

/**
 * Mapper de MapStruct para convertir entre {@link QuestionnaireResponse} y {@link QuestionnaireResponseEntity}.
 *
 * Se utiliza en los adaptadores de infraestructura para persistir y recuperar datos desde la base de datos.
 */
@Mapper(
    componentModel = "spring",
    uses = {
        QuestionnaireManagementRecordPersistenceMapper.class,
        QuestionPersistenceMapper.class,
        AnswerOptionPersistenceMapper.class
    },
    builder = @Builder(disableBuilder = true)
)
public interface QuestionnaireResponsePersistenceMapper {

    /**
     * Convierte una entidad JPA a un modelo de dominio.
     *
     * @param entity la entidad JPA.
     * @return el modelo de dominio.
     */
    @Mapping(target = "id", source = "id")
    @Mapping(target = "questionnaireManagementRecord", source = "questionnaireManagementRecord")
    @Mapping(target = "question", source = "question")
    @Mapping(target = "answerOption", source = "answerOption")
    @Mapping(target = "createdAt", source = "createdAt")
    @Mapping(target = "updatedAt", source = "updatedAt")
    QuestionnaireResponse toDomain(QuestionnaireResponseEntity entity);

    /**
     * Convierte un modelo de dominio a una entidad JPA.
     *
     * @param domain el modelo de dominio.
     * @return la entidad JPA.
     */
    @Mapping(target = "id", source = "id")
    @Mapping(target = "questionnaireManagementRecord", source = "questionnaireManagementRecord")
    @Mapping(target = "question", source = "question")
    @Mapping(target = "answerOption", source = "answerOption")
    // Los campos de auditoría usualmente se ignoran al persistir desde dominio para que el @PrePersist/@PreUpdate de JPA actúe
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    QuestionnaireResponseEntity toEntity(QuestionnaireResponse domain);

    /**
     * Convierte una lista de modelos de dominio a una lista de entidades JPA.
     * MapStruct utilizará el método toEntity definido anteriormente para cada elemento.
     *
     * @param domainList Lista de modelos de dominio.
     * @return Lista de entidades JPA.
     */
    List<QuestionnaireResponseEntity> toEntityList(List<QuestionnaireResponse> domainList);
}

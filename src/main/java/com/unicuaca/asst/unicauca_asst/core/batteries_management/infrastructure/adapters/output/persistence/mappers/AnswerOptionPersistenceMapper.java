package com.unicuaca.asst.unicauca_asst.core.batteries_management.infrastructure.adapters.output.persistence.mappers;

import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.models.AnswerOption;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.infrastructure.adapters.output.persistence.jpa.entities.AnswerOptionEntity;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper para convertir entre entidades JPA y modelos del dominio relacionados con opciones de respuesta.
 * Utiliza MapStruct para generar el código automáticamente.
 */
@Mapper(
    componentModel = "spring",
    builder = @Builder(disableBuilder = true)
)
public interface AnswerOptionPersistenceMapper {

    /**
     * Convierte una entidad JPA a un modelo de dominio.
     *
     * @param entity la entidad JPA.
     * @return el modelo de dominio.
     */
    @Mapping(target = "id", source = "id")
    @Mapping(target = "text", source = "text")
    @Mapping(target = "value", source = "value")
    @Mapping(target = "order", source = "order")
    AnswerOption toDomain(AnswerOptionEntity entity);

    /**
     * Convierte un modelo de dominio a una entidad JPA.
     *
     * @param domain el modelo de dominio.
     * @return la entidad JPA.
     */
    @Mapping(target = "id", source = "id")
    @Mapping(target = "text", source = "text")
    @Mapping(target = "value", source = "value")
    @Mapping(target = "order", source = "order")
    AnswerOptionEntity toEntity(AnswerOption domain);
}

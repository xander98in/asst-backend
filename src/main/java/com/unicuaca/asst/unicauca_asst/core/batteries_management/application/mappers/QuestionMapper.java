package com.unicuaca.asst.unicauca_asst.core.batteries_management.application.mappers;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.unicuaca.asst.unicauca_asst.core.batteries_management.application.dto.response.QuestionResponseDTO;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.application.dto.response.QuestionWithQuestionnaireResponseDTO;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.models.Question;

/**
 * Mapper de la capa de aplicación para convertir modelos de dominio de preguntas
 * a DTOs de respuesta.
 */
@Mapper(componentModel = "spring")
public interface QuestionMapper {

    /**
     * Convierte un modelo de dominio {@link Question} en un DTO de respuesta
     * sin información del cuestionario asociado.
     * 
     * @param question el modelo de dominio a convertir
     * @return el DTO de respuesta correspondiente
     */
    @Mapping(target = "id", source = "id")
    @Mapping(target = "text", source = "text")
    @Mapping(target = "order", source = "order")
    QuestionResponseDTO toQuestionResponseDTO(Question question);

    /**
     * Convierte una lista de modelos de dominio {@link Question} en una lista
     * de DTOs de respuesta sin información del cuestionario.
     * 
     * @param questions la lista de modelos de dominio a convertir
     * @return la lista de DTOs de respuesta correspondientes
     */
    List<QuestionResponseDTO> toQuestionResponseDTOList(List<Question> questions);

    /**
     * Convierte un modelo de dominio {@link Question} en un DTO de respuesta
     * con información del cuestionario asociado.
     * 
     * @param question el modelo de dominio a convertir
     * @return el DTO de respuesta correspondiente
     */
    @Mapping(target = "id", source = "id")
    @Mapping(target = "text", source = "text")
    @Mapping(target = "order", source = "order")
    @Mapping(target = "questionnaireId", source = "questionnaire.id")
    @Mapping(target = "questionnaireName", source = "questionnaire.name")
    @Mapping(target = "questionnaireAbbreviation", source = "questionnaire.abbreviation")
    @Mapping(target = "questionnaireDescription", source = "questionnaire.description")
    QuestionWithQuestionnaireResponseDTO toQuestionWithQuestionnaireResponseDTO(Question question);

    /**
     * Convierte una lista de modelos de dominio {@link Question} en una lista
     * de DTOs de respuesta con información del cuestionario asociado.
     * 
     * @param questions la lista de modelos de dominio a convertir
     * @return la lista de DTOs de respuesta correspondientes
     */
    List<QuestionWithQuestionnaireResponseDTO> toQuestionWithQuestionnaireResponseDTOList(List<Question> questions);
}

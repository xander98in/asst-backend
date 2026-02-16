package com.unicuaca.asst.unicauca_asst.core.batteries_management.application.mappers;

import com.unicuaca.asst.unicauca_asst.core.batteries_management.application.dto.request.QuestionnaireAnswerRequestDTO;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.application.dto.request.QuestionnaireResponseBatchCreateRequestDTO;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.models.AnswerOption;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.models.Question;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.models.QuestionnaireManagementRecord;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.models.QuestionnaireResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface QuestionnaireResponseMapper {

    /**
     * Convierte el DTO batch (padre) en una lista de modelos de dominio.
     * <p>
     * Como MapStruct no hace "desaplanado" automático de un objeto padre a una lista hija inyectando campos del padre,
     * implementamos este método default para orquestar la conversión.
     *
     * @param dto DTO con el ID del registro y la lista de respuestas.
     * @return Lista de modelos de dominio listos para ser procesados.
     */
    default List<QuestionnaireResponse> toDomainList(QuestionnaireResponseBatchCreateRequestDTO dto) {
        if (dto == null || dto.getAnswers() == null) {
            return List.of();
        }

        // Iteramos sobre cada respuesta "hija" y la convertimos,
        // pasando el ID del "padre" como contexto.
        return dto.getAnswers().stream()
            .map(answerDto -> toDomain(answerDto, dto.getQuestionnaireManagementRecordId()))
            .toList();
    }

    /**
     * Convierte una respuesta individual (DTO hijo) en un modelo de dominio.
     * * @param answerDto DTO con questionId y value.
     * @param recordId ID del registro de gestión (viene del DTO padre).
     * @return Objeto de dominio completo.
     */
    @Mapping(target = "id", ignore = true) // Es nuevo, no tiene ID aún
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    // 1. Mapeo del Registro de Gestión (Solo ID)
    @Mapping(target = "questionnaireManagementRecord", expression = "java(buildRecord(recordId))")
    // 2. Mapeo de la Pregunta (Solo ID)
    @Mapping(target = "question", expression = "java(buildQuestion(answerDto.getQuestionId()))")
    // 3. Mapeo de la Opción de Respuesta (Solo Value)
    @Mapping(target = "answerOption", expression = "java(buildAnswerOption(answerDto.getValue()))")
    QuestionnaireResponse toDomain(QuestionnaireAnswerRequestDTO answerDto, Long recordId);

    // --- Métodos Helper para construir los objetos anidados ---

    default QuestionnaireManagementRecord buildRecord(Long id) {
        if (id == null) return null;
        return QuestionnaireManagementRecord.builder().id(id).build();
    }

    default Question buildQuestion(Long id) {
        if (id == null) return null;
        return Question.builder().id(id).build();
    }

    default AnswerOption buildAnswerOption(Integer value) {
        if (value == null) return null;
        // Nota: Mapeamos al campo 'value', los demás (id, text, order) quedan nulos
        return AnswerOption.builder().value(value).build();
    }


}

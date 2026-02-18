package com.unicuaca.asst.unicauca_asst.core.batteries_management.application.mappers;

import com.unicuaca.asst.unicauca_asst.core.batteries_management.application.dto.request.QuestionnaireAnswerCreateRequestDTO;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.application.dto.request.QuestionnaireAnswerUpdateRequestDTO;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.application.dto.request.QuestionnaireResponseBatchCreateRequestDTO;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.application.dto.request.QuestionnaireResponseBatchUpdateRequestDTO;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.application.dto.response.QuestionnaireAnswerResponseDTO;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.application.dto.response.QuestionnaireResponseBatchResponseDTO;
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
        // Iteramos sobre cada respuesta "hija" y la convertimos, pasando el ID del "padre" como contexto.
        return dto.getAnswers().stream()
            .map(answerDto -> toDomain(answerDto, dto.getQuestionnaireManagementRecordId()))
            .toList();
    }

    /**
     * Convierte una respuesta individual (DTO hijo) en un modelo de dominio.
     *
     * @param answerDto DTO con questionId y value.
     * @param recordId ID del registro de gestión (viene del DTO padre).
     * @return Objeto de dominio completo.
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    // 1. Mapeo del Registro de Gestión (Solo ID)
    @Mapping(target = "questionnaireManagementRecord", expression = "java(buildRecord(recordId))")
    // 2. Mapeo de la Pregunta (Solo ID)
    @Mapping(target = "question", expression = "java(buildQuestion(answerDto.getQuestionId()))")
    // 3. Mapeo de la Opción de Respuesta (Solo Value)
    @Mapping(target = "answerOption", expression = "java(buildAnswerOption(answerDto.getValue()))")
    QuestionnaireResponse toDomain(QuestionnaireAnswerCreateRequestDTO answerDto, Long recordId);

    /**
     * Convierte el DTO batch de actualización a una lista de modelos de dominio.
     *
     * @param dto DTO con el ID del registro y la lista de respuestas a actualizar (cada una con su ID).
     * @return Lista de modelos de dominio listos para ser procesados en la actualización.
     */
    default List<QuestionnaireResponse> toDomainListForUpdate(QuestionnaireResponseBatchUpdateRequestDTO dto) {
        if (dto == null || dto.getAnswers() == null) {
            return List.of();
        }
        return dto.getAnswers().stream()
            .map(answerDto -> toDomainForUpdate(answerDto, dto.getQuestionnaireManagementRecordId()))
            .toList();
    }

    /**
     * Mapea un item de actualización al dominio.
     *
     * @param answerDto DTO con ID de la respuesta, questionId y value.
     * @param recordId ID del registro de gestión (viene del DTO padre).
     * @return Objeto de dominio completo con ID para actualización.
     */
    @Mapping(target = "id", source = "answerDto.id") // ID de la respuesta
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "questionnaireManagementRecord", expression = "java(buildRecord(recordId))")
    @Mapping(target = "question", expression = "java(buildQuestion(answerDto.getQuestionId()))")
    @Mapping(target = "answerOption", expression = "java(buildAnswerOption(answerDto.getValue()))")
    QuestionnaireResponse toDomainForUpdate(QuestionnaireAnswerUpdateRequestDTO answerDto, Long recordId);

    /**
     * Convierte una lista de respuestas del dominio en un DTO Batch estructurado.
     * Toma la información del padre del primer elemento de la lista.
     *
     * @param domainList Lista de respuestas del dominio.
     * @return DTO estructurado con cabecera y lista de respuestas.
     */
    default QuestionnaireResponseBatchResponseDTO toBatchResponseDTO(List<QuestionnaireResponse> domainList) {
        if (domainList == null || domainList.isEmpty()) {
            return null;
        }

        QuestionnaireResponse first = domainList.get(0);
        QuestionnaireManagementRecord record = first.getQuestionnaireManagementRecord();

        return QuestionnaireResponseBatchResponseDTO.builder()
            .id(record.getId())
            .batteryManagementRecordId(record.getBatteryManagementRecord().getId())
            .statusId(record.getStatus().getId())
            .statusName(record.getStatus().getName())
            .questionnaireId(record.getQuestionnaire().getId())
            .questionnaireName(record.getQuestionnaire().getName())
            .questionnaireAbbreviation(record.getQuestionnaire().getAbbreviation())
            .questionnaireDescription(record.getQuestionnaire().getDescription())
            .createdAt(record.getCreatedAt())
            .updatedAt(record.getUpdatedAt())
            .answers(toAnswerResponseList(domainList))
            .build();
    }

    /**
     * Convierte una lista de dominios en lista de DTOs hijos.
     */
    List<QuestionnaireAnswerResponseDTO> toAnswerResponseList(List<QuestionnaireResponse> list);

    /**
     * Convierte una respuesta individual de dominio a DTO hijo.
     */
    @Mapping(target = "id", source = "id")
    @Mapping(target = "questionId", source = "question.id")
    @Mapping(target = "questionText", source = "question.text")
    @Mapping(target = "questionOrder", source = "question.order")
    @Mapping(target = "answerOptionId", source = "answerOption.id")
    @Mapping(target = "answerOptionText", source = "answerOption.text")
    @Mapping(target = "answerOptionValue", source = "answerOption.value")
    @Mapping(target = "answerOptionOrder", source = "answerOption.order")
    QuestionnaireAnswerResponseDTO toAnswerResponseDTO(QuestionnaireResponse domain);

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
        return AnswerOption.builder().value(value).build();
    }


}

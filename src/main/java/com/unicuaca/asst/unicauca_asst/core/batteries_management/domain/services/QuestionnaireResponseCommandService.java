package com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.services;

import com.unicuaca.asst.unicauca_asst.common.application.output.ResultFormatterOutputPort;
import com.unicuaca.asst.unicauca_asst.common.exceptions.structure.ErrorCode;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.models.AnswerOption;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.models.Question;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.models.QuestionnaireManagementRecord;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.models.QuestionnaireResponse;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.ports.input.QuestionnaireResponseCommandCUInputPort;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.ports.output.*;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Objects;

/**
 * Servicio de dominio para la gestión de respuestas de cuestionarios.
 * Implementa el puerto de entrada para casos de uso relacionados con las respuestas.
 *
 * Este servicio se encarga de procesar la lógica de negocio relacionada con la gestión
 * de respuestas, incluyendo validaciones, transformaciones y coordinación con
 * otros servicios o repositorios si es necesario.
 */
@RequiredArgsConstructor
public class QuestionnaireResponseCommandService implements QuestionnaireResponseCommandCUInputPort {

    private final QuestionnaireManagementRecordQueryRepository questionnaireManagementRecordQueryRepository;
    private final QuestionQueryRepository questionQueryRepository;
    private final AnswerOptionQueryRepository answerOptionQueryRepository;
    private final QuestionnaireResponseQueryRepository questionnaireResponseQueryRepository;
    private final QuestionnaireResponseCommandRepository questionnaireResponseCommandRepository;
    private final ResultFormatterOutputPort resultFormatter;

    @Override
    public void createQuestionnaireResponseBatch(List<QuestionnaireResponse> responses) {

        // 1. Validar lista vacía
        if (responses == null || responses.isEmpty()) {
            resultFormatter.throwBusinessRuleViolation(
                ErrorCode.EMPTY_LIST_OF_RESPONSES.getCode(),
                ErrorCode.EMPTY_LIST_OF_RESPONSES.getMessageKey()
            );
            return; // Detiene la ejecución (aunque el throw ya debería hacerlo)
        }

        // 2. Obtener ID de referencia y validar consistencia
        Long recordIdRef = responses.get(0).getQuestionnaireManagementRecord().getId();

        // Regla de Negocio: Todos los elementos deben pertenecer al mismo ID de registro
        boolean allSameRecordId = responses.stream()
            .map(r -> r.getQuestionnaireManagementRecord().getId())
            .allMatch(id -> Objects.equals(id, recordIdRef));

        if (!allSameRecordId) {
            resultFormatter.throwBusinessRuleViolation(
                ErrorCode.DIFFERENT_RECORD_IDS_IN_RESPONSES.getCode(),
                ErrorCode.DIFFERENT_RECORD_IDS_IN_RESPONSES.getMessageKey()
            );
        }

        // Regla de Negocio: No debe haber preguntas duplicadas en el batch
        long uniqueQuestionsCount = responses.stream()
            .map(r -> r.getQuestion().getId())
            .distinct()
            .count();

        if (uniqueQuestionsCount != responses.size()) {
            resultFormatter.throwBusinessRuleViolation(
                ErrorCode.DUPLICATE_QUESTION_IN_BATCH.getCode(),
                ErrorCode.DUPLICATE_QUESTION_IN_BATCH.getMessageKey()
            );
        }

        // 3. Buscar el Registro de Gestión de Cuestionario (con relaciones)
        // Si no existe, lanza excepción de Entidad No Encontrada
        QuestionnaireManagementRecord managementRecord = questionnaireManagementRecordQueryRepository.findByIdWithAll(recordIdRef)
            .orElseGet(() -> {
                resultFormatter.throwEntityNotFound(
                    ErrorCode.ENTITY_NOT_FOUND.getCode(),
                    String.format(ErrorCode.ENTITY_NOT_FOUND.getMessageKey(), "El registro de gestión de cuestionario con ID " + recordIdRef + " no existe.")
                );
                return null;
            });

        // 4. Iterar, Validar y Enriquecer cada respuesta
        responses.forEach(response -> {

            // A. Obtener la Pregunta completa (con su cuestionario)
            Long questionId = response.getQuestion().getId();
            Question question = questionQueryRepository.getQuestionByIdWithQuestionnaire(questionId)
                .orElseGet(() -> {
                    resultFormatter.throwEntityNotFound(
                        ErrorCode.ENTITY_NOT_FOUND.getCode(),
                        String.format(ErrorCode.ENTITY_NOT_FOUND.getMessageKey(), "La pregunta con ID " + questionId + " no existe.")
                    );
                    return null;
                });

            // (Opcional pero recomendado) Validar que la pregunta pertenezca al cuestionario del registro
            if (!question.getQuestionnaire().getId().equals(managementRecord.getQuestionnaire().getId())) {
                resultFormatter.throwBusinessRuleViolation(
                    ErrorCode.QUESTION_DOES_NOT_BELONG_TO_QUESTIONNAIRE.getCode(),
                    String.format(ErrorCode.QUESTION_DOES_NOT_BELONG_TO_QUESTIONNAIRE.getMessageKey(), questionId)
                );
            }

            // B. Obtener la Opción de Respuesta por valor
            Integer answerValue = response.getAnswerOption().getValue();
            AnswerOption answerOption = answerOptionQueryRepository.getAnswerOptionByValue(answerValue)
                .orElseGet(() -> {
                    resultFormatter.throwEntityNotFound(
                        ErrorCode.ENTITY_NOT_FOUND.getCode(),
                        String.format(ErrorCode.ENTITY_NOT_FOUND.getMessageKey(), "La opción de respuesta con valor " + answerValue + " no existe.")
                    );
                    return null;
                });

            // C. Verificar Duplicidad (Regla de Negocio)
            boolean alreadyAnswered = questionnaireResponseQueryRepository.existsByRecordIdAndQuestionId(recordIdRef, questionId);

            if (alreadyAnswered) {
                resultFormatter.throwBusinessRuleViolation(
                    ErrorCode.QUESTION_ANSWERED_ALREADY.getCode(), // O usa un código específico como DUPLICATE_RESPONSE
                    String.format(ErrorCode.QUESTION_ANSWERED_ALREADY.getMessageKey(), questionId)
                );
            }

            // D. Setear la información completa en el objeto de dominio (Hydration)
            response.setQuestionnaireManagementRecord(managementRecord);
            response.setQuestion(question);
            response.setAnswerOption(answerOption);
        });

        questionnaireResponseCommandRepository.saveAll(responses);
    }
}

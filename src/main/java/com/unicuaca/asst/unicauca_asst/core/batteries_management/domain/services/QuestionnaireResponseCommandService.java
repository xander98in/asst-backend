package com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.services;

import com.unicuaca.asst.unicauca_asst.common.application.output.ResultFormatterOutputPort;
import com.unicuaca.asst.unicauca_asst.common.exceptions.structure.ErrorCode;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.models.*;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.models.enums.BatteryManagementRecordStatusCode;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.models.enums.QuestionnaireEnum;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.models.enums.QuestionnaireManagementRecordStatusEnum;
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
    private final QuestionnaireManagementRecordStatusQueryRepository questionnaireManagementRecordStatusQueryRepository;
    private final BatteryManagementRecordStatusQueryRepository batteryManagementRecordStatusQueryRepository;
    private final BatteryManagementRecordCommandRepository batteryManagementRecordCommandRepository;
    private final QuestionnaireResponseCommandRepository questionnaireResponseCommandRepository;
    private final QuestionnaireManagementRecordCommandRepository questionnaireManagementRecordCommandRepository;
    private final ResultFormatterOutputPort resultFormatter;

    @Override
    public void createQuestionnaireResponseBatch(List<QuestionnaireResponse> responses) {

        // Validar lista vacía
        if (responses == null || responses.isEmpty()) {
            resultFormatter.throwBusinessRuleViolation(
                ErrorCode.EMPTY_LIST_OF_RESPONSES.getCode(),
                ErrorCode.EMPTY_LIST_OF_RESPONSES.getMessageKey(),
                "No se han proporcionado respuestas para procesar."
            );
            return;
        }

        // Obtener ID de referencia y validar consistencia
        Long recordIdRef = responses.get(0).getQuestionnaireManagementRecord().getId();

        // Regla de Negocio: Todos los elementos deben pertenecer al mismo ID de registro
        boolean allSameRecordId = responses.stream()
            .map(r -> r.getQuestionnaireManagementRecord().getId())
            .allMatch(id -> Objects.equals(id, recordIdRef));

        if (!allSameRecordId) {
            resultFormatter.throwBusinessRuleViolation(
                ErrorCode.DIFFERENT_RECORD_IDS_IN_RESPONSES.getCode(),
                ErrorCode.DIFFERENT_RECORD_IDS_IN_RESPONSES.getMessageKey(),
                "Las respuestas deben pertenecer al mismo registro de gestión."
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
                ErrorCode.DUPLICATE_QUESTION_IN_BATCH.getMessageKey(),
                "No se puede responder la misma pregunta más de una vez en el mismo lote."
            );
        }

        // Buscar el Registro de Gestión de Cuestionario (con relaciones)
        QuestionnaireManagementRecord managementRecord = questionnaireManagementRecordQueryRepository.findByIdWithAll(recordIdRef)
            .orElseGet(() -> {
                resultFormatter.throwEntityNotFound(
                    ErrorCode.ENTITY_NOT_FOUND.getCode(),
                    String.format(ErrorCode.ENTITY_NOT_FOUND.getMessageKey(), "El registro de gestión de cuestionario con ID " + recordIdRef + " no existe."),
                    "El registro de gestión de cuestionario no fue encontrado."
                );
                return null;
            });

        // Iterar, Validar y Enriquecer cada respuesta
        responses.forEach(response -> {

            // Obtener la Pregunta completa (con su cuestionario)
            Long questionId = response.getQuestion().getId();
            Question question = questionQueryRepository.getQuestionByIdWithQuestionnaire(questionId)
                .orElseGet(() -> {
                    resultFormatter.throwEntityNotFound(
                        ErrorCode.ENTITY_NOT_FOUND.getCode(),
                        String.format(ErrorCode.ENTITY_NOT_FOUND.getMessageKey(), "La pregunta con ID " + questionId + " no existe."),
                        "La pregunta indicada no fue encontrada."
                    );
                    return null;
                });

            // (Opcional pero recomendado) Validar que la pregunta pertenezca al cuestionario del registro
            if (!question.getQuestionnaire().getId().equals(managementRecord.getQuestionnaire().getId())) {
                resultFormatter.throwBusinessRuleViolation(
                    ErrorCode.QUESTION_DOES_NOT_BELONG_TO_QUESTIONNAIRE.getCode(),
                    String.format(ErrorCode.QUESTION_DOES_NOT_BELONG_TO_QUESTIONNAIRE.getMessageKey(), questionId),
                    "La pregunta no pertenece al cuestionario del registro actual."
                );
            }

            // Obtener la Opción de Respuesta por valor
            Integer answerValue = response.getAnswerOption().getValue();
            AnswerOption answerOption = answerOptionQueryRepository.getAnswerOptionByValue(answerValue)
                .orElseGet(() -> {
                    resultFormatter.throwEntityNotFound(
                        ErrorCode.ENTITY_NOT_FOUND.getCode(),
                        String.format(ErrorCode.ENTITY_NOT_FOUND.getMessageKey(), "La opción de respuesta con valor " + answerValue + " no existe."),
                        "La opción de respuesta seleccionada no es válida."
                    );
                    return null;
                });

            // Verificar Duplicidad (Regla de Negocio)
            boolean alreadyAnswered = questionnaireResponseQueryRepository.existsByRecordIdAndQuestionId(recordIdRef, questionId);

            if (alreadyAnswered) {
                resultFormatter.throwBusinessRuleViolation(
                    ErrorCode.QUESTION_ANSWERED_ALREADY.getCode(),
                    String.format(ErrorCode.QUESTION_ANSWERED_ALREADY.getMessageKey(), questionId),
                    "La pregunta ya ha sido respondida para este registro."
                );
            }

            // Setear la información completa en el objeto de dominio (Hydration)
            response.setQuestionnaireManagementRecord(managementRecord);
            response.setQuestion(question);
            response.setAnswerOption(answerOption);
        });

        // Persistir todas las respuestas (Batch)
        questionnaireResponseCommandRepository.saveAll(responses);

        // =================================================================================
        // Actualizar el estado del Registro de Gestión a "DILIGENCIADO"
        // =================================================================================

        // Obtener el nombre del estado desde el Enum
        String targetStatusName = QuestionnaireManagementRecordStatusEnum.DILIGENCIADO.getName();

        // Buscar el objeto Estado en la base de datos
        QuestionnaireManagementRecordStatus diligenciadoStatus = questionnaireManagementRecordStatusQueryRepository
            .getQuestionnaireManagementRecordStatusByName(targetStatusName)
            .orElseGet(() -> {
                resultFormatter.throwEntityNotFound(
                    ErrorCode.ENTITY_NOT_FOUND.getCode(),
                    String.format(ErrorCode.ENTITY_NOT_FOUND.getMessageKey(),
                        "El estado '" + targetStatusName + "' no se encuentra configurado en el sistema."),
                    "No fue posible completar la operación. Por favor, intente más tarde."
                );
                return null;
            });

        managementRecord.setStatus(diligenciadoStatus);
        questionnaireManagementRecordCommandRepository.save(managementRecord);

        // =================================================================================
        // Verificar completitud y actualizar estado de la Batería
        // =================================================================================

        Long batteryId = managementRecord.getBatteryManagementRecord().getId();
        String statusDiligenciado = QuestionnaireManagementRecordStatusEnum.DILIGENCIADO.getName();

        // Obtener qué cuestionarios de esta batería ya están "Diligenciados"
        List<String> completedAbbreviations = questionnaireManagementRecordQueryRepository
            .findAbbreviationsByBatteryIdAndStatusName(batteryId, statusDiligenciado);

        // Evaluar Reglas de Negocio
        boolean hasExt = completedAbbreviations.contains(QuestionnaireEnum.EXT.getAbbreviation());
        boolean hasEst = completedAbbreviations.contains(QuestionnaireEnum.EST.getAbbreviation());
        boolean hasIla = completedAbbreviations.contains(QuestionnaireEnum.ILA.getAbbreviation());
        boolean hasIlb = completedAbbreviations.contains(QuestionnaireEnum.ILB.getAbbreviation());

        // Debe tener EXT + EST + (ILA ó ILB). Total 3.
        boolean isBatteryComplete = hasExt && hasEst && (hasIla || hasIlb) && completedAbbreviations.size() == 3;

        // Determinar el nuevo estado de la batería
        String targetBatteryStatusName;
        if (isBatteryComplete) {
            targetBatteryStatusName = BatteryManagementRecordStatusCode.COMPLETED.getDescription(); // "Diligenciado"
        } else {
            targetBatteryStatusName = BatteryManagementRecordStatusCode.IN_PROCESSING.getDescription(); // "En diligenciamiento"
        }

        // Actualizar el estado de la Batería
        BatteryManagementRecord batteryRecord = managementRecord.getBatteryManagementRecord();
        BatteryManagementRecordStatus newStatus = batteryManagementRecordStatusQueryRepository
            .getStatusByName(targetBatteryStatusName)
            .orElseGet(() -> {
                resultFormatter.throwEntityNotFound(
                    ErrorCode.ENTITY_NOT_FOUND.getCode(),
                    String.format(ErrorCode.ENTITY_NOT_FOUND.getMessageKey(),
                        "El estado de batería '" + targetBatteryStatusName + "' no existe."),
                    "No fue posible completar la operación. Por favor, intente más tarde."
                );
                return null;
            });

        batteryRecord.setStatus(newStatus);
        // Usamos el puerto de comando de Batería
        batteryManagementRecordCommandRepository.updateBatteryManagementRecord(batteryRecord);
    }

    /**
     * Actualiza un batch de respuestas de cuestionario existentes asociadas a un mismo registro de
     * gestión de cuestionarios.
     *
     * @param responses Lista de respuestas a actualizar.
     */
    @Override
    public void updateQuestionnaireResponseBatch(List<QuestionnaireResponse> responses) {

        // Validar lista vacía
        if (responses == null || responses.isEmpty()) {
            resultFormatter.throwBusinessRuleViolation(
                ErrorCode.EMPTY_LIST_OF_RESPONSES.getCode(),
                ErrorCode.EMPTY_LIST_OF_RESPONSES.getMessageKey(),
                "No se han proporcionado respuestas para procesar."
            );
            return;
        }

        // Obtener ID de referencia y validar consistencia del Registro de Gestión en el input
        Long recordIdRef = responses.get(0).getQuestionnaireManagementRecord().getId();

        // Regla de Negocio: Todos los elementos deben pertenecer al mismo ID de registro
        boolean allSameRecordId = responses.stream()
            .map(r -> r.getQuestionnaireManagementRecord().getId())
            .allMatch(id -> Objects.equals(id, recordIdRef));

        if (!allSameRecordId) {
            resultFormatter.throwBusinessRuleViolation(
                ErrorCode.DIFFERENT_RECORD_IDS_IN_RESPONSES.getCode(),
                ErrorCode.DIFFERENT_RECORD_IDS_IN_RESPONSES.getMessageKey(),
                "Las respuestas deben pertenecer al mismo registro de gestión."
            );
        }

        // Regla de Negocio: No debe haber preguntas duplicadas en el batch de actualización
        long uniqueQuestionsCount = responses.stream()
            .map(r -> r.getQuestion().getId())
            .distinct()
            .count();

        if (uniqueQuestionsCount != responses.size()) {
            resultFormatter.throwBusinessRuleViolation(
                ErrorCode.DUPLICATE_QUESTION_IN_BATCH.getCode(),
                ErrorCode.DUPLICATE_QUESTION_IN_BATCH.getMessageKey(),
                "No se puede responder la misma pregunta más de una vez en el mismo lote."
            );
        }

        // Buscar y Validar existencia del Registro de Gestión de Cuestionario
        questionnaireManagementRecordQueryRepository.findById(recordIdRef)
            .orElseGet(() -> {
                resultFormatter.throwEntityNotFound(
                    ErrorCode.ENTITY_NOT_FOUND.getCode(),
                    String.format(ErrorCode.ENTITY_NOT_FOUND.getMessageKey(), "El registro de gestión de cuestionario con ID " + recordIdRef + " no existe."),
                    "El registro de gestión de cuestionario no fue encontrado."
                );
                return null;
            });

        // Procesar cada actualización
        responses.forEach(inputResponse -> {

            Long responseId = inputResponse.getId();

            // Obtener la Respuesta Existente de la BD (con todas sus relaciones)
            QuestionnaireResponse existingResponse = questionnaireResponseQueryRepository.getByIdWithAllRelations(responseId)
                .orElseGet(() -> {
                    resultFormatter.throwEntityNotFound(
                        ErrorCode.ENTITY_NOT_FOUND.getCode(),
                        String.format(ErrorCode.ENTITY_NOT_FOUND.getMessageKey(), "La respuesta con ID " + responseId + " no existe."),
                        "La respuesta indicada no fue encontrada."
                    );
                    return null;
                });

            // Verificar que la respuesta que recuperamos de BD realmente pertenece al registro de gestión que estamos procesando.
            if (!existingResponse.getQuestionnaireManagementRecord().getId().equals(recordIdRef)) {
                resultFormatter.throwBusinessRuleViolation(
                    ErrorCode.RESPONSE_BELONGS_TO_OTHER_RECORD.getCode(),
                    String.format(ErrorCode.RESPONSE_BELONGS_TO_OTHER_RECORD.getMessageKey(), responseId, recordIdRef),
                    "La respuesta no pertenece al registro de gestión indicado."
                );
            }

            // Validar consistencia de la pregunta; aseguramos que el front no mandó un ID de respuesta con un ID de pregunta que no cuadra.
            if (!existingResponse.getQuestion().getId().equals(inputResponse.getQuestion().getId())) {
                resultFormatter.throwBusinessRuleViolation(
                    ErrorCode.RESPONSE_QUESTION_MISMATCH.getCode(),
                    String.format(ErrorCode.RESPONSE_QUESTION_MISMATCH.getMessageKey(), responseId),
                    "La pregunta proporcionada no coincide con la almacenada en la respuesta."
                );
            }

            // Obtener la nueva Opción de Respuesta por valor
            Integer newValue = inputResponse.getAnswerOption().getValue();

            // Optimización: Si el valor es el mismo, no hacemos nada (evita queries innecesarios de AnswerOption)
            if (!Objects.equals(existingResponse.getAnswerOption().getValue(), newValue)) {

                AnswerOption newAnswerOption = answerOptionQueryRepository.getAnswerOptionByValue(newValue)
                    .orElseGet(() -> {
                        resultFormatter.throwEntityNotFound(
                            ErrorCode.ENTITY_NOT_FOUND.getCode(),
                            String.format(ErrorCode.ENTITY_NOT_FOUND.getMessageKey(), "La opción de respuesta con valor " + newValue + " no existe."),
                            "La opción de respuesta seleccionada no es válida."
                        );
                        return null;
                    });

                // Actualizar el objeto de dominio existente (Managed Entity)
                existingResponse.setAnswerOption(newAnswerOption);

                // Actualizamos el objeto de entrada con la data real para el saveAll final
                inputResponse.setQuestionnaireManagementRecord(existingResponse.getQuestionnaireManagementRecord());
                inputResponse.setQuestion(existingResponse.getQuestion());
                inputResponse.setAnswerOption(newAnswerOption);
                inputResponse.setCreatedAt(existingResponse.getCreatedAt());
            } else {
                // Si el valor es igual, igual debemos hidratar el objeto para que el saveAll no falle por nulos
                inputResponse.setQuestionnaireManagementRecord(existingResponse.getQuestionnaireManagementRecord());
                inputResponse.setQuestion(existingResponse.getQuestion());
                inputResponse.setAnswerOption(existingResponse.getAnswerOption());
                inputResponse.setCreatedAt(existingResponse.getCreatedAt());
            }
        });

        // Persistir los cambios
        questionnaireResponseCommandRepository.saveAll(responses);
    }
}

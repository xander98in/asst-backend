package com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.services;

import com.unicuaca.asst.unicauca_asst.common.application.output.ResultFormatterOutputPort;
import com.unicuaca.asst.unicauca_asst.common.exceptions.structure.ErrorCode;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.models.AnswerOption;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.models.BatteryManagementRecord;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.models.BatteryManagementRecordStatus;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.models.Question;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.models.QuestionnaireManagementRecord;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.models.QuestionnaireManagementRecordStatus;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.models.QuestionnaireResponse;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.models.enums.BatteryManagementRecordStatusCode;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.models.enums.QuestionnaireEnum;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.models.enums.QuestionnaireManagementRecordStatusEnum;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.ports.input.QuestionnaireResponseCommandCUInputPort;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.ports.output.AnswerOptionQueryRepository;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.ports.output.BatteryManagementRecordCommandRepository;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.ports.output.BatteryManagementRecordStatusQueryRepository;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.ports.output.QuestionQueryRepository;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.ports.output.QuestionnaireManagementRecordCommandRepository;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.ports.output.QuestionnaireManagementRecordQueryRepository;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.ports.output.QuestionnaireManagementRecordStatusQueryRepository;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.ports.output.QuestionnaireResponseCommandRepository;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.ports.output.QuestionnaireResponseQueryRepository;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Servicio de dominio para la gestión de respuestas de cuestionarios en bloque.
 * 
 * <p>Implementa la lógica para crear y actualizar lotes de respuestas, validando la integridad
 * de las preguntas y opciones, y sincronizando el estado de avance de la batería.</p>
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

    /**
     * Procesa un lote de nuevas respuestas para un cuestionario.
     *
     * @param responses lista de respuestas a guardar
     */
    @Override
    public void createQuestionnaireResponseBatch(List<QuestionnaireResponse> responses) {

        // Validar lista vacía
        if (responses == null || responses.isEmpty()) {
            resultFormatter.throwBusinessRuleViolation(
                ErrorCode.EMPTY_LIST_OF_RESPONSES,
                "user.responses.empty_list"
            );
        }

        // Obtener ID de referencia y validar consistencia del lote
        Long recordIdRef = responses.get(0).getQuestionnaireManagementRecord().getId();
        // Regla de Negocio: Todos los elementos deben pertenecer al mismo ID de registro
        boolean allSameRecordId = responses.stream()
            .map(r -> r.getQuestionnaireManagementRecord().getId())
            .allMatch(id -> Objects.equals(id, recordIdRef));

        if (!allSameRecordId) {
            resultFormatter.throwBusinessRuleViolation(
                ErrorCode.DIFFERENT_RECORD_IDS_IN_RESPONSES,
                "user.responses.inconsistent_batch"
            );
        }

        // Regla de Negocio: No debe haber preguntas duplicadas en el batch
        long uniqueQuestionsCount = responses.stream()
            .map(r -> r.getQuestion().getId())
            .distinct()
            .count();

        if (uniqueQuestionsCount != responses.size()) {
            resultFormatter.throwBusinessRuleViolation(
                ErrorCode.DUPLICATE_QUESTION_IN_BATCH,
                "user.responses.duplicate_questions"
            );
        }

        // Recuperar registro de gestión de cuestionario con hidratación completa
        Optional<QuestionnaireManagementRecord> managementRecordOpt = questionnaireManagementRecordQueryRepository
            .findByIdWithAll(recordIdRef);
        if (managementRecordOpt.isEmpty()) {
            resultFormatter.throwEntityNotFound(
                ErrorCode.QUESTIONNAIRE_MGMT_RECORD_NOT_FOUND,
                "user.responses.record_not_found",
                recordIdRef
            );
        }
        QuestionnaireManagementRecord managementRecord = managementRecordOpt.get();

        // Hidratación y validación de cada respuesta individual
        responses.forEach(response -> {

            // Obtener la Pregunta completa (con su cuestionario)
            Long questionId = response.getQuestion().getId();

            Optional<Question> questionOpt = questionQueryRepository.getQuestionByIdWithQuestionnaire(questionId);
            if (questionOpt.isEmpty()) {
                resultFormatter.throwEntityNotFound(
                    ErrorCode.QUESTION_NOT_FOUND,
                    "user.responses.question_invalid",
                    questionId
                );
            }
            Question question = questionOpt.get();

            // Regla de Integridad: La pregunta debe pertenecer al cuestionario actual
            if (!question.getQuestionnaire().getId().equals(managementRecord.getQuestionnaire().getId())) {
                resultFormatter.throwBusinessRuleViolation(
                    ErrorCode.QUESTION_DOES_NOT_BELONG_TO_QUESTIONNAIRE,
                    "user.responses.question_mismatch",
                    questionId, managementRecord.getQuestionnaire().getName()
                );
            }

            Integer answerValue = response.getAnswerOption().getValue();
            Optional<AnswerOption> answerOptionOpt = answerOptionQueryRepository.getAnswerOptionByValue(answerValue);
            if (answerOptionOpt.isEmpty()) {
                resultFormatter.throwEntityNotFound(
                    ErrorCode.RESPONSE_OPTION_NOT_FOUND,
                    "user.responses.option_invalid",
                    answerValue
                );
            }
            AnswerOption answerOption = answerOptionOpt.get();

            // Regla de Negocio: Evitar sobreescritura accidental sin usar el comando de actualización
            if (questionnaireResponseQueryRepository.existsByRecordIdAndQuestionId(recordIdRef, questionId)) {
                resultFormatter.throwBusinessRuleViolation(
                    ErrorCode.QUESTION_ANSWERED_ALREADY,
                    "user.responses.already_answered",
                    questionId, recordIdRef
                );
            }

            response.setQuestionnaireManagementRecord(managementRecord);
            response.setQuestion(question);
            response.setAnswerOption(answerOption);
        });

        // Persistencia y sincronización de estados
        questionnaireResponseCommandRepository.saveAll(responses);
        syncStatusAfterSave(managementRecord);
    }

    /**
     * Actualiza un lote de respuestas previamente registradas.
     *
     * @param responses lista de respuestas con cambios
     */
    @Override
    public void updateQuestionnaireResponseBatch(List<QuestionnaireResponse> responses) {

        // Validar lista vacía
        if (responses == null || responses.isEmpty()) {
            resultFormatter.throwBusinessRuleViolation(
                ErrorCode.EMPTY_LIST_OF_RESPONSES,
                "user.responses.update_empty"
            );
        }

        // Obtener ID de referencia y validar consistencia del lote
        Long recordIdRef = responses.get(0).getQuestionnaireManagementRecord().getId();

        // Validación de consistencia técnica del lote
        if (!responses.stream().allMatch(r -> Objects.equals(r.getQuestionnaireManagementRecord().getId(), recordIdRef))) {
            resultFormatter.throwBusinessRuleViolation(
                ErrorCode.DIFFERENT_RECORD_IDS_IN_RESPONSES,
                "user.responses.update_inconsistent_batch"
            );
        }

        if (responses.stream().map(r -> r.getQuestion().getId()).distinct().count() != responses.size()) {
            resultFormatter.throwBusinessRuleViolation(
                ErrorCode.DUPLICATE_QUESTION_IN_BATCH,
                "user.responses.update_duplicate_questions"
            );
        }

        Optional<QuestionnaireManagementRecord> recordOpt = questionnaireManagementRecordQueryRepository.findById(recordIdRef);
        if (recordOpt.isEmpty()) {
            resultFormatter.throwEntityNotFound(
                ErrorCode.QUESTIONNAIRE_MGMT_RECORD_NOT_FOUND,
                "user.responses.update_record_not_found",
                recordIdRef
            );
        }

        // Procesamiento de cada actualización con validación de seguridad
        responses.forEach(inputResponse -> {
            Long responseId = inputResponse.getId();
            Optional<QuestionnaireResponse> existingOpt = questionnaireResponseQueryRepository.getByIdWithAllRelations(responseId);
            if (existingOpt.isEmpty()) {
                resultFormatter.throwEntityNotFound(
                    ErrorCode.RESPONSE_NOT_FOUND,
                    "user.responses.update_not_found",
                    responseId
                );
            }
            QuestionnaireResponse existing = existingOpt.get();

            // Regla de Seguridad: Impedir modificaciones en registros cruzados
            if (!existing.getQuestionnaireManagementRecord().getId().equals(recordIdRef)) {
                resultFormatter.throwBusinessRuleViolation(
                    ErrorCode.RESPONSE_BELONGS_TO_OTHER_RECORD,
                    "user.responses.security_error",
                    responseId,
                    recordIdRef
                );
            }

            // Regla de Integridad: Validar que el ID de respuesta corresponda a la pregunta esperada
            if (!existing.getQuestion().getId().equals(inputResponse.getQuestion().getId())) {
                resultFormatter.throwBusinessRuleViolation(
                    ErrorCode.RESPONSE_QUESTION_MISMATCH,
                    "user.responses.data_mismatch",
                    responseId
                );
            }

            Integer newValue = inputResponse.getAnswerOption().getValue();
            if (!Objects.equals(existing.getAnswerOption().getValue(), newValue)) {
                Optional<AnswerOption> newOptionOpt = answerOptionQueryRepository.getAnswerOptionByValue(newValue);
                if (newOptionOpt.isEmpty()) {
                    resultFormatter.throwEntityNotFound(
                        ErrorCode.RESPONSE_OPTION_NOT_FOUND,
                        "user.responses.update_option_invalid",
                        newValue
                    );
                }
                existing.setAnswerOption(newOptionOpt.get());
            }
            
            // Hidratación del objeto para persistencia consistente
            inputResponse.setQuestionnaireManagementRecord(existing.getQuestionnaireManagementRecord());
            inputResponse.setQuestion(existing.getQuestion());
            inputResponse.setAnswerOption(existing.getAnswerOption());
            inputResponse.setCreatedAt(existing.getCreatedAt());
        });

        questionnaireResponseCommandRepository.saveAll(responses);
    }

    /**
     * Sincroniza los estados del cuestionario y la batería tras una operación exitosa de guardado.
     *
     * @param managementRecord registro de gestión de cuestionario que se acaba de actualizar con respuestas
     */
    private void syncStatusAfterSave(QuestionnaireManagementRecord managementRecord) {
        String targetStatusName = QuestionnaireManagementRecordStatusEnum.DILIGENCIADO.getName();
        
        Optional<QuestionnaireManagementRecordStatus> diligenciadoStatusOpt = questionnaireManagementRecordStatusQueryRepository
            .getQuestionnaireManagementRecordStatusByName(targetStatusName);
        if (diligenciadoStatusOpt.isEmpty()) {
            resultFormatter.throwEntityNotFound(
                ErrorCode.QUESTIONNAIRE_MGMT_STATUS_NOT_FOUND,
                "user.responses.sync_status_failed",
                targetStatusName
            );
        }
        QuestionnaireManagementRecordStatus diligenciadoStatus = diligenciadoStatusOpt.get();

        managementRecord.setStatus(diligenciadoStatus);
        questionnaireManagementRecordCommandRepository.save(managementRecord);

        // Lógica de progreso general de la batería
        Long batteryId = managementRecord.getBatteryManagementRecord().getId();
        List<String> completed = questionnaireManagementRecordQueryRepository
            .findAbbreviationsByBatteryIdAndStatusName(batteryId, targetStatusName);

        boolean isComplete = completed.contains(QuestionnaireEnum.EXT.getAbbreviation()) && 
                             completed.contains(QuestionnaireEnum.EST.getAbbreviation()) && 
                             (completed.contains(QuestionnaireEnum.ILA.getAbbreviation()) || completed.contains(QuestionnaireEnum.ILB.getAbbreviation())) &&
                             completed.size() == 3;

        String targetBatteryStatus = isComplete 
            ? BatteryManagementRecordStatusCode.COMPLETED.getDescription() 
            : BatteryManagementRecordStatusCode.IN_PROCESSING.getDescription();

        BatteryManagementRecord battery = managementRecord.getBatteryManagementRecord();
        Optional<BatteryManagementRecordStatus> newStatusOpt = batteryManagementRecordStatusQueryRepository
            .getStatusByName(targetBatteryStatus);
        if (newStatusOpt.isEmpty()) {
            resultFormatter.throwEntityNotFound(
                ErrorCode.BATTERY_STATUS_NOT_FOUND,
                "user.responses.sync_status_not_found",
                targetBatteryStatus
            );
        }
        BatteryManagementRecordStatus newStatus = newStatusOpt.get();

        battery.setStatus(newStatus);
        batteryManagementRecordCommandRepository.updateBatteryManagementRecord(battery);
    }
}

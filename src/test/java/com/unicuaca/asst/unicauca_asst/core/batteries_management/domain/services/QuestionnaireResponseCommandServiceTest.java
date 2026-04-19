package com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.services;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.unicuaca.asst.unicauca_asst.common.domain.ports.output.ResultFormatterOutputPort;
import com.unicuaca.asst.unicauca_asst.common.exceptions.BusinessRuleViolationException;
import com.unicuaca.asst.unicauca_asst.common.exceptions.EntityNotFoundPersException;
import com.unicuaca.asst.unicauca_asst.common.exceptions.structure.ErrorCode;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.models.AnswerOption;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.models.BatteryManagementRecord;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.models.BatteryManagementRecordStatus;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.models.Question;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.models.Questionnaire;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.models.QuestionnaireManagementRecord;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.models.QuestionnaireManagementRecordStatus;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.models.QuestionnaireResponse;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.models.enums.BatteryManagementRecordStatusCode;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.models.enums.QuestionnaireEnum;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.models.enums.QuestionnaireManagementRecordStatusEnum;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.ports.output.AnswerOptionQueryRepository;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.ports.output.BatteryManagementRecordCommandRepository;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.ports.output.BatteryManagementRecordStatusQueryRepository;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.ports.output.QuestionQueryRepository;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.ports.output.QuestionnaireManagementRecordCommandRepository;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.ports.output.QuestionnaireManagementRecordQueryRepository;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.ports.output.QuestionnaireManagementRecordStatusQueryRepository;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.ports.output.QuestionnaireResponseCommandRepository;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.ports.output.QuestionnaireResponseQueryRepository;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class QuestionnaireResponseCommandServiceTest {

    @Mock
    private QuestionnaireManagementRecordQueryRepository questionnaireManagementRecordQueryRepository;

    @Mock
    private QuestionQueryRepository questionQueryRepository;

    @Mock
    private AnswerOptionQueryRepository answerOptionQueryRepository;

    @Mock
    private QuestionnaireResponseQueryRepository questionnaireResponseQueryRepository;

    @Mock
    private QuestionnaireManagementRecordStatusQueryRepository questionnaireManagementRecordStatusQueryRepository;

    @Mock
    private BatteryManagementRecordStatusQueryRepository batteryManagementRecordStatusQueryRepository;

    @Mock
    private BatteryManagementRecordCommandRepository batteryManagementRecordCommandRepository;

    @Mock
    private QuestionnaireResponseCommandRepository questionnaireResponseCommandRepository;

    @Mock
    private QuestionnaireManagementRecordCommandRepository questionnaireManagementRecordCommandRepository;

    @Mock
    private ResultFormatterOutputPort resultFormatter;

    @InjectMocks
    private QuestionnaireResponseCommandService questionnaireResponseCommandService;

    // ==================================================================================
    // Helpers
    // ==================================================================================

    private Questionnaire buildQuestionnaire() {
        return Questionnaire.builder()
                .id(1L).name("Intralaboral Forma A")
                .abbreviation(QuestionnaireEnum.ILA.getAbbreviation())
                .build();
    }

    private QuestionnaireManagementRecord buildManagementRecord() {
        String inProcessingName = BatteryManagementRecordStatusCode.IN_PROCESSING.getDescription();
        String creadoName = QuestionnaireManagementRecordStatusEnum.CREADO.getName();

        return QuestionnaireManagementRecord.builder()
                .id(1L)
                .batteryManagementRecord(BatteryManagementRecord.builder()
                        .id(1L)
                        .status(BatteryManagementRecordStatus.builder()
                                .id(2L).name(inProcessingName).build())
                        .build())
                .questionnaire(buildQuestionnaire())
                .status(QuestionnaireManagementRecordStatus.builder()
                        .id(1L).name(creadoName).build())
                .build();
    }

    private Question buildQuestion(Long id) {
        return Question.builder()
                .id(id).text("Pregunta " + id).order(id.intValue())
                .questionnaire(buildQuestionnaire())
                .build();
    }

    private AnswerOption buildAnswerOption(Integer value) {
        return AnswerOption.builder()
                .id(value.longValue()).text("Opción " + value)
                .value(value).order(value)
                .build();
    }

    private QuestionnaireResponse buildResponse(Long questionId, Integer answerValue) {
        return QuestionnaireResponse.builder()
                .questionnaireManagementRecord(QuestionnaireManagementRecord.builder().id(1L).build())
                .question(Question.builder().id(questionId).build())
                .answerOption(AnswerOption.builder().value(answerValue).build())
                .build();
    }

    private QuestionnaireResponse buildExistingResponse(Long responseId, Long questionId, Integer answerValue) {
        return QuestionnaireResponse.builder()
                .id(responseId)
                .questionnaireManagementRecord(buildManagementRecord())
                .question(buildQuestion(questionId))
                .answerOption(buildAnswerOption(answerValue))
                .build();
    }

    private void mockSyncStatusForInProcessing(String diligenciadoQName) {
        String inProcessingName = BatteryManagementRecordStatusCode.IN_PROCESSING.getDescription();
        QuestionnaireManagementRecordStatus diligenciadoStatus = QuestionnaireManagementRecordStatus.builder()
                .id(3L).name(diligenciadoQName).build();

        when(questionnaireManagementRecordStatusQueryRepository
                .getQuestionnaireManagementRecordStatusByName(diligenciadoQName))
                .thenReturn(Optional.of(diligenciadoStatus));
        when(questionnaireManagementRecordCommandRepository.save(any(QuestionnaireManagementRecord.class)))
                .thenReturn(buildManagementRecord());
        when(questionnaireManagementRecordQueryRepository
                .findAbbreviationsByBatteryIdAndStatusName(1L, diligenciadoQName))
                .thenReturn(List.of(QuestionnaireEnum.ILA.getAbbreviation()));

        BatteryManagementRecordStatus inProcessingStatus = BatteryManagementRecordStatus.builder()
                .id(2L).name(inProcessingName).build();
        when(batteryManagementRecordStatusQueryRepository.getStatusByName(inProcessingName))
                .thenReturn(Optional.of(inProcessingStatus));
    }

    // ==================================================================================
    // createQuestionnaireResponseBatch
    // ==================================================================================

    @Nested
    @DisplayName("createQuestionnaireResponseBatch")
    class CreateQuestionnaireResponseBatch {

        @Test
        @DisplayName("Debe crear el lote cuando todas las validaciones pasan (batería incompleta)")
        void should_createBatch_when_allValidationsPass() {
            // Arrange
            String diligenciadoQName = QuestionnaireManagementRecordStatusEnum.DILIGENCIADO.getName();
            QuestionnaireManagementRecord managementRecord = buildManagementRecord();
            List<QuestionnaireResponse> responses = List.of(
                    buildResponse(10L, 3),
                    buildResponse(11L, 4));

            when(questionnaireManagementRecordQueryRepository.findByIdWithAll(1L))
                    .thenReturn(Optional.of(managementRecord));
            when(questionQueryRepository.getQuestionByIdWithQuestionnaire(10L))
                    .thenReturn(Optional.of(buildQuestion(10L)));
            when(questionQueryRepository.getQuestionByIdWithQuestionnaire(11L))
                    .thenReturn(Optional.of(buildQuestion(11L)));
            when(answerOptionQueryRepository.getAnswerOptionByValue(3))
                    .thenReturn(Optional.of(buildAnswerOption(3)));
            when(answerOptionQueryRepository.getAnswerOptionByValue(4))
                    .thenReturn(Optional.of(buildAnswerOption(4)));
            when(questionnaireResponseQueryRepository.existsByRecordIdAndQuestionId(1L, 10L))
                    .thenReturn(false);
            when(questionnaireResponseQueryRepository.existsByRecordIdAndQuestionId(1L, 11L))
                    .thenReturn(false);

            mockSyncStatusForInProcessing(diligenciadoQName);

            // Act
            questionnaireResponseCommandService.createQuestionnaireResponseBatch(responses);

            // Assert
            verify(questionnaireResponseCommandRepository).saveAll(anyList());
            verify(batteryManagementRecordCommandRepository)
                    .updateBatteryManagementRecord(any(BatteryManagementRecord.class));
        }

        @Test
        @DisplayName("Debe crear el lote y sincronizar a Diligenciado cuando la batería está completa (EXT+EST+ILA)")
        void should_createBatch_when_batteryBecomesComplete() {
            // Arrange
            String diligenciadoQName = QuestionnaireManagementRecordStatusEnum.DILIGENCIADO.getName();
            String completedName = BatteryManagementRecordStatusCode.COMPLETED.getDescription();
            QuestionnaireManagementRecord managementRecord = buildManagementRecord();
            List<QuestionnaireResponse> responses = List.of(buildResponse(10L, 3));

            when(questionnaireManagementRecordQueryRepository.findByIdWithAll(1L))
                    .thenReturn(Optional.of(managementRecord));
            when(questionQueryRepository.getQuestionByIdWithQuestionnaire(10L))
                    .thenReturn(Optional.of(buildQuestion(10L)));
            when(answerOptionQueryRepository.getAnswerOptionByValue(3))
                    .thenReturn(Optional.of(buildAnswerOption(3)));
            when(questionnaireResponseQueryRepository.existsByRecordIdAndQuestionId(1L, 10L))
                    .thenReturn(false);

            QuestionnaireManagementRecordStatus diligenciadoStatus = QuestionnaireManagementRecordStatus.builder()
                    .id(3L).name(diligenciadoQName).build();
            when(questionnaireManagementRecordStatusQueryRepository
                    .getQuestionnaireManagementRecordStatusByName(diligenciadoQName))
                    .thenReturn(Optional.of(diligenciadoStatus));
            when(questionnaireManagementRecordCommandRepository.save(any(QuestionnaireManagementRecord.class)))
                    .thenReturn(managementRecord);
            when(questionnaireManagementRecordQueryRepository
                    .findAbbreviationsByBatteryIdAndStatusName(1L, diligenciadoQName))
                    .thenReturn(List.of(
                            QuestionnaireEnum.EXT.getAbbreviation(),
                            QuestionnaireEnum.EST.getAbbreviation(),
                            QuestionnaireEnum.ILA.getAbbreviation()));
            BatteryManagementRecordStatus completedStatus = BatteryManagementRecordStatus.builder()
                    .id(3L).name(completedName).build();
            when(batteryManagementRecordStatusQueryRepository.getStatusByName(completedName))
                    .thenReturn(Optional.of(completedStatus));

            // Act
            questionnaireResponseCommandService.createQuestionnaireResponseBatch(responses);

            // Assert
            verify(questionnaireResponseCommandRepository).saveAll(anyList());
            verify(batteryManagementRecordCommandRepository)
                    .updateBatteryManagementRecord(any(BatteryManagementRecord.class));
        }

        @Test
        @DisplayName("Debe sincronizar a Diligenciado cuando la batería se completa con ILB")
        void should_createBatch_when_batteryBecomesCompleteWithILB() {
            // Arrange — cubre hasIlb=true en (hasIla || hasIlb)
            String diligenciadoQName = QuestionnaireManagementRecordStatusEnum.DILIGENCIADO.getName();
            String completedName = BatteryManagementRecordStatusCode.COMPLETED.getDescription();
            QuestionnaireManagementRecord managementRecord = buildManagementRecord();
            List<QuestionnaireResponse> responses = List.of(buildResponse(10L, 3));

            when(questionnaireManagementRecordQueryRepository.findByIdWithAll(1L))
                    .thenReturn(Optional.of(managementRecord));
            when(questionQueryRepository.getQuestionByIdWithQuestionnaire(10L))
                    .thenReturn(Optional.of(buildQuestion(10L)));
            when(answerOptionQueryRepository.getAnswerOptionByValue(3))
                    .thenReturn(Optional.of(buildAnswerOption(3)));
            when(questionnaireResponseQueryRepository.existsByRecordIdAndQuestionId(1L, 10L))
                    .thenReturn(false);

            QuestionnaireManagementRecordStatus diligenciadoStatus = QuestionnaireManagementRecordStatus.builder()
                    .id(3L).name(diligenciadoQName).build();
            when(questionnaireManagementRecordStatusQueryRepository
                    .getQuestionnaireManagementRecordStatusByName(diligenciadoQName))
                    .thenReturn(Optional.of(diligenciadoStatus));
            when(questionnaireManagementRecordCommandRepository.save(any(QuestionnaireManagementRecord.class)))
                    .thenReturn(managementRecord);
            when(questionnaireManagementRecordQueryRepository
                    .findAbbreviationsByBatteryIdAndStatusName(1L, diligenciadoQName))
                    .thenReturn(List.of(
                            QuestionnaireEnum.EXT.getAbbreviation(),
                            QuestionnaireEnum.EST.getAbbreviation(),
                            QuestionnaireEnum.ILB.getAbbreviation()));
            BatteryManagementRecordStatus completedStatus = BatteryManagementRecordStatus.builder()
                    .id(3L).name(completedName).build();
            when(batteryManagementRecordStatusQueryRepository.getStatusByName(completedName))
                    .thenReturn(Optional.of(completedStatus));

            // Act
            questionnaireResponseCommandService.createQuestionnaireResponseBatch(responses);

            // Assert
            verify(batteryManagementRecordCommandRepository)
                    .updateBatteryManagementRecord(any(BatteryManagementRecord.class));
        }

        @Test
        @DisplayName("Debe sincronizar a En diligenciamiento cuando tiene EXT+EST pero no intralaboral")
        void should_syncToInProcessing_when_noIntralaboralQuestionnaire() {
            // Arrange — cubre hasIla=false && hasIlb=false
            String diligenciadoQName = QuestionnaireManagementRecordStatusEnum.DILIGENCIADO.getName();
            String inProcessingName = BatteryManagementRecordStatusCode.IN_PROCESSING.getDescription();
            QuestionnaireManagementRecord managementRecord = buildManagementRecord();
            List<QuestionnaireResponse> responses = List.of(buildResponse(10L, 3));

            when(questionnaireManagementRecordQueryRepository.findByIdWithAll(1L))
                    .thenReturn(Optional.of(managementRecord));
            when(questionQueryRepository.getQuestionByIdWithQuestionnaire(10L))
                    .thenReturn(Optional.of(buildQuestion(10L)));
            when(answerOptionQueryRepository.getAnswerOptionByValue(3))
                    .thenReturn(Optional.of(buildAnswerOption(3)));
            when(questionnaireResponseQueryRepository.existsByRecordIdAndQuestionId(1L, 10L))
                    .thenReturn(false);

            QuestionnaireManagementRecordStatus diligenciadoStatus = QuestionnaireManagementRecordStatus.builder()
                    .id(3L).name(diligenciadoQName).build();
            when(questionnaireManagementRecordStatusQueryRepository
                    .getQuestionnaireManagementRecordStatusByName(diligenciadoQName))
                    .thenReturn(Optional.of(diligenciadoStatus));
            when(questionnaireManagementRecordCommandRepository.save(any(QuestionnaireManagementRecord.class)))
                    .thenReturn(managementRecord);
            when(questionnaireManagementRecordQueryRepository
                    .findAbbreviationsByBatteryIdAndStatusName(1L, diligenciadoQName))
                    .thenReturn(List.of(
                            QuestionnaireEnum.EXT.getAbbreviation(),
                            QuestionnaireEnum.EST.getAbbreviation()));
            BatteryManagementRecordStatus inProcessingStatus = BatteryManagementRecordStatus.builder()
                    .id(2L).name(inProcessingName).build();
            when(batteryManagementRecordStatusQueryRepository.getStatusByName(inProcessingName))
                    .thenReturn(Optional.of(inProcessingStatus));

            // Act
            questionnaireResponseCommandService.createQuestionnaireResponseBatch(responses);

            // Assert
            verify(batteryManagementRecordCommandRepository)
                    .updateBatteryManagementRecord(any(BatteryManagementRecord.class));
        }

        @Test
        @DisplayName("Debe sincronizar a En diligenciamiento cuando tiene EXT pero falta EST")
        void should_syncToInProcessing_when_missingStressQuestionnaire() {
            // Arrange — cubre hasEst=false cuando hasExt=true (short-circuit en segundo término)
            String diligenciadoQName = QuestionnaireManagementRecordStatusEnum.DILIGENCIADO.getName();
            String inProcessingName = BatteryManagementRecordStatusCode.IN_PROCESSING.getDescription();
            QuestionnaireManagementRecord managementRecord = buildManagementRecord();
            List<QuestionnaireResponse> responses = List.of(buildResponse(10L, 3));

            when(questionnaireManagementRecordQueryRepository.findByIdWithAll(1L))
                    .thenReturn(Optional.of(managementRecord));
            when(questionQueryRepository.getQuestionByIdWithQuestionnaire(10L))
                    .thenReturn(Optional.of(buildQuestion(10L)));
            when(answerOptionQueryRepository.getAnswerOptionByValue(3))
                    .thenReturn(Optional.of(buildAnswerOption(3)));
            when(questionnaireResponseQueryRepository.existsByRecordIdAndQuestionId(1L, 10L))
                    .thenReturn(false);

            QuestionnaireManagementRecordStatus diligenciadoStatus = QuestionnaireManagementRecordStatus.builder()
                    .id(3L).name(diligenciadoQName).build();
            when(questionnaireManagementRecordStatusQueryRepository
                    .getQuestionnaireManagementRecordStatusByName(diligenciadoQName))
                    .thenReturn(Optional.of(diligenciadoStatus));
            when(questionnaireManagementRecordCommandRepository.save(any(QuestionnaireManagementRecord.class)))
                    .thenReturn(managementRecord);
            when(questionnaireManagementRecordQueryRepository
                    .findAbbreviationsByBatteryIdAndStatusName(1L, diligenciadoQName))
                    .thenReturn(List.of(
                            QuestionnaireEnum.EXT.getAbbreviation(),
                            QuestionnaireEnum.ILA.getAbbreviation()));
            BatteryManagementRecordStatus inProcessingStatus = BatteryManagementRecordStatus.builder()
                    .id(2L).name(inProcessingName).build();
            when(batteryManagementRecordStatusQueryRepository.getStatusByName(inProcessingName))
                    .thenReturn(Optional.of(inProcessingStatus));

            // Act
            questionnaireResponseCommandService.createQuestionnaireResponseBatch(responses);

            // Assert
            verify(batteryManagementRecordCommandRepository)
                    .updateBatteryManagementRecord(any(BatteryManagementRecord.class));
        }

        @Test
        @DisplayName("Debe sincronizar a En diligenciamiento cuando tiene 4 cuestionarios completados (size!=3)")
        void should_syncToInProcessing_when_fourQuestionnairesCompleted() {
            // Arrange — cubre size!=3 con todos los tipos presentes
            String diligenciadoQName = QuestionnaireManagementRecordStatusEnum.DILIGENCIADO.getName();
            String inProcessingName = BatteryManagementRecordStatusCode.IN_PROCESSING.getDescription();
            QuestionnaireManagementRecord managementRecord = buildManagementRecord();
            List<QuestionnaireResponse> responses = List.of(buildResponse(10L, 3));

            when(questionnaireManagementRecordQueryRepository.findByIdWithAll(1L))
                    .thenReturn(Optional.of(managementRecord));
            when(questionQueryRepository.getQuestionByIdWithQuestionnaire(10L))
                    .thenReturn(Optional.of(buildQuestion(10L)));
            when(answerOptionQueryRepository.getAnswerOptionByValue(3))
                    .thenReturn(Optional.of(buildAnswerOption(3)));
            when(questionnaireResponseQueryRepository.existsByRecordIdAndQuestionId(1L, 10L))
                    .thenReturn(false);

            QuestionnaireManagementRecordStatus diligenciadoStatus = QuestionnaireManagementRecordStatus.builder()
                    .id(3L).name(diligenciadoQName).build();
            when(questionnaireManagementRecordStatusQueryRepository
                    .getQuestionnaireManagementRecordStatusByName(diligenciadoQName))
                    .thenReturn(Optional.of(diligenciadoStatus));
            when(questionnaireManagementRecordCommandRepository.save(any(QuestionnaireManagementRecord.class)))
                    .thenReturn(managementRecord);
            when(questionnaireManagementRecordQueryRepository
                    .findAbbreviationsByBatteryIdAndStatusName(1L, diligenciadoQName))
                    .thenReturn(List.of(
                            QuestionnaireEnum.EXT.getAbbreviation(),
                            QuestionnaireEnum.EST.getAbbreviation(),
                            QuestionnaireEnum.ILA.getAbbreviation(),
                            QuestionnaireEnum.ILB.getAbbreviation()));
            BatteryManagementRecordStatus inProcessingStatus = BatteryManagementRecordStatus.builder()
                    .id(2L).name(inProcessingName).build();
            when(batteryManagementRecordStatusQueryRepository.getStatusByName(inProcessingName))
                    .thenReturn(Optional.of(inProcessingStatus));

            // Act
            questionnaireResponseCommandService.createQuestionnaireResponseBatch(responses);

            // Assert
            verify(batteryManagementRecordCommandRepository)
                    .updateBatteryManagementRecord(any(BatteryManagementRecord.class));
        }

        @Test
        @DisplayName("Debe lanzar BusinessRuleViolation cuando la lista de respuestas está vacía")
        void should_throwBusinessRuleViolation_when_responsesListIsEmpty() {
            // Arrange
            doThrow(new BusinessRuleViolationException(
                    ErrorCode.EMPTY_LIST_OF_RESPONSES.getCode(),
                    ErrorCode.EMPTY_LIST_OF_RESPONSES.getMessageKey(),
                    "user.responses.empty_list",
                    new Object[]{}))
                .when(resultFormatter).throwBusinessRuleViolation(
                    any(ErrorCode.class), anyString());

            // Act & Assert
            assertThatThrownBy(() -> questionnaireResponseCommandService
                    .createQuestionnaireResponseBatch(List.of()))
                    .isInstanceOf(BusinessRuleViolationException.class);

            verify(resultFormatter).throwBusinessRuleViolation(
                    ErrorCode.EMPTY_LIST_OF_RESPONSES,
                    "user.responses.empty_list");
            verify(questionnaireResponseCommandRepository, never()).saveAll(anyList());
        }

        @Test
        @DisplayName("Debe lanzar BusinessRuleViolation cuando la lista de respuestas es null")
        void should_throwBusinessRuleViolation_when_responsesListIsNull() {
            // Arrange
            doThrow(new BusinessRuleViolationException(
                    ErrorCode.EMPTY_LIST_OF_RESPONSES.getCode(),
                    ErrorCode.EMPTY_LIST_OF_RESPONSES.getMessageKey(),
                    "user.responses.empty_list",
                    new Object[]{}))
                .when(resultFormatter).throwBusinessRuleViolation(
                    any(ErrorCode.class), anyString());

            // Act & Assert
            assertThatThrownBy(() -> questionnaireResponseCommandService
                    .createQuestionnaireResponseBatch(null))
                    .isInstanceOf(BusinessRuleViolationException.class);

            verify(resultFormatter).throwBusinessRuleViolation(
                    ErrorCode.EMPTY_LIST_OF_RESPONSES,
                    "user.responses.empty_list");
            verify(questionnaireResponseCommandRepository, never()).saveAll(anyList());
        }

        @Test
        @DisplayName("Debe lanzar BusinessRuleViolation cuando las respuestas tienen IDs de registro diferentes")
        void should_throwBusinessRuleViolation_when_differentRecordIds() {
            // Arrange
            QuestionnaireResponse r1 = QuestionnaireResponse.builder()
                    .questionnaireManagementRecord(QuestionnaireManagementRecord.builder().id(1L).build())
                    .question(Question.builder().id(10L).build())
                    .answerOption(AnswerOption.builder().value(3).build())
                    .build();
            QuestionnaireResponse r2 = QuestionnaireResponse.builder()
                    .questionnaireManagementRecord(QuestionnaireManagementRecord.builder().id(2L).build())
                    .question(Question.builder().id(11L).build())
                    .answerOption(AnswerOption.builder().value(4).build())
                    .build();
            List<QuestionnaireResponse> responses = List.of(r1, r2);

            doThrow(new BusinessRuleViolationException(
                    ErrorCode.DIFFERENT_RECORD_IDS_IN_RESPONSES.getCode(),
                    ErrorCode.DIFFERENT_RECORD_IDS_IN_RESPONSES.getMessageKey(),
                    "user.responses.inconsistent_batch",
                    new Object[]{}))
                .when(resultFormatter).throwBusinessRuleViolation(
                    any(ErrorCode.class), anyString());

            // Act & Assert
            assertThatThrownBy(() -> questionnaireResponseCommandService
                    .createQuestionnaireResponseBatch(responses))
                    .isInstanceOf(BusinessRuleViolationException.class);

            verify(resultFormatter).throwBusinessRuleViolation(
                    ErrorCode.DIFFERENT_RECORD_IDS_IN_RESPONSES,
                    "user.responses.inconsistent_batch");
            verify(questionnaireResponseCommandRepository, never()).saveAll(anyList());
        }

        @Test
        @DisplayName("Debe lanzar BusinessRuleViolation cuando hay preguntas duplicadas en el lote")
        void should_throwBusinessRuleViolation_when_duplicateQuestions() {
            // Arrange
            List<QuestionnaireResponse> responses = List.of(
                    buildResponse(10L, 3),
                    buildResponse(10L, 4));

            doThrow(new BusinessRuleViolationException(
                    ErrorCode.DUPLICATE_QUESTION_IN_BATCH.getCode(),
                    ErrorCode.DUPLICATE_QUESTION_IN_BATCH.getMessageKey(),
                    "user.responses.duplicate_questions",
                    new Object[]{}))
                .when(resultFormatter).throwBusinessRuleViolation(
                    any(ErrorCode.class), anyString());

            // Act & Assert
            assertThatThrownBy(() -> questionnaireResponseCommandService
                    .createQuestionnaireResponseBatch(responses))
                    .isInstanceOf(BusinessRuleViolationException.class);

            verify(resultFormatter).throwBusinessRuleViolation(
                    ErrorCode.DUPLICATE_QUESTION_IN_BATCH,
                    "user.responses.duplicate_questions");
            verify(questionnaireResponseCommandRepository, never()).saveAll(anyList());
        }

        @Test
        @DisplayName("Debe lanzar EntityNotFound cuando el registro de gestión no existe")
        void should_throwEntityNotFound_when_managementRecordNotFound() {
            // Arrange
            List<QuestionnaireResponse> responses = List.of(buildResponse(10L, 3));

            when(questionnaireManagementRecordQueryRepository.findByIdWithAll(1L))
                    .thenReturn(Optional.empty());
            doThrow(new EntityNotFoundPersException(
                    ErrorCode.QUESTIONNAIRE_MGMT_RECORD_NOT_FOUND.getCode(),
                    ErrorCode.QUESTIONNAIRE_MGMT_RECORD_NOT_FOUND.getMessageKey(),
                    "user.responses.record_not_found",
                    new Object[]{1L}))
                .when(resultFormatter).throwEntityNotFound(
                    any(ErrorCode.class), anyString(), any());

            // Act & Assert
            assertThatThrownBy(() -> questionnaireResponseCommandService
                    .createQuestionnaireResponseBatch(responses))
                    .isInstanceOf(EntityNotFoundPersException.class);

            verify(resultFormatter).throwEntityNotFound(
                    ErrorCode.QUESTIONNAIRE_MGMT_RECORD_NOT_FOUND,
                    "user.responses.record_not_found",
                    1L);
            verify(questionnaireResponseCommandRepository, never()).saveAll(anyList());
        }

        @Test
        @DisplayName("Debe lanzar EntityNotFound cuando la pregunta no existe")
        void should_throwEntityNotFound_when_questionNotFound() {
            // Arrange
            QuestionnaireManagementRecord managementRecord = buildManagementRecord();
            List<QuestionnaireResponse> responses = List.of(buildResponse(10L, 3));

            when(questionnaireManagementRecordQueryRepository.findByIdWithAll(1L))
                    .thenReturn(Optional.of(managementRecord));
            when(questionQueryRepository.getQuestionByIdWithQuestionnaire(10L))
                    .thenReturn(Optional.empty());
            doThrow(new EntityNotFoundPersException(
                    ErrorCode.QUESTION_NOT_FOUND.getCode(),
                    ErrorCode.QUESTION_NOT_FOUND.getMessageKey(),
                    "user.responses.question_invalid",
                    new Object[]{10L}))
                .when(resultFormatter).throwEntityNotFound(
                    any(ErrorCode.class), anyString(), any());

            // Act & Assert
            assertThatThrownBy(() -> questionnaireResponseCommandService
                    .createQuestionnaireResponseBatch(responses))
                    .isInstanceOf(EntityNotFoundPersException.class);

            verify(resultFormatter).throwEntityNotFound(
                    ErrorCode.QUESTION_NOT_FOUND,
                    "user.responses.question_invalid",
                    10L);
            verify(questionnaireResponseCommandRepository, never()).saveAll(anyList());
        }

        @Test
        @DisplayName("Debe lanzar BusinessRuleViolation cuando la pregunta no pertenece al cuestionario")
        void should_throwBusinessRuleViolation_when_questionDoesNotBelongToQuestionnaire() {
            // Arrange
            QuestionnaireManagementRecord managementRecord = buildManagementRecord();
            List<QuestionnaireResponse> responses = List.of(buildResponse(10L, 3));

            Question questionOtherQuestionnaire = Question.builder()
                    .id(10L).text("Pregunta 10").order(10)
                    .questionnaire(Questionnaire.builder().id(99L).name("Otro").abbreviation("OTR").build())
                    .build();

            when(questionnaireManagementRecordQueryRepository.findByIdWithAll(1L))
                    .thenReturn(Optional.of(managementRecord));
            when(questionQueryRepository.getQuestionByIdWithQuestionnaire(10L))
                    .thenReturn(Optional.of(questionOtherQuestionnaire));
            doThrow(new BusinessRuleViolationException(
                    ErrorCode.QUESTION_DOES_NOT_BELONG_TO_QUESTIONNAIRE.getCode(),
                    ErrorCode.QUESTION_DOES_NOT_BELONG_TO_QUESTIONNAIRE.getMessageKey(),
                    "user.responses.question_mismatch",
                    new Object[]{10L, "Intralaboral Forma A"}))
                .when(resultFormatter).throwBusinessRuleViolation(
                    any(ErrorCode.class), anyString(), any(), any());

            // Act & Assert
            assertThatThrownBy(() -> questionnaireResponseCommandService
                    .createQuestionnaireResponseBatch(responses))
                    .isInstanceOf(BusinessRuleViolationException.class);

            verify(resultFormatter).throwBusinessRuleViolation(
                    ErrorCode.QUESTION_DOES_NOT_BELONG_TO_QUESTIONNAIRE,
                    "user.responses.question_mismatch",
                    10L, "Intralaboral Forma A");
            verify(questionnaireResponseCommandRepository, never()).saveAll(anyList());
        }

        @Test
        @DisplayName("Debe lanzar EntityNotFound cuando la opción de respuesta no existe")
        void should_throwEntityNotFound_when_answerOptionNotFound() {
            // Arrange
            QuestionnaireManagementRecord managementRecord = buildManagementRecord();
            List<QuestionnaireResponse> responses = List.of(buildResponse(10L, 99));

            when(questionnaireManagementRecordQueryRepository.findByIdWithAll(1L))
                    .thenReturn(Optional.of(managementRecord));
            when(questionQueryRepository.getQuestionByIdWithQuestionnaire(10L))
                    .thenReturn(Optional.of(buildQuestion(10L)));
            when(answerOptionQueryRepository.getAnswerOptionByValue(99))
                    .thenReturn(Optional.empty());
            doThrow(new EntityNotFoundPersException(
                    ErrorCode.RESPONSE_OPTION_NOT_FOUND.getCode(),
                    ErrorCode.RESPONSE_OPTION_NOT_FOUND.getMessageKey(),
                    "user.responses.option_invalid",
                    new Object[]{99}))
                .when(resultFormatter).throwEntityNotFound(
                    any(ErrorCode.class), anyString(), any());

            // Act & Assert
            assertThatThrownBy(() -> questionnaireResponseCommandService
                    .createQuestionnaireResponseBatch(responses))
                    .isInstanceOf(EntityNotFoundPersException.class);

            verify(resultFormatter).throwEntityNotFound(
                    ErrorCode.RESPONSE_OPTION_NOT_FOUND,
                    "user.responses.option_invalid",
                    99);
            verify(questionnaireResponseCommandRepository, never()).saveAll(anyList());
        }

        @Test
        @DisplayName("Debe lanzar BusinessRuleViolation cuando la pregunta ya fue respondida")
        void should_throwBusinessRuleViolation_when_questionAlreadyAnswered() {
            // Arrange
            QuestionnaireManagementRecord managementRecord = buildManagementRecord();
            List<QuestionnaireResponse> responses = List.of(buildResponse(10L, 3));

            when(questionnaireManagementRecordQueryRepository.findByIdWithAll(1L))
                    .thenReturn(Optional.of(managementRecord));
            when(questionQueryRepository.getQuestionByIdWithQuestionnaire(10L))
                    .thenReturn(Optional.of(buildQuestion(10L)));
            when(answerOptionQueryRepository.getAnswerOptionByValue(3))
                    .thenReturn(Optional.of(buildAnswerOption(3)));
            when(questionnaireResponseQueryRepository.existsByRecordIdAndQuestionId(1L, 10L))
                    .thenReturn(true);
            doThrow(new BusinessRuleViolationException(
                    ErrorCode.QUESTION_ANSWERED_ALREADY.getCode(),
                    ErrorCode.QUESTION_ANSWERED_ALREADY.getMessageKey(),
                    "user.responses.already_answered",
                    new Object[]{10L, 1L}))
                .when(resultFormatter).throwBusinessRuleViolation(
                    any(ErrorCode.class), anyString(), any(), any());

            // Act & Assert
            assertThatThrownBy(() -> questionnaireResponseCommandService
                    .createQuestionnaireResponseBatch(responses))
                    .isInstanceOf(BusinessRuleViolationException.class);

            verify(resultFormatter).throwBusinessRuleViolation(
                    ErrorCode.QUESTION_ANSWERED_ALREADY,
                    "user.responses.already_answered",
                    10L, 1L);
            verify(questionnaireResponseCommandRepository, never()).saveAll(anyList());
        }

        @Test
        @DisplayName("Debe lanzar EntityNotFound cuando el estado Diligenciado no se encuentra en sincronización")
        void should_throwEntityNotFound_when_syncStatusNotFound() {
            // Arrange
            String diligenciadoQName = QuestionnaireManagementRecordStatusEnum.DILIGENCIADO.getName();
            QuestionnaireManagementRecord managementRecord = buildManagementRecord();
            List<QuestionnaireResponse> responses = List.of(buildResponse(10L, 3));

            when(questionnaireManagementRecordQueryRepository.findByIdWithAll(1L))
                    .thenReturn(Optional.of(managementRecord));
            when(questionQueryRepository.getQuestionByIdWithQuestionnaire(10L))
                    .thenReturn(Optional.of(buildQuestion(10L)));
            when(answerOptionQueryRepository.getAnswerOptionByValue(3))
                    .thenReturn(Optional.of(buildAnswerOption(3)));
            when(questionnaireResponseQueryRepository.existsByRecordIdAndQuestionId(1L, 10L))
                    .thenReturn(false);
            when(questionnaireManagementRecordStatusQueryRepository
                    .getQuestionnaireManagementRecordStatusByName(diligenciadoQName))
                    .thenReturn(Optional.empty());
            doThrow(new EntityNotFoundPersException(
                    ErrorCode.QUESTIONNAIRE_MGMT_STATUS_NOT_FOUND.getCode(),
                    ErrorCode.QUESTIONNAIRE_MGMT_STATUS_NOT_FOUND.getMessageKey(),
                    "user.responses.sync_status_failed",
                    new Object[]{diligenciadoQName}))
                .when(resultFormatter).throwEntityNotFound(
                    any(ErrorCode.class), anyString(), any());

            // Act & Assert
            assertThatThrownBy(() -> questionnaireResponseCommandService
                    .createQuestionnaireResponseBatch(responses))
                    .isInstanceOf(EntityNotFoundPersException.class);

            verify(resultFormatter).throwEntityNotFound(
                    ErrorCode.QUESTIONNAIRE_MGMT_STATUS_NOT_FOUND,
                    "user.responses.sync_status_failed",
                    diligenciadoQName);
        }

        @Test
        @DisplayName("Debe lanzar EntityNotFound cuando el estado de batería no se encuentra en sincronización")
        void should_throwEntityNotFound_when_batteryStatusNotFoundOnSync() {
            // Arrange
            String diligenciadoQName = QuestionnaireManagementRecordStatusEnum.DILIGENCIADO.getName();
            String inProcessingName = BatteryManagementRecordStatusCode.IN_PROCESSING.getDescription();
            QuestionnaireManagementRecord managementRecord = buildManagementRecord();
            List<QuestionnaireResponse> responses = List.of(buildResponse(10L, 3));

            when(questionnaireManagementRecordQueryRepository.findByIdWithAll(1L))
                    .thenReturn(Optional.of(managementRecord));
            when(questionQueryRepository.getQuestionByIdWithQuestionnaire(10L))
                    .thenReturn(Optional.of(buildQuestion(10L)));
            when(answerOptionQueryRepository.getAnswerOptionByValue(3))
                    .thenReturn(Optional.of(buildAnswerOption(3)));
            when(questionnaireResponseQueryRepository.existsByRecordIdAndQuestionId(1L, 10L))
                    .thenReturn(false);

            QuestionnaireManagementRecordStatus diligenciadoStatus = QuestionnaireManagementRecordStatus.builder()
                    .id(3L).name(diligenciadoQName).build();
            when(questionnaireManagementRecordStatusQueryRepository
                    .getQuestionnaireManagementRecordStatusByName(diligenciadoQName))
                    .thenReturn(Optional.of(diligenciadoStatus));
            when(questionnaireManagementRecordCommandRepository.save(any(QuestionnaireManagementRecord.class)))
                    .thenReturn(managementRecord);
            when(questionnaireManagementRecordQueryRepository
                    .findAbbreviationsByBatteryIdAndStatusName(1L, diligenciadoQName))
                    .thenReturn(List.of(QuestionnaireEnum.ILA.getAbbreviation()));
            when(batteryManagementRecordStatusQueryRepository.getStatusByName(inProcessingName))
                    .thenReturn(Optional.empty());
            doThrow(new EntityNotFoundPersException(
                    ErrorCode.BATTERY_STATUS_NOT_FOUND.getCode(),
                    ErrorCode.BATTERY_STATUS_NOT_FOUND.getMessageKey(),
                    "user.responses.sync_status_not_found",
                    new Object[]{inProcessingName}))
                .when(resultFormatter).throwEntityNotFound(
                    any(ErrorCode.class), anyString(), any());

            // Act & Assert
            assertThatThrownBy(() -> questionnaireResponseCommandService
                    .createQuestionnaireResponseBatch(responses))
                    .isInstanceOf(EntityNotFoundPersException.class);

            verify(resultFormatter).throwEntityNotFound(
                    ErrorCode.BATTERY_STATUS_NOT_FOUND,
                    "user.responses.sync_status_not_found",
                    inProcessingName);
        }
    }

    // ==================================================================================
    // updateQuestionnaireResponseBatch
    // ==================================================================================

    @Nested
    @DisplayName("updateQuestionnaireResponseBatch")
    class UpdateQuestionnaireResponseBatch {

        @Test
        @DisplayName("Debe actualizar el lote cuando todas las validaciones pasan")
        void should_updateBatch_when_allValidationsPass() {
            // Arrange
            QuestionnaireManagementRecord managementRecord = buildManagementRecord();
            QuestionnaireResponse inputResponse = QuestionnaireResponse.builder()
                    .id(1L)
                    .questionnaireManagementRecord(QuestionnaireManagementRecord.builder().id(1L).build())
                    .question(Question.builder().id(10L).build())
                    .answerOption(AnswerOption.builder().value(5).build())
                    .build();
            List<QuestionnaireResponse> responses = List.of(inputResponse);

            QuestionnaireResponse existingResponse = buildExistingResponse(1L, 10L, 3);
            AnswerOption newAnswerOption = buildAnswerOption(5);

            when(questionnaireManagementRecordQueryRepository.findById(1L))
                    .thenReturn(Optional.of(managementRecord));
            when(questionnaireResponseQueryRepository.getByIdWithAllRelations(1L))
                    .thenReturn(Optional.of(existingResponse));
            when(answerOptionQueryRepository.getAnswerOptionByValue(5))
                    .thenReturn(Optional.of(newAnswerOption));

            // Act
            questionnaireResponseCommandService.updateQuestionnaireResponseBatch(responses);

            // Assert
            verify(questionnaireResponseCommandRepository).saveAll(anyList());
        }

        @Test
        @DisplayName("Debe actualizar sin consultar opción cuando el valor no cambió")
        void should_updateBatch_when_answerValueUnchanged() {
            // Arrange — cubre Objects.equals(existing.value, newValue) = true
            QuestionnaireManagementRecord managementRecord = buildManagementRecord();
            QuestionnaireResponse inputResponse = QuestionnaireResponse.builder()
                    .id(1L)
                    .questionnaireManagementRecord(QuestionnaireManagementRecord.builder().id(1L).build())
                    .question(Question.builder().id(10L).build())
                    .answerOption(AnswerOption.builder().value(3).build())
                    .build();
            List<QuestionnaireResponse> responses = List.of(inputResponse);

            QuestionnaireResponse existingResponse = buildExistingResponse(1L, 10L, 3);

            when(questionnaireManagementRecordQueryRepository.findById(1L))
                    .thenReturn(Optional.of(managementRecord));
            when(questionnaireResponseQueryRepository.getByIdWithAllRelations(1L))
                    .thenReturn(Optional.of(existingResponse));

            // Act
            questionnaireResponseCommandService.updateQuestionnaireResponseBatch(responses);

            // Assert
            verify(questionnaireResponseCommandRepository).saveAll(anyList());
            verify(answerOptionQueryRepository, never()).getAnswerOptionByValue(any());
        }

        @Test
        @DisplayName("Debe lanzar BusinessRuleViolation cuando la lista de actualización está vacía")
        void should_throwBusinessRuleViolation_when_updateListIsEmpty() {
            // Arrange
            doThrow(new BusinessRuleViolationException(
                    ErrorCode.EMPTY_LIST_OF_RESPONSES.getCode(),
                    ErrorCode.EMPTY_LIST_OF_RESPONSES.getMessageKey(),
                    "user.responses.update_empty",
                    new Object[]{}))
                .when(resultFormatter).throwBusinessRuleViolation(
                    any(ErrorCode.class), anyString());

            // Act & Assert
            assertThatThrownBy(() -> questionnaireResponseCommandService
                    .updateQuestionnaireResponseBatch(List.of()))
                    .isInstanceOf(BusinessRuleViolationException.class);

            verify(resultFormatter).throwBusinessRuleViolation(
                    ErrorCode.EMPTY_LIST_OF_RESPONSES,
                    "user.responses.update_empty");
            verify(questionnaireResponseCommandRepository, never()).saveAll(anyList());
        }

        @Test
        @DisplayName("Debe lanzar BusinessRuleViolation cuando la lista de actualización es null")
        void should_throwBusinessRuleViolation_when_updateListIsNull() {
            // Arrange
            doThrow(new BusinessRuleViolationException(
                    ErrorCode.EMPTY_LIST_OF_RESPONSES.getCode(),
                    ErrorCode.EMPTY_LIST_OF_RESPONSES.getMessageKey(),
                    "user.responses.update_empty",
                    new Object[]{}))
                .when(resultFormatter).throwBusinessRuleViolation(
                    any(ErrorCode.class), anyString());

            // Act & Assert
            assertThatThrownBy(() -> questionnaireResponseCommandService
                    .updateQuestionnaireResponseBatch(null))
                    .isInstanceOf(BusinessRuleViolationException.class);

            verify(resultFormatter).throwBusinessRuleViolation(
                    ErrorCode.EMPTY_LIST_OF_RESPONSES,
                    "user.responses.update_empty");
            verify(questionnaireResponseCommandRepository, never()).saveAll(anyList());
        }

        @Test
        @DisplayName("Debe lanzar BusinessRuleViolation cuando hay IDs de registro diferentes en actualización")
        void should_throwBusinessRuleViolation_when_differentRecordIdsOnUpdate() {
            // Arrange
            QuestionnaireResponse r1 = QuestionnaireResponse.builder()
                    .id(1L)
                    .questionnaireManagementRecord(QuestionnaireManagementRecord.builder().id(1L).build())
                    .question(Question.builder().id(10L).build())
                    .answerOption(AnswerOption.builder().value(3).build())
                    .build();
            QuestionnaireResponse r2 = QuestionnaireResponse.builder()
                    .id(2L)
                    .questionnaireManagementRecord(QuestionnaireManagementRecord.builder().id(2L).build())
                    .question(Question.builder().id(11L).build())
                    .answerOption(AnswerOption.builder().value(4).build())
                    .build();
            List<QuestionnaireResponse> responses = List.of(r1, r2);

            doThrow(new BusinessRuleViolationException(
                    ErrorCode.DIFFERENT_RECORD_IDS_IN_RESPONSES.getCode(),
                    ErrorCode.DIFFERENT_RECORD_IDS_IN_RESPONSES.getMessageKey(),
                    "user.responses.update_inconsistent_batch",
                    new Object[]{}))
                .when(resultFormatter).throwBusinessRuleViolation(
                    any(ErrorCode.class), anyString());

            // Act & Assert
            assertThatThrownBy(() -> questionnaireResponseCommandService
                    .updateQuestionnaireResponseBatch(responses))
                    .isInstanceOf(BusinessRuleViolationException.class);

            verify(resultFormatter).throwBusinessRuleViolation(
                    ErrorCode.DIFFERENT_RECORD_IDS_IN_RESPONSES,
                    "user.responses.update_inconsistent_batch");
            verify(questionnaireResponseCommandRepository, never()).saveAll(anyList());
        }

        @Test
        @DisplayName("Debe lanzar BusinessRuleViolation cuando hay preguntas duplicadas en actualización")
        void should_throwBusinessRuleViolation_when_duplicateQuestionsOnUpdate() {
            // Arrange
            QuestionnaireResponse r1 = QuestionnaireResponse.builder()
                    .id(1L)
                    .questionnaireManagementRecord(QuestionnaireManagementRecord.builder().id(1L).build())
                    .question(Question.builder().id(10L).build())
                    .answerOption(AnswerOption.builder().value(3).build())
                    .build();
            QuestionnaireResponse r2 = QuestionnaireResponse.builder()
                    .id(2L)
                    .questionnaireManagementRecord(QuestionnaireManagementRecord.builder().id(1L).build())
                    .question(Question.builder().id(10L).build())
                    .answerOption(AnswerOption.builder().value(4).build())
                    .build();
            List<QuestionnaireResponse> responses = List.of(r1, r2);

            doThrow(new BusinessRuleViolationException(
                    ErrorCode.DUPLICATE_QUESTION_IN_BATCH.getCode(),
                    ErrorCode.DUPLICATE_QUESTION_IN_BATCH.getMessageKey(),
                    "user.responses.update_duplicate_questions",
                    new Object[]{}))
                .when(resultFormatter).throwBusinessRuleViolation(
                    any(ErrorCode.class), anyString());

            // Act & Assert
            assertThatThrownBy(() -> questionnaireResponseCommandService
                    .updateQuestionnaireResponseBatch(responses))
                    .isInstanceOf(BusinessRuleViolationException.class);

            verify(resultFormatter).throwBusinessRuleViolation(
                    ErrorCode.DUPLICATE_QUESTION_IN_BATCH,
                    "user.responses.update_duplicate_questions");
            verify(questionnaireResponseCommandRepository, never()).saveAll(anyList());
        }

        @Test
        @DisplayName("Debe lanzar EntityNotFound cuando el registro de gestión no existe en actualización")
        void should_throwEntityNotFound_when_managementRecordNotFoundOnUpdate() {
            // Arrange
            QuestionnaireResponse inputResponse = QuestionnaireResponse.builder()
                    .id(1L)
                    .questionnaireManagementRecord(QuestionnaireManagementRecord.builder().id(1L).build())
                    .question(Question.builder().id(10L).build())
                    .answerOption(AnswerOption.builder().value(3).build())
                    .build();
            List<QuestionnaireResponse> responses = List.of(inputResponse);

            when(questionnaireManagementRecordQueryRepository.findById(1L))
                    .thenReturn(Optional.empty());
            doThrow(new EntityNotFoundPersException(
                    ErrorCode.QUESTIONNAIRE_MGMT_RECORD_NOT_FOUND.getCode(),
                    ErrorCode.QUESTIONNAIRE_MGMT_RECORD_NOT_FOUND.getMessageKey(),
                    "user.responses.update_record_not_found",
                    new Object[]{1L}))
                .when(resultFormatter).throwEntityNotFound(
                    any(ErrorCode.class), anyString(), any());

            // Act & Assert
            assertThatThrownBy(() -> questionnaireResponseCommandService
                    .updateQuestionnaireResponseBatch(responses))
                    .isInstanceOf(EntityNotFoundPersException.class);

            verify(resultFormatter).throwEntityNotFound(
                    ErrorCode.QUESTIONNAIRE_MGMT_RECORD_NOT_FOUND,
                    "user.responses.update_record_not_found",
                    1L);
            verify(questionnaireResponseCommandRepository, never()).saveAll(anyList());
        }

        @Test
        @DisplayName("Debe lanzar EntityNotFound cuando la respuesta existente no se encuentra")
        void should_throwEntityNotFound_when_existingResponseNotFound() {
            // Arrange
            QuestionnaireManagementRecord managementRecord = buildManagementRecord();
            QuestionnaireResponse inputResponse = QuestionnaireResponse.builder()
                    .id(1L)
                    .questionnaireManagementRecord(QuestionnaireManagementRecord.builder().id(1L).build())
                    .question(Question.builder().id(10L).build())
                    .answerOption(AnswerOption.builder().value(3).build())
                    .build();
            List<QuestionnaireResponse> responses = List.of(inputResponse);

            when(questionnaireManagementRecordQueryRepository.findById(1L))
                    .thenReturn(Optional.of(managementRecord));
            when(questionnaireResponseQueryRepository.getByIdWithAllRelations(1L))
                    .thenReturn(Optional.empty());
            doThrow(new EntityNotFoundPersException(
                    ErrorCode.RESPONSE_NOT_FOUND.getCode(),
                    ErrorCode.RESPONSE_NOT_FOUND.getMessageKey(),
                    "user.responses.update_not_found",
                    new Object[]{1L}))
                .when(resultFormatter).throwEntityNotFound(
                    any(ErrorCode.class), anyString(), any());

            // Act & Assert
            assertThatThrownBy(() -> questionnaireResponseCommandService
                    .updateQuestionnaireResponseBatch(responses))
                    .isInstanceOf(EntityNotFoundPersException.class);

            verify(resultFormatter).throwEntityNotFound(
                    ErrorCode.RESPONSE_NOT_FOUND,
                    "user.responses.update_not_found",
                    1L);
            verify(questionnaireResponseCommandRepository, never()).saveAll(anyList());
        }

        @Test
        @DisplayName("Debe lanzar BusinessRuleViolation cuando la respuesta pertenece a otro registro")
        void should_throwBusinessRuleViolation_when_responseBelongsToOtherRecord() {
            // Arrange
            QuestionnaireManagementRecord managementRecord = buildManagementRecord();
            QuestionnaireResponse inputResponse = QuestionnaireResponse.builder()
                    .id(1L)
                    .questionnaireManagementRecord(QuestionnaireManagementRecord.builder().id(1L).build())
                    .question(Question.builder().id(10L).build())
                    .answerOption(AnswerOption.builder().value(3).build())
                    .build();
            List<QuestionnaireResponse> responses = List.of(inputResponse);

            QuestionnaireResponse existingFromOtherRecord = QuestionnaireResponse.builder()
                    .id(1L)
                    .questionnaireManagementRecord(QuestionnaireManagementRecord.builder().id(99L).build())
                    .question(buildQuestion(10L))
                    .answerOption(buildAnswerOption(3))
                    .build();

            when(questionnaireManagementRecordQueryRepository.findById(1L))
                    .thenReturn(Optional.of(managementRecord));
            when(questionnaireResponseQueryRepository.getByIdWithAllRelations(1L))
                    .thenReturn(Optional.of(existingFromOtherRecord));
            doThrow(new BusinessRuleViolationException(
                    ErrorCode.RESPONSE_BELONGS_TO_OTHER_RECORD.getCode(),
                    ErrorCode.RESPONSE_BELONGS_TO_OTHER_RECORD.getMessageKey(),
                    "user.responses.security_error",
                    new Object[]{1L, 1L}))
                .when(resultFormatter).throwBusinessRuleViolation(
                    any(ErrorCode.class), anyString(), any(), any());

            // Act & Assert
            assertThatThrownBy(() -> questionnaireResponseCommandService
                    .updateQuestionnaireResponseBatch(responses))
                    .isInstanceOf(BusinessRuleViolationException.class);

            verify(resultFormatter).throwBusinessRuleViolation(
                    ErrorCode.RESPONSE_BELONGS_TO_OTHER_RECORD,
                    "user.responses.security_error",
                    1L, 1L);
            verify(questionnaireResponseCommandRepository, never()).saveAll(anyList());
        }

        @Test
        @DisplayName("Debe lanzar BusinessRuleViolation cuando la pregunta no coincide en actualización")
        void should_throwBusinessRuleViolation_when_questionMismatchOnUpdate() {
            // Arrange
            QuestionnaireManagementRecord managementRecord = buildManagementRecord();
            QuestionnaireResponse inputResponse = QuestionnaireResponse.builder()
                    .id(1L)
                    .questionnaireManagementRecord(QuestionnaireManagementRecord.builder().id(1L).build())
                    .question(Question.builder().id(99L).build())
                    .answerOption(AnswerOption.builder().value(3).build())
                    .build();
            List<QuestionnaireResponse> responses = List.of(inputResponse);

            QuestionnaireResponse existingResponse = buildExistingResponse(1L, 10L, 3);

            when(questionnaireManagementRecordQueryRepository.findById(1L))
                    .thenReturn(Optional.of(managementRecord));
            when(questionnaireResponseQueryRepository.getByIdWithAllRelations(1L))
                    .thenReturn(Optional.of(existingResponse));
            doThrow(new BusinessRuleViolationException(
                    ErrorCode.RESPONSE_QUESTION_MISMATCH.getCode(),
                    ErrorCode.RESPONSE_QUESTION_MISMATCH.getMessageKey(),
                    "user.responses.data_mismatch",
                    new Object[]{1L}))
                .when(resultFormatter).throwBusinessRuleViolation(
                    any(ErrorCode.class), anyString(), any());

            // Act & Assert
            assertThatThrownBy(() -> questionnaireResponseCommandService
                    .updateQuestionnaireResponseBatch(responses))
                    .isInstanceOf(BusinessRuleViolationException.class);

            verify(resultFormatter).throwBusinessRuleViolation(
                    ErrorCode.RESPONSE_QUESTION_MISMATCH,
                    "user.responses.data_mismatch",
                    1L);
            verify(questionnaireResponseCommandRepository, never()).saveAll(anyList());
        }

        @Test
        @DisplayName("Debe lanzar EntityNotFound cuando la opción de respuesta no existe en actualización")
        void should_throwEntityNotFound_when_answerOptionNotFoundOnUpdate() {
            // Arrange
            QuestionnaireManagementRecord managementRecord = buildManagementRecord();
            QuestionnaireResponse inputResponse = QuestionnaireResponse.builder()
                    .id(1L)
                    .questionnaireManagementRecord(QuestionnaireManagementRecord.builder().id(1L).build())
                    .question(Question.builder().id(10L).build())
                    .answerOption(AnswerOption.builder().value(99).build())
                    .build();
            List<QuestionnaireResponse> responses = List.of(inputResponse);

            QuestionnaireResponse existingResponse = buildExistingResponse(1L, 10L, 3);

            when(questionnaireManagementRecordQueryRepository.findById(1L))
                    .thenReturn(Optional.of(managementRecord));
            when(questionnaireResponseQueryRepository.getByIdWithAllRelations(1L))
                    .thenReturn(Optional.of(existingResponse));
            when(answerOptionQueryRepository.getAnswerOptionByValue(99))
                    .thenReturn(Optional.empty());
            doThrow(new EntityNotFoundPersException(
                    ErrorCode.RESPONSE_OPTION_NOT_FOUND.getCode(),
                    ErrorCode.RESPONSE_OPTION_NOT_FOUND.getMessageKey(),
                    "user.responses.update_option_invalid",
                    new Object[]{99}))
                .when(resultFormatter).throwEntityNotFound(
                    any(ErrorCode.class), anyString(), any());

            // Act & Assert
            assertThatThrownBy(() -> questionnaireResponseCommandService
                    .updateQuestionnaireResponseBatch(responses))
                    .isInstanceOf(EntityNotFoundPersException.class);

            verify(resultFormatter).throwEntityNotFound(
                    ErrorCode.RESPONSE_OPTION_NOT_FOUND,
                    "user.responses.update_option_invalid",
                    99);
            verify(questionnaireResponseCommandRepository, never()).saveAll(anyList());
        }
    }
}

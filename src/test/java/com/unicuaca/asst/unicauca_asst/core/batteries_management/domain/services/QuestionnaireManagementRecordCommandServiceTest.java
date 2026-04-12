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

import com.unicuaca.asst.unicauca_asst.common.application.output.ResultFormatterOutputPort;
import com.unicuaca.asst.unicauca_asst.common.exceptions.BusinessRuleViolationException;
import com.unicuaca.asst.unicauca_asst.common.exceptions.EntityAlreadyExistsException;
import com.unicuaca.asst.unicauca_asst.common.exceptions.EntityNotFoundPersException;
import com.unicuaca.asst.unicauca_asst.common.exceptions.structure.ErrorCode;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.models.BatteryManagementRecord;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.models.BatteryManagementRecordStatus;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.models.Questionnaire;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.models.QuestionnaireManagementRecord;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.models.QuestionnaireManagementRecordStatus;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.models.enums.BatteryManagementRecordStatusCode;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.models.enums.QuestionnaireEnum;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.models.enums.QuestionnaireManagementRecordStatusEnum;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.ports.output.BatteryManagementRecordCommandRepository;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.ports.output.BatteryManagementRecordQueryRepository;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.ports.output.BatteryManagementRecordStatusQueryRepository;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.ports.output.QuestionnaireManagementRecordCommandRepository;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.ports.output.QuestionnaireManagementRecordQueryRepository;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.ports.output.QuestionnaireManagementRecordStatusQueryRepository;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.ports.output.QuestionnaireQueryRepository;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.ports.output.QuestionnaireResponseCommandRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class QuestionnaireManagementRecordCommandServiceTest {

    @Mock
    private BatteryManagementRecordQueryRepository batteryManagementRecordQueryRepository;

    @Mock
    private QuestionnaireQueryRepository questionnaireQueryRepository;

    @Mock
    private QuestionnaireManagementRecordStatusQueryRepository questionnaireManagementRecordStatusQueryRepository;

    @Mock
    private QuestionnaireManagementRecordQueryRepository questionnaireManagementRecordQueryRepository;

    @Mock
    private BatteryManagementRecordStatusQueryRepository batteryManagementRecordStatusQueryRepository;

    @Mock
    private QuestionnaireResponseCommandRepository questionnaireResponseCommandRepository;

    @Mock
    private QuestionnaireManagementRecordCommandRepository questionnaireManagementRecordCommandRepository;

    @Mock
    private BatteryManagementRecordCommandRepository batteryManagementRecordCommandRepository;

    @Mock
    private ResultFormatterOutputPort resultFormatter;

    @InjectMocks
    private QuestionnaireManagementRecordCommandService questionnaireManagementRecordCommandService;

    private QuestionnaireManagementRecord buildQuestionnaireManagementRecord() {
        String inProcessingName = BatteryManagementRecordStatusCode.IN_PROCESSING.getDescription();
        String creadoName = QuestionnaireManagementRecordStatusEnum.CREADO.getName();

        return QuestionnaireManagementRecord.builder()
                .id(1L)
                .batteryManagementRecord(BatteryManagementRecord.builder()
                        .id(1L)
                        .status(BatteryManagementRecordStatus.builder()
                                .id(2L).name(inProcessingName).build())
                        .build())
                .questionnaire(Questionnaire.builder()
                        .id(1L).name("Intralaboral Forma A")
                        .abbreviation(QuestionnaireEnum.ILA.getAbbreviation()).build())
                .status(QuestionnaireManagementRecordStatus.builder()
                        .id(1L).name(creadoName).build())
                .build();
    }

    private BatteryManagementRecordStatus buildBatteryStatus(Long id, BatteryManagementRecordStatusCode code) {
        return BatteryManagementRecordStatus.builder()
                .id(id).name(code.getDescription()).build();
    }

    // ==================================================================================
    // createQuestionnaireManagementRecord
    // ==================================================================================

    @Nested
    @DisplayName("createQuestionnaireManagementRecord")
    class CreateQuestionnaireManagementRecord {

        @Test
        @DisplayName("Debe crear el registro cuando todas las validaciones pasan")
        void should_createRecord_when_allValidationsPass() {
            // Arrange
            QuestionnaireManagementRecord record = buildQuestionnaireManagementRecord();
            BatteryManagementRecord batteryRecord = record.getBatteryManagementRecord();
            Questionnaire questionnaire = record.getQuestionnaire();
            String creadoName = QuestionnaireManagementRecordStatusEnum.CREADO.getName();
            QuestionnaireManagementRecordStatus initialStatus = QuestionnaireManagementRecordStatus.builder()
                    .id(1L).name(creadoName).build();

            when(batteryManagementRecordQueryRepository.getBatteryManagementRecordById(1L))
                    .thenReturn(Optional.of(batteryRecord));
            when(questionnaireQueryRepository.getById(1L))
                    .thenReturn(Optional.of(questionnaire));
            when(questionnaireManagementRecordQueryRepository
                    .existsByBatteryManagementRecordIdAndQuestionnaireId(1L, 1L))
                    .thenReturn(false);
            when(questionnaireManagementRecordStatusQueryRepository
                    .getQuestionnaireManagementRecordStatusByName(creadoName))
                    .thenReturn(Optional.of(initialStatus));
            when(questionnaireManagementRecordCommandRepository.save(any(QuestionnaireManagementRecord.class)))
                    .thenReturn(record);

            // Act
            QuestionnaireManagementRecord result = questionnaireManagementRecordCommandService
                    .createQuestionnaireManagementRecord(record);

            // Assert
            assertThat(result).isNotNull();
            assertThat(result.getStatus().getName()).isEqualTo(creadoName);
            assertThat(result.getQuestionnaire()).isNotNull();
            verify(questionnaireManagementRecordCommandRepository)
                    .save(any(QuestionnaireManagementRecord.class));
        }

        @Test
        @DisplayName("Debe lanzar EntityNotFound cuando la batería no existe")
        void should_throwEntityNotFound_when_batteryRecordNotFound() {
            // Arrange
            QuestionnaireManagementRecord record = buildQuestionnaireManagementRecord();

            when(batteryManagementRecordQueryRepository.getBatteryManagementRecordById(1L))
                    .thenReturn(Optional.empty());
            doThrow(new EntityNotFoundPersException(
                    ErrorCode.BATTERY_RECORD_NOT_FOUND.getCode(),
                    ErrorCode.BATTERY_RECORD_NOT_FOUND.getMessageKey(),
                    "user.questionnaire_management.battery_not_found",
                    new Object[]{1L}))
                .when(resultFormatter).throwEntityNotFound(
                    any(ErrorCode.class), anyString(), any());

            // Act & Assert
            assertThatThrownBy(() -> questionnaireManagementRecordCommandService
                    .createQuestionnaireManagementRecord(record))
                    .isInstanceOf(EntityNotFoundPersException.class);

            verify(resultFormatter).throwEntityNotFound(
                    ErrorCode.BATTERY_RECORD_NOT_FOUND,
                    "user.questionnaire_management.battery_not_found",
                    1L);
            verify(questionnaireManagementRecordCommandRepository, never()).save(any());
        }

        @Test
        @DisplayName("Debe lanzar EntityNotFound cuando el cuestionario no existe")
        void should_throwEntityNotFound_when_questionnaireNotFound() {
            // Arrange
            QuestionnaireManagementRecord record = buildQuestionnaireManagementRecord();
            BatteryManagementRecord batteryRecord = record.getBatteryManagementRecord();

            when(batteryManagementRecordQueryRepository.getBatteryManagementRecordById(1L))
                    .thenReturn(Optional.of(batteryRecord));
            when(questionnaireQueryRepository.getById(1L))
                    .thenReturn(Optional.empty());
            doThrow(new EntityNotFoundPersException(
                    ErrorCode.QUESTIONNAIRE_NOT_FOUND_BY_REF.getCode(),
                    ErrorCode.QUESTIONNAIRE_NOT_FOUND_BY_REF.getMessageKey(),
                    "user.questionnaire_management.not_found",
                    new Object[]{1L}))
                .when(resultFormatter).throwEntityNotFound(
                    any(ErrorCode.class), anyString(), any());

            // Act & Assert
            assertThatThrownBy(() -> questionnaireManagementRecordCommandService
                    .createQuestionnaireManagementRecord(record))
                    .isInstanceOf(EntityNotFoundPersException.class);

            verify(resultFormatter).throwEntityNotFound(
                    ErrorCode.QUESTIONNAIRE_NOT_FOUND_BY_REF,
                    "user.questionnaire_management.not_found",
                    1L);
            verify(questionnaireManagementRecordCommandRepository, never()).save(any());
        }

        @Test
        @DisplayName("Debe lanzar EntityAlreadyExists cuando el cuestionario ya está asignado")
        void should_throwEntityAlreadyExists_when_questionnaireAlreadyAssigned() {
            // Arrange
            QuestionnaireManagementRecord record = buildQuestionnaireManagementRecord();
            BatteryManagementRecord batteryRecord = record.getBatteryManagementRecord();
            Questionnaire questionnaire = record.getQuestionnaire();

            when(batteryManagementRecordQueryRepository.getBatteryManagementRecordById(1L))
                    .thenReturn(Optional.of(batteryRecord));
            when(questionnaireQueryRepository.getById(1L))
                    .thenReturn(Optional.of(questionnaire));
            when(questionnaireManagementRecordQueryRepository
                    .existsByBatteryManagementRecordIdAndQuestionnaireId(1L, 1L))
                    .thenReturn(true);
            doThrow(new EntityAlreadyExistsException(
                    ErrorCode.QUESTIONNAIRE_ALREADY_ASSIGNED.getCode(),
                    ErrorCode.QUESTIONNAIRE_ALREADY_ASSIGNED.getMessageKey(),
                    "user.questionnaire_management.already_assigned",
                    new Object[]{"Intralaboral Forma A"}))
                .when(resultFormatter).throwEntityAlreadyExists(
                    any(ErrorCode.class), anyString(), any());

            // Act & Assert
            assertThatThrownBy(() -> questionnaireManagementRecordCommandService
                    .createQuestionnaireManagementRecord(record))
                    .isInstanceOf(EntityAlreadyExistsException.class);

            verify(resultFormatter).throwEntityAlreadyExists(
                    ErrorCode.QUESTIONNAIRE_ALREADY_ASSIGNED,
                    "user.questionnaire_management.already_assigned",
                    "Intralaboral Forma A");
            verify(questionnaireManagementRecordCommandRepository, never()).save(any());
        }

        @Test
        @DisplayName("Debe lanzar EntityNotFound cuando el estado inicial 'Creado' no existe en catálogo")
        void should_throwEntityNotFound_when_initialStatusNotFound() {
            // Arrange
            QuestionnaireManagementRecord record = buildQuestionnaireManagementRecord();
            BatteryManagementRecord batteryRecord = record.getBatteryManagementRecord();
            Questionnaire questionnaire = record.getQuestionnaire();
            String creadoName = QuestionnaireManagementRecordStatusEnum.CREADO.getName();

            when(batteryManagementRecordQueryRepository.getBatteryManagementRecordById(1L))
                    .thenReturn(Optional.of(batteryRecord));
            when(questionnaireQueryRepository.getById(1L))
                    .thenReturn(Optional.of(questionnaire));
            when(questionnaireManagementRecordQueryRepository
                    .existsByBatteryManagementRecordIdAndQuestionnaireId(1L, 1L))
                    .thenReturn(false);
            when(questionnaireManagementRecordStatusQueryRepository
                    .getQuestionnaireManagementRecordStatusByName(creadoName))
                    .thenReturn(Optional.empty());
            doThrow(new EntityNotFoundPersException(
                    ErrorCode.QUESTIONNAIRE_MGMT_STATUS_NOT_FOUND.getCode(),
                    ErrorCode.QUESTIONNAIRE_MGMT_STATUS_NOT_FOUND.getMessageKey(),
                    "user.questionnaire_management.config_error",
                    new Object[]{creadoName}))
                .when(resultFormatter).throwEntityNotFound(
                    any(ErrorCode.class), anyString(), any());

            // Act & Assert
            assertThatThrownBy(() -> questionnaireManagementRecordCommandService
                    .createQuestionnaireManagementRecord(record))
                    .isInstanceOf(EntityNotFoundPersException.class);

            verify(resultFormatter).throwEntityNotFound(
                    ErrorCode.QUESTIONNAIRE_MGMT_STATUS_NOT_FOUND,
                    "user.questionnaire_management.config_error",
                    creadoName);
            verify(questionnaireManagementRecordCommandRepository, never()).save(any());
        }
    }

    // ==================================================================================
    // deleteQuestionnaireManagementRecord
    // ==================================================================================

    @Nested
    @DisplayName("deleteQuestionnaireManagementRecord")
    class DeleteQuestionnaireManagementRecord {

        @Test
        @DisplayName("Debe eliminar y sincronizar a Diligenciado cuando los 3 cuestionarios están completos (EXT+EST+ILA)")
        void should_deleteRecordAndSyncToBatteryComplete_when_allQuestionnairesCompleted() {
            // Arrange
            QuestionnaireManagementRecord record = buildQuestionnaireManagementRecord();
            String inProcessingName = BatteryManagementRecordStatusCode.IN_PROCESSING.getDescription();
            String completedName = BatteryManagementRecordStatusCode.COMPLETED.getDescription();
            String diligenciadoQName = QuestionnaireManagementRecordStatusEnum.DILIGENCIADO.getName();

            BatteryManagementRecord batteryRecord = BatteryManagementRecord.builder()
                    .id(1L)
                    .status(buildBatteryStatus(2L, BatteryManagementRecordStatusCode.IN_PROCESSING))
                    .build();
            BatteryManagementRecordStatus completedStatus = buildBatteryStatus(3L, BatteryManagementRecordStatusCode.COMPLETED);

            when(questionnaireManagementRecordQueryRepository.findByIdWithAll(1L))
                    .thenReturn(Optional.of(record));
            when(questionnaireManagementRecordQueryRepository
                    .findAbbreviationsByBatteryIdAndStatusName(1L, diligenciadoQName))
                    .thenReturn(List.of(
                            QuestionnaireEnum.EXT.getAbbreviation(),
                            QuestionnaireEnum.EST.getAbbreviation(),
                            QuestionnaireEnum.ILA.getAbbreviation()));
            when(batteryManagementRecordQueryRepository.getBatteryManagementRecordById(1L))
                    .thenReturn(Optional.of(batteryRecord));
            when(batteryManagementRecordStatusQueryRepository.getStatusByName(completedName))
                    .thenReturn(Optional.of(completedStatus));

            // Act
            questionnaireManagementRecordCommandService.deleteQuestionnaireManagementRecord(1L);

            // Assert
            verify(questionnaireResponseCommandRepository)
                    .deleteByQuestionnaireManagementRecordId(1L);
            verify(questionnaireManagementRecordCommandRepository).deleteById(1L);
            verify(batteryManagementRecordCommandRepository)
                    .updateBatteryManagementRecord(any(BatteryManagementRecord.class));
        }

        @Test
        @DisplayName("Debe eliminar y sincronizar a En diligenciamiento cuando la batería está incompleta")
        void should_deleteRecordAndSyncToInProcessing_when_batteryIncomplete() {
            // Arrange
            QuestionnaireManagementRecord record = buildQuestionnaireManagementRecord();
            String completedName = BatteryManagementRecordStatusCode.COMPLETED.getDescription();
            String inProcessingName = BatteryManagementRecordStatusCode.IN_PROCESSING.getDescription();
            String diligenciadoQName = QuestionnaireManagementRecordStatusEnum.DILIGENCIADO.getName();

            BatteryManagementRecord batteryRecord = BatteryManagementRecord.builder()
                    .id(1L)
                    .status(buildBatteryStatus(3L, BatteryManagementRecordStatusCode.COMPLETED))
                    .build();
            BatteryManagementRecordStatus inProcessingStatus = buildBatteryStatus(2L, BatteryManagementRecordStatusCode.IN_PROCESSING);

            when(questionnaireManagementRecordQueryRepository.findByIdWithAll(1L))
                    .thenReturn(Optional.of(record));
            when(questionnaireManagementRecordQueryRepository
                    .findAbbreviationsByBatteryIdAndStatusName(1L, diligenciadoQName))
                    .thenReturn(List.of(QuestionnaireEnum.EXT.getAbbreviation()));
            when(batteryManagementRecordQueryRepository.getBatteryManagementRecordById(1L))
                    .thenReturn(Optional.of(batteryRecord));
            when(batteryManagementRecordStatusQueryRepository.getStatusByName(inProcessingName))
                    .thenReturn(Optional.of(inProcessingStatus));

            // Act
            questionnaireManagementRecordCommandService.deleteQuestionnaireManagementRecord(1L);

            // Assert
            verify(batteryManagementRecordCommandRepository)
                    .updateBatteryManagementRecord(any(BatteryManagementRecord.class));
        }

        @Test
        @DisplayName("Debe sincronizar a Diligenciado cuando la batería tiene ILB en lugar de ILA")
        void should_deleteRecordAndSyncToComplete_when_batteryHasILBInsteadOfILA() {
            // Arrange — cubre hasIlb=true en (hasIla || hasIlb)
            QuestionnaireManagementRecord record = buildQuestionnaireManagementRecord();
            String inProcessingName = BatteryManagementRecordStatusCode.IN_PROCESSING.getDescription();
            String completedName = BatteryManagementRecordStatusCode.COMPLETED.getDescription();
            String diligenciadoQName = QuestionnaireManagementRecordStatusEnum.DILIGENCIADO.getName();

            BatteryManagementRecord batteryRecord = BatteryManagementRecord.builder()
                    .id(1L)
                    .status(buildBatteryStatus(2L, BatteryManagementRecordStatusCode.IN_PROCESSING))
                    .build();
            BatteryManagementRecordStatus completedStatus = buildBatteryStatus(3L, BatteryManagementRecordStatusCode.COMPLETED);

            when(questionnaireManagementRecordQueryRepository.findByIdWithAll(1L))
                    .thenReturn(Optional.of(record));
            when(questionnaireManagementRecordQueryRepository
                    .findAbbreviationsByBatteryIdAndStatusName(1L, diligenciadoQName))
                    .thenReturn(List.of(
                            QuestionnaireEnum.EXT.getAbbreviation(),
                            QuestionnaireEnum.EST.getAbbreviation(),
                            QuestionnaireEnum.ILB.getAbbreviation()));
            when(batteryManagementRecordQueryRepository.getBatteryManagementRecordById(1L))
                    .thenReturn(Optional.of(batteryRecord));
            when(batteryManagementRecordStatusQueryRepository.getStatusByName(completedName))
                    .thenReturn(Optional.of(completedStatus));

            // Act
            questionnaireManagementRecordCommandService.deleteQuestionnaireManagementRecord(1L);

            // Assert
            verify(questionnaireManagementRecordCommandRepository).deleteById(1L);
            verify(batteryManagementRecordCommandRepository)
                    .updateBatteryManagementRecord(any(BatteryManagementRecord.class));
        }

        @Test
        @DisplayName("Debe sincronizar a En diligenciamiento cuando no hay cuestionarios completados")
        void should_deleteRecordAndSyncToInProcessing_when_noCompletedQuestionnaires() {
            // Arrange — cubre hasExt=false (lista vacía, short-circuit en primer término)
            QuestionnaireManagementRecord record = buildQuestionnaireManagementRecord();
            String completedName = BatteryManagementRecordStatusCode.COMPLETED.getDescription();
            String inProcessingName = BatteryManagementRecordStatusCode.IN_PROCESSING.getDescription();
            String diligenciadoQName = QuestionnaireManagementRecordStatusEnum.DILIGENCIADO.getName();

            BatteryManagementRecord batteryRecord = BatteryManagementRecord.builder()
                    .id(1L)
                    .status(buildBatteryStatus(3L, BatteryManagementRecordStatusCode.COMPLETED))
                    .build();
            BatteryManagementRecordStatus inProcessingStatus = buildBatteryStatus(2L, BatteryManagementRecordStatusCode.IN_PROCESSING);

            when(questionnaireManagementRecordQueryRepository.findByIdWithAll(1L))
                    .thenReturn(Optional.of(record));
            when(questionnaireManagementRecordQueryRepository
                    .findAbbreviationsByBatteryIdAndStatusName(1L, diligenciadoQName))
                    .thenReturn(List.of());
            when(batteryManagementRecordQueryRepository.getBatteryManagementRecordById(1L))
                    .thenReturn(Optional.of(batteryRecord));
            when(batteryManagementRecordStatusQueryRepository.getStatusByName(inProcessingName))
                    .thenReturn(Optional.of(inProcessingStatus));

            // Act
            questionnaireManagementRecordCommandService.deleteQuestionnaireManagementRecord(1L);

            // Assert
            verify(questionnaireManagementRecordCommandRepository).deleteById(1L);
            verify(batteryManagementRecordCommandRepository)
                    .updateBatteryManagementRecord(any(BatteryManagementRecord.class));
        }

        @Test
        @DisplayName("Debe sincronizar a En diligenciamiento cuando falta el cuestionario de estrés")
        void should_deleteRecordAndSyncToInProcessing_when_missingStressQuestionnaire() {
            // Arrange — cubre hasEst=false cuando hasExt=true (short-circuit en segundo término)
            QuestionnaireManagementRecord record = buildQuestionnaireManagementRecord();
            String completedName = BatteryManagementRecordStatusCode.COMPLETED.getDescription();
            String inProcessingName = BatteryManagementRecordStatusCode.IN_PROCESSING.getDescription();
            String diligenciadoQName = QuestionnaireManagementRecordStatusEnum.DILIGENCIADO.getName();

            BatteryManagementRecord batteryRecord = BatteryManagementRecord.builder()
                    .id(1L)
                    .status(buildBatteryStatus(3L, BatteryManagementRecordStatusCode.COMPLETED))
                    .build();
            BatteryManagementRecordStatus inProcessingStatus = buildBatteryStatus(2L, BatteryManagementRecordStatusCode.IN_PROCESSING);

            when(questionnaireManagementRecordQueryRepository.findByIdWithAll(1L))
                    .thenReturn(Optional.of(record));
            when(questionnaireManagementRecordQueryRepository
                    .findAbbreviationsByBatteryIdAndStatusName(1L, diligenciadoQName))
                    .thenReturn(List.of(
                            QuestionnaireEnum.EXT.getAbbreviation(),
                            QuestionnaireEnum.ILA.getAbbreviation()));
            when(batteryManagementRecordQueryRepository.getBatteryManagementRecordById(1L))
                    .thenReturn(Optional.of(batteryRecord));
            when(batteryManagementRecordStatusQueryRepository.getStatusByName(inProcessingName))
                    .thenReturn(Optional.of(inProcessingStatus));

            // Act
            questionnaireManagementRecordCommandService.deleteQuestionnaireManagementRecord(1L);

            // Assert
            verify(questionnaireManagementRecordCommandRepository).deleteById(1L);
            verify(batteryManagementRecordCommandRepository)
                    .updateBatteryManagementRecord(any(BatteryManagementRecord.class));
        }

        @Test
        @DisplayName("Debe sincronizar a En diligenciamiento cuando tiene EXT+EST pero ningún intralaboral")
        void should_deleteRecordAndSyncToInProcessing_when_noIntralaboralQuestionnaire() {
            // Arrange — cubre hasIla=false && hasIlb=false (rama del OR completa en false)
            QuestionnaireManagementRecord record = buildQuestionnaireManagementRecord();
            String inProcessingName = BatteryManagementRecordStatusCode.IN_PROCESSING.getDescription();
            String diligenciadoQName = QuestionnaireManagementRecordStatusEnum.DILIGENCIADO.getName();

            BatteryManagementRecord batteryRecord = BatteryManagementRecord.builder()
                    .id(1L)
                    .status(buildBatteryStatus(3L, BatteryManagementRecordStatusCode.COMPLETED))
                    .build();
            BatteryManagementRecordStatus inProcessingStatus = buildBatteryStatus(2L, BatteryManagementRecordStatusCode.IN_PROCESSING);

            when(questionnaireManagementRecordQueryRepository.findByIdWithAll(1L))
                    .thenReturn(Optional.of(record));
            when(questionnaireManagementRecordQueryRepository
                    .findAbbreviationsByBatteryIdAndStatusName(1L, diligenciadoQName))
                    .thenReturn(List.of(
                            QuestionnaireEnum.EXT.getAbbreviation(),
                            QuestionnaireEnum.EST.getAbbreviation()));
            when(batteryManagementRecordQueryRepository.getBatteryManagementRecordById(1L))
                    .thenReturn(Optional.of(batteryRecord));
            when(batteryManagementRecordStatusQueryRepository.getStatusByName(inProcessingName))
                    .thenReturn(Optional.of(inProcessingStatus));

            // Act
            questionnaireManagementRecordCommandService.deleteQuestionnaireManagementRecord(1L);

            // Assert
            verify(questionnaireManagementRecordCommandRepository).deleteById(1L);
            verify(batteryManagementRecordCommandRepository)
                    .updateBatteryManagementRecord(any(BatteryManagementRecord.class));
        }

        @Test
        @DisplayName("Debe sincronizar a En diligenciamiento cuando tiene 4 cuestionarios completados (size!=3)")
        void should_deleteRecordAndSyncToInProcessing_when_fourQuestionnairesCompleted() {
            // Arrange — cubre size!=3 cuando hasExt && hasEst && (hasIla||hasIlb) son true
            QuestionnaireManagementRecord record = buildQuestionnaireManagementRecord();
            String inProcessingName = BatteryManagementRecordStatusCode.IN_PROCESSING.getDescription();
            String diligenciadoQName = QuestionnaireManagementRecordStatusEnum.DILIGENCIADO.getName();

            BatteryManagementRecord batteryRecord = BatteryManagementRecord.builder()
                    .id(1L)
                    .status(buildBatteryStatus(3L, BatteryManagementRecordStatusCode.COMPLETED))
                    .build();
            BatteryManagementRecordStatus inProcessingStatus = buildBatteryStatus(2L, BatteryManagementRecordStatusCode.IN_PROCESSING);

            when(questionnaireManagementRecordQueryRepository.findByIdWithAll(1L))
                    .thenReturn(Optional.of(record));
            when(questionnaireManagementRecordQueryRepository
                    .findAbbreviationsByBatteryIdAndStatusName(1L, diligenciadoQName))
                    .thenReturn(List.of(
                            QuestionnaireEnum.EXT.getAbbreviation(),
                            QuestionnaireEnum.EST.getAbbreviation(),
                            QuestionnaireEnum.ILA.getAbbreviation(),
                            QuestionnaireEnum.ILB.getAbbreviation()));
            when(batteryManagementRecordQueryRepository.getBatteryManagementRecordById(1L))
                    .thenReturn(Optional.of(batteryRecord));
            when(batteryManagementRecordStatusQueryRepository.getStatusByName(inProcessingName))
                    .thenReturn(Optional.of(inProcessingStatus));

            // Act
            questionnaireManagementRecordCommandService.deleteQuestionnaireManagementRecord(1L);

            // Assert
            verify(questionnaireManagementRecordCommandRepository).deleteById(1L);
            verify(batteryManagementRecordCommandRepository)
                    .updateBatteryManagementRecord(any(BatteryManagementRecord.class));
        }

        @Test
        @DisplayName("Debe eliminar sin actualizar batería cuando el estado ya coincide con el objetivo")
        void should_deleteRecordAndNotUpdateBattery_when_statusAlreadyMatchesTarget() {
            // Arrange
            QuestionnaireManagementRecord record = buildQuestionnaireManagementRecord();
            String inProcessingName = BatteryManagementRecordStatusCode.IN_PROCESSING.getDescription();
            String diligenciadoQName = QuestionnaireManagementRecordStatusEnum.DILIGENCIADO.getName();

            BatteryManagementRecord batteryRecord = BatteryManagementRecord.builder()
                    .id(1L)
                    .status(buildBatteryStatus(2L, BatteryManagementRecordStatusCode.IN_PROCESSING))
                    .build();

            when(questionnaireManagementRecordQueryRepository.findByIdWithAll(1L))
                    .thenReturn(Optional.of(record));
            when(questionnaireManagementRecordQueryRepository
                    .findAbbreviationsByBatteryIdAndStatusName(1L, diligenciadoQName))
                    .thenReturn(List.of(QuestionnaireEnum.EXT.getAbbreviation()));
            when(batteryManagementRecordQueryRepository.getBatteryManagementRecordById(1L))
                    .thenReturn(Optional.of(batteryRecord));

            // Act
            questionnaireManagementRecordCommandService.deleteQuestionnaireManagementRecord(1L);

            // Assert
            verify(questionnaireManagementRecordCommandRepository).deleteById(1L);
            verify(batteryManagementRecordCommandRepository, never())
                    .updateBatteryManagementRecord(any());
        }

        @Test
        @DisplayName("Debe lanzar EntityNotFound cuando el registro a eliminar no existe")
        void should_throwEntityNotFound_when_recordNotFoundOnDelete() {
            // Arrange
            when(questionnaireManagementRecordQueryRepository.findByIdWithAll(1L))
                    .thenReturn(Optional.empty());
            doThrow(new EntityNotFoundPersException(
                    ErrorCode.QUESTIONNAIRE_MGMT_RECORD_NOT_FOUND.getCode(),
                    ErrorCode.QUESTIONNAIRE_MGMT_RECORD_NOT_FOUND.getMessageKey(),
                    "user.questionnaire_management.delete_not_found",
                    new Object[]{1L}))
                .when(resultFormatter).throwEntityNotFound(
                    any(ErrorCode.class), anyString(), any());

            // Act & Assert
            assertThatThrownBy(() -> questionnaireManagementRecordCommandService
                    .deleteQuestionnaireManagementRecord(1L))
                    .isInstanceOf(EntityNotFoundPersException.class);

            verify(resultFormatter).throwEntityNotFound(
                    ErrorCode.QUESTIONNAIRE_MGMT_RECORD_NOT_FOUND,
                    "user.questionnaire_management.delete_not_found",
                    1L);
            verify(questionnaireManagementRecordCommandRepository, never()).deleteById(anyLong());
        }

        @Test
        @DisplayName("Debe lanzar BusinessRuleViolation cuando el cuestionario está en estado Cerrado")
        void should_throwBusinessRuleViolation_when_statusIsClosed() {
            // Arrange
            String cerradoName = QuestionnaireManagementRecordStatusEnum.CERRADO.getName();
            QuestionnaireManagementRecord record = buildQuestionnaireManagementRecord();
            record.setStatus(QuestionnaireManagementRecordStatus.builder()
                    .id(4L).name(cerradoName).build());

            when(questionnaireManagementRecordQueryRepository.findByIdWithAll(1L))
                    .thenReturn(Optional.of(record));
            doThrow(new BusinessRuleViolationException(
                    ErrorCode.QUESTIONNAIRE_RECORD_DELETE_NOT_ALLOWED.getCode(),
                    ErrorCode.QUESTIONNAIRE_RECORD_DELETE_NOT_ALLOWED.getMessageKey(),
                    "user.questionnaire_management.delete_not_allowed",
                    new Object[]{cerradoName, 1L}))
                .when(resultFormatter).throwBusinessRuleViolation(
                    any(ErrorCode.class), anyString(), any(), any());

            // Act & Assert
            assertThatThrownBy(() -> questionnaireManagementRecordCommandService
                    .deleteQuestionnaireManagementRecord(1L))
                    .isInstanceOf(BusinessRuleViolationException.class);

            verify(resultFormatter).throwBusinessRuleViolation(
                    ErrorCode.QUESTIONNAIRE_RECORD_DELETE_NOT_ALLOWED,
                    "user.questionnaire_management.delete_not_allowed",
                    cerradoName, 1L);
            verify(questionnaireManagementRecordCommandRepository, never()).deleteById(anyLong());
        }

        @Test
        @DisplayName("Debe lanzar EntityNotFound cuando la batería no se encuentra durante la sincronización")
        void should_throwEntityNotFound_when_batteryNotFoundOnSync() {
            // Arrange
            QuestionnaireManagementRecord record = buildQuestionnaireManagementRecord();
            String diligenciadoQName = QuestionnaireManagementRecordStatusEnum.DILIGENCIADO.getName();

            when(questionnaireManagementRecordQueryRepository.findByIdWithAll(1L))
                    .thenReturn(Optional.of(record));
            when(questionnaireManagementRecordQueryRepository
                    .findAbbreviationsByBatteryIdAndStatusName(1L, diligenciadoQName))
                    .thenReturn(List.of());
            when(batteryManagementRecordQueryRepository.getBatteryManagementRecordById(1L))
                    .thenReturn(Optional.empty());
            doThrow(new EntityNotFoundPersException(
                    ErrorCode.BATTERY_RECORD_NOT_FOUND.getCode(),
                    ErrorCode.BATTERY_RECORD_NOT_FOUND.getMessageKey(),
                    "user.questionnaire_management.sync_battery_failed",
                    new Object[]{1L}))
                .when(resultFormatter).throwEntityNotFound(
                    any(ErrorCode.class), anyString(), any());

            // Act & Assert
            assertThatThrownBy(() -> questionnaireManagementRecordCommandService
                    .deleteQuestionnaireManagementRecord(1L))
                    .isInstanceOf(EntityNotFoundPersException.class);

            verify(resultFormatter).throwEntityNotFound(
                    ErrorCode.BATTERY_RECORD_NOT_FOUND,
                    "user.questionnaire_management.sync_battery_failed",
                    1L);
            verify(questionnaireManagementRecordCommandRepository).deleteById(1L);
        }

        @Test
        @DisplayName("Debe lanzar EntityNotFound cuando el estado de batería no se encuentra durante la sincronización")
        void should_throwEntityNotFound_when_batteryStatusNotFoundOnSync() {
            // Arrange
            QuestionnaireManagementRecord record = buildQuestionnaireManagementRecord();
            String completedName = BatteryManagementRecordStatusCode.COMPLETED.getDescription();
            String inProcessingName = BatteryManagementRecordStatusCode.IN_PROCESSING.getDescription();
            String diligenciadoQName = QuestionnaireManagementRecordStatusEnum.DILIGENCIADO.getName();

            BatteryManagementRecord batteryRecord = BatteryManagementRecord.builder()
                    .id(1L)
                    .status(buildBatteryStatus(3L, BatteryManagementRecordStatusCode.COMPLETED))
                    .build();

            when(questionnaireManagementRecordQueryRepository.findByIdWithAll(1L))
                    .thenReturn(Optional.of(record));
            when(questionnaireManagementRecordQueryRepository
                    .findAbbreviationsByBatteryIdAndStatusName(1L, diligenciadoQName))
                    .thenReturn(List.of(QuestionnaireEnum.EXT.getAbbreviation()));
            when(batteryManagementRecordQueryRepository.getBatteryManagementRecordById(1L))
                    .thenReturn(Optional.of(batteryRecord));
            when(batteryManagementRecordStatusQueryRepository.getStatusByName(inProcessingName))
                    .thenReturn(Optional.empty());
            doThrow(new EntityNotFoundPersException(
                    ErrorCode.BATTERY_STATUS_NOT_FOUND.getCode(),
                    ErrorCode.BATTERY_STATUS_NOT_FOUND.getMessageKey(),
                    "user.questionnaire_management.sync_status_not_found",
                    new Object[]{inProcessingName}))
                .when(resultFormatter).throwEntityNotFound(
                    any(ErrorCode.class), anyString(), any());

            // Act & Assert
            assertThatThrownBy(() -> questionnaireManagementRecordCommandService
                    .deleteQuestionnaireManagementRecord(1L))
                    .isInstanceOf(EntityNotFoundPersException.class);

            verify(resultFormatter).throwEntityNotFound(
                    ErrorCode.BATTERY_STATUS_NOT_FOUND,
                    "user.questionnaire_management.sync_status_not_found",
                    inProcessingName);
            verify(questionnaireManagementRecordCommandRepository).deleteById(1L);
        }
    }
}

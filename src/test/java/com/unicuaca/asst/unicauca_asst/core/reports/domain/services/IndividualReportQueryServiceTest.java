package com.unicuaca.asst.unicauca_asst.core.reports.domain.services;

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
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.models.BatteryManagementRecord;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.models.BatteryManagementRecordStatus;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.models.PersonEvaluatedDetails;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.models.Questionnaire;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.models.QuestionnaireManagementRecord;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.models.QuestionnaireResponse;
import com.unicuaca.asst.unicauca_asst.core.catalog.domain.models.JobPositionType;
import com.unicuaca.asst.unicauca_asst.core.reports.domain.models.AnalysisSpace;
import com.unicuaca.asst.unicauca_asst.core.reports.domain.models.Evaluator;
import com.unicuaca.asst.unicauca_asst.core.reports.domain.ports.output.AnalysisSpaceQueryRepository;
import com.unicuaca.asst.unicauca_asst.core.reports.domain.ports.output.EvaluatorQueryRepository;
import com.unicuaca.asst.unicauca_asst.core.reports.domain.ports.output.IndividualReportPdfOutputPort;
import com.unicuaca.asst.unicauca_asst.core.reports.domain.ports.output.ReportDataQueryRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class IndividualReportQueryServiceTest {

    @Mock
    private ReportDataQueryRepository reportDataQueryRepository;

    @Mock
    private AnalysisSpaceQueryRepository analysisSpaceQueryRepository;

    @Mock
    private EvaluatorQueryRepository evaluatorQueryRepository;

    @Mock
    private IndividualReportPdfOutputPort pdfOutputPort;

    @Mock
    private ScoringEngine scoringEngine;

    @Mock
    private ResultFormatterOutputPort resultFormatter;

    @InjectMocks
    private IndividualReportQueryService individualReportQueryService;

    // ==================================================================================
    // getIndividualReport
    // ==================================================================================

    @Nested
    @DisplayName("getIndividualReport")
    class GetIndividualReport {

        @Test
        @DisplayName("Debe calcular informe individual usando ILA cuando tipo de cargo <= 2")
        void should_calculate_when_jobPositionILA() {
            // Arrange
            Long recordId = 1L;
            BatteryManagementRecord record = buildClosedRecord();
            PersonEvaluatedDetails details = buildPersonDetails(2L);
            when(reportDataQueryRepository.getBatteryRecordById(recordId)).thenReturn(Optional.of(record));
            when(reportDataQueryRepository.getPersonDetailsByBatteryRecordId(recordId)).thenReturn(Optional.of(details));
            when(reportDataQueryRepository.getQuestionnaireRecordsByBatteryId(recordId))
                    .thenReturn(buildAllQuestionnaireRecords());
            when(reportDataQueryRepository.getResponsesByQuestionnaireRecordId(any()))
                    .thenReturn(List.of(buildResponse()));
            ScoringEngine.IndividualScoringResult expected = buildScoringResult();
            when(scoringEngine.calculateIndividualReport(any(), any(), any(), eq("ILA"), eq(2)))
                    .thenReturn(expected);

            // Act
            ScoringEngine.IndividualScoringResult result = individualReportQueryService.getIndividualReport(recordId);

            // Assert
            assertThat(result).isSameAs(expected);
            verify(scoringEngine).calculateIndividualReport(any(), any(), any(), eq("ILA"), eq(2));
        }

        @Test
        @DisplayName("Debe calcular informe individual usando ILB cuando tipo de cargo > 2")
        void should_calculate_when_jobPositionILB() {
            // Arrange
            Long recordId = 1L;
            when(reportDataQueryRepository.getBatteryRecordById(recordId)).thenReturn(Optional.of(buildClosedRecord()));
            when(reportDataQueryRepository.getPersonDetailsByBatteryRecordId(recordId))
                    .thenReturn(Optional.of(buildPersonDetails(3L)));
            when(reportDataQueryRepository.getQuestionnaireRecordsByBatteryId(recordId))
                    .thenReturn(buildQuestionnaireRecords("ILB", "EXT", "EST"));
            when(reportDataQueryRepository.getResponsesByQuestionnaireRecordId(any()))
                    .thenReturn(List.of(buildResponse()));
            when(scoringEngine.calculateIndividualReport(any(), any(), any(), eq("ILB"), eq(3)))
                    .thenReturn(buildScoringResult());

            // Act
            individualReportQueryService.getIndividualReport(recordId);

            // Assert
            verify(scoringEngine).calculateIndividualReport(any(), any(), any(), eq("ILB"), eq(3));
        }

        @Test
        @DisplayName("Debe lanzar EntityNotFound cuando la batería no existe")
        void should_throwEntityNotFound_when_batteryNotFound() {
            // Arrange
            when(reportDataQueryRepository.getBatteryRecordById(1L)).thenReturn(Optional.empty());
            doThrow(new EntityNotFoundPersException(
                    ErrorCode.BATTERY_RECORD_NOT_FOUND.getCode(),
                    ErrorCode.BATTERY_RECORD_NOT_FOUND.getMessageKey(),
                    "user.battery.query_not_found",
                    new Object[]{1L}))
                .when(resultFormatter).throwEntityNotFound(
                    any(ErrorCode.class), anyString(), any());

            // Act & Assert
            assertThatThrownBy(() -> individualReportQueryService.getIndividualReport(1L))
                    .isInstanceOf(EntityNotFoundPersException.class);

            verify(scoringEngine, never()).calculateIndividualReport(any(), any(), any(), any(), any(Integer.class));
        }

        @Test
        @DisplayName("Debe lanzar BusinessRuleViolation cuando la batería no está cerrada")
        void should_throwBusinessRule_when_batteryNotClosed() {
            // Arrange
            BatteryManagementRecord record = buildRecordWithStatus("Diligenciado");
            when(reportDataQueryRepository.getBatteryRecordById(1L)).thenReturn(Optional.of(record));
            doThrow(new BusinessRuleViolationException(
                    ErrorCode.REPORT_BATTERY_NOT_CLOSED.getCode(),
                    ErrorCode.REPORT_BATTERY_NOT_CLOSED.getMessageKey(),
                    "user.report.battery_not_closed",
                    new Object[]{1L}))
                .when(resultFormatter).throwBusinessRuleViolation(
                    any(ErrorCode.class), anyString(), any());

            // Act & Assert
            assertThatThrownBy(() -> individualReportQueryService.getIndividualReport(1L))
                    .isInstanceOf(BusinessRuleViolationException.class);
        }

        @Test
        @DisplayName("Debe lanzar EntityNotFound cuando los detalles de la persona no existen")
        void should_throwEntityNotFound_when_personDetailsNotFound() {
            // Arrange
            when(reportDataQueryRepository.getBatteryRecordById(1L)).thenReturn(Optional.of(buildClosedRecord()));
            when(reportDataQueryRepository.getPersonDetailsByBatteryRecordId(1L)).thenReturn(Optional.empty());
            doThrow(new EntityNotFoundPersException(
                    ErrorCode.PERSON_DETAILS_NOT_FOUND.getCode(),
                    ErrorCode.PERSON_DETAILS_NOT_FOUND.getMessageKey(),
                    "user.person_evaluated_details.query_by_record_not_found",
                    new Object[]{1L}))
                .when(resultFormatter).throwEntityNotFound(
                    any(ErrorCode.class), anyString(), any());

            // Act & Assert
            assertThatThrownBy(() -> individualReportQueryService.getIndividualReport(1L))
                    .isInstanceOf(EntityNotFoundPersException.class);
        }

        @Test
        @DisplayName("Debe lanzar BusinessRuleViolation cuando falta un cuestionario")
        void should_throwBusinessRule_when_questionnaireMissing() {
            // Arrange
            Long recordId = 1L;
            when(reportDataQueryRepository.getBatteryRecordById(recordId)).thenReturn(Optional.of(buildClosedRecord()));
            when(reportDataQueryRepository.getPersonDetailsByBatteryRecordId(recordId))
                    .thenReturn(Optional.of(buildPersonDetails(2L)));
            // Falta "ILA" en la lista
            when(reportDataQueryRepository.getQuestionnaireRecordsByBatteryId(recordId))
                    .thenReturn(buildQuestionnaireRecords("EXT", "EST"));
            doThrow(new BusinessRuleViolationException(
                    ErrorCode.REPORT_INCOMPLETE_BATTERY.getCode(),
                    ErrorCode.REPORT_INCOMPLETE_BATTERY.getMessageKey(),
                    "user.report.incomplete_battery",
                    new Object[]{recordId, "ILA"}))
                .when(resultFormatter).throwBusinessRuleViolation(
                    any(ErrorCode.class), anyString(), any(), any());

            // Act & Assert
            assertThatThrownBy(() -> individualReportQueryService.getIndividualReport(recordId))
                    .isInstanceOf(BusinessRuleViolationException.class);
        }

        @Test
        @DisplayName("Debe lanzar BusinessRuleViolation cuando un cuestionario no tiene respuestas")
        void should_throwBusinessRule_when_questionnaireHasNoResponses() {
            // Arrange
            Long recordId = 1L;
            when(reportDataQueryRepository.getBatteryRecordById(recordId)).thenReturn(Optional.of(buildClosedRecord()));
            when(reportDataQueryRepository.getPersonDetailsByBatteryRecordId(recordId))
                    .thenReturn(Optional.of(buildPersonDetails(2L)));
            when(reportDataQueryRepository.getQuestionnaireRecordsByBatteryId(recordId))
                    .thenReturn(buildAllQuestionnaireRecords());
            when(reportDataQueryRepository.getResponsesByQuestionnaireRecordId(any()))
                    .thenReturn(List.of());
            doThrow(new BusinessRuleViolationException(
                    ErrorCode.REPORT_INCOMPLETE_BATTERY.getCode(),
                    ErrorCode.REPORT_INCOMPLETE_BATTERY.getMessageKey(),
                    "user.report.incomplete_battery",
                    new Object[]{recordId, "ILA"}))
                .when(resultFormatter).throwBusinessRuleViolation(
                    any(ErrorCode.class), anyString(), any(), any());

            // Act & Assert
            assertThatThrownBy(() -> individualReportQueryService.getIndividualReport(recordId))
                    .isInstanceOf(BusinessRuleViolationException.class);
        }
    }

    // ==================================================================================
    // getIndividualReportPdf
    // ==================================================================================

    @Nested
    @DisplayName("getIndividualReportPdf")
    class GetIndividualReportPdf {

        @Test
        @DisplayName("Debe generar PDF cuando espacio, evaluador y batería son válidos")
        void should_generatePdf_when_allValid() {
            // Arrange
            Long recordId = 1L;
            Long spaceId = 10L;
            Long userId = 100L;
            Long evaluatorId = 5L;

            AnalysisSpace space = AnalysisSpace.builder()
                    .id(spaceId).creatorUserId(userId).evaluatorId(evaluatorId).build();
            Evaluator evaluator = Evaluator.builder().id(evaluatorId).build();
            byte[] pdfBytes = new byte[]{1, 2, 3};

            when(analysisSpaceQueryRepository.findByIdWithBatteries(spaceId)).thenReturn(Optional.of(space));
            when(evaluatorQueryRepository.findById(evaluatorId)).thenReturn(Optional.of(evaluator));
            when(reportDataQueryRepository.getBatteryRecordById(recordId)).thenReturn(Optional.of(buildClosedRecord()));
            when(reportDataQueryRepository.getPersonDetailsByBatteryRecordId(recordId))
                    .thenReturn(Optional.of(buildPersonDetails(2L)));
            when(reportDataQueryRepository.getQuestionnaireRecordsByBatteryId(recordId))
                    .thenReturn(buildAllQuestionnaireRecords());
            when(reportDataQueryRepository.getResponsesByQuestionnaireRecordId(any()))
                    .thenReturn(List.of(buildResponse()));
            when(scoringEngine.calculateIndividualReport(any(), any(), any(), eq("ILA"), eq(2)))
                    .thenReturn(buildScoringResult());
            when(pdfOutputPort.generatePdf(any(), any(), any(), any(), eq("ILA")))
                    .thenReturn(pdfBytes);

            // Act
            byte[] result = individualReportQueryService.getIndividualReportPdf(recordId, spaceId, userId);

            // Assert
            assertThat(result).isEqualTo(pdfBytes);
            verify(pdfOutputPort).generatePdf(any(), any(), any(), eq(evaluator), eq("ILA"));
        }

        @Test
        @DisplayName("Debe generar PDF usando ILB cuando tipo de cargo > 2")
        void should_generatePdf_when_jobPositionILB() {
            // Arrange
            Long recordId = 1L;
            Long spaceId = 10L;
            Long userId = 100L;
            Long evaluatorId = 5L;

            AnalysisSpace space = AnalysisSpace.builder()
                    .id(spaceId).creatorUserId(userId).evaluatorId(evaluatorId).build();
            Evaluator evaluator = Evaluator.builder().id(evaluatorId).build();
            byte[] pdfBytes = new byte[]{9, 9, 9};

            when(analysisSpaceQueryRepository.findByIdWithBatteries(spaceId)).thenReturn(Optional.of(space));
            when(evaluatorQueryRepository.findById(evaluatorId)).thenReturn(Optional.of(evaluator));
            when(reportDataQueryRepository.getBatteryRecordById(recordId)).thenReturn(Optional.of(buildClosedRecord()));
            when(reportDataQueryRepository.getPersonDetailsByBatteryRecordId(recordId))
                    .thenReturn(Optional.of(buildPersonDetails(3L)));
            when(reportDataQueryRepository.getQuestionnaireRecordsByBatteryId(recordId))
                    .thenReturn(buildQuestionnaireRecords("ILB", "EXT", "EST"));
            when(reportDataQueryRepository.getResponsesByQuestionnaireRecordId(any()))
                    .thenReturn(List.of(buildResponse()));
            when(scoringEngine.calculateIndividualReport(any(), any(), any(), eq("ILB"), eq(3)))
                    .thenReturn(buildScoringResult());
            when(pdfOutputPort.generatePdf(any(), any(), any(), any(), eq("ILB")))
                    .thenReturn(pdfBytes);

            // Act
            byte[] result = individualReportQueryService.getIndividualReportPdf(recordId, spaceId, userId);

            // Assert
            assertThat(result).isEqualTo(pdfBytes);
            verify(pdfOutputPort).generatePdf(any(), any(), any(), eq(evaluator), eq("ILB"));
        }

        @Test
        @DisplayName("Debe lanzar EntityNotFound cuando el espacio no existe")
        void should_throwEntityNotFound_when_spaceNotFound() {
            // Arrange
            when(analysisSpaceQueryRepository.findByIdWithBatteries(10L)).thenReturn(Optional.empty());
            doThrow(new EntityNotFoundPersException(
                    ErrorCode.ANALYSIS_SPACE_NOT_FOUND.getCode(),
                    ErrorCode.ANALYSIS_SPACE_NOT_FOUND.getMessageKey(),
                    "user.report.space_not_found",
                    new Object[]{10L}))
                .when(resultFormatter).throwEntityNotFound(
                    any(ErrorCode.class), anyString(), any());

            // Act & Assert
            assertThatThrownBy(() -> individualReportQueryService.getIndividualReportPdf(1L, 10L, 100L))
                    .isInstanceOf(EntityNotFoundPersException.class);

            verify(pdfOutputPort, never()).generatePdf(any(), any(), any(), any(), any());
        }

        @Test
        @DisplayName("Debe lanzar BusinessRuleViolation cuando el espacio no pertenece al usuario")
        void should_throwBusinessRule_when_spaceOwnerMismatch() {
            // Arrange
            AnalysisSpace space = AnalysisSpace.builder()
                    .id(10L).creatorUserId(100L).evaluatorId(5L).build();
            when(analysisSpaceQueryRepository.findByIdWithBatteries(10L)).thenReturn(Optional.of(space));
            doThrow(new BusinessRuleViolationException(
                    ErrorCode.ANALYSIS_SPACE_ACCESS_DENIED.getCode(),
                    ErrorCode.ANALYSIS_SPACE_ACCESS_DENIED.getMessageKey(),
                    "user.report.space_access_denied",
                    new Object[]{999L, 10L}))
                .when(resultFormatter).throwBusinessRuleViolation(
                    any(ErrorCode.class), anyString(), any(), any());

            // Act & Assert
            assertThatThrownBy(() -> individualReportQueryService.getIndividualReportPdf(1L, 10L, 999L))
                    .isInstanceOf(BusinessRuleViolationException.class);

            verify(pdfOutputPort, never()).generatePdf(any(), any(), any(), any(), any());
        }

        @Test
        @DisplayName("Debe lanzar EntityNotFound cuando el evaluador del espacio no existe")
        void should_throwEntityNotFound_when_evaluatorNotFound() {
            // Arrange
            AnalysisSpace space = AnalysisSpace.builder()
                    .id(10L).creatorUserId(100L).evaluatorId(5L).build();
            when(analysisSpaceQueryRepository.findByIdWithBatteries(10L)).thenReturn(Optional.of(space));
            when(evaluatorQueryRepository.findById(5L)).thenReturn(Optional.empty());
            doThrow(new EntityNotFoundPersException(
                    ErrorCode.EVALUATOR_NOT_FOUND.getCode(),
                    ErrorCode.EVALUATOR_NOT_FOUND.getMessageKey(),
                    "user.report.evaluator_not_found",
                    new Object[]{5L}))
                .when(resultFormatter).throwEntityNotFound(
                    any(ErrorCode.class), anyString(), any());

            // Act & Assert
            assertThatThrownBy(() -> individualReportQueryService.getIndividualReportPdf(1L, 10L, 100L))
                    .isInstanceOf(EntityNotFoundPersException.class);

            verify(pdfOutputPort, never()).generatePdf(any(), any(), any(), any(), any());
        }
    }

    // ==================================================================================
    // Helpers
    // ==================================================================================

    private BatteryManagementRecord buildClosedRecord() {
        return buildRecordWithStatus("Cerrado");
    }

    private BatteryManagementRecord buildRecordWithStatus(String statusName) {
        return BatteryManagementRecord.builder()
                .id(1L)
                .status(BatteryManagementRecordStatus.builder().id(1L).name(statusName).build())
                .build();
    }

    private PersonEvaluatedDetails buildPersonDetails(Long jobPositionTypeId) {
        JobPositionType type = JobPositionType.builder().id(jobPositionTypeId).name("TIPO").build();
        return PersonEvaluatedDetails.builder()
                .id(1L)
                .jobPositionType(type)
                .build();
    }

    private List<QuestionnaireManagementRecord> buildAllQuestionnaireRecords() {
        return buildQuestionnaireRecords("ILA", "EXT", "EST");
    }

    private List<QuestionnaireManagementRecord> buildQuestionnaireRecords(String... abbreviations) {
        return java.util.Arrays.stream(abbreviations)
                .map(abbr -> QuestionnaireManagementRecord.builder()
                        .id((long) abbr.hashCode())
                        .questionnaire(Questionnaire.builder()
                                .id(1L).abbreviation(abbr).name(abbr).build())
                        .build())
                .toList();
    }

    private QuestionnaireResponse buildResponse() {
        return QuestionnaireResponse.builder().build();
    }

    private ScoringEngine.IndividualScoringResult buildScoringResult() {
        return new ScoringEngine.IndividualScoringResult(null, null, null,
                new ScoringEngine.StressResult(0.0, 0.0, "MUY BAJO"));
    }
}

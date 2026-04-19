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
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.models.PersonEvaluatedDetails;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.models.Questionnaire;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.models.QuestionnaireManagementRecord;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.models.QuestionnaireResponse;
import com.unicuaca.asst.unicauca_asst.core.catalog.domain.models.JobPositionType;
import com.unicuaca.asst.unicauca_asst.core.reports.domain.models.AnalysisSpace;
import com.unicuaca.asst.unicauca_asst.core.reports.domain.models.AnalysisSpaceBattery;
import com.unicuaca.asst.unicauca_asst.core.reports.domain.ports.output.AnalysisSpaceQueryRepository;
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
class GroupReportQueryServiceTest {

    @Mock
    private AnalysisSpaceQueryRepository analysisSpaceQueryRepository;

    @Mock
    private ReportDataQueryRepository reportDataQueryRepository;

    @Mock
    private ScoringEngine scoringEngine;

    @Mock
    private GroupReportEngine groupReportEngine;

    @Mock
    private ResultFormatterOutputPort resultFormatter;

    @InjectMocks
    private GroupReportQueryService groupReportQueryService;

    // ==================================================================================
    // getGroupSummary
    // ==================================================================================

    @Nested
    @DisplayName("getGroupSummary")
    class GetGroupSummary {

        @Test
        @DisplayName("Debe delegar al engine con resultados individuales y formas")
        void should_delegate_when_spaceValid() {
            // Arrange
            Long spaceId = 1L;
            Long userId = 10L;
            setupSpaceWithOneBattery(spaceId, userId, 100L, 2L);
            ScoringEngine.IndividualScoringResult scoring = buildScoringResult();
            when(scoringEngine.calculateIndividualReport(any(), any(), any(), eq("ILA"), eq(2)))
                    .thenReturn(scoring);
            GroupReportEngine.GroupSummaryResult expected =
                    buildGroupSummary();
            when(groupReportEngine.calculateGroupSummary(any(), any())).thenReturn(expected);

            // Act
            GroupReportEngine.GroupSummaryResult result = groupReportQueryService.getGroupSummary(spaceId, userId);

            // Assert
            assertThat(result).isSameAs(expected);
            verify(groupReportEngine).calculateGroupSummary(any(), any());
        }

        @Test
        @DisplayName("Debe lanzar EntityNotFound cuando el espacio no existe")
        void should_throwEntityNotFound_when_spaceNotFound() {
            // Arrange
            when(analysisSpaceQueryRepository.findByIdWithBatteries(1L)).thenReturn(Optional.empty());
            doThrow(new EntityNotFoundPersException(
                    ErrorCode.ANALYSIS_SPACE_NOT_FOUND.getCode(),
                    ErrorCode.ANALYSIS_SPACE_NOT_FOUND.getMessageKey(),
                    "user.report.space_not_found",
                    new Object[]{1L}))
                .when(resultFormatter).throwEntityNotFound(
                    any(ErrorCode.class), anyString(), any());

            // Act & Assert
            assertThatThrownBy(() -> groupReportQueryService.getGroupSummary(1L, 10L))
                    .isInstanceOf(EntityNotFoundPersException.class);

            verify(groupReportEngine, never()).calculateGroupSummary(any(), any());
        }

        @Test
        @DisplayName("Debe lanzar BusinessRuleViolation cuando el espacio no pertenece al usuario")
        void should_throwBusinessRule_when_ownerMismatch() {
            // Arrange
            AnalysisSpace space = AnalysisSpace.builder().id(1L).creatorUserId(10L).batteries(List.of()).build();
            when(analysisSpaceQueryRepository.findByIdWithBatteries(1L)).thenReturn(Optional.of(space));
            doThrow(new BusinessRuleViolationException(
                    ErrorCode.ANALYSIS_SPACE_ACCESS_DENIED.getCode(),
                    ErrorCode.ANALYSIS_SPACE_ACCESS_DENIED.getMessageKey(),
                    "user.report.space_access_denied",
                    new Object[]{99L, 1L}))
                .when(resultFormatter).throwBusinessRuleViolation(
                    any(ErrorCode.class), anyString(), any(), any());

            // Act & Assert
            assertThatThrownBy(() -> groupReportQueryService.getGroupSummary(1L, 99L))
                    .isInstanceOf(BusinessRuleViolationException.class);

            verify(groupReportEngine, never()).calculateGroupSummary(any(), any());
        }

        @Test
        @DisplayName("Debe lanzar EntityNotFound cuando faltan detalles de persona para una batería")
        void should_throwEntityNotFound_when_personDetailsMissing() {
            // Arrange
            Long spaceId = 1L;
            Long userId = 10L;
            AnalysisSpace space = buildSpaceWithBattery(spaceId, userId, 100L);
            when(analysisSpaceQueryRepository.findByIdWithBatteries(spaceId)).thenReturn(Optional.of(space));
            when(reportDataQueryRepository.getPersonDetailsByBatteryRecordId(100L)).thenReturn(Optional.empty());
            doThrow(new EntityNotFoundPersException(
                    ErrorCode.PERSON_DETAILS_NOT_FOUND.getCode(),
                    ErrorCode.PERSON_DETAILS_NOT_FOUND.getMessageKey(),
                    "user.person_evaluated_details.query_by_record_not_found",
                    new Object[]{100L}))
                .when(resultFormatter).throwEntityNotFound(
                    any(ErrorCode.class), anyString(), any());

            // Act & Assert
            assertThatThrownBy(() -> groupReportQueryService.getGroupSummary(spaceId, userId))
                    .isInstanceOf(EntityNotFoundPersException.class);
        }

        @Test
        @DisplayName("Debe lanzar BusinessRuleViolation cuando falta un cuestionario en una batería")
        void should_throwBusinessRule_when_questionnaireMissing() {
            // Arrange
            Long spaceId = 1L;
            Long userId = 10L;
            Long batteryId = 100L;
            AnalysisSpace space = buildSpaceWithBattery(spaceId, userId, batteryId);
            when(analysisSpaceQueryRepository.findByIdWithBatteries(spaceId)).thenReturn(Optional.of(space));
            when(reportDataQueryRepository.getPersonDetailsByBatteryRecordId(batteryId))
                    .thenReturn(Optional.of(buildPersonDetails(2L)));
            when(reportDataQueryRepository.getQuestionnaireRecordsByBatteryId(batteryId))
                    .thenReturn(buildQuestionnaireRecords("EXT", "EST"));
            doThrow(new BusinessRuleViolationException(
                    ErrorCode.REPORT_INCOMPLETE_BATTERY.getCode(),
                    ErrorCode.REPORT_INCOMPLETE_BATTERY.getMessageKey(),
                    "user.report.incomplete_battery",
                    new Object[]{batteryId, "ILA"}))
                .when(resultFormatter).throwBusinessRuleViolation(
                    any(ErrorCode.class), anyString(), any(), any());

            // Act & Assert
            assertThatThrownBy(() -> groupReportQueryService.getGroupSummary(spaceId, userId))
                    .isInstanceOf(BusinessRuleViolationException.class);
        }
    }

    // ==================================================================================
    // getDomainsAndDimensions
    // ==================================================================================

    @Nested
    @DisplayName("getDomainsAndDimensions")
    class GetDomainsAndDimensions {

        @Test
        @DisplayName("Debe delegar al engine el cálculo por dominios y dimensiones")
        void should_delegate_when_spaceValid() {
            // Arrange
            Long spaceId = 1L;
            Long userId = 10L;
            setupSpaceWithOneBattery(spaceId, userId, 100L, 2L);
            when(scoringEngine.calculateIndividualReport(any(), any(), any(), eq("ILA"), eq(2)))
                    .thenReturn(buildScoringResult());
            GroupReportEngine.DomainsAndDimensionsResult expected =
                    new GroupReportEngine.DomainsAndDimensionsResult(List.of(), List.of());
            when(groupReportEngine.calculateDomainsAndDimensions(any())).thenReturn(expected);

            // Act
            GroupReportEngine.DomainsAndDimensionsResult result =
                    groupReportQueryService.getDomainsAndDimensions(spaceId, userId);

            // Assert
            assertThat(result).isSameAs(expected);
            verify(groupReportEngine).calculateDomainsAndDimensions(any());
        }
    }

    // ==================================================================================
    // getRiskMatrix
    // ==================================================================================

    @Nested
    @DisplayName("getRiskMatrix")
    class GetRiskMatrix {

        @Test
        @DisplayName("Debe delegar al engine el cálculo de la matriz de riesgo con niveles de estrés")
        void should_delegate_when_spaceValid() {
            // Arrange
            Long spaceId = 1L;
            Long userId = 10L;
            setupSpaceWithOneBattery(spaceId, userId, 100L, 2L);
            when(scoringEngine.calculateIndividualReport(any(), any(), any(), eq("ILA"), eq(2)))
                    .thenReturn(buildScoringResult());
            GroupReportEngine.RiskMatrixResult expected =
                    org.mockito.Mockito.mock(GroupReportEngine.RiskMatrixResult.class);
            when(groupReportEngine.calculateRiskMatrix(any(), any())).thenReturn(expected);

            // Act
            GroupReportEngine.RiskMatrixResult result =
                    groupReportQueryService.getRiskMatrix(spaceId, userId);

            // Assert
            assertThat(result).isSameAs(expected);
            verify(groupReportEngine).calculateRiskMatrix(any(), any());
        }
    }

    // ==================================================================================
    // Helpers
    // ==================================================================================

    private AnalysisSpace buildSpaceWithBattery(Long spaceId, Long userId, Long batteryId) {
        return AnalysisSpace.builder()
                .id(spaceId)
                .creatorUserId(userId)
                .batteries(List.of(AnalysisSpaceBattery.builder()
                        .analysisSpaceId(spaceId)
                        .batteryManagementRecordId(batteryId)
                        .build()))
                .build();
    }

    private void setupSpaceWithOneBattery(Long spaceId, Long userId, Long batteryId, Long jobPositionTypeId) {
        AnalysisSpace space = buildSpaceWithBattery(spaceId, userId, batteryId);
        when(analysisSpaceQueryRepository.findByIdWithBatteries(spaceId)).thenReturn(Optional.of(space));
        when(reportDataQueryRepository.getPersonDetailsByBatteryRecordId(batteryId))
                .thenReturn(Optional.of(buildPersonDetails(jobPositionTypeId)));
        when(reportDataQueryRepository.getQuestionnaireRecordsByBatteryId(batteryId))
                .thenReturn(buildQuestionnaireRecords("ILA", "EXT", "EST"));
        when(reportDataQueryRepository.getResponsesByQuestionnaireRecordId(any()))
                .thenReturn(List.of(QuestionnaireResponse.builder().build()));
    }

    private PersonEvaluatedDetails buildPersonDetails(Long jobPositionTypeId) {
        JobPositionType type = JobPositionType.builder().id(jobPositionTypeId).name("TIPO").build();
        return PersonEvaluatedDetails.builder().id(1L).jobPositionType(type).build();
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

    private ScoringEngine.IndividualScoringResult buildScoringResult() {
        return new ScoringEngine.IndividualScoringResult(null, null, null,
                new ScoringEngine.StressResult(0.0, 0.0, "MUY BAJO"));
    }

    private GroupReportEngine.GroupSummaryResult buildGroupSummary() {
        // Construyo un summary vacío/mínimo. El engine es mockeado, por lo que la forma no importa.
        return org.mockito.Mockito.mock(GroupReportEngine.GroupSummaryResult.class);
    }
}

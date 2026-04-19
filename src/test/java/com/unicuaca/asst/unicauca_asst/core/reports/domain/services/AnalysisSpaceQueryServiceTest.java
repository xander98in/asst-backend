package com.unicuaca.asst.unicauca_asst.core.reports.domain.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import com.unicuaca.asst.unicauca_asst.common.domain.ports.output.ResultFormatterOutputPort;
import com.unicuaca.asst.unicauca_asst.common.exceptions.BusinessRuleViolationException;
import com.unicuaca.asst.unicauca_asst.common.exceptions.EntityNotFoundPersException;
import com.unicuaca.asst.unicauca_asst.common.exceptions.structure.ErrorCode;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.models.BatteryManagementRecordInformation;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.ports.input.BatteryManagementRecordQueryCUInputPort;
import com.unicuaca.asst.unicauca_asst.core.reports.domain.models.AnalysisSpace;
import com.unicuaca.asst.unicauca_asst.core.reports.domain.ports.output.AnalysisSpaceQueryRepository;

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
class AnalysisSpaceQueryServiceTest {

    @Mock
    private AnalysisSpaceQueryRepository analysisSpaceQueryRepository;

    @Mock
    private BatteryManagementRecordQueryCUInputPort batteryManagementRecordQueryCUInputPort;

    @Mock
    private ResultFormatterOutputPort resultFormatter;

    @InjectMocks
    private AnalysisSpaceQueryService analysisSpaceQueryService;

    // ==================================================================================
    // getAnalysisSpacesByUser
    // ==================================================================================

    @Nested
    @DisplayName("getAnalysisSpacesByUser")
    class GetAnalysisSpacesByUser {

        @Test
        @DisplayName("Debe retornar lista de espacios del usuario")
        void should_returnList_when_spacesExist() {
            // Arrange
            List<AnalysisSpace> spaces = List.of(buildSpace(1L, 10L), buildSpace(2L, 10L));
            when(analysisSpaceQueryRepository.findAllByCreatorUserId(10L)).thenReturn(spaces);

            // Act
            List<AnalysisSpace> result = analysisSpaceQueryService.getAnalysisSpacesByUser(10L);

            // Assert
            assertThat(result).hasSize(2);
        }
    }

    // ==================================================================================
    // getAnalysisSpaceById
    // ==================================================================================

    @Nested
    @DisplayName("getAnalysisSpaceById")
    class GetAnalysisSpaceById {

        @Test
        @DisplayName("Debe retornar espacio cuando existe y pertenece al usuario")
        void should_returnSpace_when_ownerMatches() {
            // Arrange
            AnalysisSpace space = buildSpace(1L, 10L);
            when(analysisSpaceQueryRepository.findByIdWithBatteries(1L)).thenReturn(Optional.of(space));

            // Act
            AnalysisSpace result = analysisSpaceQueryService.getAnalysisSpaceById(1L, 10L);

            // Assert
            assertThat(result).isNotNull();
            assertThat(result.getId()).isEqualTo(1L);
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
            assertThatThrownBy(() -> analysisSpaceQueryService.getAnalysisSpaceById(1L, 10L))
                    .isInstanceOf(EntityNotFoundPersException.class);
        }

        @Test
        @DisplayName("Debe lanzar BusinessRuleViolation cuando el espacio no pertenece al usuario")
        void should_throwBusinessRule_when_ownerMismatch() {
            // Arrange
            AnalysisSpace space = buildSpace(1L, 10L);
            when(analysisSpaceQueryRepository.findByIdWithBatteries(1L)).thenReturn(Optional.of(space));
            doThrow(new BusinessRuleViolationException(
                    ErrorCode.ANALYSIS_SPACE_ACCESS_DENIED.getCode(),
                    ErrorCode.ANALYSIS_SPACE_ACCESS_DENIED.getMessageKey(),
                    "user.report.space_access_denied",
                    new Object[]{99L, 1L}))
                .when(resultFormatter).throwBusinessRuleViolation(
                    any(ErrorCode.class), anyString(), any(), any());

            // Act & Assert
            assertThatThrownBy(() -> analysisSpaceQueryService.getAnalysisSpaceById(1L, 99L))
                    .isInstanceOf(BusinessRuleViolationException.class);
        }
    }

    // ==================================================================================
    // getSpaceBatteriesWithMultifilter
    // ==================================================================================

    @Nested
    @DisplayName("getSpaceBatteriesWithMultifilter")
    class GetSpaceBatteriesWithMultifilter {

        @Test
        @DisplayName("Debe delegar al módulo de baterías tras validar ownership")
        void should_delegate_when_spaceValid() {
            // Arrange
            AnalysisSpace space = buildSpace(1L, 10L);
            when(analysisSpaceQueryRepository.findByIdWithBatteries(1L)).thenReturn(Optional.of(space));
            Page<BatteryManagementRecordInformation> page = new PageImpl<>(List.of());
            when(batteryManagementRecordQueryCUInputPort.listByAnalysisSpaceWithMultipleFilters(
                    eq(1L), any(), any(), any(), any(), any(), any(), any(), eq(0), eq(10)))
                    .thenReturn(page);

            // Act
            Page<BatteryManagementRecordInformation> result =
                    analysisSpaceQueryService.getSpaceBatteriesWithMultifilter(
                            1L, 10L, null, null, null, null, null, null, null, 0, 10);

            // Assert
            assertThat(result).isNotNull();
            verify(batteryManagementRecordQueryCUInputPort).listByAnalysisSpaceWithMultipleFilters(
                    eq(1L), any(), any(), any(), any(), any(), any(), any(), eq(0), eq(10));
        }

        @Test
        @DisplayName("Debe lanzar EntityNotFound cuando el espacio no existe y no delegar")
        void should_throwAndNotDelegate_when_spaceNotFound() {
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
            assertThatThrownBy(() -> analysisSpaceQueryService.getSpaceBatteriesWithMultifilter(
                    1L, 10L, null, null, null, null, null, null, null, 0, 10))
                    .isInstanceOf(EntityNotFoundPersException.class);

            verify(batteryManagementRecordQueryCUInputPort, never())
                    .listByAnalysisSpaceWithMultipleFilters(
                            any(), any(), any(), any(), any(), any(), any(), any(), any(), any());
        }
    }

    // ==================================================================================
    // Helpers
    // ==================================================================================

    private AnalysisSpace buildSpace(Long id, Long creatorUserId) {
        return AnalysisSpace.builder()
                .id(id)
                .name("Espacio X")
                .evaluatorId(5L)
                .creatorUserId(creatorUserId)
                .createdAt(LocalDateTime.now())
                .batteries(List.of())
                .build();
    }
}

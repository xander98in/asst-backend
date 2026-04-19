package com.unicuaca.asst.unicauca_asst.core.reports.domain.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.unicuaca.asst.unicauca_asst.common.domain.ports.output.ResultFormatterOutputPort;
import com.unicuaca.asst.unicauca_asst.common.exceptions.BusinessRuleViolationException;
import com.unicuaca.asst.unicauca_asst.common.exceptions.EntityAlreadyExistsException;
import com.unicuaca.asst.unicauca_asst.common.exceptions.EntityNotFoundPersException;
import com.unicuaca.asst.unicauca_asst.common.exceptions.structure.ErrorCode;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.models.BatteryManagementRecord;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.models.BatteryManagementRecordStatus;
import com.unicuaca.asst.unicauca_asst.core.reports.domain.models.AnalysisSpace;
import com.unicuaca.asst.unicauca_asst.core.reports.domain.models.AnalysisSpaceBattery;
import com.unicuaca.asst.unicauca_asst.core.reports.domain.models.Evaluator;
import com.unicuaca.asst.unicauca_asst.core.reports.domain.ports.output.AnalysisSpaceCommandRepository;
import com.unicuaca.asst.unicauca_asst.core.reports.domain.ports.output.AnalysisSpaceQueryRepository;
import com.unicuaca.asst.unicauca_asst.core.reports.domain.ports.output.EvaluatorQueryRepository;
import com.unicuaca.asst.unicauca_asst.core.reports.domain.ports.output.ReportDataQueryRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AnalysisSpaceCommandServiceTest {

    @Mock
    private AnalysisSpaceCommandRepository analysisSpaceCommandRepository;

    @Mock
    private AnalysisSpaceQueryRepository analysisSpaceQueryRepository;

    @Mock
    private ReportDataQueryRepository reportDataQueryRepository;

    @Mock
    private EvaluatorQueryRepository evaluatorQueryRepository;

    @Mock
    private ResultFormatterOutputPort resultFormatter;

    @InjectMocks
    private AnalysisSpaceCommandService analysisSpaceCommandService;

    // ==================================================================================
    // createAnalysisSpace
    // ==================================================================================

    @Nested
    @DisplayName("createAnalysisSpace")
    class CreateAnalysisSpace {

        @Test
        @DisplayName("Debe crear espacio cuando no supera tope, nombre es único y evaluador pertenece al usuario")
        void should_createSpace_when_validConditions() {
            // Arrange
            when(analysisSpaceCommandRepository.countByCreatorUserId(10L)).thenReturn(2);
            when(analysisSpaceCommandRepository.existsByNameAndCreatorUserId("Espacio 1", 10L))
                    .thenReturn(false);
            when(evaluatorQueryRepository.findById(5L))
                    .thenReturn(Optional.of(buildEvaluator(5L, 10L)));
            AnalysisSpace saved = buildSpace(1L, 10L);
            when(analysisSpaceCommandRepository.save(any(AnalysisSpace.class))).thenReturn(saved);

            // Act
            AnalysisSpace result = analysisSpaceCommandService.createAnalysisSpace("Espacio 1", 5L, 10L);

            // Assert
            ArgumentCaptor<AnalysisSpace> captor = ArgumentCaptor.forClass(AnalysisSpace.class);
            verify(analysisSpaceCommandRepository).save(captor.capture());
            AnalysisSpace toSave = captor.getValue();
            assertThat(toSave.getName()).isEqualTo("Espacio 1");
            assertThat(toSave.getEvaluatorId()).isEqualTo(5L);
            assertThat(toSave.getCreatorUserId()).isEqualTo(10L);
            assertThat(toSave.getCreatedAt()).isNotNull();
            assertThat(result.getId()).isEqualTo(1L);
        }

        @Test
        @DisplayName("Debe lanzar BusinessRuleViolation cuando se alcanza el tope de espacios")
        void should_throwBusinessRule_when_limitReached() {
            // Arrange
            when(analysisSpaceCommandRepository.countByCreatorUserId(10L)).thenReturn(10);
            doThrow(new BusinessRuleViolationException(
                    ErrorCode.ANALYSIS_SPACE_LIMIT_REACHED.getCode(),
                    ErrorCode.ANALYSIS_SPACE_LIMIT_REACHED.getMessageKey(),
                    "user.report.space_limit_reached",
                    new Object[]{10L, 10}))
                .when(resultFormatter).throwBusinessRuleViolation(
                    any(ErrorCode.class), anyString(), any(), any());

            // Act & Assert
            assertThatThrownBy(() -> analysisSpaceCommandService.createAnalysisSpace("Espacio", 5L, 10L))
                    .isInstanceOf(BusinessRuleViolationException.class);

            verify(analysisSpaceCommandRepository, never()).save(any());
        }

        @Test
        @DisplayName("Debe lanzar EntityAlreadyExists cuando el nombre ya existe para el usuario")
        void should_throwAlreadyExists_when_nameDuplicated() {
            // Arrange
            when(analysisSpaceCommandRepository.countByCreatorUserId(10L)).thenReturn(0);
            when(analysisSpaceCommandRepository.existsByNameAndCreatorUserId("Espacio 1", 10L))
                    .thenReturn(true);
            doThrow(new EntityAlreadyExistsException(
                    ErrorCode.ANALYSIS_SPACE_NAME_EXISTS.getCode(),
                    ErrorCode.ANALYSIS_SPACE_NAME_EXISTS.getMessageKey(),
                    "user.report.space_name_exists",
                    new Object[]{"Espacio 1"}))
                .when(resultFormatter).throwEntityAlreadyExists(
                    any(ErrorCode.class), anyString(), any());

            // Act & Assert
            assertThatThrownBy(() -> analysisSpaceCommandService.createAnalysisSpace("Espacio 1", 5L, 10L))
                    .isInstanceOf(EntityAlreadyExistsException.class);

            verify(analysisSpaceCommandRepository, never()).save(any());
        }

        @Test
        @DisplayName("Debe lanzar EntityNotFound cuando el evaluador no existe")
        void should_throwEntityNotFound_when_evaluatorMissing() {
            // Arrange
            when(analysisSpaceCommandRepository.countByCreatorUserId(10L)).thenReturn(0);
            when(analysisSpaceCommandRepository.existsByNameAndCreatorUserId("Espacio", 10L))
                    .thenReturn(false);
            when(evaluatorQueryRepository.findById(5L)).thenReturn(Optional.empty());
            doThrow(new EntityNotFoundPersException(
                    ErrorCode.EVALUATOR_NOT_FOUND.getCode(),
                    ErrorCode.EVALUATOR_NOT_FOUND.getMessageKey(),
                    "user.report.evaluator_not_found",
                    new Object[]{5L}))
                .when(resultFormatter).throwEntityNotFound(
                    any(ErrorCode.class), anyString(), any());

            // Act & Assert
            assertThatThrownBy(() -> analysisSpaceCommandService.createAnalysisSpace("Espacio", 5L, 10L))
                    .isInstanceOf(EntityNotFoundPersException.class);
        }

        @Test
        @DisplayName("Debe lanzar BusinessRuleViolation cuando el evaluador no pertenece al usuario")
        void should_throwBusinessRule_when_evaluatorOwnerMismatch() {
            // Arrange
            when(analysisSpaceCommandRepository.countByCreatorUserId(10L)).thenReturn(0);
            when(analysisSpaceCommandRepository.existsByNameAndCreatorUserId("Espacio", 10L))
                    .thenReturn(false);
            when(evaluatorQueryRepository.findById(5L))
                    .thenReturn(Optional.of(buildEvaluator(5L, 99L)));
            doThrow(new BusinessRuleViolationException(
                    ErrorCode.EVALUATOR_ACCESS_DENIED.getCode(),
                    ErrorCode.EVALUATOR_ACCESS_DENIED.getMessageKey(),
                    "user.report.evaluator_access_denied",
                    new Object[]{10L, 5L}))
                .when(resultFormatter).throwBusinessRuleViolation(
                    any(ErrorCode.class), anyString(), any(), any());

            // Act & Assert
            assertThatThrownBy(() -> analysisSpaceCommandService.createAnalysisSpace("Espacio", 5L, 10L))
                    .isInstanceOf(BusinessRuleViolationException.class);

            verify(analysisSpaceCommandRepository, never()).save(any());
        }
    }

    // ==================================================================================
    // addBatteriesToSpace
    // ==================================================================================

    @Nested
    @DisplayName("addBatteriesToSpace")
    class AddBatteriesToSpace {

        @Test
        @DisplayName("Debe agregar baterías cerradas que no estén ya en el espacio")
        void should_addBatteries_when_closedAndNotInSpace() {
            // Arrange
            AnalysisSpace space = buildSpace(1L, 10L);
            when(analysisSpaceQueryRepository.findByIdWithBatteries(1L)).thenReturn(Optional.of(space));
            when(reportDataQueryRepository.getBatteryRecordById(100L))
                    .thenReturn(Optional.of(buildBatteryRecord("Cerrado")));
            when(analysisSpaceQueryRepository.existsBatteryInSpace(1L, 100L)).thenReturn(false);

            // Act
            analysisSpaceCommandService.addBatteriesToSpace(1L, List.of(100L), 10L);

            // Assert
            verify(analysisSpaceCommandRepository).addBatteryToSpace(1L, 100L);
        }

        @Test
        @DisplayName("Debe saltar baterías ya asociadas al espacio")
        void should_skip_when_batteryAlreadyInSpace() {
            // Arrange
            AnalysisSpace space = buildSpace(1L, 10L);
            when(analysisSpaceQueryRepository.findByIdWithBatteries(1L)).thenReturn(Optional.of(space));
            when(reportDataQueryRepository.getBatteryRecordById(100L))
                    .thenReturn(Optional.of(buildBatteryRecord("Cerrado")));
            when(analysisSpaceQueryRepository.existsBatteryInSpace(1L, 100L)).thenReturn(true);

            // Act
            analysisSpaceCommandService.addBatteriesToSpace(1L, List.of(100L), 10L);

            // Assert
            verify(analysisSpaceCommandRepository, never()).addBatteryToSpace(any(), any());
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
            assertThatThrownBy(() -> analysisSpaceCommandService.addBatteriesToSpace(1L, List.of(100L), 10L))
                    .isInstanceOf(EntityNotFoundPersException.class);
        }

        @Test
        @DisplayName("Debe lanzar BusinessRuleViolation cuando el espacio no pertenece al usuario")
        void should_throwBusinessRule_when_spaceOwnerMismatch() {
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
            assertThatThrownBy(() -> analysisSpaceCommandService.addBatteriesToSpace(1L, List.of(100L), 99L))
                    .isInstanceOf(BusinessRuleViolationException.class);
        }

        @Test
        @DisplayName("Debe lanzar EntityNotFound cuando la batería no existe")
        void should_throwEntityNotFound_when_batteryNotFound() {
            // Arrange
            AnalysisSpace space = buildSpace(1L, 10L);
            when(analysisSpaceQueryRepository.findByIdWithBatteries(1L)).thenReturn(Optional.of(space));
            when(reportDataQueryRepository.getBatteryRecordById(100L)).thenReturn(Optional.empty());
            doThrow(new EntityNotFoundPersException(
                    ErrorCode.BATTERY_RECORD_NOT_FOUND.getCode(),
                    ErrorCode.BATTERY_RECORD_NOT_FOUND.getMessageKey(),
                    "user.battery.query_not_found",
                    new Object[]{100L}))
                .when(resultFormatter).throwEntityNotFound(
                    any(ErrorCode.class), anyString(), any());

            // Act & Assert
            assertThatThrownBy(() -> analysisSpaceCommandService.addBatteriesToSpace(1L, List.of(100L), 10L))
                    .isInstanceOf(EntityNotFoundPersException.class);
        }

        @Test
        @DisplayName("Debe lanzar BusinessRuleViolation cuando la batería no está cerrada")
        void should_throwBusinessRule_when_batteryNotClosed() {
            // Arrange
            AnalysisSpace space = buildSpace(1L, 10L);
            when(analysisSpaceQueryRepository.findByIdWithBatteries(1L)).thenReturn(Optional.of(space));
            when(reportDataQueryRepository.getBatteryRecordById(100L))
                    .thenReturn(Optional.of(buildBatteryRecord("Diligenciado")));
            doThrow(new BusinessRuleViolationException(
                    ErrorCode.REPORT_BATTERY_NOT_CLOSED.getCode(),
                    ErrorCode.REPORT_BATTERY_NOT_CLOSED.getMessageKey(),
                    "user.report.battery_not_closed",
                    new Object[]{100L}))
                .when(resultFormatter).throwBusinessRuleViolation(
                    any(ErrorCode.class), anyString(), any());

            // Act & Assert
            assertThatThrownBy(() -> analysisSpaceCommandService.addBatteriesToSpace(1L, List.of(100L), 10L))
                    .isInstanceOf(BusinessRuleViolationException.class);

            verify(analysisSpaceCommandRepository, never()).addBatteryToSpace(any(), any());
        }
    }

    // ==================================================================================
    // removeBatteryFromSpace
    // ==================================================================================

    @Nested
    @DisplayName("removeBatteryFromSpace")
    class RemoveBatteryFromSpace {

        @Test
        @DisplayName("Debe remover batería cuando el espacio tiene más de una")
        void should_removeBattery_when_spaceHasMultipleBatteries() {
            // Arrange
            AnalysisSpace space = buildSpaceWithBatteries(1L, 10L, 2);
            when(analysisSpaceQueryRepository.findByIdWithBatteries(1L)).thenReturn(Optional.of(space));
            when(analysisSpaceQueryRepository.existsBatteryInSpace(1L, 100L)).thenReturn(true);

            // Act
            analysisSpaceCommandService.removeBatteryFromSpace(1L, 100L, 10L);

            // Assert
            verify(analysisSpaceCommandRepository).removeBatteryFromSpace(1L, 100L);
        }

        @Test
        @DisplayName("Debe lanzar EntityNotFound cuando la batería no está en el espacio")
        void should_throwEntityNotFound_when_batteryNotInSpace() {
            // Arrange
            AnalysisSpace space = buildSpaceWithBatteries(1L, 10L, 2);
            when(analysisSpaceQueryRepository.findByIdWithBatteries(1L)).thenReturn(Optional.of(space));
            when(analysisSpaceQueryRepository.existsBatteryInSpace(1L, 100L)).thenReturn(false);
            doThrow(new EntityNotFoundPersException(
                    ErrorCode.ANALYSIS_SPACE_BATTERY_NOT_FOUND.getCode(),
                    ErrorCode.ANALYSIS_SPACE_BATTERY_NOT_FOUND.getMessageKey(),
                    "user.report.space_battery_not_found",
                    new Object[]{100L, 1L}))
                .when(resultFormatter).throwEntityNotFound(
                    any(ErrorCode.class), anyString(), any(), any());

            // Act & Assert
            assertThatThrownBy(() -> analysisSpaceCommandService.removeBatteryFromSpace(1L, 100L, 10L))
                    .isInstanceOf(EntityNotFoundPersException.class);

            verify(analysisSpaceCommandRepository, never()).removeBatteryFromSpace(any(), any());
        }

        @Test
        @DisplayName("Debe remover cuando la lista de baterías es null (short-circuit del AND)")
        void should_removeBattery_when_batteriesListIsNull() {
            // Arrange
            AnalysisSpace space = buildSpace(1L, 10L);
            space.setBatteries(null);
            when(analysisSpaceQueryRepository.findByIdWithBatteries(1L)).thenReturn(Optional.of(space));
            when(analysisSpaceQueryRepository.existsBatteryInSpace(1L, 100L)).thenReturn(true);

            // Act
            analysisSpaceCommandService.removeBatteryFromSpace(1L, 100L, 10L);

            // Assert
            verify(analysisSpaceCommandRepository).removeBatteryFromSpace(1L, 100L);
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
            assertThatThrownBy(() -> analysisSpaceCommandService.removeBatteryFromSpace(1L, 100L, 10L))
                    .isInstanceOf(EntityNotFoundPersException.class);

            verify(analysisSpaceCommandRepository, never()).removeBatteryFromSpace(any(), any());
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
            assertThatThrownBy(() -> analysisSpaceCommandService.removeBatteryFromSpace(1L, 100L, 99L))
                    .isInstanceOf(BusinessRuleViolationException.class);

            verify(analysisSpaceCommandRepository, never()).removeBatteryFromSpace(any(), any());
        }

        @Test
        @DisplayName("Debe lanzar BusinessRuleViolation cuando el espacio quedaría sin baterías")
        void should_throwBusinessRule_when_lastBattery() {
            // Arrange
            AnalysisSpace space = buildSpaceWithBatteries(1L, 10L, 1);
            when(analysisSpaceQueryRepository.findByIdWithBatteries(1L)).thenReturn(Optional.of(space));
            when(analysisSpaceQueryRepository.existsBatteryInSpace(1L, 100L)).thenReturn(true);
            doThrow(new BusinessRuleViolationException(
                    ErrorCode.ANALYSIS_SPACE_MIN_BATTERIES.getCode(),
                    ErrorCode.ANALYSIS_SPACE_MIN_BATTERIES.getMessageKey(),
                    "user.report.space_min_batteries",
                    new Object[]{}))
                .when(resultFormatter).throwBusinessRuleViolation(
                    any(ErrorCode.class), anyString());

            // Act & Assert
            assertThatThrownBy(() -> analysisSpaceCommandService.removeBatteryFromSpace(1L, 100L, 10L))
                    .isInstanceOf(BusinessRuleViolationException.class);

            verify(analysisSpaceCommandRepository, never()).removeBatteryFromSpace(any(), any());
        }
    }

    // ==================================================================================
    // deleteAnalysisSpace
    // ==================================================================================

    @Nested
    @DisplayName("deleteAnalysisSpace")
    class DeleteAnalysisSpace {

        @Test
        @DisplayName("Debe eliminar espacio cuando existe y pertenece al usuario")
        void should_deleteSpace_when_ownerMatches() {
            // Arrange
            AnalysisSpace space = buildSpace(1L, 10L);
            when(analysisSpaceQueryRepository.findByIdWithBatteries(1L)).thenReturn(Optional.of(space));

            // Act
            analysisSpaceCommandService.deleteAnalysisSpace(1L, 10L);

            // Assert
            verify(analysisSpaceCommandRepository).deleteById(1L);
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
            assertThatThrownBy(() -> analysisSpaceCommandService.deleteAnalysisSpace(1L, 10L))
                    .isInstanceOf(EntityNotFoundPersException.class);

            verify(analysisSpaceCommandRepository, never()).deleteById(any());
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
            assertThatThrownBy(() -> analysisSpaceCommandService.deleteAnalysisSpace(1L, 99L))
                    .isInstanceOf(BusinessRuleViolationException.class);

            verify(analysisSpaceCommandRepository, never()).deleteById(any());
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

    private AnalysisSpace buildSpaceWithBatteries(Long id, Long creatorUserId, int count) {
        AnalysisSpace space = buildSpace(id, creatorUserId);
        space.setBatteries(java.util.stream.IntStream.range(0, count)
                .mapToObj(i -> AnalysisSpaceBattery.builder()
                        .analysisSpaceId(id)
                        .batteryManagementRecordId((long) (100 + i))
                        .addedAt(LocalDateTime.now())
                        .build())
                .toList());
        return space;
    }

    private Evaluator buildEvaluator(Long id, Long creatorUserId) {
        return Evaluator.builder()
                .id(id)
                .creatorUserId(creatorUserId)
                .build();
    }

    private BatteryManagementRecord buildBatteryRecord(String statusName) {
        return BatteryManagementRecord.builder()
                .id(100L)
                .status(BatteryManagementRecordStatus.builder().id(1L).name(statusName).build())
                .build();
    }
}

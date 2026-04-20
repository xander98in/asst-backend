package com.unicuaca.asst.unicauca_asst.core.reports.domain.services;

import java.time.LocalDate;
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
import com.unicuaca.asst.unicauca_asst.common.exceptions.EntityNotFoundPersException;
import com.unicuaca.asst.unicauca_asst.common.exceptions.structure.ErrorCode;
import com.unicuaca.asst.unicauca_asst.core.reports.domain.models.Evaluator;
import com.unicuaca.asst.unicauca_asst.core.reports.domain.ports.output.EvaluatorCommandRepository;
import com.unicuaca.asst.unicauca_asst.core.reports.domain.ports.output.EvaluatorQueryRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EvaluatorCommandServiceTest {

    @Mock
    private EvaluatorCommandRepository evaluatorCommandRepository;

    @Mock
    private EvaluatorQueryRepository evaluatorQueryRepository;

    @Mock
    private ResultFormatterOutputPort resultFormatter;

    @InjectMocks
    private EvaluatorCommandService evaluatorCommandService;

    // ==================================================================================
    // createEvaluator
    // ==================================================================================

    @Nested
    @DisplayName("createEvaluator")
    class CreateEvaluator {

        @Test
        @DisplayName("Debe crear evaluador asignando la fecha de creación")
        void should_createEvaluator_when_validData() {
            // Arrange
            Evaluator input = buildEvaluator(null, 10L);
            Evaluator saved = buildEvaluator(1L, 10L);
            when(evaluatorCommandRepository.save(any(Evaluator.class))).thenReturn(saved);

            // Act
            Evaluator result = evaluatorCommandService.createEvaluator(input);

            // Assert
            ArgumentCaptor<Evaluator> captor = ArgumentCaptor.forClass(Evaluator.class);
            verify(evaluatorCommandRepository).save(captor.capture());
            assertThat(captor.getValue().getCreatedAt()).isNotNull();
            assertThat(result.getId()).isEqualTo(1L);
        }
    }

    // ==================================================================================
    // updateEvaluator
    // ==================================================================================

    @Nested
    @DisplayName("updateEvaluator")
    class UpdateEvaluator {

        @Test
        @DisplayName("Debe actualizar evaluador cuando existe y pertenece al usuario")
        void should_updateEvaluator_when_ownerMatches() {
            // Arrange
            Evaluator existing = buildEvaluator(1L, 10L);
            Evaluator updates = Evaluator.builder()
                    .fullName("NUEVO NOMBRE")
                    .identificationNumber("99999")
                    .profession("PSICOLOGÍA")
                    .postgraduateDegree("MAESTRÍA")
                    .professionalCardNumber("TP-123")
                    .occupationalHealthLicense("LIC-456")
                    .licenseIssueDate(LocalDate.of(2030, 1, 1))
                    .build();
            when(evaluatorQueryRepository.findById(1L)).thenReturn(Optional.of(existing));
            when(evaluatorCommandRepository.save(any(Evaluator.class))).thenReturn(existing);

            // Act
            Evaluator result = evaluatorCommandService.updateEvaluator(1L, updates, 10L);

            // Assert
            assertThat(result.getFullName()).isEqualTo("NUEVO NOMBRE");
            assertThat(result.getIdentificationNumber()).isEqualTo("99999");
            assertThat(result.getProfession()).isEqualTo("PSICOLOGÍA");
            verify(evaluatorCommandRepository).save(existing);
        }

        @Test
        @DisplayName("Debe lanzar EntityNotFound cuando el evaluador no existe")
        void should_throwEntityNotFound_when_evaluatorNotFound() {
            // Arrange
            when(evaluatorQueryRepository.findById(1L)).thenReturn(Optional.empty());
            doThrow(new EntityNotFoundPersException(
                    ErrorCode.EVALUATOR_NOT_FOUND.getCode(),
                    ErrorCode.EVALUATOR_NOT_FOUND.getMessageKey(),
                    "user.report.evaluator_not_found",
                    new Object[]{1L}))
                .when(resultFormatter).throwEntityNotFound(
                    any(ErrorCode.class), anyString(), any());

            // Act & Assert
            assertThatThrownBy(() -> evaluatorCommandService.updateEvaluator(1L, buildEvaluator(null, 10L), 10L))
                    .isInstanceOf(EntityNotFoundPersException.class);

            verify(evaluatorCommandRepository, never()).save(any());
        }

        @Test
        @DisplayName("Debe lanzar BusinessRuleViolation cuando el evaluador no pertenece al usuario")
        void should_throwBusinessRule_when_ownerMismatch() {
            // Arrange
            Evaluator existing = buildEvaluator(1L, 10L);
            when(evaluatorQueryRepository.findById(1L)).thenReturn(Optional.of(existing));
            doThrow(new BusinessRuleViolationException(
                    ErrorCode.EVALUATOR_ACCESS_DENIED.getCode(),
                    ErrorCode.EVALUATOR_ACCESS_DENIED.getMessageKey(),
                    "user.report.evaluator_access_denied",
                    new Object[]{99L, 1L}))
                .when(resultFormatter).throwBusinessRuleViolation(
                    any(ErrorCode.class), anyString(), any(), any());

            // Act & Assert
            assertThatThrownBy(() -> evaluatorCommandService.updateEvaluator(1L, buildEvaluator(null, 99L), 99L))
                    .isInstanceOf(BusinessRuleViolationException.class);

            verify(evaluatorCommandRepository, never()).save(any());
        }
    }

    // ==================================================================================
    // deleteEvaluator
    // ==================================================================================

    @Nested
    @DisplayName("deleteEvaluator")
    class DeleteEvaluator {

        @Test
        @DisplayName("Debe eliminar evaluador cuando existe, pertenece al usuario y no está en uso")
        void should_deleteEvaluator_when_validConditions() {
            // Arrange
            Evaluator existing = buildEvaluator(1L, 10L);
            when(evaluatorQueryRepository.findById(1L)).thenReturn(Optional.of(existing));
            when(evaluatorQueryRepository.isEvaluatorInUseByAnySpace(1L)).thenReturn(false);

            // Act
            evaluatorCommandService.deleteEvaluator(1L, 10L);

            // Assert
            verify(evaluatorCommandRepository).deleteById(1L);
        }

        @Test
        @DisplayName("Debe lanzar EntityNotFound cuando el evaluador no existe")
        void should_throwEntityNotFound_when_evaluatorNotFound() {
            // Arrange
            when(evaluatorQueryRepository.findById(1L)).thenReturn(Optional.empty());
            doThrow(new EntityNotFoundPersException(
                    ErrorCode.EVALUATOR_NOT_FOUND.getCode(),
                    ErrorCode.EVALUATOR_NOT_FOUND.getMessageKey(),
                    "user.report.evaluator_not_found",
                    new Object[]{1L}))
                .when(resultFormatter).throwEntityNotFound(
                    any(ErrorCode.class), anyString(), any());

            // Act & Assert
            assertThatThrownBy(() -> evaluatorCommandService.deleteEvaluator(1L, 10L))
                    .isInstanceOf(EntityNotFoundPersException.class);

            verify(evaluatorCommandRepository, never()).deleteById(any());
        }

        @Test
        @DisplayName("Debe lanzar BusinessRuleViolation cuando el evaluador no pertenece al usuario")
        void should_throwBusinessRule_when_ownerMismatch() {
            // Arrange
            Evaluator existing = buildEvaluator(1L, 10L);
            when(evaluatorQueryRepository.findById(1L)).thenReturn(Optional.of(existing));
            doThrow(new BusinessRuleViolationException(
                    ErrorCode.EVALUATOR_ACCESS_DENIED.getCode(),
                    ErrorCode.EVALUATOR_ACCESS_DENIED.getMessageKey(),
                    "user.report.evaluator_access_denied",
                    new Object[]{99L, 1L}))
                .when(resultFormatter).throwBusinessRuleViolation(
                    any(ErrorCode.class), anyString(), any(), any());

            // Act & Assert
            assertThatThrownBy(() -> evaluatorCommandService.deleteEvaluator(1L, 99L))
                    .isInstanceOf(BusinessRuleViolationException.class);

            verify(evaluatorCommandRepository, never()).deleteById(any());
        }

        @Test
        @DisplayName("Debe lanzar BusinessRuleViolation cuando el evaluador está en uso")
        void should_throwBusinessRule_when_evaluatorInUse() {
            // Arrange
            Evaluator existing = buildEvaluator(1L, 10L);
            when(evaluatorQueryRepository.findById(1L)).thenReturn(Optional.of(existing));
            when(evaluatorQueryRepository.isEvaluatorInUseByAnySpace(1L)).thenReturn(true);
            doThrow(new BusinessRuleViolationException(
                    ErrorCode.EVALUATOR_IN_USE.getCode(),
                    ErrorCode.EVALUATOR_IN_USE.getMessageKey(),
                    "user.report.evaluator_in_use",
                    new Object[]{1L}))
                .when(resultFormatter).throwBusinessRuleViolation(
                    any(ErrorCode.class), anyString(), any());

            // Act & Assert
            assertThatThrownBy(() -> evaluatorCommandService.deleteEvaluator(1L, 10L))
                    .isInstanceOf(BusinessRuleViolationException.class);

            verify(evaluatorCommandRepository, never()).deleteById(any());
        }
    }

    // ==================================================================================
    // Helpers
    // ==================================================================================

    private Evaluator buildEvaluator(Long id, Long creatorUserId) {
        return Evaluator.builder()
                .id(id)
                .fullName("JUAN PÉREZ")
                .identificationNumber("1061234567")
                .profession("PSICOLOGÍA")
                .postgraduateDegree("ESPECIALIZACIÓN")
                .professionalCardNumber("TP-000")
                .occupationalHealthLicense("LIC-000")
                .licenseIssueDate(LocalDate.of(2028, 12, 31))
                .creatorUserId(creatorUserId)
                .build();
    }
}

package com.unicuaca.asst.unicauca_asst.core.reports.domain.services;

import java.time.LocalDate;
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
import com.unicuaca.asst.unicauca_asst.core.reports.domain.models.Evaluator;
import com.unicuaca.asst.unicauca_asst.core.reports.domain.ports.output.EvaluatorQueryRepository;

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
class EvaluatorQueryServiceTest {

    @Mock
    private EvaluatorQueryRepository evaluatorQueryRepository;

    @Mock
    private ResultFormatterOutputPort resultFormatter;

    @InjectMocks
    private EvaluatorQueryService evaluatorQueryService;

    // ==================================================================================
    // getEvaluatorsByUser
    // ==================================================================================

    @Nested
    @DisplayName("getEvaluatorsByUser")
    class GetEvaluatorsByUser {

        @Test
        @DisplayName("Debe retornar lista de evaluadores del usuario")
        void should_returnList_when_evaluatorsExist() {
            // Arrange
            List<Evaluator> evaluators = List.of(buildEvaluator(1L, 10L), buildEvaluator(2L, 10L));
            when(evaluatorQueryRepository.findAllByCreatorUserId(10L)).thenReturn(evaluators);

            // Act
            List<Evaluator> result = evaluatorQueryService.getEvaluatorsByUser(10L);

            // Assert
            assertThat(result).hasSize(2);
        }
    }

    // ==================================================================================
    // getEvaluatorById
    // ==================================================================================

    @Nested
    @DisplayName("getEvaluatorById")
    class GetEvaluatorById {

        @Test
        @DisplayName("Debe retornar evaluador cuando existe y pertenece al usuario")
        void should_returnEvaluator_when_ownerMatches() {
            // Arrange
            Evaluator evaluator = buildEvaluator(1L, 10L);
            when(evaluatorQueryRepository.findById(1L)).thenReturn(Optional.of(evaluator));

            // Act
            Evaluator result = evaluatorQueryService.getEvaluatorById(1L, 10L);

            // Assert
            assertThat(result).isNotNull();
            assertThat(result.getId()).isEqualTo(1L);
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
            assertThatThrownBy(() -> evaluatorQueryService.getEvaluatorById(1L, 10L))
                    .isInstanceOf(EntityNotFoundPersException.class);
        }

        @Test
        @DisplayName("Debe lanzar BusinessRuleViolation cuando el evaluador no pertenece al usuario")
        void should_throwBusinessRule_when_ownerMismatch() {
            // Arrange
            Evaluator evaluator = buildEvaluator(1L, 10L);
            when(evaluatorQueryRepository.findById(1L)).thenReturn(Optional.of(evaluator));
            doThrow(new BusinessRuleViolationException(
                    ErrorCode.EVALUATOR_ACCESS_DENIED.getCode(),
                    ErrorCode.EVALUATOR_ACCESS_DENIED.getMessageKey(),
                    "user.report.evaluator_access_denied",
                    new Object[]{99L, 1L}))
                .when(resultFormatter).throwBusinessRuleViolation(
                    any(ErrorCode.class), anyString(), any(), any());

            // Act & Assert
            assertThatThrownBy(() -> evaluatorQueryService.getEvaluatorById(1L, 99L))
                    .isInstanceOf(BusinessRuleViolationException.class);
        }
    }

    // ==================================================================================
    // getEvaluatorsByUserPaged
    // ==================================================================================

    @Nested
    @DisplayName("getEvaluatorsByUserPaged")
    class GetEvaluatorsByUserPaged {

        @Test
        @DisplayName("Debe usar la consulta sin término cuando searchTerm es null")
        void should_useSimpleQuery_when_searchTermIsNull() {
            // Arrange
            Page<Evaluator> page = new PageImpl<>(List.of(buildEvaluator(1L, 10L)));
            when(evaluatorQueryRepository.findAllByCreatorUserIdPaged(10L, 0, 10)).thenReturn(page);

            // Act
            Page<Evaluator> result = evaluatorQueryService.getEvaluatorsByUserPaged(10L, null, 0, 10);

            // Assert
            assertThat(result.getContent()).hasSize(1);
            verify(evaluatorQueryRepository).findAllByCreatorUserIdPaged(10L, 0, 10);
            verify(evaluatorQueryRepository, never())
                    .findAllByCreatorUserIdWithSearchTerm(any(), any(), any(), any());
        }

        @Test
        @DisplayName("Debe usar la consulta sin término cuando searchTerm es blank")
        void should_useSimpleQuery_when_searchTermIsBlank() {
            // Arrange
            Page<Evaluator> page = new PageImpl<>(List.of());
            when(evaluatorQueryRepository.findAllByCreatorUserIdPaged(10L, 0, 10)).thenReturn(page);

            // Act
            Page<Evaluator> result = evaluatorQueryService.getEvaluatorsByUserPaged(10L, "   ", 0, 10);

            // Assert
            assertThat(result.getContent()).isEmpty();
            verify(evaluatorQueryRepository).findAllByCreatorUserIdPaged(10L, 0, 10);
        }

        @Test
        @DisplayName("Debe usar la consulta con término aplicando trim")
        void should_useSearchQuery_when_termProvided() {
            // Arrange
            Page<Evaluator> page = new PageImpl<>(List.of(buildEvaluator(1L, 10L)));
            when(evaluatorQueryRepository.findAllByCreatorUserIdWithSearchTerm(
                    eq(10L), eq("juan"), eq(0), eq(10))).thenReturn(page);

            // Act
            Page<Evaluator> result = evaluatorQueryService.getEvaluatorsByUserPaged(10L, "  juan  ", 0, 10);

            // Assert
            assertThat(result.getContent()).hasSize(1);
            verify(evaluatorQueryRepository, never()).findAllByCreatorUserIdPaged(any(), any(), any());
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
                .licenseExpirationDate(LocalDate.of(2028, 12, 31))
                .creatorUserId(creatorUserId)
                .build();
    }
}

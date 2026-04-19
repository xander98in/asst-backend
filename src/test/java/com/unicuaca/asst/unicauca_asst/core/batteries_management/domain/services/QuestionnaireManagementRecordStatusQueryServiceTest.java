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
import com.unicuaca.asst.unicauca_asst.common.exceptions.EntityNotFoundPersException;
import com.unicuaca.asst.unicauca_asst.common.exceptions.structure.ErrorCode;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.models.QuestionnaireManagementRecordStatus;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.ports.output.QuestionnaireManagementRecordStatusQueryRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class QuestionnaireManagementRecordStatusQueryServiceTest {

    @Mock
    private QuestionnaireManagementRecordStatusQueryRepository questionnaireManagementRecordStatusQueryRepository;

    @Mock
    private ResultFormatterOutputPort resultFormatter;

    @InjectMocks
    private QuestionnaireManagementRecordStatusQueryService questionnaireManagementRecordStatusQueryService;

    // ==================================================================================
    // getQuestionnaireManagementRecordStatusById
    // ==================================================================================

    @Nested
    @DisplayName("getQuestionnaireManagementRecordStatusById")
    class GetQuestionnaireManagementRecordStatusById {

        @Test
        @DisplayName("Debe retornar estado cuando existe por ID")
        void should_returnStatus_when_foundById() {
            // Arrange
            QuestionnaireManagementRecordStatus status = QuestionnaireManagementRecordStatus.builder()
                    .id(1L).name("Creado").build();
            when(questionnaireManagementRecordStatusQueryRepository
                    .getQuestionnaireManagementRecordStatusById(1L))
                    .thenReturn(Optional.of(status));

            // Act
            QuestionnaireManagementRecordStatus result = questionnaireManagementRecordStatusQueryService
                    .getQuestionnaireManagementRecordStatusById(1L);

            // Assert
            assertThat(result).isNotNull();
            assertThat(result.getName()).isEqualTo("Creado");
        }

        @Test
        @DisplayName("Debe lanzar EntityNotFound cuando el estado no existe por ID")
        void should_throwEntityNotFound_when_statusNotFoundById() {
            // Arrange
            when(questionnaireManagementRecordStatusQueryRepository
                    .getQuestionnaireManagementRecordStatusById(99L))
                    .thenReturn(Optional.empty());
            doThrow(new EntityNotFoundPersException(
                    ErrorCode.QUESTIONNAIRE_MGMT_STATUS_NOT_FOUND.getCode(),
                    ErrorCode.QUESTIONNAIRE_MGMT_STATUS_NOT_FOUND.getMessageKey(),
                    "user.questionnaire_management.status_id_not_found",
                    new Object[]{"99"}))
                .when(resultFormatter).throwEntityNotFound(
                    any(ErrorCode.class), anyString(), any());

            // Act & Assert
            assertThatThrownBy(() -> questionnaireManagementRecordStatusQueryService
                    .getQuestionnaireManagementRecordStatusById(99L))
                    .isInstanceOf(EntityNotFoundPersException.class);

            verify(resultFormatter).throwEntityNotFound(
                    ErrorCode.QUESTIONNAIRE_MGMT_STATUS_NOT_FOUND,
                    "user.questionnaire_management.status_id_not_found",
                    "99");
        }
    }

    // ==================================================================================
    // getQuestionnaireManagementRecordStatusByName
    // ==================================================================================

    @Nested
    @DisplayName("getQuestionnaireManagementRecordStatusByName")
    class GetQuestionnaireManagementRecordStatusByName {

        @Test
        @DisplayName("Debe retornar estado cuando existe por nombre")
        void should_returnStatus_when_foundByName() {
            // Arrange
            QuestionnaireManagementRecordStatus status = QuestionnaireManagementRecordStatus.builder()
                    .id(1L).name("Creado").build();
            when(questionnaireManagementRecordStatusQueryRepository
                    .getQuestionnaireManagementRecordStatusByName("Creado"))
                    .thenReturn(Optional.of(status));

            // Act
            QuestionnaireManagementRecordStatus result = questionnaireManagementRecordStatusQueryService
                    .getQuestionnaireManagementRecordStatusByName("Creado");

            // Assert
            assertThat(result).isNotNull();
            assertThat(result.getName()).isEqualTo("Creado");
        }

        @Test
        @DisplayName("Debe lanzar EntityNotFound cuando el estado no existe por nombre")
        void should_throwEntityNotFound_when_statusNotFoundByName() {
            // Arrange
            when(questionnaireManagementRecordStatusQueryRepository
                    .getQuestionnaireManagementRecordStatusByName("Inexistente"))
                    .thenReturn(Optional.empty());
            doThrow(new EntityNotFoundPersException(
                    ErrorCode.QUESTIONNAIRE_MGMT_STATUS_NOT_FOUND.getCode(),
                    ErrorCode.QUESTIONNAIRE_MGMT_STATUS_NOT_FOUND.getMessageKey(),
                    "user.questionnaire_management.status_name_not_found",
                    new Object[]{"Inexistente"}))
                .when(resultFormatter).throwEntityNotFound(
                    any(ErrorCode.class), anyString(), any());

            // Act & Assert
            assertThatThrownBy(() -> questionnaireManagementRecordStatusQueryService
                    .getQuestionnaireManagementRecordStatusByName("Inexistente"))
                    .isInstanceOf(EntityNotFoundPersException.class);

            verify(resultFormatter).throwEntityNotFound(
                    ErrorCode.QUESTIONNAIRE_MGMT_STATUS_NOT_FOUND,
                    "user.questionnaire_management.status_name_not_found",
                    "Inexistente");
        }
    }

    // ==================================================================================
    // getAllQuestionnaireManagementRecordStatuses
    // ==================================================================================

    @Nested
    @DisplayName("getAllQuestionnaireManagementRecordStatuses")
    class GetAllQuestionnaireManagementRecordStatuses {

        @Test
        @DisplayName("Debe retornar todos los estados disponibles")
        void should_returnAllStatuses() {
            // Arrange
            List<QuestionnaireManagementRecordStatus> statuses = List.of(
                    QuestionnaireManagementRecordStatus.builder().id(1L).name("Creado").build(),
                    QuestionnaireManagementRecordStatus.builder().id(2L).name("En diligenciamiento").build(),
                    QuestionnaireManagementRecordStatus.builder().id(3L).name("Diligenciado").build()
            );
            when(questionnaireManagementRecordStatusQueryRepository
                    .getAllQuestionnaireManagementRecordStatuses())
                    .thenReturn(statuses);

            // Act
            List<QuestionnaireManagementRecordStatus> result = questionnaireManagementRecordStatusQueryService
                    .getAllQuestionnaireManagementRecordStatuses();

            // Assert
            assertThat(result).hasSize(3);
        }
    }
}

package com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.services;

import java.util.List;

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
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.models.QuestionnaireResponse;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.ports.output.QuestionnaireManagementRecordQueryRepository;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.ports.output.QuestionnaireResponseQueryRepository;

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
class QuestionnaireResponseQueryServiceTest {

    @Mock
    private QuestionnaireManagementRecordQueryRepository questionnaireManagementRecordQueryRepository;

    @Mock
    private QuestionnaireResponseQueryRepository questionnaireResponseQueryRepository;

    @Mock
    private ResultFormatterOutputPort resultFormatter;

    @InjectMocks
    private QuestionnaireResponseQueryService questionnaireResponseQueryService;

    // ==================================================================================
    // getResponsesByQuestionnaireManagementRecordId
    // ==================================================================================

    @Nested
    @DisplayName("getResponsesByQuestionnaireManagementRecordId")
    class GetResponsesByQuestionnaireManagementRecordId {

        @Test
        @DisplayName("Debe retornar respuestas cuando el registro existe y tiene respuestas")
        void should_returnResponses_when_recordExistsAndHasResponses() {
            // Arrange
            List<QuestionnaireResponse> responses = List.of(
                    QuestionnaireResponse.builder().id(1L).build(),
                    QuestionnaireResponse.builder().id(2L).build()
            );
            when(questionnaireManagementRecordQueryRepository.existsById(1L)).thenReturn(true);
            when(questionnaireResponseQueryRepository.findAllByRecordIdWithAllRelations(1L))
                    .thenReturn(responses);

            // Act
            List<QuestionnaireResponse> result = questionnaireResponseQueryService
                    .getResponsesByQuestionnaireManagementRecordId(1L);

            // Assert
            assertThat(result).hasSize(2);
        }

        @Test
        @DisplayName("Debe retornar lista vacía cuando el registro existe pero no tiene respuestas")
        void should_returnEmptyList_when_recordExistsButNoResponses() {
            // Arrange
            when(questionnaireManagementRecordQueryRepository.existsById(1L)).thenReturn(true);
            when(questionnaireResponseQueryRepository.findAllByRecordIdWithAllRelations(1L))
                    .thenReturn(List.of());

            // Act
            List<QuestionnaireResponse> result = questionnaireResponseQueryService
                    .getResponsesByQuestionnaireManagementRecordId(1L);

            // Assert
            assertThat(result).isNotNull();
            assertThat(result).isEmpty();
        }

        @Test
        @DisplayName("Debe lanzar EntityNotFound cuando el registro no existe")
        void should_throwEntityNotFound_when_recordDoesNotExist() {
            // Arrange
            when(questionnaireManagementRecordQueryRepository.existsById(1L)).thenReturn(false);
            doThrow(new EntityNotFoundPersException(
                    ErrorCode.QUESTIONNAIRE_MGMT_RECORD_NOT_FOUND.getCode(),
                    ErrorCode.QUESTIONNAIRE_MGMT_RECORD_NOT_FOUND.getMessageKey(),
                    "user.responses.query_not_found",
                    new Object[]{1L}))
                .when(resultFormatter).throwEntityNotFound(
                    any(ErrorCode.class), anyString(), any());

            // Act & Assert
            assertThatThrownBy(() -> questionnaireResponseQueryService
                    .getResponsesByQuestionnaireManagementRecordId(1L))
                    .isInstanceOf(EntityNotFoundPersException.class);

            verify(resultFormatter).throwEntityNotFound(
                    ErrorCode.QUESTIONNAIRE_MGMT_RECORD_NOT_FOUND,
                    "user.responses.query_not_found",
                    1L);
            verify(questionnaireResponseQueryRepository, never())
                    .findAllByRecordIdWithAllRelations(anyLong());
        }
    }
}

package com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.services;

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
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.models.QuestionnaireManagementRecord;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.ports.output.QuestionnaireManagementRecordQueryRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class QuestionnaireManagementRecordQueryServiceTest {

    @Mock
    private QuestionnaireManagementRecordQueryRepository questionnaireManagementRecordQueryRepository;

    @Mock
    private ResultFormatterOutputPort resultFormatter;

    @InjectMocks
    private QuestionnaireManagementRecordQueryService questionnaireManagementRecordQueryService;

    // ==================================================================================
    // getByBatteryRecordIdAndQuestionnaireAbbreviation
    // ==================================================================================

    @Nested
    @DisplayName("getByBatteryRecordIdAndQuestionnaireAbbreviation")
    class GetByBatteryRecordIdAndQuestionnaireAbbreviation {

        @Test
        @DisplayName("Debe retornar registro cuando existe por ID de batería y abreviatura")
        void should_returnRecord_when_foundByBatteryRecordIdAndAbbreviation() {
            // Arrange
            QuestionnaireManagementRecord record = QuestionnaireManagementRecord.builder()
                    .id(1L)
                    .build();

            when(questionnaireManagementRecordQueryRepository
                    .findByBatteryManagementRecordIdAndQuestionnaireAbbreviationWithAll(1L, "ILA"))
                    .thenReturn(Optional.of(record));

            // Act
            QuestionnaireManagementRecord result = questionnaireManagementRecordQueryService
                    .getByBatteryRecordIdAndQuestionnaireAbbreviation(1L, "ILA");

            // Assert
            assertThat(result).isNotNull();
            assertThat(result.getId()).isEqualTo(1L);
        }

        @Test
        @DisplayName("Debe lanzar EntityNotFound cuando el registro no existe")
        void should_throwEntityNotFound_when_recordNotFound() {
            // Arrange
            when(questionnaireManagementRecordQueryRepository
                    .findByBatteryManagementRecordIdAndQuestionnaireAbbreviationWithAll(1L, "ILA"))
                    .thenReturn(Optional.empty());
            doThrow(new EntityNotFoundPersException(
                    ErrorCode.QUESTIONNAIRE_MGMT_RECORD_NOT_FOUND.getCode(),
                    ErrorCode.QUESTIONNAIRE_MGMT_RECORD_NOT_FOUND.getMessageKey(),
                    "user.questionnaire_management.query_not_found",
                    new Object[]{"ILA"}))
                .when(resultFormatter).throwEntityNotFound(
                    any(ErrorCode.class), anyString(), any());

            // Act & Assert
            assertThatThrownBy(() -> questionnaireManagementRecordQueryService
                    .getByBatteryRecordIdAndQuestionnaireAbbreviation(1L, "ILA"))
                    .isInstanceOf(EntityNotFoundPersException.class);

            verify(resultFormatter).throwEntityNotFound(
                    ErrorCode.QUESTIONNAIRE_MGMT_RECORD_NOT_FOUND,
                    "user.questionnaire_management.query_not_found",
                    "ILA");
        }
    }
}

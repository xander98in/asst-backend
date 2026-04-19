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
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.models.Questionnaire;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.ports.output.QuestionnaireQueryRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class QuestionnaireQueryServiceTest {

    @Mock
    private QuestionnaireQueryRepository questionnaireQueryRepository;

    @Mock
    private ResultFormatterOutputPort resultFormatter;

    @InjectMocks
    private QuestionnaireQueryService questionnaireQueryService;

    // ==================================================================================
    // getAllQuestionnaires
    // ==================================================================================

    @Nested
    @DisplayName("getAllQuestionnaires")
    class GetAllQuestionnaires {

        @Test
        @DisplayName("Debe retornar todos los cuestionarios disponibles")
        void should_returnAllQuestionnaires() {
            // Arrange
            List<Questionnaire> questionnaires = List.of(
                    Questionnaire.builder().id(1L).name("Intralaboral Forma A").abbreviation("ILA").build(),
                    Questionnaire.builder().id(2L).name("Extralaboral").abbreviation("EXT").build()
            );
            when(questionnaireQueryRepository.getAll()).thenReturn(questionnaires);

            // Act
            List<Questionnaire> result = questionnaireQueryService.getAllQuestionnaires();

            // Assert
            assertThat(result).hasSize(2);
        }
    }

    // ==================================================================================
    // getQuestionnaireByAbbreviation
    // ==================================================================================

    @Nested
    @DisplayName("getQuestionnaireByAbbreviation")
    class GetQuestionnaireByAbbreviation {

        @Test
        @DisplayName("Debe retornar cuestionario cuando existe por abreviatura")
        void should_returnQuestionnaire_when_foundByAbbreviation() {
            // Arrange
            Questionnaire questionnaire = Questionnaire.builder()
                    .id(1L).name("Intralaboral Forma A").abbreviation("ILA").build();
            when(questionnaireQueryRepository.getByAbbreviation("ILA"))
                    .thenReturn(Optional.of(questionnaire));

            // Act
            Questionnaire result = questionnaireQueryService.getQuestionnaireByAbbreviation("ILA");

            // Assert
            assertThat(result).isNotNull();
            assertThat(result.getAbbreviation()).isEqualTo("ILA");
        }

        @Test
        @DisplayName("Debe lanzar EntityNotFound cuando el cuestionario no existe por abreviatura")
        void should_throwEntityNotFound_when_questionnaireNotFoundByAbbreviation() {
            // Arrange
            when(questionnaireQueryRepository.getByAbbreviation("XYZ"))
                    .thenReturn(Optional.empty());
            doThrow(new EntityNotFoundPersException(
                    ErrorCode.QUESTIONNAIRE_NOT_FOUND_BY_REF.getCode(),
                    ErrorCode.QUESTIONNAIRE_NOT_FOUND_BY_REF.getMessageKey(),
                    "user.questionnaire.not_found",
                    new Object[]{"XYZ"}))
                .when(resultFormatter).throwEntityNotFound(
                    any(ErrorCode.class), anyString(), any());

            // Act & Assert
            assertThatThrownBy(() -> questionnaireQueryService
                    .getQuestionnaireByAbbreviation("XYZ"))
                    .isInstanceOf(EntityNotFoundPersException.class);

            verify(resultFormatter).throwEntityNotFound(
                    ErrorCode.QUESTIONNAIRE_NOT_FOUND_BY_REF,
                    "user.questionnaire.not_found",
                    "XYZ");
        }
    }
}

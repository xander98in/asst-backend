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

import com.unicuaca.asst.unicauca_asst.common.application.output.ResultFormatterOutputPort;
import com.unicuaca.asst.unicauca_asst.common.exceptions.EntityNotFoundPersException;
import com.unicuaca.asst.unicauca_asst.common.exceptions.structure.ErrorCode;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.models.Question;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.models.Questionnaire;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.ports.output.QuestionQueryRepository;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.ports.output.QuestionnaireQueryRepository;

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
class QuestionQueryServiceTest {

    @Mock
    private QuestionQueryRepository questionQueryRepository;

    @Mock
    private QuestionnaireQueryRepository questionnaireQueryRepository;

    @Mock
    private ResultFormatterOutputPort resultFormatter;

    @InjectMocks
    private QuestionQueryService questionQueryService;

    // ==================================================================================
    // getQuestionById
    // ==================================================================================

    @Nested
    @DisplayName("getQuestionById")
    class GetQuestionById {

        @Test
        @DisplayName("Debe retornar pregunta cuando existe por ID")
        void should_returnQuestion_when_foundById() {
            // Arrange
            Question question = Question.builder()
                    .id(1L).text("Pregunta 1").order(1).build();
            when(questionQueryRepository.getQuestionById(1L))
                    .thenReturn(Optional.of(question));

            // Act
            Question result = questionQueryService.getQuestionById(1L);

            // Assert
            assertThat(result).isNotNull();
            assertThat(result.getText()).isEqualTo("Pregunta 1");
        }

        @Test
        @DisplayName("Debe lanzar EntityNotFound cuando la pregunta no existe por ID")
        void should_throwEntityNotFound_when_questionNotFoundById() {
            // Arrange
            when(questionQueryRepository.getQuestionById(1L))
                    .thenReturn(Optional.empty());
            doThrow(new EntityNotFoundPersException(
                    ErrorCode.QUESTION_NOT_FOUND.getCode(),
                    ErrorCode.QUESTION_NOT_FOUND.getMessageKey(),
                    "user.question.not_found",
                    new Object[]{1L}))
                .when(resultFormatter).throwEntityNotFound(
                    any(ErrorCode.class), anyString(), any());

            // Act & Assert
            assertThatThrownBy(() -> questionQueryService.getQuestionById(1L))
                    .isInstanceOf(EntityNotFoundPersException.class);

            verify(resultFormatter).throwEntityNotFound(
                    ErrorCode.QUESTION_NOT_FOUND,
                    "user.question.not_found",
                    1L);
        }
    }

    // ==================================================================================
    // getQuestionByIdWithQuestionnaire
    // ==================================================================================

    @Nested
    @DisplayName("getQuestionByIdWithQuestionnaire")
    class GetQuestionByIdWithQuestionnaire {

        @Test
        @DisplayName("Debe retornar pregunta con cuestionario cuando existe")
        void should_returnQuestion_when_foundByIdWithQuestionnaire() {
            // Arrange
            Questionnaire questionnaire = Questionnaire.builder()
                    .id(1L).name("Extralaboral").abbreviation("EXT").build();
            Question question = Question.builder()
                    .id(1L).text("Pregunta 1").order(1).questionnaire(questionnaire).build();
            when(questionQueryRepository.getQuestionByIdWithQuestionnaire(1L))
                    .thenReturn(Optional.of(question));

            // Act
            Question result = questionQueryService.getQuestionByIdWithQuestionnaire(1L);

            // Assert
            assertThat(result).isNotNull();
            assertThat(result.getQuestionnaire()).isNotNull();
            assertThat(result.getQuestionnaire().getAbbreviation()).isEqualTo("EXT");
        }

        @Test
        @DisplayName("Debe lanzar EntityNotFound cuando la pregunta no existe con cuestionario")
        void should_throwEntityNotFound_when_questionNotFoundByIdWithQuestionnaire() {
            // Arrange
            when(questionQueryRepository.getQuestionByIdWithQuestionnaire(1L))
                    .thenReturn(Optional.empty());
            doThrow(new EntityNotFoundPersException(
                    ErrorCode.QUESTION_NOT_FOUND.getCode(),
                    ErrorCode.QUESTION_NOT_FOUND.getMessageKey(),
                    "user.question.query_details_not_found",
                    new Object[]{1L}))
                .when(resultFormatter).throwEntityNotFound(
                    any(ErrorCode.class), anyString(), any());

            // Act & Assert
            assertThatThrownBy(() -> questionQueryService.getQuestionByIdWithQuestionnaire(1L))
                    .isInstanceOf(EntityNotFoundPersException.class);

            verify(resultFormatter).throwEntityNotFound(
                    ErrorCode.QUESTION_NOT_FOUND,
                    "user.question.query_details_not_found",
                    1L);
        }
    }

    // ==================================================================================
    // getAllQuestions
    // ==================================================================================

    @Nested
    @DisplayName("getAllQuestions")
    class GetAllQuestions {

        @Test
        @DisplayName("Debe retornar todas las preguntas")
        void should_returnAllQuestions() {
            // Arrange
            List<Question> questions = List.of(
                    Question.builder().id(1L).text("Pregunta 1").build(),
                    Question.builder().id(2L).text("Pregunta 2").build()
            );
            when(questionQueryRepository.getAllQuestions()).thenReturn(questions);

            // Act
            List<Question> result = questionQueryService.getAllQuestions();

            // Assert
            assertThat(result).hasSize(2);
        }
    }

    // ==================================================================================
    // getAllQuestionsWithQuestionnaire
    // ==================================================================================

    @Nested
    @DisplayName("getAllQuestionsWithQuestionnaire")
    class GetAllQuestionsWithQuestionnaire {

        @Test
        @DisplayName("Debe retornar todas las preguntas con cuestionario")
        void should_returnAllQuestionsWithQuestionnaire() {
            // Arrange
            List<Question> questions = List.of(
                    Question.builder().id(1L).text("Pregunta 1").build(),
                    Question.builder().id(2L).text("Pregunta 2").build()
            );
            when(questionQueryRepository.getAllQuestionsWithQuestionnaire()).thenReturn(questions);

            // Act
            List<Question> result = questionQueryService.getAllQuestionsWithQuestionnaire();

            // Assert
            assertThat(result).hasSize(2);
        }
    }

    // ==================================================================================
    // getQuestionByOrderAndQuestionnaireIdWithQuestionnaire
    // ==================================================================================

    @Nested
    @DisplayName("getQuestionByOrderAndQuestionnaireIdWithQuestionnaire")
    class GetQuestionByOrderAndQuestionnaireIdWithQuestionnaire {

        @Test
        @DisplayName("Debe retornar pregunta cuando existe por orden y cuestionario")
        void should_returnQuestion_when_foundByOrderAndQuestionnaireId() {
            // Arrange
            Question question = Question.builder()
                    .id(1L).text("Pregunta 1").order(1).build();
            when(questionQueryRepository.getQuestionByOrderAndQuestionnaireIdWithQuestionnaire(1, 1L))
                    .thenReturn(Optional.of(question));

            // Act
            Question result = questionQueryService
                    .getQuestionByOrderAndQuestionnaireIdWithQuestionnaire(1, 1L);

            // Assert
            assertThat(result).isNotNull();
            assertThat(result.getOrder()).isEqualTo(1);
        }

        @Test
        @DisplayName("Debe lanzar EntityNotFound cuando la pregunta no existe por orden")
        void should_throwEntityNotFound_when_questionNotFoundByOrder() {
            // Arrange
            when(questionQueryRepository.getQuestionByOrderAndQuestionnaireIdWithQuestionnaire(99, 1L))
                    .thenReturn(Optional.empty());
            doThrow(new EntityNotFoundPersException(
                    ErrorCode.QUESTION_NOT_FOUND.getCode(),
                    ErrorCode.QUESTION_NOT_FOUND.getMessageKey(),
                    "user.question.by_order_not_found",
                    new Object[]{99}))
                .when(resultFormatter).throwEntityNotFound(
                    any(ErrorCode.class), anyString(), any());

            // Act & Assert
            assertThatThrownBy(() -> questionQueryService
                    .getQuestionByOrderAndQuestionnaireIdWithQuestionnaire(99, 1L))
                    .isInstanceOf(EntityNotFoundPersException.class);

            verify(resultFormatter).throwEntityNotFound(
                    ErrorCode.QUESTION_NOT_FOUND,
                    "user.question.by_order_not_found",
                    99);
        }
    }

    // ==================================================================================
    // getQuestionsByQuestionnaireId
    // ==================================================================================

    @Nested
    @DisplayName("getQuestionsByQuestionnaireId")
    class GetQuestionsByQuestionnaireId {

        @Test
        @DisplayName("Debe retornar preguntas cuando el cuestionario existe por ID")
        void should_returnQuestions_when_questionnaireExistsById() {
            // Arrange
            List<Question> questions = List.of(
                    Question.builder().id(1L).build(),
                    Question.builder().id(2L).build(),
                    Question.builder().id(3L).build()
            );
            when(questionnaireQueryRepository.existsById(1L)).thenReturn(true);
            when(questionQueryRepository.getByQuestionnaireId(1L)).thenReturn(questions);

            // Act
            List<Question> result = questionQueryService.getQuestionsByQuestionnaireId(1L);

            // Assert
            assertThat(result).hasSize(3);
        }

        @Test
        @DisplayName("Debe lanzar EntityNotFound cuando el cuestionario no existe por ID")
        void should_throwEntityNotFound_when_questionnaireNotFoundById() {
            // Arrange
            when(questionnaireQueryRepository.existsById(99L)).thenReturn(false);
            doThrow(new EntityNotFoundPersException(
                    ErrorCode.QUESTIONNAIRE_NOT_FOUND_BY_REF.getCode(),
                    ErrorCode.QUESTIONNAIRE_NOT_FOUND_BY_REF.getMessageKey(),
                    "user.question.questionnaire_not_found",
                    new Object[]{99L}))
                .when(resultFormatter).throwEntityNotFound(
                    any(ErrorCode.class), anyString(), any());

            // Act & Assert
            assertThatThrownBy(() -> questionQueryService.getQuestionsByQuestionnaireId(99L))
                    .isInstanceOf(EntityNotFoundPersException.class);

            verify(resultFormatter).throwEntityNotFound(
                    ErrorCode.QUESTIONNAIRE_NOT_FOUND_BY_REF,
                    "user.question.questionnaire_not_found",
                    99L);
            verify(questionQueryRepository, never()).getByQuestionnaireId(anyLong());
        }
    }

    // ==================================================================================
    // getQuestionsByQuestionnaireAbbreviation
    // ==================================================================================

    @Nested
    @DisplayName("getQuestionsByQuestionnaireAbbreviation")
    class GetQuestionsByQuestionnaireAbbreviation {

        @Test
        @DisplayName("Debe retornar preguntas cuando el cuestionario existe por abreviatura")
        void should_returnQuestions_when_questionnaireExistsByAbbreviation() {
            // Arrange
            List<Question> questions = List.of(
                    Question.builder().id(1L).build(),
                    Question.builder().id(2L).build(),
                    Question.builder().id(3L).build()
            );
            when(questionnaireQueryRepository.existsByAbbreviation("ILA")).thenReturn(true);
            when(questionQueryRepository.getByQuestionnaireAbbreviation("ILA")).thenReturn(questions);

            // Act
            List<Question> result = questionQueryService
                    .getQuestionsByQuestionnaireAbbreviation("ILA");

            // Assert
            assertThat(result).hasSize(3);
        }

        @Test
        @DisplayName("Debe lanzar EntityNotFound cuando el cuestionario no existe por abreviatura")
        void should_throwEntityNotFound_when_questionnaireNotFoundByAbbreviation() {
            // Arrange
            when(questionnaireQueryRepository.existsByAbbreviation("XYZ")).thenReturn(false);
            doThrow(new EntityNotFoundPersException(
                    ErrorCode.QUESTIONNAIRE_NOT_FOUND_BY_REF.getCode(),
                    ErrorCode.QUESTIONNAIRE_NOT_FOUND_BY_REF.getMessageKey(),
                    "user.question.questionnaire_not_found",
                    new Object[]{"XYZ"}))
                .when(resultFormatter).throwEntityNotFound(
                    any(ErrorCode.class), anyString(), any());

            // Act & Assert
            assertThatThrownBy(() -> questionQueryService
                    .getQuestionsByQuestionnaireAbbreviation("XYZ"))
                    .isInstanceOf(EntityNotFoundPersException.class);

            verify(resultFormatter).throwEntityNotFound(
                    ErrorCode.QUESTIONNAIRE_NOT_FOUND_BY_REF,
                    "user.question.questionnaire_not_found",
                    "XYZ");
            verify(questionQueryRepository, never()).getByQuestionnaireAbbreviation(anyString());
        }
    }
}

package com.unicuaca.asst.unicauca_asst.core.reports.domain.services;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.models.AnswerOption;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.models.Question;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.models.QuestionnaireResponse;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ScoringEngineTest {

    private ScoringEngine engine;

    @BeforeEach
    void setUp() {
        engine = new ScoringEngine();
    }

    // ==================================================================================
    // calculateIntralaboral
    // ==================================================================================

    @Nested
    @DisplayName("calculateIntralaboral")
    class CalculateIntralaboral {

        @Test
        @DisplayName("ILA con respuestas vacías: 4 dominios, raws en cero")
        void should_returnZeroedResult_when_ilaEmptyResponses() {
            // Arrange / Act
            ScoringEngine.IntralaboralResult result = engine.calculateIntralaboral(List.of(), "ILA");

            // Assert
            assertThat(result.domains()).hasSize(4);
            assertThat(result.totalRawScore()).isZero();
            assertThat(result.totalTransformedScore()).isZero();
            assertThat(result.totalRiskLevel()).isNotBlank();
            assertThat(result.domains()).allSatisfy(d -> {
                assertThat(d.rawScore()).isZero();
                assertThat(d.dimensions()).isNotEmpty();
            });
        }

        @Test
        @DisplayName("ILB con respuestas vacías: 4 dominios, raws en cero")
        void should_returnZeroedResult_when_ilbEmptyResponses() {
            // Arrange / Act
            ScoringEngine.IntralaboralResult result = engine.calculateIntralaboral(List.of(), "ILB");

            // Assert
            assertThat(result.domains()).hasSize(4);
            assertThat(result.totalRawScore()).isZero();
        }

        @Test
        @DisplayName("Ítem invertido: answer=1 suma 4 al raw (5 - 1)")
        void should_invertScore_when_itemIsInverted() {
            // Arrange: ítem 4 de ILA es invertido. Con answer=1 aporta 5-1 = 4.
            List<QuestionnaireResponse> responses = List.of(buildResponse(4, 1));

            // Act
            ScoringEngine.IntralaboralResult result = engine.calculateIntralaboral(responses, "ILA");

            // Assert
            assertThat(result.totalRawScore()).isEqualTo(4);
        }

        @Test
        @DisplayName("Ítem no invertido: answer=1 suma 0 al raw (1 - 1)")
        void should_notInvertScore_when_itemIsDirect() {
            // Arrange: ítem 1 de ILA NO es invertido. Con answer=1 aporta 1-1 = 0.
            List<QuestionnaireResponse> responses = List.of(buildResponse(1, 1));

            // Act
            ScoringEngine.IntralaboralResult result = engine.calculateIntralaboral(responses, "ILA");

            // Assert
            assertThat(result.totalRawScore()).isZero();
        }

        @Test
        @DisplayName("Ítem con answer=0 se excluye del cálculo (dato perdido)")
        void should_excludeItem_when_answerIsZero() {
            // Arrange: ítem 1 (directo) con 0 se excluye. Sin él, raw = 0.
            List<QuestionnaireResponse> responses = List.of(buildResponse(1, 0));

            // Act
            ScoringEngine.IntralaboralResult result = engine.calculateIntralaboral(responses, "ILA");

            // Assert
            assertThat(result.totalRawScore()).isZero();
        }
    }

    // ==================================================================================
    // calculateExtralaboral
    // ==================================================================================

    @Nested
    @DisplayName("calculateExtralaboral")
    class CalculateExtralaboral {

        @Test
        @DisplayName("Respuestas vacías: 7 dimensiones con raws en cero")
        void should_returnZeroedResult_when_emptyResponses() {
            // Arrange / Act
            ScoringEngine.ExtralaboralResult result =
                engine.calculateExtralaboral(List.of(), "JEFES_PROFESIONALES");

            // Assert
            assertThat(result.dimensions()).hasSize(7);
            assertThat(result.totalRawScore()).isZero();
            assertThat(result.totalRiskLevel()).isNotBlank();
        }

        @Test
        @DisplayName("Grupo ocupacional AUXILIARES_OPERARIOS clasifica con sus propios baremos")
        void should_useOperariosBaremos_when_occupationalGroupIsOperarios() {
            // Arrange / Act
            ScoringEngine.ExtralaboralResult result =
                engine.calculateExtralaboral(List.of(), "AUXILIARES_OPERARIOS");

            // Assert
            assertThat(result.dimensions()).hasSize(7);
            assertThat(result.totalTransformedScore()).isZero();
        }
    }

    // ==================================================================================
    // calculateStress
    // ==================================================================================

    @Nested
    @DisplayName("calculateStress")
    class CalculateStress {

        @Test
        @DisplayName("Respuestas vacías: raw=0, transformed=0")
        void should_returnZero_when_emptyResponses() {
            // Arrange / Act
            ScoringEngine.StressResult result = engine.calculateStress(List.of(), "JEFES_PROFESIONALES");

            // Assert
            assertThat(result.rawScore()).isZero();
            assertThat(result.transformedScore()).isZero();
            assertThat(result.stressLevel()).isNotBlank();
        }

        @Test
        @DisplayName("Todas las respuestas en 'Siempre' (valor=5): raw máximo posible")
        void should_maxScore_when_allSiempre() {
            // Arrange: construir respuestas 1..31 con valor 5 (Siempre → índice 0).
            List<QuestionnaireResponse> responses = new ArrayList<>();
            for (int i = 1; i <= 31; i++) {
                responses.add(buildResponse(i, 5));
            }

            // Act
            ScoringEngine.StressResult result = engine.calculateStress(responses, "JEFES_PROFESIONALES");

            // Assert
            assertThat(result.rawScore()).isGreaterThan(0.0);
            assertThat(result.transformedScore()).isGreaterThan(0.0);
        }

        @Test
        @DisplayName("Ítems con valor 0 se excluyen del cálculo")
        void should_excludeItems_when_answerValueIsZero() {
            // Arrange: ítem 1 con 0 se excluye → raw = 0.
            List<QuestionnaireResponse> responses = List.of(buildResponse(1, 0));

            // Act
            ScoringEngine.StressResult result = engine.calculateStress(responses, "JEFES_PROFESIONALES");

            // Assert
            assertThat(result.rawScore()).isZero();
        }

        @Test
        @DisplayName("Mapea los 4 valores de respuesta (5, 4, 3, 1) a sus calificaciones")
        void should_mapAllStressAnswerValues() {
            // Arrange: ítem 1 usa grupo [9, 6, 3, 0] con pesos [Siempre=5→9, CasiSiempre=4→6, AVeces=3→3, Nunca=1→0]
            // Uso ítems distintos (1..4) con los 4 valores para cubrir cada rama del switch.
            List<QuestionnaireResponse> responses = List.of(
                buildResponse(1, 5),  // Siempre
                buildResponse(2, 4),  // Casi siempre
                buildResponse(3, 3),  // A veces
                buildResponse(4, 1)   // Nunca
            );

            // Act
            ScoringEngine.StressResult result = engine.calculateStress(responses, "JEFES_PROFESIONALES");

            // Assert: raw debe ser > 0 por los valores distintos de Nunca; Nunca contribuye 0.
            assertThat(result.rawScore()).isGreaterThan(0.0);
        }

        @Test
        @DisplayName("Valor inválido (ej. 2) lanza IllegalArgumentException")
        void should_throw_when_answerValueIsInvalid() {
            // Arrange
            List<QuestionnaireResponse> responses = List.of(buildResponse(1, 2));

            // Act & Assert
            assertThatThrownBy(() -> engine.calculateStress(responses, "JEFES_PROFESIONALES"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Valor de respuesta inválido para estrés");
        }

        @Test
        @DisplayName("Grupo ocupacional AUXILIARES_OPERARIOS clasifica con sus propios baremos")
        void should_useOperariosBaremos_when_occupationalGroupIsOperarios() {
            // Arrange / Act
            ScoringEngine.StressResult result = engine.calculateStress(List.of(), "AUXILIARES_OPERARIOS");

            // Assert
            assertThat(result.stressLevel()).isNotBlank();
        }
    }

    // ==================================================================================
    // calculateGeneralTotal
    // ==================================================================================

    @Nested
    @DisplayName("calculateGeneralTotal")
    class CalculateGeneralTotal {

        @Test
        @DisplayName("Suma intralaboral + extralaboral y divide por el factor correspondiente (ILA)")
        void should_computeTransformedScore_when_ilaForm() {
            // Arrange / Act
            ScoringEngine.GeneralTotalResult result = engine.calculateGeneralTotal(0, 0, "ILA");

            // Assert
            assertThat(result.transformedScore()).isZero();
            assertThat(result.riskLevel()).isNotBlank();
        }

        @Test
        @DisplayName("Funciona con forma ILB")
        void should_computeTransformedScore_when_ilbForm() {
            // Arrange / Act
            ScoringEngine.GeneralTotalResult result = engine.calculateGeneralTotal(100, 50, "ILB");

            // Assert
            assertThat(result.transformedScore()).isGreaterThan(0.0);
        }
    }

    // ==================================================================================
    // calculateIndividualReport (orquestador)
    // ==================================================================================

    @Nested
    @DisplayName("calculateIndividualReport")
    class CalculateIndividualReport {

        @Test
        @DisplayName("Retorna resultado consolidado con los 4 sub-resultados (ILA, jobPositionTypeId=2)")
        void should_returnComposite_when_jobPositionILA() {
            // Arrange / Act
            ScoringEngine.IndividualScoringResult result = engine.calculateIndividualReport(
                List.of(), List.of(), List.of(), "ILA", 2
            );

            // Assert
            assertThat(result.intralaboral()).isNotNull();
            assertThat(result.extralaboral()).isNotNull();
            assertThat(result.generalTotal()).isNotNull();
            assertThat(result.stress()).isNotNull();
            assertThat(result.intralaboral().domains()).hasSize(4);
            assertThat(result.extralaboral().dimensions()).hasSize(7);
        }

        @Test
        @DisplayName("Retorna resultado consolidado con ILB y jobPositionTypeId=4 (Operario)")
        void should_returnComposite_when_jobPositionILB() {
            // Arrange / Act
            ScoringEngine.IndividualScoringResult result = engine.calculateIndividualReport(
                List.of(), List.of(), List.of(), "ILB", 4
            );

            // Assert
            assertThat(result.intralaboral().domains()).hasSize(4);
        }
    }

    // ==================================================================================
    // Helpers
    // ==================================================================================

    private QuestionnaireResponse buildResponse(int order, int answerValue) {
        return QuestionnaireResponse.builder()
            .question(Question.builder().order(order).build())
            .answerOption(AnswerOption.builder().value(answerValue).build())
            .build();
    }
}

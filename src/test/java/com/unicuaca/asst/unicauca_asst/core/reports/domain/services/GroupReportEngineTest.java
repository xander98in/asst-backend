package com.unicuaca.asst.unicauca_asst.core.reports.domain.services;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import com.unicuaca.asst.unicauca_asst.core.reports.domain.models.enums.SemaphoreColor;

import static org.assertj.core.api.Assertions.assertThat;

class GroupReportEngineTest {

    private GroupReportEngine engine;

    @BeforeEach
    void setUp() {
        engine = new GroupReportEngine();
    }

    // ==================================================================================
    // calculateGroupSummary
    // ==================================================================================

    @Nested
    @DisplayName("calculateGroupSummary")
    class CalculateGroupSummary {

        @Test
        @DisplayName("Mezcla ILA/ILB: cuenta formas y agrega distribución de niveles")
        void should_aggregate_when_mixedForms() {
            // Arrange: 2 ILA + 1 ILB con niveles variados
            List<ScoringEngine.IndividualScoringResult> results = List.of(
                buildResult(10.0, "Riesgo bajo", 8.0, "Riesgo medio", 9.0, "Riesgo medio", 5.0, "Bajo"),
                buildResult(20.0, "Riesgo medio", 15.0, "Riesgo alto", 18.0, "Riesgo alto", 12.0, "Medio"),
                buildResult(30.0, "Riesgo muy alto", 25.0, "Riesgo muy alto", 28.0, "Riesgo muy alto", 20.0, "Alto")
            );
            List<String> forms = List.of("ILA", "ILA", "ILB");

            // Act
            GroupReportEngine.GroupSummaryResult summary = engine.calculateGroupSummary(results, forms);

            // Assert
            assertThat(summary.totalPersons()).isEqualTo(3);
            assertThat(summary.formaACount()).isEqualTo(2);
            assertThat(summary.formaBCount()).isEqualTo(1);
            assertThat(summary.intralaboral().distribution().bajo()).isEqualTo(1);
            assertThat(summary.intralaboral().distribution().medio()).isEqualTo(1);
            assertThat(summary.intralaboral().distribution().muyAlto()).isEqualTo(1);
            assertThat(summary.stress().distribution().bajo()).isEqualTo(1);
            assertThat(summary.stress().distribution().medio()).isEqualTo(1);
            assertThat(summary.stress().distribution().alto()).isEqualTo(1);
            assertThat(summary.intralaboralFormaA().totalPersons()).isEqualTo(2);
            assertThat(summary.intralaboralFormaB().totalPersons()).isEqualTo(1);
        }

        @Test
        @DisplayName("Solo forma ILB: formaA subset devuelve N/A")
        void should_returnNA_when_noResultsForFormA() {
            // Arrange
            List<ScoringEngine.IndividualScoringResult> results = List.of(
                buildResult(20.0, "Riesgo bajo", 10.0, "Riesgo bajo", 15.0, "Riesgo bajo", 8.0, "Bajo")
            );
            List<String> forms = List.of("ILB");

            // Act
            GroupReportEngine.GroupSummaryResult summary = engine.calculateGroupSummary(results, forms);

            // Assert
            assertThat(summary.formaACount()).isZero();
            assertThat(summary.formaBCount()).isEqualTo(1);
            assertThat(summary.intralaboralFormaA().averageRiskLevel()).isEqualTo("N/A");
            assertThat(summary.intralaboralFormaB().totalPersons()).isEqualTo(1);
        }

        @Test
        @DisplayName("Solo forma ILA: formaB subset devuelve N/A")
        void should_returnNA_when_noResultsForFormB() {
            // Arrange
            List<ScoringEngine.IndividualScoringResult> results = List.of(
                buildResult(20.0, "Riesgo bajo", 10.0, "Riesgo bajo", 15.0, "Riesgo bajo", 8.0, "Bajo")
            );
            List<String> forms = List.of("ILA");

            // Act
            GroupReportEngine.GroupSummaryResult summary = engine.calculateGroupSummary(results, forms);

            // Assert
            assertThat(summary.intralaboralFormaA().totalPersons()).isEqualTo(1);
            assertThat(summary.intralaboralFormaB().averageRiskLevel()).isEqualTo("N/A");
        }

        @Test
        @DisplayName("Nivel desconocido cae en la rama default sin alterar la distribución")
        void should_ignoreUnknownLevel_when_levelNotRecognized() {
            // Arrange: nivel "Desconocido" no coincide con ningún case → default.
            List<ScoringEngine.IndividualScoringResult> results = List.of(
                buildResult(5.0, "Desconocido", 3.0, "Desconocido",
                            4.0, "Desconocido", 2.0, "Desconocido")
            );

            // Act
            GroupReportEngine.GroupSummaryResult summary =
                engine.calculateGroupSummary(results, List.of("ILA"));

            // Assert: ningún contador incrementa para niveles desconocidos.
            GroupReportEngine.RiskDistribution dist = summary.intralaboral().distribution();
            assertThat(dist.sinRiesgo()).isZero();
            assertThat(dist.bajo()).isZero();
            assertThat(dist.medio()).isZero();
            assertThat(dist.alto()).isZero();
            assertThat(dist.muyAlto()).isZero();
        }

        @Test
        @DisplayName("Reconoce variantes de nombres: 'Muy bajo' y 'Sin riesgo o riesgo despreciable'")
        void should_countSinRiesgo_when_nameVariantsProvided() {
            // Arrange: stress usa "Muy bajo"; intralaboral usa "Sin riesgo o riesgo despreciable"
            List<ScoringEngine.IndividualScoringResult> results = List.of(
                buildResult(5.0, "Sin riesgo o riesgo despreciable", 3.0, "Sin riesgo o riesgo despreciable",
                            4.0, "Sin riesgo o riesgo despreciable", 2.0, "Muy bajo")
            );

            // Act
            GroupReportEngine.GroupSummaryResult summary =
                engine.calculateGroupSummary(results, List.of("ILA"));

            // Assert
            assertThat(summary.intralaboral().distribution().sinRiesgo()).isEqualTo(1);
            assertThat(summary.stress().distribution().sinRiesgo()).isEqualTo(1);
        }
    }

    // ==================================================================================
    // calculateDomainsAndDimensions
    // ==================================================================================

    @Nested
    @DisplayName("calculateDomainsAndDimensions")
    class CalculateDomainsAndDimensions {

        @Test
        @DisplayName("Agrupa dominios y dimensiones intralaborales y extralaborales")
        void should_aggregate_when_resultsProvided() {
            // Arrange: 2 personas con los mismos dominios/dimensiones
            ScoringEngine.DimensionScore dim1 = new ScoringEngine.DimensionScore("DimA", 5, 40.0, "Riesgo bajo");
            ScoringEngine.DimensionScore dim2 = new ScoringEngine.DimensionScore("DimB", 10, 70.0, "Riesgo alto");
            ScoringEngine.DomainScore domain = new ScoringEngine.DomainScore(
                "Dominio Test", 15, 55.0, "Riesgo medio", List.of(dim1, dim2)
            );

            ScoringEngine.DimensionScore extDim = new ScoringEngine.DimensionScore(
                "ExtDim", 8, 30.0, "Riesgo bajo"
            );

            ScoringEngine.IndividualScoringResult p1 = new ScoringEngine.IndividualScoringResult(
                new ScoringEngine.IntralaboralResult(List.of(domain), 15, 55.0, "Riesgo medio"),
                new ScoringEngine.ExtralaboralResult(List.of(extDim), 8, 30.0, "Riesgo bajo"),
                new ScoringEngine.GeneralTotalResult(50.0, "Riesgo medio"),
                new ScoringEngine.StressResult(10.0, 40.0, "Medio")
            );

            // Act
            GroupReportEngine.DomainsAndDimensionsResult result =
                engine.calculateDomainsAndDimensions(List.of(p1, p1));

            // Assert
            assertThat(result.intralaboralDomains()).hasSize(1);
            GroupReportEngine.DomainDistribution d = result.intralaboralDomains().get(0);
            assertThat(d.domainName()).isEqualTo("Dominio Test");
            assertThat(d.averageTransformedScore()).isEqualTo(55.0);
            assertThat(d.dimensions()).hasSize(2);
            assertThat(d.dimensions().get(0).dimensionName()).isEqualTo("DimA");
            assertThat(result.extralaboralDimensions()).hasSize(1);
            assertThat(result.extralaboralDimensions().get(0).dimensionName()).isEqualTo("ExtDim");
        }

        @Test
        @DisplayName("Lista vacía retorna distribuciones vacías")
        void should_returnEmpty_when_noResults() {
            // Arrange / Act
            GroupReportEngine.DomainsAndDimensionsResult result =
                engine.calculateDomainsAndDimensions(List.of());

            // Assert
            assertThat(result.intralaboralDomains()).isEmpty();
            assertThat(result.extralaboralDimensions()).isEmpty();
        }
    }

    // ==================================================================================
    // calculateRiskMatrix
    // ==================================================================================

    @Nested
    @DisplayName("calculateRiskMatrix")
    class CalculateRiskMatrix {

        @Test
        @DisplayName("Todos los resultados con riesgo alto y estrés alto: magnitud ROJO e índice ROJO")
        void should_returnRojo_when_allHighRiskAndStress() {
            // Arrange
            ScoringEngine.IndividualScoringResult r = buildResultWithStructure(
                "Riesgo alto", "Riesgo alto", "Riesgo alto", "Riesgo alto", "Riesgo alto"
            );

            // Act
            GroupReportEngine.RiskMatrixResult matrix = engine.calculateRiskMatrix(
                List.of(r, r), List.of("Alto", "Alto")
            );

            // Assert
            assertThat(matrix.intralaboralTotal().riskMagnitudePercent()).isEqualTo(100.0);
            assertThat(matrix.intralaboralTotal().magnitudeSemaphore()).isEqualTo(SemaphoreColor.ROJO);
            assertThat(matrix.intralaboralTotal().associationIndex()).isEqualTo(1.0);
            assertThat(matrix.intralaboralTotal().associationSemaphore()).isEqualTo(SemaphoreColor.ROJO);
            assertThat(matrix.intralaboralDimensions()).hasSize(1);
            assertThat(matrix.intralaboralDomains()).hasSize(1);
            assertThat(matrix.extralaboralDimensions()).hasSize(1);
        }

        @Test
        @DisplayName("Riesgo bajo en todos: magnitud VERDE")
        void should_returnVerde_when_lowRisk() {
            // Arrange
            ScoringEngine.IndividualScoringResult r = buildResultWithStructure(
                "Riesgo bajo", "Riesgo bajo", "Riesgo bajo", "Riesgo bajo", "Bajo"
            );

            // Act
            GroupReportEngine.RiskMatrixResult matrix = engine.calculateRiskMatrix(
                List.of(r, r), List.of("Bajo", "Bajo")
            );

            // Assert
            assertThat(matrix.intralaboralTotal().riskMagnitudePercent()).isZero();
            assertThat(matrix.intralaboralTotal().magnitudeSemaphore()).isEqualTo(SemaphoreColor.VERDE);
            assertThat(matrix.intralaboralTotal().associationIndex()).isZero();
            assertThat(matrix.intralaboralTotal().associationSemaphore()).isEqualTo(SemaphoreColor.VERDE);
        }

        @Test
        @DisplayName("Magnitud entre 40 y 60 (exclusivo): semáforo AMARILLO")
        void should_returnAmarillo_when_magnitudeInMiddle() {
            // Arrange: 3 personas con riesgo alto, 2 sin — magnitud = 60% -> ROJO, bajo eso
            // Usamos 2 de 4 = 50% → AMARILLO
            ScoringEngine.IndividualScoringResult high = buildResultWithStructure(
                "Riesgo alto", "Riesgo alto", "Riesgo alto", "Riesgo alto", "Alto"
            );
            ScoringEngine.IndividualScoringResult low = buildResultWithStructure(
                "Riesgo bajo", "Riesgo bajo", "Riesgo bajo", "Riesgo bajo", "Bajo"
            );

            // Act
            GroupReportEngine.RiskMatrixResult matrix = engine.calculateRiskMatrix(
                List.of(high, high, low, low),
                List.of("Alto", "Alto", "Bajo", "Bajo")
            );

            // Assert
            assertThat(matrix.intralaboralTotal().riskMagnitudePercent()).isEqualTo(50.0);
            assertThat(matrix.intralaboralTotal().magnitudeSemaphore()).isEqualTo(SemaphoreColor.AMARILLO);
        }

        @Test
        @DisplayName("Índice de asociación entre 0.29 y 0.70 (exclusivo): semáforo AMARILLO")
        void should_returnAmarillo_when_associationInMiddle() {
            // Arrange: 2 personas con estrés alto, de las cuales 1 tiene riesgo alto → 1/2 = 0.5
            ScoringEngine.IndividualScoringResult high = buildResultWithStructure(
                "Riesgo alto", "Riesgo alto", "Riesgo alto", "Riesgo alto", "Alto"
            );
            ScoringEngine.IndividualScoringResult low = buildResultWithStructure(
                "Riesgo bajo", "Riesgo bajo", "Riesgo bajo", "Riesgo bajo", "Bajo"
            );

            // Act
            GroupReportEngine.RiskMatrixResult matrix = engine.calculateRiskMatrix(
                List.of(high, low),
                List.of("Alto", "Alto")
            );

            // Assert
            assertThat(matrix.intralaboralTotal().associationIndex()).isEqualTo(0.5);
            assertThat(matrix.intralaboralTotal().associationSemaphore()).isEqualTo(SemaphoreColor.AMARILLO);
        }

        @Test
        @DisplayName("Reconoce 'Riesgo medio' y 'Riesgo muy alto' como riesgo medio/alto, y 'Medio' / 'Muy alto' como estrés medio/alto")
        void should_recognizeAllMediumAndVeryHighVariants() {
            // Arrange: dos personas, una con "Riesgo medio"+"Medio", otra con "Riesgo muy alto"+"Muy alto"
            ScoringEngine.IndividualScoringResult medium = buildResultWithStructure(
                "Riesgo medio", "Riesgo medio", "Riesgo medio", "Riesgo medio", "Medio"
            );
            ScoringEngine.IndividualScoringResult veryHigh = buildResultWithStructure(
                "Riesgo muy alto", "Riesgo muy alto", "Riesgo muy alto", "Riesgo muy alto", "Muy alto"
            );

            // Act
            GroupReportEngine.RiskMatrixResult matrix = engine.calculateRiskMatrix(
                List.of(medium, veryHigh), List.of("Medio", "Muy alto")
            );

            // Assert: ambas personas cuentan como riesgo medio/alto → magnitud 100%
            assertThat(matrix.intralaboralTotal().riskMagnitudePercent()).isEqualTo(100.0);
            // Ambas tienen estrés medio/alto; ambas tienen riesgo medio/alto → índice 1.0
            assertThat(matrix.intralaboralTotal().associationIndex()).isEqualTo(1.0);
        }

        @Test
        @DisplayName("Riesgo alto con estrés bajo: cubre la rama (riesgo=true, estrés=false) del filtro de asociación")
        void should_coverRiskTrueStressFalseBranch_when_highRiskButLowStress() {
            // Arrange: persona1 con riesgo alto + estrés bajo (para ejercitar la rama faltante),
            // persona2 con riesgo bajo + estrés alto (para que totalMediumOrHighStress > 0 y se ejecute el filtro).
            ScoringEngine.IndividualScoringResult highRiskLowStress = buildResultWithStructure(
                "Riesgo alto", "Riesgo alto", "Riesgo alto", "Riesgo alto", "Bajo"
            );
            ScoringEngine.IndividualScoringResult lowRiskHighStress = buildResultWithStructure(
                "Riesgo bajo", "Riesgo bajo", "Riesgo bajo", "Riesgo bajo", "Alto"
            );

            // Act
            GroupReportEngine.RiskMatrixResult matrix = engine.calculateRiskMatrix(
                List.of(highRiskLowStress, lowRiskHighStress),
                List.of("Bajo", "Alto")
            );

            // Assert: magnitud 50% (1 de 2 con riesgo alto), pero asociación 0 porque el único con
            // estrés medio/alto NO tiene riesgo medio/alto.
            assertThat(matrix.intralaboralTotal().riskMagnitudePercent()).isEqualTo(50.0);
            assertThat(matrix.intralaboralTotal().associationIndex()).isZero();
        }

        @Test
        @DisplayName("Lista vacía: entradas vacías y magnitud 0")
        void should_returnEmpty_when_noResults() {
            // Arrange / Act
            GroupReportEngine.RiskMatrixResult matrix = engine.calculateRiskMatrix(List.of(), List.of());

            // Assert
            assertThat(matrix.intralaboralDimensions()).isEmpty();
            assertThat(matrix.intralaboralDomains()).isEmpty();
            assertThat(matrix.extralaboralDimensions()).isEmpty();
            assertThat(matrix.intralaboralTotal().riskMagnitudePercent()).isZero();
            assertThat(matrix.intralaboralTotal().associationIndex()).isZero();
        }
    }

    // ==================================================================================
    // Helpers
    // ==================================================================================

    /** Construye un resultado mínimo con scores y niveles totales, sin estructura de dominios. */
    private ScoringEngine.IndividualScoringResult buildResult(
        double intraScore, String intraLevel,
        double extraScore, String extraLevel,
        double generalScore, String generalLevel,
        double stressScore, String stressLevel
    ) {
        return new ScoringEngine.IndividualScoringResult(
            new ScoringEngine.IntralaboralResult(List.of(), 0, intraScore, intraLevel),
            new ScoringEngine.ExtralaboralResult(List.of(), 0, extraScore, extraLevel),
            new ScoringEngine.GeneralTotalResult(generalScore, generalLevel),
            new ScoringEngine.StressResult(0.0, stressScore, stressLevel)
        );
    }

    /** Construye un resultado con un dominio/dimensión/dimensión extra para probar matriz de riesgo. */
    private ScoringEngine.IndividualScoringResult buildResultWithStructure(
        String dimLevel, String domainLevel, String intraTotalLevel,
        String extraDimLevel, String stressLevel
    ) {
        ScoringEngine.DimensionScore dim = new ScoringEngine.DimensionScore("DimX", 0, 0.0, dimLevel);
        ScoringEngine.DomainScore domain = new ScoringEngine.DomainScore(
            "DomX", 0, 0.0, domainLevel, List.of(dim)
        );
        ScoringEngine.DimensionScore extDim = new ScoringEngine.DimensionScore(
            "ExtDimX", 0, 0.0, extraDimLevel
        );
        return new ScoringEngine.IndividualScoringResult(
            new ScoringEngine.IntralaboralResult(List.of(domain), 0, 0.0, intraTotalLevel),
            new ScoringEngine.ExtralaboralResult(List.of(extDim), 0, 0.0, extraDimLevel),
            new ScoringEngine.GeneralTotalResult(0.0, intraTotalLevel),
            new ScoringEngine.StressResult(0.0, 0.0, stressLevel)
        );
    }
}

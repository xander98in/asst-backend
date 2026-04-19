package com.unicuaca.asst.unicauca_asst.core.reports.domain.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.models.QuestionnaireResponse;
import com.unicuaca.asst.unicauca_asst.core.reports.domain.config.BaremTables;
import com.unicuaca.asst.unicauca_asst.core.reports.domain.config.BaremTables.BaremThresholds;
import com.unicuaca.asst.unicauca_asst.core.reports.domain.config.ScoringConstants;
import com.unicuaca.asst.unicauca_asst.core.reports.domain.config.ScoringConstants.StressWeightGroup;
import com.unicuaca.asst.unicauca_asst.core.reports.domain.config.TransformationFactors;

/**
 * Motor de calificación de la Batería de Instrumentos para la Evaluación
 * de Factores de Riesgo Psicosocial (Ministerio de la Protección Social, Colombia, 2010).
 *
 * <p>Implementa los 5 pasos del manual:</p>
 * <ol>
 *   <li>Calificación de ítems (directos e invertidos)</li>
 *   <li>Obtención de puntajes brutos por dimensión</li>
 *   <li>Transformación de puntajes brutos</li>
 *   <li>Comparación con baremos</li>
 *   <li>Clasificación en niveles de riesgo</li>
 * </ol>
 *
 * <p>Clase de dominio pura, sin estado. No accede a base de datos — recibe datos
 * ya cargados como parámetros y retorna resultados calculados.</p>
 */
public class ScoringEngine {

    // ── Orden oficial de presentación (manual de la batería) ────────────

    private static final List<String> INTRALABORAL_DOMAIN_ORDER = List.of(
        "Liderazgo y relaciones sociales en el trabajo",
        "Control sobre el trabajo",
        "Demandas del trabajo",
        "Recompensas"
    );

    private static final List<String> EXTRALABORAL_DIMENSION_ORDER = List.of(
        "Tiempo fuera del trabajo",
        "Relaciones familiares",
        "Comunicación y relaciones interpersonales",
        "Situación económica del grupo familiar",
        "Características de la vivienda y de su entorno",
        "Influencia del entorno extralaboral sobre el trabajo",
        "Desplazamiento vivienda - trabajo - vivienda"
    );

    // ── Records de resultado ────────────────────────────────────────────

    /** Resultado de calificación de una dimensión. */
    public record DimensionScore(String dimensionName, int rawScore, double transformedScore, String riskLevel) {}

    /** Resultado de calificación de un dominio con sus dimensiones. */
    public record DomainScore(String domainName, int rawScore, double transformedScore, String riskLevel,
                              List<DimensionScore> dimensions) {}

    /** Resultado completo del cuestionario intralaboral. */
    public record IntralaboralResult(List<DomainScore> domains, int totalRawScore, double totalTransformedScore,
                                     String totalRiskLevel) {}

    /** Resultado completo del cuestionario extralaboral. */
    public record ExtralaboralResult(List<DimensionScore> dimensions, int totalRawScore, double totalTransformedScore,
                                     String totalRiskLevel) {}

    /** Resultado del cuestionario de estrés. */
    public record StressResult(double rawScore, double transformedScore, String stressLevel) {}

    /** Resultado del puntaje total general (intralaboral + extralaboral). */
    public record GeneralTotalResult(double transformedScore, String riskLevel) {}

    /** Resultado consolidado de la calificación individual completa. */
    public record IndividualScoringResult(IntralaboralResult intralaboral, ExtralaboralResult extralaboral,
                                          GeneralTotalResult generalTotal, StressResult stress) {}

    // ── Método orquestador ──────────────────────────────────────────────

    /**
     * Calcula el informe individual completo de riesgo psicosocial.
     *
     * @param intralaboralResponses respuestas del cuestionario intralaboral (ILA o ILB)
     * @param extralaboralResponses respuestas del cuestionario extralaboral
     * @param stressResponses       respuestas del cuestionario de estrés
     * @param intralaboralForm      forma aplicada: "ILA" o "ILB"
     * @param jobPositionTypeId     ID del tipo de cargo (1=Jefatura, 2=Profesional, 3=Auxiliar, 4=Operario)
     * @return resultado consolidado con intralaboral, extralaboral, total general y estrés
     */
    public IndividualScoringResult calculateIndividualReport(
        List<QuestionnaireResponse> intralaboralResponses,
        List<QuestionnaireResponse> extralaboralResponses,
        List<QuestionnaireResponse> stressResponses,
        String intralaboralForm,
        int jobPositionTypeId
    ) {
        String occupationalGroup = BaremTables.getOccupationalGroup(jobPositionTypeId);

        IntralaboralResult intralaboral = calculateIntralaboral(intralaboralResponses, intralaboralForm);
        ExtralaboralResult extralaboral = calculateExtralaboral(extralaboralResponses, occupationalGroup);
        GeneralTotalResult generalTotal = calculateGeneralTotal(
            intralaboral.totalRawScore(), extralaboral.totalRawScore(), intralaboralForm
        );
        StressResult stress = calculateStress(stressResponses, occupationalGroup);

        return new IndividualScoringResult(intralaboral, extralaboral, generalTotal, stress);
    }

    // ── Cálculo intralaboral ────────────────────────────────────────────

    /**
     * Calcula los puntajes del cuestionario intralaboral: dimensiones, dominios y total.
     *
     * @param responses respuestas del cuestionario intralaboral
     * @param form      forma del cuestionario: "ILA" o "ILB"
     * @return resultado intralaboral con dominios, dimensiones y total
     */
    public IntralaboralResult calculateIntralaboral(List<QuestionnaireResponse> responses, String form) {
        Map<String, List<Integer>> dimensionItems = ScoringConstants.DIMENSION_ITEMS.get(form);
        Map<String, List<String>> domainDimensions = ScoringConstants.DOMAIN_DIMENSIONS.get(form);

        // Paso 1-3: Calcular puntaje por dimensión
        Map<String, DimensionScore> dimensionScores = new java.util.LinkedHashMap<>();
        for (Map.Entry<String, List<Integer>> entry : dimensionItems.entrySet()) {
            String dimensionName = entry.getKey();
            List<Integer> items = entry.getValue();

            int rawScore = calculateDimensionRawScore(responses, items, form);
            int factor = TransformationFactors.getDimensionFactor(form, dimensionName);
            double transformedScore = roundToOneDecimal((double) rawScore / factor * 100);

            BaremThresholds thresholds = BaremTables.getIntralaboralDimensionBarem(form, dimensionName);
            String riskLevel = BaremTables.classify(transformedScore, thresholds);

            dimensionScores.put(dimensionName, new DimensionScore(dimensionName, rawScore, transformedScore, riskLevel));
        }

        // Paso 2-5: Calcular puntaje por dominio
        List<DomainScore> domains = new ArrayList<>();
        int totalRawScore = 0;

        for (Map.Entry<String, List<String>> entry : domainDimensions.entrySet()) {
            String domainName = entry.getKey();
            List<String> dimensionNames = entry.getValue();

            List<DimensionScore> domainDimensionScores = new ArrayList<>();
            int domainRawScore = 0;

            for (String dimName : dimensionNames) {
                DimensionScore dimScore = dimensionScores.get(dimName);
                domainDimensionScores.add(dimScore);
                domainRawScore += dimScore.rawScore();
            }

            int domainFactor = TransformationFactors.getDomainFactor(form, domainName);
            double domainTransformed = roundToOneDecimal((double) domainRawScore / domainFactor * 100);

            BaremThresholds domainThresholds = BaremTables.getIntralaboralDomainBarem(form, domainName);
            String domainRiskLevel = BaremTables.classify(domainTransformed, domainThresholds);

            domains.add(new DomainScore(domainName, domainRawScore, domainTransformed, domainRiskLevel, domainDimensionScores));
            totalRawScore += domainRawScore;
        }

        // Ordenar dominios según el orden oficial del manual
        domains.sort((a, b) -> {
            int indexA = INTRALABORAL_DOMAIN_ORDER.indexOf(a.domainName());
            int indexB = INTRALABORAL_DOMAIN_ORDER.indexOf(b.domainName());
            return Integer.compare(indexA == -1 ? Integer.MAX_VALUE : indexA,
                                   indexB == -1 ? Integer.MAX_VALUE : indexB);
        });

        // Total intralaboral
        int totalFactor = TransformationFactors.getIntralaboralTotalFactor(form);
        double totalTransformed = roundToOneDecimal((double) totalRawScore / totalFactor * 100);

        BaremThresholds totalThresholds = BaremTables.getIntralaboralTotalBarem(form);
        String totalRiskLevel = BaremTables.classify(totalTransformed, totalThresholds);

        return new IntralaboralResult(domains, totalRawScore, totalTransformed, totalRiskLevel);
    }

    // ── Cálculo extralaboral ────────────────────────────────────────────

    /**
     * Calcula los puntajes del cuestionario extralaboral: dimensiones y total.
     *
     * @param responses         respuestas del cuestionario extralaboral
     * @param occupationalGroup grupo ocupacional para seleccionar baremos
     * @return resultado extralaboral con dimensiones y total
     */
    public ExtralaboralResult calculateExtralaboral(List<QuestionnaireResponse> responses, String occupationalGroup) {
        Map<String, List<Integer>> dimensionItems = ScoringConstants.DIMENSION_ITEMS.get("EXT");

        List<DimensionScore> dimensions = new ArrayList<>();
        int totalRawScore = 0;

        for (Map.Entry<String, List<Integer>> entry : dimensionItems.entrySet()) {
            String dimensionName = entry.getKey();
            List<Integer> items = entry.getValue();

            int rawScore = calculateDimensionRawScore(responses, items, "EXT");
            int factor = TransformationFactors.getDimensionFactor("EXT", dimensionName);
            double transformedScore = roundToOneDecimal((double) rawScore / factor * 100);

            BaremThresholds thresholds = BaremTables.getExtralaboralDimensionBarem(occupationalGroup, dimensionName);
            String riskLevel = BaremTables.classify(transformedScore, thresholds);

            dimensions.add(new DimensionScore(dimensionName, rawScore, transformedScore, riskLevel));
            totalRawScore += rawScore;
        }

        // Ordenar dimensiones según el orden oficial del manual
        dimensions.sort((a, b) -> {
            int indexA = EXTRALABORAL_DIMENSION_ORDER.indexOf(a.dimensionName());
            int indexB = EXTRALABORAL_DIMENSION_ORDER.indexOf(b.dimensionName());
            return Integer.compare(indexA == -1 ? Integer.MAX_VALUE : indexA,
                                   indexB == -1 ? Integer.MAX_VALUE : indexB);
        });

        int totalFactor = TransformationFactors.getExtralaboralTotalFactor();
        double totalTransformed = roundToOneDecimal((double) totalRawScore / totalFactor * 100);

        BaremThresholds totalThresholds = BaremTables.getExtralaboralTotalBarem(occupationalGroup);
        String totalRiskLevel = BaremTables.classify(totalTransformed, totalThresholds);

        return new ExtralaboralResult(dimensions, totalRawScore, totalTransformed, totalRiskLevel);
    }

    // ── Cálculo de estrés ───────────────────────────────────────────────

    /**
     * Calcula el puntaje del cuestionario de estrés usando promedios ponderados.
     *
     * @param responses         respuestas del cuestionario de estrés
     * @param occupationalGroup grupo ocupacional para seleccionar baremos
     * @return resultado de estrés con puntaje bruto, transformado y nivel
     */
    public StressResult calculateStress(List<QuestionnaireResponse> responses, String occupationalGroup) {
        // Paso 1: Calificar cada ítem según su grupo de puntuación
        Map<Integer, Integer> itemScores = new java.util.HashMap<>();
        for (QuestionnaireResponse response : responses) {
            int order = response.getQuestion().getOrder();
            int answerValue = response.getAnswerOption().getValue();
            // Ítems sin respuesta (valor 0 = "Sin valor") se excluyen del cálculo
            if (answerValue == 0) {
                continue;
            }
            int[] scores = ScoringConstants.STRESS_ITEM_SCORES.get(order);
            int scoreIndex = mapStressAnswerToIndex(answerValue);
            itemScores.put(order, scores[scoreIndex]);
        }

        // Paso 2: Calcular puntaje bruto con promedios ponderados
        double rawScore = 0.0;
        for (StressWeightGroup group : ScoringConstants.STRESS_WEIGHT_GROUPS) {
            double sum = 0.0;
            int count = 0;
            for (int item = group.startItem(); item <= group.endItem(); item++) {
                Integer score = itemScores.get(item);
                if (score != null) {
                    sum += score;
                    count++;
                }
            }
            if (count > 0) {
                rawScore += (sum / count) * group.weight();
            }
        }

        // Paso 3: Transformar
        double transformedScore = roundToOneDecimal(rawScore / TransformationFactors.getStressTotalFactor() * 100);

        // Pasos 4-5: Clasificar
        BaremThresholds thresholds = BaremTables.getStressBarem(occupationalGroup);
        String stressLevel = BaremTables.classifyStress(transformedScore, thresholds);

        return new StressResult(rawScore, transformedScore, stressLevel);
    }

    // ── Cálculo total general ───────────────────────────────────────────

    /**
     * Calcula el puntaje total general combinando intralaboral y extralaboral.
     *
     * @param intralaboralRawScore puntaje bruto total intralaboral
     * @param extralaboralRawScore puntaje bruto total extralaboral
     * @param form                 forma del cuestionario intralaboral: "ILA" o "ILB"
     * @return resultado total general con puntaje transformado y nivel de riesgo
     */
    public GeneralTotalResult calculateGeneralTotal(int intralaboralRawScore, int extralaboralRawScore, String form) {
        int totalRawScore = intralaboralRawScore + extralaboralRawScore;
        int factor = TransformationFactors.getGeneralTotalFactor(form);
        double transformedScore = roundToOneDecimal((double) totalRawScore / factor * 100);

        BaremThresholds thresholds = BaremTables.getGeneralTotalBarem(form);
        String riskLevel = BaremTables.classify(transformedScore, thresholds);

        return new GeneralTotalResult(transformedScore, riskLevel);
    }

    // ── Métodos auxiliares privados ──────────────────────────────────────

    /**
     * Calcula el puntaje bruto de una dimensión sumando las calificaciones de sus ítems.
     *
     * @param responses     respuestas del cuestionario
     * @param items         números de orden de los ítems de la dimensión
     * @param questionnaire abreviatura del cuestionario ("ILA", "ILB", "EXT")
     * @return puntaje bruto de la dimensión
     */
    private int calculateDimensionRawScore(List<QuestionnaireResponse> responses, List<Integer> items, String questionnaire) {
        Set<Integer> itemSet = Set.copyOf(items);
        int rawScore = 0;

        for (QuestionnaireResponse response : responses) {
            int order = response.getQuestion().getOrder();
            if (!itemSet.contains(order)) {
                continue;
            }
            int answerValue = response.getAnswerOption().getValue();
            // Ítems sin respuesta (valor 0 = "Sin valor") se excluyen del cálculo
            // según el manual: "dato perdido, sin calificación alguna"
            if (answerValue == 0) {
                continue;
            }
            if (ScoringConstants.isInvertedItem(questionnaire, order)) {
                rawScore += 5 - answerValue;
            } else {
                rawScore += answerValue - 1;
            }
        }

        return rawScore;
    }

    /**
     * Redondea un valor a 1 decimal.
     *
     * @param value valor a redondear
     * @return valor redondeado a un decimal
     */
    private double roundToOneDecimal(double value) {
        return Math.round(value * 10.0) / 10.0;
    }

    /**
     * Mapea el valor de respuesta de la BD al índice del array de calificaciones de estrés.
     * El cuestionario de estrés tiene 4 opciones: Siempre(BD=5)→0, Casi siempre(BD=4)→1,
     * A veces(BD=3)→2, Nunca(BD=1)→3.
     *
     * @param answerValue valor de la opción de respuesta en BD (1, 3, 4 o 5)
     * @return índice del array de calificaciones (0-3)
     */
    private int mapStressAnswerToIndex(int answerValue) {
        return switch (answerValue) {
            case 5 -> 0;  // Siempre
            case 4 -> 1;  // Casi siempre
            case 3 -> 2;  // A veces
            case 1 -> 3;  // Nunca
            default -> throw new IllegalArgumentException(
                "Valor de respuesta inválido para estrés: " + answerValue + ". Valores esperados: 1, 3, 4, 5"
            );
        };
    }
}

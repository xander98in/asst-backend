package com.unicuaca.asst.unicauca_asst.core.reports.domain.services;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.IntStream;

import com.unicuaca.asst.unicauca_asst.core.reports.domain.config.BaremTables;

/**
 * Motor de cálculo de estadísticas grupales de riesgo psicosocial.
 *
 * <p>Clase de dominio pura (sin Spring, sin estado) que recibe una lista de
 * resultados individuales calculados por {@link ScoringEngine} y genera
 * distribuciones, promedios y la matriz de riesgo del grupo.</p>
 */
public class GroupReportEngine {

    // ── Records de resumen general ─────────────────────────────────────

    /** Distribución de personas por nivel de riesgo. */
    public record RiskDistribution(int sinRiesgo, int bajo, int medio, int alto, int muyAlto, int invalido, int noEvaluado) {}

    /** Resumen grupal de un cuestionario. */
    public record QuestionnaireGroupSummary(
        int totalPersons,
        int formaACount,
        int formaBCount,
        double averageTransformedScore,
        String averageRiskLevel,
        RiskDistribution distribution
    ) {}

    /** Resultado consolidado del resumen grupal. */
    public record GroupSummaryResult(
        int totalPersons,
        int formaACount,
        int formaBCount,
        QuestionnaireGroupSummary intralaboral,
        QuestionnaireGroupSummary extralaboral,
        QuestionnaireGroupSummary generalTotal,
        QuestionnaireGroupSummary stress
    ) {}

    // ── Records de dominios y dimensiones ──────────────────────────────

    /** Distribución de riesgo de una dimensión en el grupo. */
    public record DimensionDistribution(
        String dimensionName,
        RiskDistribution distribution,
        double averageTransformedScore
    ) {}

    /** Distribución de riesgo de un dominio con sus dimensiones. */
    public record DomainDistribution(
        String domainName,
        RiskDistribution distribution,
        double averageTransformedScore,
        List<DimensionDistribution> dimensions
    ) {}

    /** Resultado de dominios y dimensiones del grupo. */
    public record DomainsAndDimensionsResult(
        List<DomainDistribution> intralaboralDomains,
        List<DimensionDistribution> extralaboralDimensions
    ) {}

    // ── Records de la matriz de riesgo ─────────────────────────────────

    /** Entrada de la matriz de riesgo con magnitud, asociación y semáforos. */
    public record RiskMatrixEntry(
        String name,
        double riskMagnitudePercent,
        double associationIndex,
        String magnitudeSemaphore,
        String associationSemaphore
    ) {}

    /** Resultado completo de la matriz de riesgo del grupo. */
    public record RiskMatrixResult(
        List<RiskMatrixEntry> intralaboralDimensions,
        List<RiskMatrixEntry> intralaboralDomains,
        RiskMatrixEntry intralaboralTotal,
        List<RiskMatrixEntry> extralaboralDimensions,
        RiskMatrixEntry extralaboralTotal
    ) {}

    // ── 1. Resumen general del grupo ───────────────────────────────────

    /**
     * Calcula el resumen grupal: distribución de riesgo, promedios y moda
     * para cada cuestionario.
     *
     * @param results resultados individuales de cada persona del grupo
     * @param forms   lista paralela con la forma intralaboral usada ("ILA"/"ILB")
     * @return resumen grupal consolidado
     */
    public GroupSummaryResult calculateGroupSummary(
        List<ScoringEngine.IndividualScoringResult> results,
        List<String> forms
    ) {
        int totalPersons = results.size();
        int formaACount = (int) forms.stream().filter("ILA"::equals).count();
        int formaBCount = (int) forms.stream().filter("ILB"::equals).count();

        String majorityForm = (formaACount >= formaBCount) ? "ILA" : "ILB";
        String occupationalGroup = (formaACount >= formaBCount) ? BaremTables.JEFES_PROFESIONALES : BaremTables.AUXILIARES_OPERARIOS;

        // Intralaboral
        List<String> intraRiskLevels = results.stream()
            .map(r -> r.intralaboral().totalRiskLevel()).toList();
        List<Double> intraScores = results.stream()
            .map(r -> r.intralaboral().totalTransformedScore()).toList();
        QuestionnaireGroupSummary intralaboral = buildSummary(
            totalPersons, formaACount, formaBCount, intraScores, intraRiskLevels,
            score -> BaremTables.classify(score, BaremTables.getIntralaboralTotalBarem(majorityForm))
        );

        // Extralaboral
        List<String> extraRiskLevels = results.stream()
            .map(r -> r.extralaboral().totalRiskLevel()).toList();
        List<Double> extraScores = results.stream()
            .map(r -> r.extralaboral().totalTransformedScore()).toList();
        QuestionnaireGroupSummary extralaboral = buildSummary(
            totalPersons, formaACount, formaBCount, extraScores, extraRiskLevels,
            score -> BaremTables.classify(score, BaremTables.getExtralaboralTotalBarem(occupationalGroup))
        );

        // Total general
        List<String> generalRiskLevels = results.stream()
            .map(r -> r.generalTotal().riskLevel()).toList();
        List<Double> generalScores = results.stream()
            .map(r -> r.generalTotal().transformedScore()).toList();
        QuestionnaireGroupSummary generalTotal = buildSummary(
            totalPersons, formaACount, formaBCount, generalScores, generalRiskLevels,
            score -> BaremTables.classify(score, BaremTables.getGeneralTotalBarem(majorityForm))
        );

        // Estrés
        List<String> stressLevels = results.stream()
            .map(r -> r.stress().stressLevel()).toList();
        List<Double> stressScores = results.stream()
            .map(r -> r.stress().transformedScore()).toList();
        QuestionnaireGroupSummary stress = buildSummary(
            totalPersons, formaACount, formaBCount, stressScores, stressLevels,
            score -> BaremTables.classifyStress(score, BaremTables.getStressBarem(occupationalGroup))
        );

        return new GroupSummaryResult(
            totalPersons, formaACount, formaBCount,
            intralaboral, extralaboral, generalTotal, stress
        );
    }

    // ── 2. Dominios y dimensiones ──────────────────────────────────────

    /**
     * Calcula la distribución de riesgo por dominio y dimensión del grupo.
     * Las dimensiones exclusivas de una forma (ej. "Relación con los colaboradores"
     * solo en ILA) se calculan solo sobre las personas que las tienen.
     *
     * @param results resultados individuales de cada persona del grupo
     * @return distribuciones intralaborales (dominios + dimensiones) y extralaborales
     */
    public DomainsAndDimensionsResult calculateDomainsAndDimensions(
        List<ScoringEngine.IndividualScoringResult> results
    ) {
        // Intralaboral: recopilar datos por dominio y dimensión
        Map<String, List<String>> domainRiskLevels = new LinkedHashMap<>();
        Map<String, List<Double>> domainScores = new LinkedHashMap<>();
        Map<String, Map<String, List<String>>> dimRiskLevelsByDomain = new LinkedHashMap<>();
        Map<String, Map<String, List<Double>>> dimScoresByDomain = new LinkedHashMap<>();

        for (ScoringEngine.IndividualScoringResult result : results) {
            for (ScoringEngine.DomainScore domain : result.intralaboral().domains()) {
                domainRiskLevels.computeIfAbsent(domain.domainName(), k -> new ArrayList<>())
                    .add(domain.riskLevel());
                domainScores.computeIfAbsent(domain.domainName(), k -> new ArrayList<>())
                    .add(domain.transformedScore());

                Map<String, List<String>> dimLevels = dimRiskLevelsByDomain
                    .computeIfAbsent(domain.domainName(), k -> new LinkedHashMap<>());
                Map<String, List<Double>> dimScrs = dimScoresByDomain
                    .computeIfAbsent(domain.domainName(), k -> new LinkedHashMap<>());

                for (ScoringEngine.DimensionScore dim : domain.dimensions()) {
                    dimLevels.computeIfAbsent(dim.dimensionName(), k -> new ArrayList<>())
                        .add(dim.riskLevel());
                    dimScrs.computeIfAbsent(dim.dimensionName(), k -> new ArrayList<>())
                        .add(dim.transformedScore());
                }
            }
        }

        // Construir resultado intralaboral
        List<DomainDistribution> intralaboralDomains = new ArrayList<>();
        for (String domainName : domainRiskLevels.keySet()) {
            RiskDistribution domainDist = countRiskDistribution(domainRiskLevels.get(domainName));
            double avgScore = roundToOneDecimal(
                domainScores.get(domainName).stream().mapToDouble(Double::doubleValue).average().orElse(0.0)
            );

            List<DimensionDistribution> dimensions = new ArrayList<>();
            Map<String, List<String>> dimLevels = dimRiskLevelsByDomain.get(domainName);
            Map<String, List<Double>> dimScrs = dimScoresByDomain.get(domainName);

            for (String dimName : dimLevels.keySet()) {
                RiskDistribution dimDist = countRiskDistribution(dimLevels.get(dimName));
                double avgDimScore = roundToOneDecimal(
                    dimScrs.get(dimName).stream().mapToDouble(Double::doubleValue).average().orElse(0.0)
                );
                dimensions.add(new DimensionDistribution(dimName, dimDist, avgDimScore));
            }

            intralaboralDomains.add(new DomainDistribution(domainName, domainDist, avgScore, dimensions));
        }

        // Extralaboral: recopilar dimensiones
        Map<String, List<String>> extDimRiskLevels = new LinkedHashMap<>();
        Map<String, List<Double>> extDimScores = new LinkedHashMap<>();

        for (ScoringEngine.IndividualScoringResult result : results) {
            for (ScoringEngine.DimensionScore dim : result.extralaboral().dimensions()) {
                extDimRiskLevels.computeIfAbsent(dim.dimensionName(), k -> new ArrayList<>())
                    .add(dim.riskLevel());
                extDimScores.computeIfAbsent(dim.dimensionName(), k -> new ArrayList<>())
                    .add(dim.transformedScore());
            }
        }

        List<DimensionDistribution> extralaboralDimensions = new ArrayList<>();
        for (String dimName : extDimRiskLevels.keySet()) {
            RiskDistribution dist = countRiskDistribution(extDimRiskLevels.get(dimName));
            double avgScore = roundToOneDecimal(
                extDimScores.get(dimName).stream().mapToDouble(Double::doubleValue).average().orElse(0.0)
            );
            extralaboralDimensions.add(new DimensionDistribution(dimName, dist, avgScore));
        }

        return new DomainsAndDimensionsResult(intralaboralDomains, extralaboralDimensions);
    }

    // ── 3. Matriz de riesgo ────────────────────────────────────────────

    /**
     * Calcula la matriz de riesgo del grupo: magnitud del riesgo, índice de asociación
     * con estrés y semáforos para cada dimensión, dominio y totales.
     *
     * @param results      resultados individuales de cada persona
     * @param stressLevels lista paralela con el nivel de estrés de cada persona
     * @return matriz de riesgo completa
     */
    public RiskMatrixResult calculateRiskMatrix(
        List<ScoringEngine.IndividualScoringResult> results,
        List<String> stressLevels
    ) {
        int totalPersons = results.size();
        long totalHighStress = stressLevels.stream().filter(this::isHighStress).count();

        // Recopilar pares (riskLevel, stressLevel) por dimensión y dominio intralaboral
        Map<String, List<String[]>> intraDimData = new LinkedHashMap<>();
        Map<String, List<String[]>> intraDomainData = new LinkedHashMap<>();

        for (int i = 0; i < results.size(); i++) {
            ScoringEngine.IndividualScoringResult result = results.get(i);
            String stressLevel = stressLevels.get(i);

            for (ScoringEngine.DomainScore domain : result.intralaboral().domains()) {
                intraDomainData.computeIfAbsent(domain.domainName(), k -> new ArrayList<>())
                    .add(new String[]{domain.riskLevel(), stressLevel});

                for (ScoringEngine.DimensionScore dim : domain.dimensions()) {
                    intraDimData.computeIfAbsent(dim.dimensionName(), k -> new ArrayList<>())
                        .add(new String[]{dim.riskLevel(), stressLevel});
                }
            }
        }

        // Dimensiones intralaborales
        List<RiskMatrixEntry> intralaboralDimensions = new ArrayList<>();
        for (Map.Entry<String, List<String[]>> entry : intraDimData.entrySet()) {
            intralaboralDimensions.add(
                buildRiskMatrixEntry(entry.getKey(), entry.getValue(), totalPersons, totalHighStress)
            );
        }

        // Dominios intralaborales
        List<RiskMatrixEntry> intralaboralDomains = new ArrayList<>();
        for (Map.Entry<String, List<String[]>> entry : intraDomainData.entrySet()) {
            intralaboralDomains.add(
                buildRiskMatrixEntry(entry.getKey(), entry.getValue(), totalPersons, totalHighStress)
            );
        }

        // Total intralaboral
        List<String[]> intraTotalData = IntStream.range(0, results.size())
            .mapToObj(i -> new String[]{results.get(i).intralaboral().totalRiskLevel(), stressLevels.get(i)})
            .toList();
        RiskMatrixEntry intralaboralTotal = buildRiskMatrixEntry(
            "Total intralaboral", intraTotalData, totalPersons, totalHighStress
        );

        // Dimensiones extralaborales
        Map<String, List<String[]>> extraDimData = new LinkedHashMap<>();
        for (int i = 0; i < results.size(); i++) {
            String stressLevel = stressLevels.get(i);
            for (ScoringEngine.DimensionScore dim : results.get(i).extralaboral().dimensions()) {
                extraDimData.computeIfAbsent(dim.dimensionName(), k -> new ArrayList<>())
                    .add(new String[]{dim.riskLevel(), stressLevel});
            }
        }

        List<RiskMatrixEntry> extralaboralDimensions = new ArrayList<>();
        for (Map.Entry<String, List<String[]>> entry : extraDimData.entrySet()) {
            extralaboralDimensions.add(
                buildRiskMatrixEntry(entry.getKey(), entry.getValue(), totalPersons, totalHighStress)
            );
        }

        // Total extralaboral
        List<String[]> extraTotalData = IntStream.range(0, results.size())
            .mapToObj(i -> new String[]{results.get(i).extralaboral().totalRiskLevel(), stressLevels.get(i)})
            .toList();
        RiskMatrixEntry extralaboralTotal = buildRiskMatrixEntry(
            "Total extralaboral", extraTotalData, totalPersons, totalHighStress
        );

        return new RiskMatrixResult(
            intralaboralDimensions, intralaboralDomains, intralaboralTotal,
            extralaboralDimensions, extralaboralTotal
        );
    }

    // ── Métodos auxiliares privados ─────────────────────────────────────

    private QuestionnaireGroupSummary buildSummary(
        int totalPersons, int formaACount, int formaBCount,
        List<Double> scores, List<String> levels,
        Function<Double, String> classifier
    ) {
        double avgScore = roundToOneDecimal(
            scores.stream().mapToDouble(Double::doubleValue).average().orElse(0.0)
        );
        String avgLevel = classifier.apply(avgScore);
        RiskDistribution distribution = countRiskDistribution(levels);
        return new QuestionnaireGroupSummary(
            totalPersons, formaACount, formaBCount, avgScore, avgLevel, distribution
        );
    }

    private RiskMatrixEntry buildRiskMatrixEntry(
        String name, List<String[]> riskStressPairs, int totalPersons, long totalHighStress
    ) {
        long mediumOrHighRiskCount = riskStressPairs.stream()
            .filter(pair -> isMediumOrHighRisk(pair[0]))
            .count();

        double magnitudePercent = totalPersons > 0
            ? roundToOneDecimal((double) mediumOrHighRiskCount / totalPersons * 100)
            : 0.0;

        double associationIndex = 0.0;
        if (totalHighStress > 0) {
            long highRiskAndHighStress = riskStressPairs.stream()
                .filter(pair -> isHighRisk(pair[0]) && isHighStress(pair[1]))
                .count();
            associationIndex = roundToTwoDecimals((double) highRiskAndHighStress / totalHighStress);
        }

        return new RiskMatrixEntry(
            name, magnitudePercent, associationIndex,
            getMagnitudeSemaphore(magnitudePercent),
            getAssociationSemaphore(associationIndex)
        );
    }

    private RiskDistribution countRiskDistribution(List<String> levels) {
        int sinRiesgo = 0, bajo = 0, medio = 0, alto = 0, muyAlto = 0;
        for (String level : levels) {
            switch (level) {
                case "Sin riesgo o riesgo despreciable", "Muy bajo" -> sinRiesgo++;
                case "Riesgo bajo", "Bajo" -> bajo++;
                case "Riesgo medio", "Medio" -> medio++;
                case "Riesgo alto", "Alto" -> alto++;
                case "Riesgo muy alto", "Muy alto" -> muyAlto++;
                default -> { /* nivel desconocido */ }
            }
        }
        return new RiskDistribution(sinRiesgo, bajo, medio, alto, muyAlto, 0, 0);
    }

    private String findMode(List<String> levels) {
        if (levels.isEmpty()) {
            return "";
        }
        Map<String, Integer> frequency = new LinkedHashMap<>();
        for (String level : levels) {
            frequency.merge(level, 1, Integer::sum);
        }
        return frequency.entrySet().stream()
            .max(Map.Entry.comparingByValue())
            .map(Map.Entry::getKey)
            .orElse("");
    }

    private boolean isHighRisk(String riskLevel) {
        return "Riesgo alto".equals(riskLevel) || "Riesgo muy alto".equals(riskLevel);
    }

    private boolean isMediumOrHighRisk(String riskLevel) {
        return "Riesgo medio".equals(riskLevel) || "Riesgo alto".equals(riskLevel) || "Riesgo muy alto".equals(riskLevel);
    }

    private boolean isHighStress(String stressLevel) {
        return "Alto".equals(stressLevel) || "Muy alto".equals(stressLevel);
    }

    private String getMagnitudeSemaphore(double magnitudePercent) {
        if (magnitudePercent <= 40.0) return "VERDE";
        if (magnitudePercent < 60.0) return "AMARILLO";
        return "ROJO";
    }

    private String getAssociationSemaphore(double associationIndex) {
        if (associationIndex <= 0.29) return "VERDE";
        if (associationIndex < 0.70) return "AMARILLO";
        return "ROJO";
    }

    private double roundToOneDecimal(double value) {
        return Math.round(value * 10.0) / 10.0;
    }

    private double roundToTwoDecimals(double value) {
        return Math.round(value * 100.0) / 100.0;
    }
}

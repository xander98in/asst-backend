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
        QuestionnaireGroupSummary intralaboralFormaA,
        QuestionnaireGroupSummary intralaboralFormaB,
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

        // Separar resultados por forma
        List<ScoringEngine.IndividualScoringResult> formaAResults = new ArrayList<>();
        List<ScoringEngine.IndividualScoringResult> formaBResults = new ArrayList<>();
        for (int i = 0; i < results.size(); i++) {
            if ("ILA".equals(forms.get(i))) {
                formaAResults.add(results.get(i));
            } else {
                formaBResults.add(results.get(i));
            }
        }

        // Forma A
        QuestionnaireGroupSummary intralaboralFormaA = buildFormSummary(
            formaAResults, formaACount, 0,
            r -> r.intralaboral().totalTransformedScore(),
            r -> r.intralaboral().totalRiskLevel(),
            score -> BaremTables.classify(score, BaremTables.getIntralaboralTotalBarem("ILA"))
        );

        // Forma B
        QuestionnaireGroupSummary intralaboralFormaB = buildFormSummary(
            formaBResults, 0, formaBCount,
            r -> r.intralaboral().totalTransformedScore(),
            r -> r.intralaboral().totalRiskLevel(),
            score -> BaremTables.classify(score, BaremTables.getIntralaboralTotalBarem("ILB"))
        );

        return new GroupSummaryResult(
            totalPersons, formaACount, formaBCount,
            intralaboral, intralaboralFormaA, intralaboralFormaB,
            extralaboral, generalTotal, stress
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
        long totalMediumOrHighStress = stressLevels.stream().filter(this::isMediumOrHighStress).count();

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
                buildRiskMatrixEntry(entry.getKey(), entry.getValue(), totalPersons, totalMediumOrHighStress)
            );
        }

        // Dominios intralaborales
        List<RiskMatrixEntry> intralaboralDomains = new ArrayList<>();
        for (Map.Entry<String, List<String[]>> entry : intraDomainData.entrySet()) {
            intralaboralDomains.add(
                buildRiskMatrixEntry(entry.getKey(), entry.getValue(), totalPersons, totalMediumOrHighStress)
            );
        }

        // Total intralaboral
        List<String[]> intraTotalData = IntStream.range(0, results.size())
            .mapToObj(i -> new String[]{results.get(i).intralaboral().totalRiskLevel(), stressLevels.get(i)})
            .toList();
        RiskMatrixEntry intralaboralTotal = buildRiskMatrixEntry(
            "Total intralaboral", intraTotalData, totalPersons, totalMediumOrHighStress
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
                buildRiskMatrixEntry(entry.getKey(), entry.getValue(), totalPersons, totalMediumOrHighStress)
            );
        }

        // Total extralaboral
        List<String[]> extraTotalData = IntStream.range(0, results.size())
            .mapToObj(i -> new String[]{results.get(i).extralaboral().totalRiskLevel(), stressLevels.get(i)})
            .toList();
        RiskMatrixEntry extralaboralTotal = buildRiskMatrixEntry(
            "Total extralaboral", extraTotalData, totalPersons, totalMediumOrHighStress
        );

        return new RiskMatrixResult(
            intralaboralDimensions, intralaboralDomains, intralaboralTotal,
            extralaboralDimensions, extralaboralTotal
        );
    }

    // ── Métodos auxiliares privados ─────────────────────────────────────

    /**
     * Construye el resumen grupal de un cuestionario a partir de la lista de puntajes y niveles
     * calculando el promedio, el nivel promedio y la distribución de riesgo.
     *
     * @param totalPersons total de personas incluidas en el resumen
     * @param formaACount  cantidad de personas con Forma A
     * @param formaBCount  cantidad de personas con Forma B
     * @param scores       lista de puntajes transformados individuales
     * @param levels       lista de niveles de riesgo individuales
     * @param classifier   función para clasificar el puntaje promedio con los baremos correspondientes
     * @return resumen grupal con promedio, nivel promedio y distribución de riesgo
     */
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

    /**
     * Construye el resumen grupal para un subconjunto de resultados filtrados por forma (A o B).
     *
     * @param formResults  resultados individuales de la forma específica
     * @param formaACount  cantidad de personas con Forma A en este subconjunto
     * @param formaBCount  cantidad de personas con Forma B en este subconjunto
     * @param scoreExtractor función para extraer el puntaje del resultado individual
     * @param levelExtractor función para extraer el nivel de riesgo del resultado individual
     * @param classifier   función para clasificar el puntaje promedio con los baremos correspondientes
     * @return resumen grupal del subconjunto, o resumen vacío con "N/A" si no hay resultados
     */
    private QuestionnaireGroupSummary buildFormSummary(
        List<ScoringEngine.IndividualScoringResult> formResults,
        int formaACount, int formaBCount,
        Function<ScoringEngine.IndividualScoringResult, Double> scoreExtractor,
        Function<ScoringEngine.IndividualScoringResult, String> levelExtractor,
        Function<Double, String> classifier
    ) {
        if (formResults.isEmpty()) {
            return new QuestionnaireGroupSummary(
                0, formaACount, formaBCount, 0.0, "N/A",
                new RiskDistribution(0, 0, 0, 0, 0, 0, 0)
            );
        }
        int total = formResults.size();
        List<Double> scores = formResults.stream().map(scoreExtractor).toList();
        List<String> levels = formResults.stream().map(levelExtractor).toList();
        return buildSummary(total, formaACount, formaBCount, scores, levels, classifier);
    }

    /**
     * Construye una entrada de la matriz de riesgo a partir de los pares (riesgo, estrés) individuales,
     * calculando la magnitud del riesgo, el índice de asociación con estrés y los semáforos asociados.
     *
     * @param name                    nombre del dominio, dimensión o total que identifica la entrada
     * @param riskStressPairs         lista de pares [nivel de riesgo, nivel de estrés] de cada persona
     * @param totalPersons            total de personas evaluadas
     * @param totalMediumOrHighStress total de personas con estrés medio, alto o muy alto
     * @return entrada de la matriz con magnitud, índice de asociación y semáforos
     */
    private RiskMatrixEntry buildRiskMatrixEntry(
        String name, List<String[]> riskStressPairs, int totalPersons, long totalMediumOrHighStress
    ) {
        long mediumOrHighRiskCount = riskStressPairs.stream()
            .filter(pair -> isMediumOrHighRisk(pair[0]))
            .count();

        double magnitudePercent = totalPersons > 0
            ? roundToOneDecimal((double) mediumOrHighRiskCount / totalPersons * 100)
            : 0.0;

        double associationIndex = 0.0;
        if (totalMediumOrHighStress > 0) {
            long mediumOrHighRiskAndStress = riskStressPairs.stream()
                .filter(pair -> isMediumOrHighRisk(pair[0]) && isMediumOrHighStress(pair[1]))
                .count();
            associationIndex = roundToTwoDecimals((double) mediumOrHighRiskAndStress / totalMediumOrHighStress);
        }

        return new RiskMatrixEntry(
            name, magnitudePercent, associationIndex,
            getMagnitudeSemaphore(magnitudePercent),
            getAssociationSemaphore(associationIndex)
        );
    }

    /**
     * Cuenta la cantidad de personas en cada nivel de riesgo a partir de una lista de niveles individuales.
     *
     * @param levels lista de niveles de riesgo individuales
     * @return distribución con las cantidades por cada nivel (sin riesgo, bajo, medio, alto, muy alto)
     */
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

    /**
     * Calcula la moda (nivel más frecuente) dentro de una lista de niveles de riesgo.
     *
     * @param levels lista de niveles de riesgo individuales
     * @return nivel más frecuente, o cadena vacía si la lista es vacía
     */
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

    /**
     * Indica si un nivel de riesgo corresponde a "Riesgo medio", "Riesgo alto" o "Riesgo muy alto".
     *
     * @param riskLevel nivel de riesgo a evaluar
     * @return true si el nivel es medio, alto o muy alto; false en caso contrario
     */
    private boolean isMediumOrHighRisk(String riskLevel) {
        return "Riesgo medio".equals(riskLevel) || "Riesgo alto".equals(riskLevel) || "Riesgo muy alto".equals(riskLevel);
    }

    /**
     * Indica si un nivel de estrés corresponde a "Medio", "Alto" o "Muy alto".
     *
     * @param stressLevel nivel de estrés a evaluar
     * @return true si el nivel es medio, alto o muy alto; false en caso contrario
     */
    private boolean isMediumOrHighStress(String stressLevel) {
        return "Medio".equals(stressLevel) || "Alto".equals(stressLevel) || "Muy alto".equals(stressLevel);
    }

    /**
     * Obtiene el semáforo asociado a la magnitud del riesgo según los umbrales definidos:
     * VERDE (≤ 40%), AMARILLO (&lt; 60%) o ROJO (≥ 60%).
     *
     * @param magnitudePercent porcentaje de magnitud del riesgo
     * @return color del semáforo ("VERDE", "AMARILLO" o "ROJO")
     */
    private String getMagnitudeSemaphore(double magnitudePercent) {
        if (magnitudePercent <= 40.0) return "VERDE";
        if (magnitudePercent < 60.0) return "AMARILLO";
        return "ROJO";
    }

    /**
     * Obtiene el semáforo asociado al índice de asociación con estrés según los umbrales definidos:
     * VERDE (≤ 0.29), AMARILLO (&lt; 0.70) o ROJO (≥ 0.70).
     *
     * @param associationIndex índice de asociación entre riesgo y estrés
     * @return color del semáforo ("VERDE", "AMARILLO" o "ROJO")
     */
    private String getAssociationSemaphore(double associationIndex) {
        if (associationIndex <= 0.29) return "VERDE";
        if (associationIndex < 0.70) return "AMARILLO";
        return "ROJO";
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
     * Redondea un valor a 2 decimales.
     *
     * @param value valor a redondear
     * @return valor redondeado a dos decimales
     */
    private double roundToTwoDecimals(double value) {
        return Math.round(value * 100.0) / 100.0;
    }
}

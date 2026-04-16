package com.unicuaca.asst.unicauca_asst.core.reports.domain.config;

import java.util.Map;

/**
 * Factores de transformación para la calificación de la Batería de Instrumentos
 * para la Evaluación de Factores de Riesgo Psicosocial (Ministerio de la Protección Social, Colombia, 2010).
 *
 * Fórmula de transformación: (puntaje bruto / factor) × 100
 *
 * Clase de utilidad — no instanciable. Todos los valores son inmutables.
 */
public final class TransformationFactors {

    private TransformationFactors() {}

    // ── Factores por dimensión ──────────────────────────────────────────

    private static final Map<String, Integer> ILA_DIMENSION_FACTORS = Map.ofEntries(
        Map.entry("Características del liderazgo", 52),
        Map.entry("Relaciones sociales en el trabajo", 56),
        Map.entry("Retroalimentación del desempeño", 20),
        Map.entry("Relación con los colaboradores", 36),
        Map.entry("Claridad de rol", 28),
        Map.entry("Capacitación", 12),
        Map.entry("Participación y manejo del cambio", 16),
        Map.entry("Oportunidades para el uso y desarrollo de habilidades y conocimientos", 16),
        Map.entry("Control y autonomía sobre el trabajo", 12),
        Map.entry("Demandas ambientales y de esfuerzo físico", 48),
        Map.entry("Demandas emocionales", 36),
        Map.entry("Demandas cuantitativas", 24),
        Map.entry("Influencia del trabajo sobre el entorno extralaboral", 16),
        Map.entry("Exigencias de responsabilidad del cargo", 24),
        Map.entry("Demandas de carga mental", 20),
        Map.entry("Consistencia del rol", 20),
        Map.entry("Demandas de la jornada de trabajo", 12),
        Map.entry("Recompensas derivadas de la pertenencia a la organización y del trabajo que se realiza", 20),
        Map.entry("Reconocimiento y compensación", 24)
    );

    private static final Map<String, Integer> ILB_DIMENSION_FACTORS = Map.ofEntries(
        Map.entry("Características del liderazgo", 52),
        Map.entry("Relaciones sociales en el trabajo", 48),
        Map.entry("Retroalimentación del desempeño", 20),
        Map.entry("Claridad de rol", 20),
        Map.entry("Capacitación", 12),
        Map.entry("Participación y manejo del cambio", 12),
        Map.entry("Oportunidades para el uso y desarrollo de habilidades y conocimientos", 16),
        Map.entry("Control y autonomía sobre el trabajo", 12),
        Map.entry("Demandas ambientales y de esfuerzo físico", 48),
        Map.entry("Demandas emocionales", 36),
        Map.entry("Demandas cuantitativas", 12),
        Map.entry("Influencia del trabajo sobre el entorno extralaboral", 16),
        Map.entry("Demandas de carga mental", 20),
        Map.entry("Demandas de la jornada de trabajo", 24),
        Map.entry("Recompensas derivadas de la pertenencia a la organización y del trabajo que se realiza", 16),
        Map.entry("Reconocimiento y compensación", 24)
    );

    private static final Map<String, Integer> EXT_DIMENSION_FACTORS = Map.of(
        "Tiempo fuera del trabajo", 16,
        "Relaciones familiares", 12,
        "Comunicación y relaciones interpersonales", 20,
        "Situación económica del grupo familiar", 12,
        "Características de la vivienda y de su entorno", 36,
        "Influencia del entorno extralaboral sobre el trabajo", 12,
        "Desplazamiento vivienda - trabajo - vivienda", 16
    );

    public static final Map<String, Map<String, Integer>> DIMENSION_FACTORS = Map.of(
        "ILA", ILA_DIMENSION_FACTORS,
        "ILB", ILB_DIMENSION_FACTORS,
        "EXT", EXT_DIMENSION_FACTORS
    );

    // ── Factores por dominio (solo intralaboral) ────────────────────────

    public static final Map<String, Map<String, Integer>> DOMAIN_FACTORS = Map.of(
        "ILA", Map.of(
            "Liderazgo y relaciones sociales en el trabajo", 164,
            "Control sobre el trabajo", 84,
            "Demandas del trabajo", 200,
            "Recompensas", 44
        ),
        "ILB", Map.of(
            "Liderazgo y relaciones sociales en el trabajo", 120,
            "Control sobre el trabajo", 72,
            "Demandas del trabajo", 156,
            "Recompensas", 40
        )
    );

    // ── Factores para total intralaboral ────────────────────────────────

    public static final Map<String, Integer> INTRALABORAL_TOTAL_FACTORS = Map.of(
        "ILA", 492,
        "ILB", 388
    );

    // ── Factor total extralaboral ───────────────────────────────────────

    public static final int EXTRALABORAL_TOTAL_FACTOR = 124;

    // ── Factor total general (intralaboral + extralaboral) ──────────────

    public static final Map<String, Integer> GENERAL_TOTAL_FACTORS = Map.of(
        "ILA", 616,
        "ILB", 512
    );

    // ── Factor de transformación de estrés ──────────────────────────────

    public static final double STRESS_TOTAL_FACTOR = 61.16;

    // ── Métodos de utilidad ─────────────────────────────────────────────

    public static int getDimensionFactor(String questionnaire, String dimension) {
        return DIMENSION_FACTORS.get(questionnaire).get(dimension);
    }

    public static int getDomainFactor(String questionnaire, String domain) {
        return DOMAIN_FACTORS.get(questionnaire).get(domain);
    }

    public static int getIntralaboralTotalFactor(String questionnaire) {
        return INTRALABORAL_TOTAL_FACTORS.get(questionnaire);
    }

    public static int getExtralaboralTotalFactor() {
        return EXTRALABORAL_TOTAL_FACTOR;
    }

    public static int getGeneralTotalFactor(String questionnaire) {
        return GENERAL_TOTAL_FACTORS.get(questionnaire);
    }

    public static double getStressTotalFactor() {
        return STRESS_TOTAL_FACTOR;
    }
}

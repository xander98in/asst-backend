package com.unicuaca.asst.unicauca_asst.core.reports.domain.config;

import java.util.Map;

/**
 * Tablas de baremos para clasificar puntajes transformados en niveles de riesgo psicosocial.
 * Basado en el Manual de la Batería de Riesgo Psicosocial
 * (Ministerio de la Protección Social, Colombia, 2010).
 *
 * Clase de utilidad — no instanciable. Todos los valores son inmutables.
 */
public final class BaremTables {

    private BaremTables() {}

    // ── Record de umbrales ──────────────────────────────────────────────

    /**
     * Límites superiores de los primeros 4 niveles de riesgo.
     * Si el puntaje supera {@code alto}, se clasifica como el nivel máximo.
     */
    public record BaremThresholds(double sinRiesgo, double bajo, double medio, double alto) {}

    // ── Grupos ocupacionales ────────────────────────────────────────────

    public static final String JEFES_PROFESIONALES = "JEFES_PROFESIONALES";
    public static final String AUXILIARES_OPERARIOS = "AUXILIARES_OPERARIOS";

    // ══════════════════════════════════════════════════════════════════════
    // INTRALABORAL FORMA A (ILA)
    // ══════════════════════════════════════════════════════════════════════

    // ── 1. ILA — Dimensiones ────────────────────────────────────────────

    private static final Map<String, BaremThresholds> ILA_DIMENSION_BAREMS = Map.ofEntries(
        Map.entry("Características del liderazgo", new BaremThresholds(3.8, 15.4, 30.8, 46.2)),
        Map.entry("Relaciones sociales en el trabajo", new BaremThresholds(5.4, 16.1, 25.0, 37.5)),
        Map.entry("Retroalimentación del desempeño", new BaremThresholds(10.0, 25.0, 40.0, 55.0)),
        Map.entry("Relación con los colaboradores", new BaremThresholds(13.9, 25.0, 33.3, 47.2)),
        Map.entry("Claridad de rol", new BaremThresholds(0.9, 10.7, 21.4, 39.3)),
        Map.entry("Capacitación", new BaremThresholds(0.9, 16.7, 33.3, 50.0)),
        Map.entry("Participación y manejo del cambio", new BaremThresholds(12.5, 25.0, 37.5, 50.0)),
        Map.entry("Oportunidades para el uso y desarrollo de habilidades y conocimientos", new BaremThresholds(0.9, 6.3, 18.8, 31.3)),
        Map.entry("Control y autonomía sobre el trabajo", new BaremThresholds(8.3, 25.0, 41.7, 58.3)),
        Map.entry("Demandas ambientales y de esfuerzo físico", new BaremThresholds(14.6, 22.9, 31.3, 39.6)),
        Map.entry("Demandas emocionales", new BaremThresholds(16.7, 25.0, 33.3, 47.2)),
        Map.entry("Demandas cuantitativas", new BaremThresholds(25.0, 33.3, 45.8, 54.2)),
        Map.entry("Influencia del trabajo sobre el entorno extralaboral", new BaremThresholds(18.8, 31.3, 43.8, 50.0)),
        Map.entry("Exigencias de responsabilidad del cargo", new BaremThresholds(37.5, 54.2, 66.7, 79.2)),
        Map.entry("Demandas de carga mental", new BaremThresholds(60.0, 70.0, 80.0, 90.0)),
        Map.entry("Consistencia del rol", new BaremThresholds(15.0, 25.0, 35.0, 45.0)),
        Map.entry("Demandas de la jornada de trabajo", new BaremThresholds(8.3, 25.0, 33.3, 50.0)),
        Map.entry("Recompensas derivadas de la pertenencia a la organización y del trabajo que se realiza", new BaremThresholds(0.9, 5.0, 10.0, 20.0)),
        Map.entry("Reconocimiento y compensación", new BaremThresholds(4.2, 16.7, 25.0, 37.5))
    );

    // ── 2. ILA — Dominios ───────────────────────────────────────────────

    private static final Map<String, BaremThresholds> ILA_DOMAIN_BAREMS = Map.of(
        "Liderazgo y relaciones sociales en el trabajo", new BaremThresholds(9.1, 17.7, 25.6, 34.8),
        "Control sobre el trabajo", new BaremThresholds(10.7, 19.0, 29.8, 40.5),
        "Demandas del trabajo", new BaremThresholds(28.5, 35.0, 41.5, 47.5),
        "Recompensas", new BaremThresholds(4.5, 11.4, 20.5, 29.5)
    );

    // ── 3. ILA — Total ──────────────────────────────────────────────────

    private static final BaremThresholds ILA_TOTAL_BAREM = new BaremThresholds(19.7, 25.8, 31.5, 38.0);

    // ══════════════════════════════════════════════════════════════════════
    // INTRALABORAL FORMA B (ILB)
    // ══════════════════════════════════════════════════════════════════════

    // ── 4. ILB — Dimensiones ────────────────────────────────────────────

    private static final Map<String, BaremThresholds> ILB_DIMENSION_BAREMS = Map.ofEntries(
        Map.entry("Características del liderazgo", new BaremThresholds(3.8, 13.5, 25.0, 38.5)),
        Map.entry("Relaciones sociales en el trabajo", new BaremThresholds(6.3, 14.6, 27.1, 37.5)),
        Map.entry("Retroalimentación del desempeño", new BaremThresholds(5.0, 20.0, 30.0, 50.0)),
        Map.entry("Claridad de rol", new BaremThresholds(0.9, 5.0, 15.0, 30.0)),
        Map.entry("Capacitación", new BaremThresholds(0.9, 16.7, 25.0, 50.0)),
        Map.entry("Participación y manejo del cambio", new BaremThresholds(16.7, 33.3, 41.7, 58.3)),
        Map.entry("Oportunidades para el uso y desarrollo de habilidades y conocimientos", new BaremThresholds(12.5, 25.0, 37.5, 56.3)),
        Map.entry("Control y autonomía sobre el trabajo", new BaremThresholds(33.3, 50.0, 66.7, 75.0)),
        Map.entry("Demandas ambientales y de esfuerzo físico", new BaremThresholds(22.9, 31.3, 39.6, 47.9)),
        Map.entry("Demandas emocionales", new BaremThresholds(19.4, 27.8, 38.9, 47.2)),
        Map.entry("Demandas cuantitativas", new BaremThresholds(16.7, 33.3, 41.7, 50.0)),
        Map.entry("Influencia del trabajo sobre el entorno extralaboral", new BaremThresholds(12.5, 25.0, 31.3, 50.0)),
        Map.entry("Demandas de carga mental", new BaremThresholds(50.0, 65.0, 75.0, 85.0)),
        Map.entry("Demandas de la jornada de trabajo", new BaremThresholds(25.0, 37.5, 45.8, 58.3)),
        Map.entry("Recompensas derivadas de la pertenencia a la organización y del trabajo que se realiza", new BaremThresholds(0.9, 6.3, 12.5, 18.8)),
        Map.entry("Reconocimiento y compensación", new BaremThresholds(0.9, 12.5, 25.0, 37.5))
    );

    // ── 5. ILB — Dominios ───────────────────────────────────────────────

    private static final Map<String, BaremThresholds> ILB_DOMAIN_BAREMS = Map.of(
        "Liderazgo y relaciones sociales en el trabajo", new BaremThresholds(8.3, 17.5, 26.7, 38.3),
        "Control sobre el trabajo", new BaremThresholds(19.4, 26.4, 34.7, 43.1),
        "Demandas del trabajo", new BaremThresholds(26.9, 33.3, 37.8, 44.2),
        "Recompensas", new BaremThresholds(2.5, 10.0, 17.5, 27.5)
    );

    // ── 6. ILB — Total ──────────────────────────────────────────────────

    private static final BaremThresholds ILB_TOTAL_BAREM = new BaremThresholds(20.6, 26.0, 31.2, 38.7);

    // ── Mapas de lookup intralaboral ─────────────────────────────────────

    public static final Map<String, Map<String, BaremThresholds>> INTRALABORAL_DIMENSION_BAREMS = Map.of(
        "ILA", ILA_DIMENSION_BAREMS,
        "ILB", ILB_DIMENSION_BAREMS
    );

    public static final Map<String, Map<String, BaremThresholds>> INTRALABORAL_DOMAIN_BAREMS = Map.of(
        "ILA", ILA_DOMAIN_BAREMS,
        "ILB", ILB_DOMAIN_BAREMS
    );

    public static final Map<String, BaremThresholds> INTRALABORAL_TOTAL_BAREMS = Map.of(
        "ILA", ILA_TOTAL_BAREM,
        "ILB", ILB_TOTAL_BAREM
    );

    // ══════════════════════════════════════════════════════════════════════
    // EXTRALABORAL
    // ══════════════════════════════════════════════════════════════════════

    // ── 7. Extralaboral — Jefes/Profesionales/Técnicos (tipo cargo 1-2) ─

    private static final Map<String, BaremThresholds> EXT_JEFES_DIMENSION_BAREMS = Map.of(
        "Tiempo fuera del trabajo", new BaremThresholds(6.3, 25.0, 37.5, 50.0),
        "Relaciones familiares", new BaremThresholds(8.3, 25.0, 33.3, 50.0),
        "Comunicación y relaciones interpersonales", new BaremThresholds(0.9, 10.0, 20.0, 30.0),
        "Situación económica del grupo familiar", new BaremThresholds(8.3, 25.0, 33.3, 50.0),
        "Características de la vivienda y de su entorno", new BaremThresholds(5.6, 11.1, 13.9, 22.2),
        "Influencia del entorno extralaboral sobre el trabajo", new BaremThresholds(8.3, 16.7, 25.0, 41.7),
        "Desplazamiento vivienda - trabajo - vivienda", new BaremThresholds(0.9, 12.5, 25.0, 43.8)
    );

    private static final BaremThresholds EXT_JEFES_TOTAL_BAREM = new BaremThresholds(11.3, 16.9, 22.6, 29.0);

    // ── 8. Extralaboral — Auxiliares/Operarios (tipo cargo 3-4) ─────────

    private static final Map<String, BaremThresholds> EXT_AUXILIARES_DIMENSION_BAREMS = Map.of(
        "Tiempo fuera del trabajo", new BaremThresholds(6.3, 25.0, 37.5, 50.0),
        "Relaciones familiares", new BaremThresholds(8.3, 25.0, 33.3, 50.0),
        "Comunicación y relaciones interpersonales", new BaremThresholds(5.0, 15.0, 25.0, 35.0),
        "Situación económica del grupo familiar", new BaremThresholds(16.7, 25.0, 41.7, 50.0),
        "Características de la vivienda y de su entorno", new BaremThresholds(5.6, 11.1, 16.7, 27.8),
        "Influencia del entorno extralaboral sobre el trabajo", new BaremThresholds(0.9, 16.7, 25.0, 41.7),
        "Desplazamiento vivienda - trabajo - vivienda", new BaremThresholds(0.9, 12.5, 25.0, 43.8)
    );

    private static final BaremThresholds EXT_AUXILIARES_TOTAL_BAREM = new BaremThresholds(12.9, 17.7, 24.2, 32.3);

    // ── Mapas de lookup extralaboral ─────────────────────────────────────

    public static final Map<String, Map<String, BaremThresholds>> EXTRALABORAL_DIMENSION_BAREMS = Map.of(
        JEFES_PROFESIONALES, EXT_JEFES_DIMENSION_BAREMS,
        AUXILIARES_OPERARIOS, EXT_AUXILIARES_DIMENSION_BAREMS
    );

    public static final Map<String, BaremThresholds> EXTRALABORAL_TOTAL_BAREMS = Map.of(
        JEFES_PROFESIONALES, EXT_JEFES_TOTAL_BAREM,
        AUXILIARES_OPERARIOS, EXT_AUXILIARES_TOTAL_BAREM
    );

    // ══════════════════════════════════════════════════════════════════════
    // TOTAL GENERAL (intralaboral + extralaboral)
    // ══════════════════════════════════════════════════════════════════════

    // ── 9. Total general ────────────────────────────────────────────────

    public static final Map<String, BaremThresholds> GENERAL_TOTAL_BAREMS = Map.of(
        "ILA", new BaremThresholds(18.8, 24.4, 29.5, 35.4),
        "ILB", new BaremThresholds(19.9, 24.8, 29.5, 35.4)
    );

    // ══════════════════════════════════════════════════════════════════════
    // ESTRÉS
    // ══════════════════════════════════════════════════════════════════════

    // ── 10-11. Estrés por grupo ocupacional ─────────────────────────────

    public static final Map<String, BaremThresholds> STRESS_BAREMS = Map.of(
        JEFES_PROFESIONALES, new BaremThresholds(7.8, 12.6, 17.7, 25.0),
        AUXILIARES_OPERARIOS, new BaremThresholds(6.5, 11.8, 17.0, 23.4)
    );

    // ══════════════════════════════════════════════════════════════════════
    // MÉTODOS DE UTILIDAD
    // ══════════════════════════════════════════════════════════════════════

    public static BaremThresholds getIntralaboralDimensionBarem(String questionnaire, String dimension) {
        return INTRALABORAL_DIMENSION_BAREMS.get(questionnaire).get(dimension);
    }

    public static BaremThresholds getIntralaboralDomainBarem(String questionnaire, String domain) {
        return INTRALABORAL_DOMAIN_BAREMS.get(questionnaire).get(domain);
    }

    public static BaremThresholds getIntralaboralTotalBarem(String questionnaire) {
        return INTRALABORAL_TOTAL_BAREMS.get(questionnaire);
    }

    public static BaremThresholds getExtralaboralDimensionBarem(String occupationalGroup, String dimension) {
        return EXTRALABORAL_DIMENSION_BAREMS.get(occupationalGroup).get(dimension);
    }

    public static BaremThresholds getExtralaboralTotalBarem(String occupationalGroup) {
        return EXTRALABORAL_TOTAL_BAREMS.get(occupationalGroup);
    }

    public static BaremThresholds getGeneralTotalBarem(String questionnaire) {
        return GENERAL_TOTAL_BAREMS.get(questionnaire);
    }

    public static BaremThresholds getStressBarem(String occupationalGroup) {
        return STRESS_BAREMS.get(occupationalGroup);
    }

    /**
     * Clasifica un puntaje transformado en un nivel de riesgo psicosocial.
     */
    public static String classify(double transformedScore, BaremThresholds thresholds) {
        if (transformedScore <= thresholds.sinRiesgo()) return "Sin riesgo o riesgo despreciable";
        if (transformedScore <= thresholds.bajo()) return "Riesgo bajo";
        if (transformedScore <= thresholds.medio()) return "Riesgo medio";
        if (transformedScore <= thresholds.alto()) return "Riesgo alto";
        return "Riesgo muy alto";
    }

    /**
     * Clasifica un puntaje transformado de estrés en un nivel.
     */
    public static String classifyStress(double transformedScore, BaremThresholds thresholds) {
        if (transformedScore <= thresholds.sinRiesgo()) return "Muy bajo";
        if (transformedScore <= thresholds.bajo()) return "Bajo";
        if (transformedScore <= thresholds.medio()) return "Medio";
        if (transformedScore <= thresholds.alto()) return "Alto";
        return "Muy alto";
    }

    /**
     * Determina el grupo ocupacional a partir del ID del tipo de cargo.
     *
     * @param jobPositionTypeId 1=Jefatura, 2=Profesional, 3=Auxiliar, 4=Operario
     * @return constante de grupo ocupacional
     */
    public static String getOccupationalGroup(int jobPositionTypeId) {
        return (jobPositionTypeId <= 2) ? JEFES_PROFESIONALES : AUXILIARES_OPERARIOS;
    }
}

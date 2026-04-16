package com.unicuaca.asst.unicauca_asst.core.reports.domain.config;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Constantes de calificación de la Batería de Instrumentos para la Evaluación
 * de Factores de Riesgo Psicosocial (Ministerio de la Protección Social, Colombia, 2010).
 *
 * Clase de utilidad — no instanciable. Todos los valores son inmutables.
 */
public final class ScoringConstants {

    private ScoringConstants() {}

    // ── 1. Ítems invertidos por cuestionario ────────────────────────────

    private static final Set<Integer> ILA_INVERTED = Set.of(
        4, 5, 6, 9, 12, 14, 32, 34,
        39, 40, 41, 42, 43, 44, 45, 46, 47,
        48, 49, 50, 51, 53, 54, 55, 56, 57, 58, 59,
        60, 61, 62, 63, 64, 65, 66, 67, 68, 69,
        70, 71, 72, 73, 74, 75, 76, 77, 78, 79,
        81, 82, 83, 84, 85, 86, 87, 88, 89,
        90, 91, 92, 93, 94, 95, 96, 97, 98, 99,
        100, 101, 102, 103, 104, 105
    );

    private static final Set<Integer> ILB_INVERTED = Set.of(
        4, 5, 6, 9, 12, 14, 22, 24,
        29, 30, 31, 32, 33, 34, 35, 36, 37, 38,
        39, 40, 41, 42, 43, 44, 45, 46, 47,
        48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 58, 59,
        60, 61, 62, 63, 64, 65, 67, 68, 69,
        70, 71, 72, 73, 74, 75, 76, 77, 78, 79,
        80, 81, 82, 83, 84, 85, 86, 87, 88, 97
    );

    private static final Set<Integer> EXT_INVERTED = Set.of(
        1, 4, 5, 7, 8, 9, 10, 11, 12, 13, 14, 15,
        16, 17, 18, 19, 20, 21, 22, 23, 25, 27, 29
    );

    public static final Map<String, Set<Integer>> INVERTED_ITEMS = Map.of(
        "ILA", ILA_INVERTED,
        "ILB", ILB_INVERTED,
        "EXT", EXT_INVERTED
    );

    // ── 2. Ítems por dimensión por cuestionario ─────────────────────────

    private static final Map<String, List<Integer>> ILA_DIMENSIONS = Map.ofEntries(
        Map.entry("Características del liderazgo", List.of(63, 64, 65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75)),
        Map.entry("Relaciones sociales en el trabajo", List.of(76, 77, 78, 79, 80, 81, 82, 83, 84, 85, 86, 87, 88, 89)),
        Map.entry("Retroalimentación del desempeño", List.of(90, 91, 92, 93, 94)),
        Map.entry("Relación con los colaboradores", List.of(115, 116, 117, 118, 119, 120, 121, 122, 123)),
        Map.entry("Claridad de rol", List.of(53, 54, 55, 56, 57, 58, 59)),
        Map.entry("Capacitación", List.of(60, 61, 62)),
        Map.entry("Participación y manejo del cambio", List.of(48, 49, 50, 51)),
        Map.entry("Oportunidades para el uso y desarrollo de habilidades y conocimientos", List.of(39, 40, 41, 42)),
        Map.entry("Control y autonomía sobre el trabajo", List.of(44, 45, 46)),
        Map.entry("Demandas ambientales y de esfuerzo físico", List.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12)),
        Map.entry("Demandas emocionales", List.of(106, 107, 108, 109, 110, 111, 112, 113, 114)),
        Map.entry("Demandas cuantitativas", List.of(13, 14, 15, 32, 43, 47)),
        Map.entry("Influencia del trabajo sobre el entorno extralaboral", List.of(35, 36, 37, 38)),
        Map.entry("Exigencias de responsabilidad del cargo", List.of(19, 22, 23, 24, 25, 26)),
        Map.entry("Demandas de carga mental", List.of(16, 17, 18, 20, 21)),
        Map.entry("Consistencia del rol", List.of(27, 28, 29, 30, 52)),
        Map.entry("Demandas de la jornada de trabajo", List.of(31, 33, 34)),
        Map.entry("Recompensas derivadas de la pertenencia a la organización y del trabajo que se realiza", List.of(95, 102, 103, 104, 105)),
        Map.entry("Reconocimiento y compensación", List.of(96, 97, 98, 99, 100, 101))
    );

    private static final Map<String, List<Integer>> ILB_DIMENSIONS = Map.ofEntries(
        Map.entry("Características del liderazgo", List.of(49, 50, 51, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61)),
        Map.entry("Relaciones sociales en el trabajo", List.of(62, 63, 64, 65, 66, 67, 68, 69, 70, 71, 72, 73)),
        Map.entry("Retroalimentación del desempeño", List.of(74, 75, 76, 77, 78)),
        Map.entry("Claridad de rol", List.of(41, 42, 43, 44, 45)),
        Map.entry("Capacitación", List.of(46, 47, 48)),
        Map.entry("Participación y manejo del cambio", List.of(38, 39, 40)),
        Map.entry("Oportunidades para el uso y desarrollo de habilidades y conocimientos", List.of(29, 30, 31, 32)),
        Map.entry("Control y autonomía sobre el trabajo", List.of(34, 35, 36)),
        Map.entry("Demandas ambientales y de esfuerzo físico", List.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12)),
        Map.entry("Demandas emocionales", List.of(89, 90, 91, 92, 93, 94, 95, 96, 97)),
        Map.entry("Demandas cuantitativas", List.of(13, 14, 15)),
        Map.entry("Influencia del trabajo sobre el entorno extralaboral", List.of(25, 26, 27, 28)),
        Map.entry("Demandas de carga mental", List.of(16, 17, 18, 19, 20)),
        Map.entry("Demandas de la jornada de trabajo", List.of(21, 22, 23, 24, 33, 37)),
        Map.entry("Recompensas derivadas de la pertenencia a la organización y del trabajo que se realiza", List.of(85, 86, 87, 88)),
        Map.entry("Reconocimiento y compensación", List.of(79, 80, 81, 82, 83, 84))
    );

    private static final Map<String, List<Integer>> EXT_DIMENSIONS = Map.of(
        "Tiempo fuera del trabajo", List.of(14, 15, 16, 17),
        "Relaciones familiares", List.of(22, 25, 27),
        "Comunicación y relaciones interpersonales", List.of(18, 19, 20, 21, 23),
        "Situación económica del grupo familiar", List.of(29, 30, 31),
        "Características de la vivienda y de su entorno", List.of(5, 6, 7, 8, 9, 10, 11, 12, 13),
        "Influencia del entorno extralaboral sobre el trabajo", List.of(24, 26, 28),
        "Desplazamiento vivienda - trabajo - vivienda", List.of(1, 2, 3, 4)
    );

    public static final Map<String, Map<String, List<Integer>>> DIMENSION_ITEMS = Map.of(
        "ILA", ILA_DIMENSIONS,
        "ILB", ILB_DIMENSIONS,
        "EXT", EXT_DIMENSIONS
    );

    // ── 3. Dimensiones por dominio (solo intralaboral) ──────────────────

    private static final Map<String, List<String>> ILA_DOMAINS = Map.of(
        "Liderazgo y relaciones sociales en el trabajo", List.of(
            "Características del liderazgo",
            "Relaciones sociales en el trabajo",
            "Retroalimentación del desempeño",
            "Relación con los colaboradores"
        ),
        "Control sobre el trabajo", List.of(
            "Claridad de rol",
            "Capacitación",
            "Participación y manejo del cambio",
            "Oportunidades para el uso y desarrollo de habilidades y conocimientos",
            "Control y autonomía sobre el trabajo"
        ),
        "Demandas del trabajo", List.of(
            "Demandas ambientales y de esfuerzo físico",
            "Demandas emocionales",
            "Demandas cuantitativas",
            "Influencia del trabajo sobre el entorno extralaboral",
            "Exigencias de responsabilidad del cargo",
            "Demandas de carga mental",
            "Consistencia del rol",
            "Demandas de la jornada de trabajo"
        ),
        "Recompensas", List.of(
            "Recompensas derivadas de la pertenencia a la organización y del trabajo que se realiza",
            "Reconocimiento y compensación"
        )
    );

    private static final Map<String, List<String>> ILB_DOMAINS = Map.of(
        "Liderazgo y relaciones sociales en el trabajo", List.of(
            "Características del liderazgo",
            "Relaciones sociales en el trabajo",
            "Retroalimentación del desempeño"
        ),
        "Control sobre el trabajo", List.of(
            "Claridad de rol",
            "Capacitación",
            "Participación y manejo del cambio",
            "Oportunidades para el uso y desarrollo de habilidades y conocimientos",
            "Control y autonomía sobre el trabajo"
        ),
        "Demandas del trabajo", List.of(
            "Demandas ambientales y de esfuerzo físico",
            "Demandas emocionales",
            "Demandas cuantitativas",
            "Influencia del trabajo sobre el entorno extralaboral",
            "Demandas de carga mental",
            "Demandas de la jornada de trabajo"
        ),
        "Recompensas", List.of(
            "Recompensas derivadas de la pertenencia a la organización y del trabajo que se realiza",
            "Reconocimiento y compensación"
        )
    );

    public static final Map<String, Map<String, List<String>>> DOMAIN_DIMENSIONS = Map.of(
        "ILA", ILA_DOMAINS,
        "ILB", ILB_DOMAINS
    );

    // ── 4. Calificación de estrés ───────────────────────────────────────
    // Cada entrada: ítem → {siempre, casiSiempre, aVeces, nunca}

    public static final Map<Integer, int[]> STRESS_ITEM_SCORES = Map.ofEntries(
        // Grupo 1: 9-6-3-0
        Map.entry(1, new int[]{9, 6, 3, 0}),
        Map.entry(2, new int[]{9, 6, 3, 0}),
        Map.entry(3, new int[]{9, 6, 3, 0}),
        Map.entry(9, new int[]{9, 6, 3, 0}),
        Map.entry(13, new int[]{9, 6, 3, 0}),
        Map.entry(14, new int[]{9, 6, 3, 0}),
        Map.entry(15, new int[]{9, 6, 3, 0}),
        Map.entry(23, new int[]{9, 6, 3, 0}),
        Map.entry(24, new int[]{9, 6, 3, 0}),
        // Grupo 2: 6-4-2-0
        Map.entry(4, new int[]{6, 4, 2, 0}),
        Map.entry(5, new int[]{6, 4, 2, 0}),
        Map.entry(6, new int[]{6, 4, 2, 0}),
        Map.entry(10, new int[]{6, 4, 2, 0}),
        Map.entry(11, new int[]{6, 4, 2, 0}),
        Map.entry(16, new int[]{6, 4, 2, 0}),
        Map.entry(17, new int[]{6, 4, 2, 0}),
        Map.entry(18, new int[]{6, 4, 2, 0}),
        Map.entry(19, new int[]{6, 4, 2, 0}),
        Map.entry(25, new int[]{6, 4, 2, 0}),
        Map.entry(26, new int[]{6, 4, 2, 0}),
        Map.entry(27, new int[]{6, 4, 2, 0}),
        Map.entry(28, new int[]{6, 4, 2, 0}),
        // Grupo 3: 3-2-1-0
        Map.entry(7, new int[]{3, 2, 1, 0}),
        Map.entry(8, new int[]{3, 2, 1, 0}),
        Map.entry(12, new int[]{3, 2, 1, 0}),
        Map.entry(20, new int[]{3, 2, 1, 0}),
        Map.entry(21, new int[]{3, 2, 1, 0}),
        Map.entry(22, new int[]{3, 2, 1, 0}),
        Map.entry(29, new int[]{3, 2, 1, 0}),
        Map.entry(30, new int[]{3, 2, 1, 0}),
        Map.entry(31, new int[]{3, 2, 1, 0})
    );

    // ── 5. Ponderación de estrés ────────────────────────────────────────

    public record StressWeightGroup(int startItem, int endItem, int weight) {}

    public static final List<StressWeightGroup> STRESS_WEIGHT_GROUPS = List.of(
        new StressWeightGroup(1, 8, 4),
        new StressWeightGroup(9, 12, 3),
        new StressWeightGroup(13, 22, 2),
        new StressWeightGroup(23, 31, 1)
    );

    // ── 6. Método de utilidad ───────────────────────────────────────────

    public static boolean isInvertedItem(String questionnaireAbbreviation, int itemOrder) {
        Set<Integer> inverted = INVERTED_ITEMS.get(questionnaireAbbreviation);
        return inverted != null && inverted.contains(itemOrder);
    }
}

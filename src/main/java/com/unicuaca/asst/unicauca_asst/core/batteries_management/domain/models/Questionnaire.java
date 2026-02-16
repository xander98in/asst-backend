package com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.models;

import lombok.*;

/**
 * Representa un cuestionario que forma parte de la batería de instrumentos
 * para la evaluación de factores de riesgo psicosocial.
 *
 * Incluye información básica como el nombre, la abreviatura y la descripción del cuestionario.
 */
@Getter
@Setter
@AllArgsConstructor
@Builder
@ToString
public class Questionnaire {

    /** Identificador único del cuestionario. */
    private Long id;

    /** Nombre del cuestionario (por ejemplo, "Intralaboral - Forma A", "Extralaboral", "Estrés"). */
    private String name;

    /** Abreviatura del cuestionario (por ejemplo, "ILA", "ILB", "EXT", "EST"). */
    private String abbreviation;

    /** Descripción u observaciones sobre el cuestionario. */
    private String description;
}

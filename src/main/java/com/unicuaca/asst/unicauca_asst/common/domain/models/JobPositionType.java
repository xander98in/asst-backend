package com.unicuaca.asst.unicauca_asst.common.domain.models;

import lombok.*;

/**
 * Representa un tipo de cargo dentro de la estructura organizacional
 * de la empresa en la que trabaja la persona evaluada.
 *
 * <p>El tipo de cargo determina qué formulario intralaboral se aplica en la
 * batería de riesgo psicosocial: los cargos de Jefatura y Profesional reciben
 * la Forma A (ILA), mientras que Auxiliar y Operario reciben la Forma B (ILB).
 * La asignación concreta del cuestionario se resuelve en el frontend.</p>
 */
@Getter
@Setter
@AllArgsConstructor
@Builder
@ToString
public class JobPositionType {

    private Long id;
    private String name;
}

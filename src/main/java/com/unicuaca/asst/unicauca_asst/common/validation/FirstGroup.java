package com.unicuaca.asst.unicauca_asst.common.validation;

/**
 * Grupo de validación ejecutado en la primera fase de validación de DTOs.
 *
 * <p>Agrupa las restricciones básicas (p. ej. {@code @NotNull}, {@code @NotBlank})
 * cuyo fallo debe cortocircuitar la validación antes de ejecutar el
 * {@link SecondGroup}. Se utiliza en combinación con {@code @GroupSequence}
 * para forzar el orden de evaluación.</p>
 */
public interface FirstGroup {

}

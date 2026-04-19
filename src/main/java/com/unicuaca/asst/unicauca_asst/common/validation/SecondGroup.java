package com.unicuaca.asst.unicauca_asst.common.validation;

/**
 * Grupo de validación ejecutado en la segunda fase de validación de DTOs.
 *
 * <p>Agrupa las restricciones de formato y de reglas de negocio (p. ej.
 * {@code @Size}, {@code @Email}, validadores personalizados) que solo deben
 * evaluarse una vez superadas las comprobaciones del {@link FirstGroup}.
 * Se utiliza en combinación con {@code @GroupSequence}.</p>
 */
public interface SecondGroup {

}

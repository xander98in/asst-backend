package com.unicuaca.asst.unicauca_asst.core.auth.domain.models.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Enum que define los roles disponibles dentro del sistema.
 *
 * <p>Cada constante expone su clave técnica, que coincide con el valor
 * almacenado en la base de datos y con la autoridad utilizada por Spring Security.</p>
 */
@Getter
@RequiredArgsConstructor
public enum RoleEnum {

    /** Rol administrador con acceso completo a la gestión del sistema. */
    ADMIN("ADMIN"),

    /** Profesional ASST encargado de aplicar y administrar las baterías. */
    PROFESIONAL_ASST("PROFESIONAL_ASST"),

    /** Persona evaluada que responde los cuestionarios. */
    PERSONA_EVALUADA("PERSONA_EVALUADA");

    /** Clave técnica del rol utilizada para autorización. */
    private final String description;
}

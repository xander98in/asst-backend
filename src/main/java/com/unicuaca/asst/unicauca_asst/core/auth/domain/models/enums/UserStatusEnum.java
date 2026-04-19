package com.unicuaca.asst.unicauca_asst.core.auth.domain.models.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Enum que define los estados posibles de un usuario del sistema.
 *
 * <p>Cada constante expone una descripción legible que se utiliza como nombre
 * persistido y como valor de comparación con el catálogo de estados de usuario.</p>
 */
@Getter
@RequiredArgsConstructor
public enum UserStatusEnum {

    /** Usuario activo y habilitado para operar en el sistema. */
    ACTIVE("Activo"),

    /** Usuario dado de baja o temporalmente inhabilitado. */
    INACTIVE("Inactivo"),

    /** Usuario bloqueado por razones de seguridad o administrativas. */
    BLOCKED("Bloqueado");

    /** Descripción legible del estado. */
    private final String description;
}

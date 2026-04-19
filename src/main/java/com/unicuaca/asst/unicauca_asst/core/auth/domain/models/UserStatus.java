package com.unicuaca.asst.unicauca_asst.core.auth.domain.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Modelo de dominio que representa un estado de usuario del sistema.
 */
@Getter
@Setter
@AllArgsConstructor
@Builder
@ToString
public class UserStatus {

    /** Identificador único del estado de usuario. */
    private Long id;

    /** Nombre del estado (p. ej. "Activo", "Inactivo", "Bloqueado"). */
    private String name;
}

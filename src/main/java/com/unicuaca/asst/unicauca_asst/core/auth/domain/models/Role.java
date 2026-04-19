package com.unicuaca.asst.unicauca_asst.core.auth.domain.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Modelo de dominio que representa un rol del sistema.
 */
@Getter
@Setter
@AllArgsConstructor
@Builder
@ToString
public class Role {

    /** Identificador único del rol. */
    private Long id;

    /** Nombre legible del rol (p. ej. "Administrador"). */
    private String name;

    /** Clave técnica del rol utilizada por Spring Security (p. ej. "ADMIN"). */
    private String keyName;
}

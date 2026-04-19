package com.unicuaca.asst.unicauca_asst.core.auth.domain.models;

import java.time.LocalDateTime;
import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Modelo de dominio que representa un usuario del sistema.
 *
 * <p>Usa {@code personEvaluatedId} ({@link Long}) en lugar del objeto completo
 * para evitar dependencia circular con el módulo {@code batteries_management}.</p>
 */
@Getter
@Setter
@AllArgsConstructor
@Builder
@ToString
public class SystemUser {

    /** Identificador único del usuario del sistema. */
    private Long id;

    /** Correo electrónico institucional del usuario. */
    private String email;

    /** Nombre de usuario utilizado para el inicio de sesión. */
    private String username;

    /** Nombre completo del usuario. */
    private String fullName;

    /** Fecha y hora en que el usuario fue registrado en el sistema. */
    private LocalDateTime registeredAt;

    /** Fecha y hora de creación del registro. */
    private LocalDateTime createdAt;

    /** Fecha y hora de la última actualización del registro. */
    private LocalDateTime updatedAt;

    /** Estado actual del usuario (activo, inactivo, bloqueado). */
    private UserStatus status;

    /** Conjunto de roles asignados al usuario. */
    private Set<Role> roles;

    /**
     * Identificador de la persona evaluada asociada, si corresponde.
     *
     * <p>Se mantiene como {@link Long} en lugar del objeto completo para evitar
     * una dependencia circular con el módulo {@code batteries_management}.</p>
     */
    private Long personEvaluatedId;
}

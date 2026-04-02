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
 * Usa personEvaluatedId (Long) en lugar del objeto completo
 * para evitar dependencia circular con el módulo batteries_management.
 */
@Getter
@Setter
@AllArgsConstructor
@Builder
@ToString
public class SystemUser {

    private Long id;
    private String email;
    private String username;
    private String fullName;
    private LocalDateTime registeredAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private UserStatus status;
    private Set<Role> roles;
    private Long personEvaluatedId;
}

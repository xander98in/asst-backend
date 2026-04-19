package com.unicuaca.asst.unicauca_asst.core.auth.application.dto.response;

import java.time.LocalDateTime;
import java.util.Set;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DTO de respuesta para representar los datos públicos de un usuario del sistema.
 *
 * <p>Expone los atributos necesarios para mostrar el usuario en la API, incluyendo
 * su estado, los roles asignados y la persona evaluada vinculada (si corresponde).</p>
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SystemUserResponseDTO {

    /** Identificador único del usuario. */
    @Schema(example = "1", description = "ID único del usuario")
    private Long id;

    /** Correo electrónico institucional del usuario. */
    @Schema(example = "juan.perez@unicauca.edu.co", description = "Correo electrónico del usuario")
    private String email;

    /** Nombre de usuario utilizado para el inicio de sesión. */
    @Schema(example = "juan.perez", description = "Nombre de usuario")
    private String username;

    /** Nombre completo del usuario. */
    @Schema(example = "Juan Pérez", description = "Nombre completo del usuario")
    private String fullName;

    /** Fecha y hora en que el usuario fue registrado en el sistema. */
    @Schema(example = "2026-03-31T10:30:00", description = "Fecha de registro del usuario")
    private LocalDateTime registeredAt;

    /** Fecha y hora de creación del registro. */
    @Schema(example = "2026-03-31T10:30:00", description = "Fecha de creación del registro")
    private LocalDateTime createdAt;

    /** Fecha y hora de la última actualización del registro. */
    @Schema(example = "2026-03-31T10:30:00", description = "Fecha de última actualización")
    private LocalDateTime updatedAt;

    /** Estado actual del usuario (Activo, Inactivo o Bloqueado). */
    @Schema(example = "Activo", description = "Estado del usuario")
    private String status;

    /** Conjunto de claves técnicas de los roles asignados al usuario. */
    @Schema(example = "[\"ADMIN\", \"PROFESIONAL_ASST\"]", description = "Claves técnicas de los roles asignados")
    private Set<String> roles;

    /** Identificador de la persona evaluada vinculada al usuario, si aplica. */
    @Schema(example = "5", description = "ID de la persona evaluada vinculada")
    private Long personEvaluatedId;
}

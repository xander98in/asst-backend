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
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SystemUserResponseDTO {

    @Schema(example = "1", description = "ID único del usuario")
    private Long id;

    @Schema(example = "juan.perez@unicauca.edu.co", description = "Correo electrónico del usuario")
    private String email;

    @Schema(example = "juan.perez", description = "Nombre de usuario")
    private String username;

    @Schema(example = "Juan Pérez", description = "Nombre completo del usuario")
    private String fullName;

    @Schema(example = "2026-03-31T10:30:00", description = "Fecha de registro del usuario")
    private LocalDateTime registeredAt;

    @Schema(example = "2026-03-31T10:30:00", description = "Fecha de creación del registro")
    private LocalDateTime createdAt;

    @Schema(example = "2026-03-31T10:30:00", description = "Fecha de última actualización")
    private LocalDateTime updatedAt;

    @Schema(example = "Activo", description = "Estado del usuario")
    private String status;

    @Schema(example = "[\"ADMIN\", \"PROFESIONAL_ASST\"]", description = "Claves técnicas de los roles asignados")
    private Set<String> roles;

    @Schema(example = "5", description = "ID de la persona evaluada vinculada")
    private Long personEvaluatedId;
}

package com.unicuaca.asst.unicauca_asst.core.auth.application.dto.request;

import java.util.Set;

import com.unicuaca.asst.unicauca_asst.common.validation.FirstGroup;
import com.unicuaca.asst.unicauca_asst.common.validation.SecondGroup;

import jakarta.validation.GroupSequence;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para la creación de un usuario del sistema.
 *
 * Contiene los datos requeridos para registrar un nuevo usuario,
 * incluyendo validaciones de formato y obligatoriedad.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@GroupSequence({FirstGroup.class, SecondGroup.class, SystemUserCreateRequestDTO.class})
public class SystemUserCreateRequestDTO {

    /**
     * Correo electrónico institucional del usuario.
     */
    @NotBlank(message = "{systemUser.email.notBlank}", groups = FirstGroup.class)
    @Size(min = 10, max = 150, message = "{systemUser.email.size}", groups = SecondGroup.class)
    @Pattern(
        regexp = "^[\\w.+\\-]+@unicauca\\.edu\\.co$",
        message = "{systemUser.email.pattern}",
        groups = SecondGroup.class
    )
    private String email;

    /**
     * Nombre de usuario único en el sistema.
     */
    @NotBlank(message = "{systemUser.username.notBlank}", groups = FirstGroup.class)
    @Size(min = 3, max = 100, message = "{systemUser.username.size}", groups = SecondGroup.class)
    private String username;

    /**
     * Nombre completo del usuario.
     */
    @NotBlank(message = "{systemUser.fullName.notBlank}", groups = FirstGroup.class)
    @Size(min = 3, max = 200, message = "{systemUser.fullName.size}", groups = SecondGroup.class)
    private String fullName;

    /**
     * IDs de los roles a asignar al usuario.
     */
    @NotEmpty(message = "{systemUser.roleIds.notEmpty}", groups = FirstGroup.class)
    private Set<Long> roleIds;

    /**
     * ID de la persona evaluada vinculada (opcional).
     */
    private Long personEvaluatedId;
}

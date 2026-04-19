package com.unicuaca.asst.unicauca_asst.core.auth.application.dto.request;

import com.unicuaca.asst.unicauca_asst.common.validation.FirstGroup;
import com.unicuaca.asst.unicauca_asst.common.validation.SecondGroup;

import jakarta.validation.GroupSequence;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para la solicitud de inicio de sesión con Google OAuth.
 *
 * <p>Transporta el token de identidad emitido por Google, que será validado
 * por la infraestructura antes de autenticar al usuario en el sistema.</p>
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@GroupSequence({FirstGroup.class, SecondGroup.class, GoogleLoginRequestDTO.class})
public class GoogleLoginRequestDTO {

    /**
     * Token de identidad emitido por Google OAuth.
     */
    @NotBlank(message = "{systemUser.googleIdToken.notBlank}", groups = FirstGroup.class)
    private String googleIdToken;
}

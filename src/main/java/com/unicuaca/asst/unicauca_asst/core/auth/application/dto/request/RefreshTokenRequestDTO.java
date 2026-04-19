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
 * DTO para la solicitud de refresco de token JWT.
 *
 * <p>Transporta el refresh token previamente emitido por el sistema,
 * utilizado para obtener un nuevo access token sin requerir reautenticación.</p>
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@GroupSequence({FirstGroup.class, SecondGroup.class, RefreshTokenRequestDTO.class})
public class RefreshTokenRequestDTO {

    /**
     * Token de refresco JWT.
     */
    @NotBlank(message = "{systemUser.refreshToken.notBlank}", groups = FirstGroup.class)
    private String refreshToken;
}

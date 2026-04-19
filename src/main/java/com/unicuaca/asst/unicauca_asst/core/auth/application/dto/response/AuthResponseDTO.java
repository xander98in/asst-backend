package com.unicuaca.asst.unicauca_asst.core.auth.application.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DTO de respuesta para el proceso de autenticación.
 *
 * <p>Contiene los tokens emitidos por el sistema (access y refresh), el tipo de token
 * y su tiempo de expiración, junto con la información del usuario autenticado.</p>
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthResponseDTO {

    /** Token de acceso JWT utilizado para autorizar peticiones al backend. */
    @Schema(example = "eyJhbGciOiJIUzI1NiIs...", description = "Token de acceso JWT")
    private String accessToken;

    /** Token de refresco JWT utilizado para obtener un nuevo access token. */
    @Schema(example = "eyJhbGciOiJIUzI1NiIs...", description = "Token de refresco JWT")
    private String refreshToken;

    /** Tipo de token emitido (por ejemplo, {@code Bearer}). */
    @Schema(example = "Bearer", description = "Tipo de token")
    private String tokenType;

    /** Tiempo de expiración del access token, expresado en segundos. */
    @Schema(example = "3600", description = "Tiempo de expiración en segundos")
    private Long expiresIn;

    /** Información del usuario autenticado. */
    @Schema(description = "Información del usuario autenticado")
    private SystemUserResponseDTO user;
}

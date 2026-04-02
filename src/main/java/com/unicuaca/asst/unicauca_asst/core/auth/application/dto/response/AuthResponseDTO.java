package com.unicuaca.asst.unicauca_asst.core.auth.application.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DTO de respuesta para el proceso de autenticación.
 * Contiene los tokens de acceso y la información del usuario autenticado.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthResponseDTO {

    @Schema(example = "eyJhbGciOiJIUzI1NiIs...", description = "Token de acceso JWT")
    private String accessToken;

    @Schema(example = "eyJhbGciOiJIUzI1NiIs...", description = "Token de refresco JWT")
    private String refreshToken;

    @Schema(example = "Bearer", description = "Tipo de token")
    private String tokenType;

    @Schema(example = "3600", description = "Tiempo de expiración en segundos")
    private Long expiresIn;

    @Schema(description = "Información del usuario autenticado")
    private SystemUserResponseDTO user;
}

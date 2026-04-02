package com.unicuaca.asst.unicauca_asst.core.auth.infrastructure.adapters.input.controllers;

import com.unicuaca.asst.unicauca_asst.common.docs.ErrorResponseApiResponse;
import com.unicuaca.asst.unicauca_asst.common.docs.VoidApiResponse;
import com.unicuaca.asst.unicauca_asst.common.response.ApiResponse;
import com.unicuaca.asst.unicauca_asst.common.response.ResponseUtil;
import com.unicuaca.asst.unicauca_asst.common.response.SuccessCode;
import com.unicuaca.asst.unicauca_asst.core.auth.application.command.AuthCommandHandler;
import com.unicuaca.asst.unicauca_asst.core.auth.application.dto.request.GoogleLoginRequestDTO;
import com.unicuaca.asst.unicauca_asst.core.auth.application.dto.request.RefreshTokenRequestDTO;
import com.unicuaca.asst.unicauca_asst.core.auth.application.dto.response.AuthResponseDTO;
import com.unicuaca.asst.unicauca_asst.core.auth.application.dto.response.SystemUserResponseDTO;
import com.unicuaca.asst.unicauca_asst.core.auth.application.query.SystemUserQueryHandler;
import com.unicuaca.asst.unicauca_asst.core.auth.infrastructure.adapters.input.controllers.docs.AuthResponseApiResponse;
import com.unicuaca.asst.unicauca_asst.core.auth.infrastructure.config.GoogleTokenService;
import com.unicuaca.asst.unicauca_asst.core.auth.infrastructure.config.JwtService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controlador REST para operaciones de autenticación.
 *
 * <p>Forma parte de la capa de entrada (Input Adapter) en la arquitectura hexagonal.
 * Gestiona login con Google OAuth, refresco de tokens y logout.</p>
 */
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "Autenticación", description = "Operaciones de autenticación: login, refresh y logout")
public class AuthController {

    private final AuthCommandHandler authCommandHandler;
    private final SystemUserQueryHandler systemUserQueryHandler;
    private final GoogleTokenService googleTokenService;
    private final JwtService jwtService;

    @Value("${jwt.access-token.expiration}")
    private long accessTokenExpirationMs;

    /**
     * Inicia sesión verificando un token de Google OAuth y generando tokens JWT propios.
     *
     * @param dto     solicitud con el Google ID Token
     * @param request solicitud HTTP entrante
     * @return respuesta con tokens JWT y datos del usuario autenticado
     */
    @Operation(
        summary = "Iniciar sesión con Google OAuth",
        description = "Verifica el token de Google, autentica al usuario y retorna los tokens JWT."
    )
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Inicio de sesión exitoso",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = AuthResponseApiResponse.class)
            )
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "400",
            description = "Solicitud inválida",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponseApiResponse.class)
            )
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "401",
            description = "Token de Google inválido o usuario no autorizado",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponseApiResponse.class)
            )
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "500",
            description = "Error interno del servidor",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponseApiResponse.class)
            )
        )
    })
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponseDTO>> login(
            @Valid @RequestBody GoogleLoginRequestDTO dto,
            HttpServletRequest request) {

        String email = googleTokenService.verifyAndExtractEmail(dto.getGoogleIdToken());
        AuthResponseDTO result = authCommandHandler.login(email);

        String accessToken = jwtService.generateAccessToken(email, result.getUser().getRoles());
        String refreshToken = jwtService.generateRefreshToken(email);

        result.setAccessToken(accessToken);
        result.setRefreshToken(refreshToken);
        result.setExpiresIn(accessTokenExpirationMs / 1000);

        return ResponseUtil.ok(request, SuccessCode.OPERATION_COMPLETED, "Inicio de sesión exitoso", result);
    }

    /**
     * Refresca el access token usando un refresh token válido.
     *
     * @param dto     solicitud con el refresh token
     * @param request solicitud HTTP entrante
     * @return respuesta con nuevos tokens JWT y datos actualizados del usuario
     */
    @Operation(
        summary = "Refrescar token de acceso",
        description = "Valida el refresh token y genera un nuevo par de tokens JWT."
    )
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Token refrescado exitosamente",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = AuthResponseApiResponse.class)
            )
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "400",
            description = "Solicitud inválida",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponseApiResponse.class)
            )
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "401",
            description = "Refresh token inválido o expirado",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponseApiResponse.class)
            )
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "500",
            description = "Error interno del servidor",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponseApiResponse.class)
            )
        )
    })
    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<AuthResponseDTO>> refresh(
            @Valid @RequestBody RefreshTokenRequestDTO dto,
            HttpServletRequest request) {

        jwtService.validateRefreshToken(dto.getRefreshToken());

        String email = jwtService.extractEmail(dto.getRefreshToken());
        SystemUserResponseDTO user = systemUserQueryHandler.getSystemUserByEmail(email);

        String accessToken = jwtService.generateAccessToken(email, user.getRoles());
        String refreshToken = jwtService.generateRefreshToken(email);

        AuthResponseDTO response = AuthResponseDTO.builder()
            .accessToken(accessToken)
            .refreshToken(refreshToken)
            .tokenType("Bearer")
            .expiresIn(accessTokenExpirationMs / 1000)
            .user(user)
            .build();

        return ResponseUtil.ok(request, SuccessCode.OPERATION_COMPLETED,
            "Token refrescado exitosamente", response);
    }

    /**
     * Cierra la sesión del usuario autenticado.
     *
     * <p>Al ser stateless, solo confirma el cierre. El frontend descarta los tokens.</p>
     *
     * @param request solicitud HTTP entrante
     * @return respuesta 200 OK confirmando el cierre de sesión
     */
    @Operation(
        summary = "Cerrar sesión",
        description = "Cierra la sesión del usuario. Al ser stateless, el frontend descarta los tokens."
    )
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Sesión cerrada exitosamente",
            content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = VoidApiResponse.class))
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "401",
            description = "No autenticado",
            content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponseApiResponse.class))
        )
    })
    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<Void>> logout(HttpServletRequest request) {
        return ResponseUtil.ok(request, SuccessCode.OPERATION_COMPLETED,
            "Sesión cerrada exitosamente", null);
    }
}

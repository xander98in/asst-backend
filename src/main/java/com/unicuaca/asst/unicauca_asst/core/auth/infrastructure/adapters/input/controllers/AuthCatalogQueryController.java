package com.unicuaca.asst.unicauca_asst.core.auth.infrastructure.adapters.input.controllers;

import java.util.List;

import com.unicuaca.asst.unicauca_asst.common.docs.ErrorResponseApiResponse;
import com.unicuaca.asst.unicauca_asst.common.response.ApiResponse;
import com.unicuaca.asst.unicauca_asst.common.response.ResponseUtil;
import com.unicuaca.asst.unicauca_asst.common.response.SuccessCode;
import com.unicuaca.asst.unicauca_asst.core.auth.application.dto.response.RoleResponseDTO;
import com.unicuaca.asst.unicauca_asst.core.auth.application.dto.response.UserStatusResponseDTO;
import com.unicuaca.asst.unicauca_asst.core.auth.application.query.RoleQueryHandler;
import com.unicuaca.asst.unicauca_asst.core.auth.application.query.UserStatusQueryHandler;
import com.unicuaca.asst.unicauca_asst.core.auth.infrastructure.adapters.input.controllers.docs.RoleListApiResponse;
import com.unicuaca.asst.unicauca_asst.core.auth.infrastructure.adapters.input.controllers.docs.UserStatusListApiResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controlador REST para catálogos del módulo de autenticación.
 *
 * <p>Expone endpoints de solo lectura para consultar los roles y estados de usuario
 * disponibles en el sistema. Forma parte de la capa de entrada (Input Adapter)
 * en la arquitectura hexagonal.</p>
 *
 * <p>Todos los endpoints requieren rol ADMIN.</p>
 */
@Tag(
    name = "Auth - Catálogos",
    description = "Catálogos del módulo de autenticación: roles y estados de usuario"
)
@RestController
@RequestMapping("/asst/users/catalog")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class AuthCatalogQueryController {

    private final RoleQueryHandler roleQueryHandler;
    private final UserStatusQueryHandler userStatusQueryHandler;

    /**
     * Lista todos los roles disponibles en el sistema.
     *
     * @param request solicitud HTTP entrante
     * @return respuesta con la lista de roles
     */
    @Operation(
        summary = "Listar roles",
        description = "Retorna todos los roles disponibles en el sistema. Solo accesible por ADMIN."
    )
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Consulta exitosa",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = RoleListApiResponse.class)
            )
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "401",
            description = "No autenticado",
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
    @GetMapping("/roles")
    public ResponseEntity<ApiResponse<List<RoleResponseDTO>>> getAllRoles(HttpServletRequest request) {
        List<RoleResponseDTO> response = roleQueryHandler.getAllRoles();
        return ResponseUtil.ok(request, SuccessCode.RETRIEVED, "Consulta exitosa", response);
    }

    /**
     * Lista todos los estados de usuario disponibles en el sistema.
     *
     * @param request solicitud HTTP entrante
     * @return respuesta con la lista de estados de usuario
     */
    @Operation(
        summary = "Listar estados de usuario",
        description = "Retorna todos los estados de usuario disponibles en el sistema. Solo accesible por ADMIN."
    )
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Consulta exitosa",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = UserStatusListApiResponse.class)
            )
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "401",
            description = "No autenticado",
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
    @GetMapping("/user-statuses")
    public ResponseEntity<ApiResponse<List<UserStatusResponseDTO>>> getAllUserStatuses(HttpServletRequest request) {
        List<UserStatusResponseDTO> response = userStatusQueryHandler.getAllUserStatuses();
        return ResponseUtil.ok(request, SuccessCode.RETRIEVED, "Consulta exitosa", response);
    }
}

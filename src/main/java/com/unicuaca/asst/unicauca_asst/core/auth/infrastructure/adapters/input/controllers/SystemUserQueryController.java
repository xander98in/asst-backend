package com.unicuaca.asst.unicauca_asst.core.auth.infrastructure.adapters.input.controllers;

import com.unicuaca.asst.unicauca_asst.common.docs.ErrorResponseApiResponse;
import com.unicuaca.asst.unicauca_asst.common.response.ApiResponse;
import com.unicuaca.asst.unicauca_asst.common.response.ResponseUtil;
import com.unicuaca.asst.unicauca_asst.common.response.SuccessCode;
import com.unicuaca.asst.unicauca_asst.core.auth.application.dto.response.SystemUserResponseDTO;
import com.unicuaca.asst.unicauca_asst.core.auth.application.query.SystemUserQueryHandler;
import com.unicuaca.asst.unicauca_asst.core.auth.infrastructure.adapters.input.controllers.docs.SystemUserPageApiResponse;
import com.unicuaca.asst.unicauca_asst.core.auth.infrastructure.adapters.input.controllers.docs.SystemUserResponseApiResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controlador REST para operaciones de lectura sobre usuarios del sistema.
 *
 * <p>Forma parte de la capa de entrada (Input Adapter) en la arquitectura hexagonal.
 * Delega la lógica al {@link SystemUserQueryHandler}.</p>
 *
 * <p>El endpoint {@code /me} es accesible por cualquier usuario autenticado;
 * los demás endpoints requieren rol ADMIN.</p>
 */
@Tag(
    name = "Usuarios del Sistema - Consultas",
    description = "Operaciones de lectura sobre usuarios"
)
@RestController
@RequestMapping("/asst/users")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class SystemUserQueryController {

    private final SystemUserQueryHandler systemUserQueryHandler;

    /**
     * Obtiene la información del usuario autenticado actualmente.
     *
     * @param request solicitud HTTP entrante
     * @return respuesta con los datos del usuario actual
     */
    @Operation(
        summary = "Obtener usuario actual",
        description = "Retorna la información del usuario autenticado. Accesible por cualquier usuario autenticado."
    )
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Consulta exitosa",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = SystemUserResponseApiResponse.class)
            )
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "401",
            description = "No autenticado",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponseApiResponse.class))
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "404",
            description = "Usuario no encontrado",
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
    @GetMapping("/me")
    public ResponseEntity<ApiResponse<SystemUserResponseDTO>> getCurrentUser(HttpServletRequest request) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        SystemUserResponseDTO response = systemUserQueryHandler.getSystemUserByEmail(email);
        return ResponseUtil.ok(request, SuccessCode.RETRIEVED, "Consulta exitosa", response);
    }

    /**
     * Consulta un usuario del sistema por su ID.
     *
     * @param id      ID del usuario
     * @param request solicitud HTTP entrante
     * @return respuesta con los datos del usuario
     */
    @Operation(
        summary = "Consultar usuario por ID",
        description = "Retorna los datos de un usuario buscando por su ID. Solo accesible por ADMIN."
    )
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Consulta exitosa",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = SystemUserResponseApiResponse.class)
            )
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "404",
            description = "Usuario no encontrado",
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
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<SystemUserResponseDTO>> getById(
            @PathVariable Long id,
            HttpServletRequest request) {
        SystemUserResponseDTO response = systemUserQueryHandler.getSystemUserById(id);
        return ResponseUtil.ok(request, SuccessCode.RETRIEVED, "Consulta exitosa", response);
    }

    /**
     * Lista usuarios del sistema de forma paginada.
     *
     * @param page    número de página (0-indexado)
     * @param size    cantidad de registros por página
     * @param request solicitud HTTP entrante
     * @return respuesta con la página de usuarios
     */
    @Operation(
        summary = "Listar usuarios paginados",
        description = "Retorna una lista paginada de todos los usuarios del sistema. Solo accesible por ADMIN."
    )
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Consulta exitosa",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = SystemUserPageApiResponse.class)
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
    @GetMapping("/list-paged")
    public ResponseEntity<ApiResponse<Page<SystemUserResponseDTO>>> listPaged(
            @Parameter(description = "Página (0-indexado)", example = "0")
            @RequestParam(defaultValue = "0") Integer page,

            @Parameter(description = "Cantidad de registros por página", example = "10")
            @RequestParam(defaultValue = "10") Integer size,
            HttpServletRequest request) {
        Page<SystemUserResponseDTO> response = systemUserQueryHandler.listPaginatedUsers(page, size);
        return ResponseUtil.ok(request, SuccessCode.RETRIEVED, "Consulta exitosa", response);
    }

    /**
     * Lista usuarios del sistema paginados filtrando por término de búsqueda.
     *
     * @param page       número de página (0-indexado)
     * @param size       cantidad de registros por página
     * @param searchTerm término de búsqueda (email, username o nombre completo)
     * @param request    solicitud HTTP entrante
     * @return respuesta con la página de usuarios filtrada
     */
    @Operation(
        summary = "Listar usuarios paginados con filtro",
        description = "Retorna una lista paginada de usuarios filtrados por término de búsqueda " +
            "en email, username o nombre completo. Solo accesible por ADMIN."
    )
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Consulta exitosa",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = SystemUserPageApiResponse.class)
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
    @GetMapping("/list-paged-filtered")
    public ResponseEntity<ApiResponse<Page<SystemUserResponseDTO>>> listPagedFiltered(
            @Parameter(description = "Página (0-indexado)", example = "0")
            @RequestParam(defaultValue = "0") Integer page,

            @Parameter(description = "Cantidad de registros por página", example = "10")
            @RequestParam(defaultValue = "10") Integer size,

            @Parameter(description = "Término de búsqueda (email, username o nombre completo)", example = "juan")
            @RequestParam(required = false) String searchTerm,
            HttpServletRequest request) {
        Page<SystemUserResponseDTO> response = systemUserQueryHandler.listPaginatedWithSearchTerm(page, size, searchTerm);
        return ResponseUtil.ok(request, SuccessCode.RETRIEVED, "Consulta exitosa", response);
    }
}

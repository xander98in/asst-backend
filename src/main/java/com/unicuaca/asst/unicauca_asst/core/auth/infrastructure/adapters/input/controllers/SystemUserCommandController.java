package com.unicuaca.asst.unicauca_asst.core.auth.infrastructure.adapters.input.controllers;

import com.unicuaca.asst.unicauca_asst.common.docs.ErrorResponseApiResponse;
import com.unicuaca.asst.unicauca_asst.common.docs.VoidApiResponse;
import com.unicuaca.asst.unicauca_asst.common.response.ApiResponse;
import com.unicuaca.asst.unicauca_asst.common.response.ResponseUtil;
import com.unicuaca.asst.unicauca_asst.common.response.SuccessCode;
import com.unicuaca.asst.unicauca_asst.core.auth.application.command.SystemUserCommandHandler;
import com.unicuaca.asst.unicauca_asst.core.auth.application.dto.request.SystemUserCreateRequestDTO;
import com.unicuaca.asst.unicauca_asst.core.auth.application.dto.request.SystemUserStatusUpdateRequestDTO;
import com.unicuaca.asst.unicauca_asst.core.auth.application.dto.request.SystemUserUpdateRequestDTO;
import com.unicuaca.asst.unicauca_asst.core.auth.application.dto.response.SystemUserResponseDTO;
import com.unicuaca.asst.unicauca_asst.core.auth.infrastructure.adapters.input.controllers.docs.SystemUserResponseApiResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controlador REST para operaciones de escritura sobre usuarios del sistema.
 *
 * <p>Forma parte de la capa de entrada (Input Adapter) en la arquitectura hexagonal.
 * Delega la lógica al {@link SystemUserCommandHandler}. Solo accesible por ADMIN.</p>
 */
@Tag(
    name = "Usuarios del Sistema - Comandos",
    description = "Operaciones de escritura sobre usuarios (solo ADMIN)"
)
@RestController
@RequestMapping("/asst/users")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class SystemUserCommandController {

    private final SystemUserCommandHandler systemUserCommandHandler;

    /**
     * Registra un nuevo usuario del sistema.
     *
     * @param dto     datos de entrada validados para la creación
     * @param request solicitud HTTP entrante
     * @return respuesta con el usuario creado y estado HTTP 201
     */
    @Operation(
        summary = "Crear nuevo usuario del sistema",
        description = "Registra un nuevo usuario con los datos proporcionados. Solo accesible por ADMIN."
    )
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "201",
            description = "Usuario creado exitosamente",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = SystemUserResponseApiResponse.class)
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
            responseCode = "404",
            description = "Rol o estado no encontrado",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponseApiResponse.class)
            )
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "409",
            description = "Ya existe un usuario con el mismo correo electrónico",
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
    @PostMapping
    public ResponseEntity<ApiResponse<SystemUserResponseDTO>> create(
            @Valid @RequestBody SystemUserCreateRequestDTO dto,
            HttpServletRequest request) {
        SystemUserResponseDTO response = systemUserCommandHandler.createSystemUser(dto);
        return ResponseUtil.created(request, "/asst/users",
            SuccessCode.CREATED, "Usuario del sistema creado exitosamente", response);
    }

    /**
     * Actualiza los datos de un usuario existente.
     *
     * @param id      ID del usuario a actualizar
     * @param dto     datos a actualizar
     * @param request solicitud HTTP entrante
     * @return respuesta con el usuario actualizado
     */
    @Operation(
        summary = "Actualizar usuario del sistema",
        description = "Actualiza la información de un usuario existente. Solo accesible por ADMIN."
    )
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Usuario actualizado exitosamente",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = SystemUserResponseApiResponse.class)
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
            responseCode = "404",
            description = "Usuario o rol no encontrado",
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
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<SystemUserResponseDTO>> update(
            @PathVariable Long id,
            @Valid @RequestBody SystemUserUpdateRequestDTO dto,
            HttpServletRequest request) {
        SystemUserResponseDTO response = systemUserCommandHandler.updateSystemUser(id, dto);
        return ResponseUtil.ok(request, SuccessCode.UPDATED,
            "Usuario del sistema actualizado exitosamente", response);
    }

    /**
     * Cambia el estado de un usuario del sistema.
     *
     * @param id      ID del usuario
     * @param dto     datos con el nuevo estado
     * @param request solicitud HTTP entrante
     * @return respuesta con el usuario actualizado
     */
    @Operation(
        summary = "Cambiar estado de usuario",
        description = "Cambia el estado de un usuario (Activo, Inactivo, Bloqueado). Solo accesible por ADMIN."
    )
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Estado actualizado exitosamente",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = SystemUserResponseApiResponse.class)
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
            responseCode = "404",
            description = "Usuario o estado no encontrado",
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
    @PatchMapping("/{id}/status")
    public ResponseEntity<ApiResponse<SystemUserResponseDTO>> changeStatus(
            @PathVariable Long id,
            @Valid @RequestBody SystemUserStatusUpdateRequestDTO dto,
            HttpServletRequest request) {
        SystemUserResponseDTO response = systemUserCommandHandler.changeUserStatus(id, dto);
        return ResponseUtil.ok(request, SuccessCode.UPDATED,
            "Estado del usuario actualizado exitosamente", response);
    }

    /**
     * Elimina un usuario del sistema.
     *
     * @param id      ID del usuario a eliminar
     * @param request solicitud HTTP entrante
     * @return respuesta 200 OK sin contenido
     */
    @Operation(
        summary = "Eliminar usuario del sistema",
        description = "Elimina un usuario por su ID. Solo accesible por ADMIN."
    )
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Usuario eliminado exitosamente",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = VoidApiResponse.class)
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
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(
            @PathVariable Long id,
            HttpServletRequest request) {
        systemUserCommandHandler.deleteSystemUser(id);
        return ResponseUtil.ok(request, SuccessCode.DELETED,
            "Usuario del sistema eliminado exitosamente", null);
    }
}

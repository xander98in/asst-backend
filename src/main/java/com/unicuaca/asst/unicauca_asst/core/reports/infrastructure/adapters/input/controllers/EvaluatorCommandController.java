package com.unicuaca.asst.unicauca_asst.core.reports.infrastructure.adapters.input.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.unicuaca.asst.unicauca_asst.common.docs.ErrorResponseApiResponse;
import com.unicuaca.asst.unicauca_asst.common.docs.VoidApiResponse;
import com.unicuaca.asst.unicauca_asst.common.response.ApiResponse;
import com.unicuaca.asst.unicauca_asst.common.response.ResponseUtil;
import com.unicuaca.asst.unicauca_asst.common.response.SuccessCode;
import com.unicuaca.asst.unicauca_asst.core.auth.application.query.SystemUserQueryHandler;
import com.unicuaca.asst.unicauca_asst.core.reports.application.command.EvaluatorCommandHandler;
import com.unicuaca.asst.unicauca_asst.core.reports.application.dto.request.EvaluatorCreateRequestDTO;
import com.unicuaca.asst.unicauca_asst.core.reports.application.dto.request.EvaluatorUpdateRequestDTO;
import com.unicuaca.asst.unicauca_asst.core.reports.application.dto.response.EvaluatorResponseDTO;
import com.unicuaca.asst.unicauca_asst.core.reports.infrastructure.adapters.input.controllers.docs.EvaluatorApiResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

/**
 * Controlador REST para operaciones de escritura sobre evaluadores.
 */
@RestController
@RequestMapping("/asst/reports/evaluators")
@RequiredArgsConstructor
@Tag(name = "Evaluadores - Comandos", description = "Operaciones de escritura sobre evaluadores")
@SecurityRequirement(name = "bearerAuth")
public class EvaluatorCommandController {

    private final EvaluatorCommandHandler evaluatorCommandHandler;
    private final SystemUserQueryHandler systemUserQueryHandler;

    /**
     * Crea un nuevo evaluador asociado al usuario autenticado.
     *
     * @param request La información del evaluador a crear.
     * @return La respuesta con el evaluador creado o un error si la solicitud es inválida.
     */
    @Operation(
        summary = "Crear un evaluador",
        description = "Registra un nuevo evaluador asociado al usuario autenticado."
    )
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "201",
            description = "Evaluador creado exitosamente",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = EvaluatorApiResponse.class)
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
            responseCode = "500",
            description = "Error interno del servidor",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponseApiResponse.class)
            )
        )
    })
    @PostMapping("")
    public ResponseEntity<ApiResponse<EvaluatorResponseDTO>> createEvaluator(
        @Valid @RequestBody EvaluatorCreateRequestDTO request,
        HttpServletRequest httpRequest
    ) {
        Long userId = getAuthenticatedUserId();
        EvaluatorResponseDTO response = evaluatorCommandHandler.createEvaluator(request, userId);
        return ResponseUtil.created(
            httpRequest,
            "/asst/reports/evaluators",
            SuccessCode.CREATED,
            "Evaluador creado exitosamente",
            response
        );
    }

    /**
     * Actualiza un evaluador existente del usuario autenticado.
     *
     * @param evaluatorId El ID del evaluador a actualizar.
     * @param request La información actualizada del evaluador.
     * @return La respuesta con el evaluador actualizado o un error si la solicitud es inválida o el evaluador no existe.
     */
    @Operation(
        summary = "Actualizar un evaluador",
        description = "Actualiza los datos de un evaluador existente del usuario autenticado."
    )
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Evaluador actualizado exitosamente",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = EvaluatorApiResponse.class)
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
            description = "Evaluador no encontrado",
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
    @PutMapping("/{evaluatorId}")
    public ResponseEntity<ApiResponse<EvaluatorResponseDTO>> updateEvaluator(
        @PathVariable Long evaluatorId,
        @Valid @RequestBody EvaluatorUpdateRequestDTO request,
        HttpServletRequest httpRequest
    ) {
        Long userId = getAuthenticatedUserId();
        EvaluatorResponseDTO response = evaluatorCommandHandler.updateEvaluator(evaluatorId, request, userId);
        return ResponseUtil.ok(httpRequest, SuccessCode.UPDATED, "Evaluador actualizado exitosamente", response);
    }

    /**
     * Elimina un evaluador del usuario autenticado si no está asociado a espacios de análisis.
     *
     * @param evaluatorId El ID del evaluador a eliminar.
     * @return La respuesta indicando el resultado de la operación o un error si el evaluador no existe o está en uso.
     */
    @Operation(
        summary = "Eliminar un evaluador",
        description = "Elimina un evaluador del usuario autenticado si no está en uso."
    )
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Evaluador eliminado exitosamente",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = VoidApiResponse.class)
            )
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "400",
            description = "El evaluador está asociado a espacios de análisis",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponseApiResponse.class)
            )
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "404",
            description = "Evaluador no encontrado",
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
    @DeleteMapping("/{evaluatorId}")
    public ResponseEntity<ApiResponse<Void>> deleteEvaluator(
        @PathVariable Long evaluatorId,
        HttpServletRequest httpRequest
    ) {
        Long userId = getAuthenticatedUserId();
        evaluatorCommandHandler.deleteEvaluator(evaluatorId, userId);
        return ResponseUtil.ok(httpRequest, SuccessCode.DELETED, "Evaluador eliminado exitosamente", null);
    }

    private Long getAuthenticatedUserId() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return systemUserQueryHandler.getSystemUserByEmail(email).getId();
    }
}

package com.unicuaca.asst.unicauca_asst.core.reports.infrastructure.adapters.input.controllers;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.unicuaca.asst.unicauca_asst.common.docs.ErrorResponseApiResponse;
import com.unicuaca.asst.unicauca_asst.common.response.ApiResponse;
import com.unicuaca.asst.unicauca_asst.common.response.ResponseUtil;
import com.unicuaca.asst.unicauca_asst.common.response.SuccessCode;
import com.unicuaca.asst.unicauca_asst.core.auth.application.query.SystemUserQueryHandler;
import com.unicuaca.asst.unicauca_asst.core.reports.application.dto.response.EvaluatorResponseDTO;
import com.unicuaca.asst.unicauca_asst.core.reports.application.query.EvaluatorQueryHandler;
import com.unicuaca.asst.unicauca_asst.core.reports.infrastructure.adapters.input.controllers.docs.EvaluatorApiResponse;
import com.unicuaca.asst.unicauca_asst.core.reports.infrastructure.adapters.input.controllers.docs.EvaluatorListApiResponse;
import com.unicuaca.asst.unicauca_asst.core.reports.infrastructure.adapters.input.controllers.docs.EvaluatorPageApiResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

/**
 * Controlador REST para operaciones de lectura sobre evaluadores.
 */
@RestController
@RequestMapping("/asst/reports/evaluators")
@RequiredArgsConstructor
@Tag(name = "Evaluadores - Consultas", description = "Operaciones de lectura sobre evaluadores")
@SecurityRequirement(name = "bearerAuth")
public class EvaluatorQueryController {

    private final EvaluatorQueryHandler evaluatorQueryHandler;
    private final SystemUserQueryHandler systemUserQueryHandler;

    @Operation(
        summary = "Listar evaluadores del usuario",
        description = "Retorna los evaluadores creados por el usuario autenticado."
    )
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Consulta exitosa",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = EvaluatorListApiResponse.class)
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
    @GetMapping("")
    public ResponseEntity<ApiResponse<List<EvaluatorResponseDTO>>> getEvaluatorsByUser(
        HttpServletRequest httpRequest
    ) {
        Long userId = getAuthenticatedUserId();
        List<EvaluatorResponseDTO> response = evaluatorQueryHandler.getEvaluatorsByUser(userId);
        return ResponseUtil.ok(httpRequest, SuccessCode.RETRIEVED, "Consulta exitosa", response);
    }

    /**
     * Lista evaluadores del usuario autenticado de forma paginada con búsqueda opcional.
     *
     * @param searchTerm  término de búsqueda (identificación, tarjeta, licencia o nombre)
     * @param page        número de página (0-indexado)
     * @param size        cantidad de registros por página
     * @param httpRequest solicitud HTTP entrante
     * @return página de {@link EvaluatorResponseDTO}
     */
    @Operation(
        summary = "Listar evaluadores paginados con búsqueda",
        description = "Lista paginada de evaluadores del usuario autenticado. El término de búsqueda filtra por número de identificación, tarjeta profesional, licencia o nombre."
    )
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Consulta exitosa",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = EvaluatorPageApiResponse.class)
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
    public ResponseEntity<ApiResponse<Page<EvaluatorResponseDTO>>> getEvaluatorsByUserPaged(
        @Parameter(description = "Término de búsqueda (identificación, tarjeta, licencia o nombre)")
        @RequestParam(required = false) String searchTerm,
        @Parameter(description = "Página (0-indexado)")
        @RequestParam(defaultValue = "0") Integer page,
        @Parameter(description = "Registros por página")
        @RequestParam(defaultValue = "10") Integer size,
        HttpServletRequest httpRequest
    ) {
        Long userId = getAuthenticatedUserId();
        Page<EvaluatorResponseDTO> response = evaluatorQueryHandler.getEvaluatorsByUserPaged(userId, searchTerm, page, size);
        return ResponseUtil.ok(httpRequest, SuccessCode.RETRIEVED, "Consulta exitosa", response);
    }

    @Operation(
        summary = "Obtener un evaluador por ID",
        description = "Retorna el detalle de un evaluador del usuario autenticado."
    )
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Consulta exitosa",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = EvaluatorApiResponse.class)
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

    /**
     * Obtiene un evaluador por su ID, asegurándose de que pertenezca al usuario autenticado.
     *
     * @param evaluatorId ID del evaluador a consultar
     * @param httpRequest solicitud HTTP entrante
     * @return detalle del evaluador solicitado
     */
    @GetMapping("/{evaluatorId}")
    public ResponseEntity<ApiResponse<EvaluatorResponseDTO>> getEvaluatorById(
        @PathVariable Long evaluatorId,
        HttpServletRequest httpRequest
    ) {
        Long userId = getAuthenticatedUserId();
        EvaluatorResponseDTO response = evaluatorQueryHandler.getEvaluatorById(evaluatorId, userId);
        return ResponseUtil.ok(httpRequest, SuccessCode.RETRIEVED, "Consulta exitosa", response);
    }

    private Long getAuthenticatedUserId() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return systemUserQueryHandler.getSystemUserByEmail(email).getId();
    }
}

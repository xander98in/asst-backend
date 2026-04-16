package com.unicuaca.asst.unicauca_asst.core.reports.infrastructure.adapters.input.controllers;

import java.util.List;

import com.unicuaca.asst.unicauca_asst.common.docs.ErrorResponseApiResponse;
import com.unicuaca.asst.unicauca_asst.common.response.ApiResponse;
import com.unicuaca.asst.unicauca_asst.common.response.ResponseUtil;
import com.unicuaca.asst.unicauca_asst.common.response.SuccessCode;
import com.unicuaca.asst.unicauca_asst.core.auth.application.query.SystemUserQueryHandler;
import com.unicuaca.asst.unicauca_asst.core.reports.application.dto.response.AnalysisSpaceResponseDTO;
import com.unicuaca.asst.unicauca_asst.core.reports.application.dto.response.AnalysisSpaceSummaryResponseDTO;
import com.unicuaca.asst.unicauca_asst.core.reports.application.query.AnalysisSpaceQueryHandler;
import com.unicuaca.asst.unicauca_asst.core.reports.infrastructure.adapters.input.controllers.docs.AnalysisSpaceApiResponse;
import com.unicuaca.asst.unicauca_asst.core.reports.infrastructure.adapters.input.controllers.docs.AnalysisSpaceSummaryListApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controlador REST para operaciones de lectura sobre espacios de análisis.
 *
 * <p>Pertenece a la capa de infraestructura (adaptador de entrada) y delega
 * las solicitudes al handler de la capa de aplicación.</p>
 */
@RestController
@RequestMapping("/asst/reports/analysis-spaces")
@RequiredArgsConstructor
@Tag(name = "Espacios de Análisis - Consultas", description = "Operaciones de lectura sobre espacios de análisis")
@SecurityRequirement(name = "bearerAuth")
public class AnalysisSpaceQueryController {

    private final AnalysisSpaceQueryHandler analysisSpaceQueryHandler;
    private final SystemUserQueryHandler systemUserQueryHandler;

    /**
     * Lista los espacios de análisis del usuario autenticado.
     */
    @Operation(
        summary = "Listar espacios de análisis del usuario",
        description = "Retorna la lista de espacios de análisis creados por el usuario autenticado."
    )
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Consulta exitosa",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = AnalysisSpaceSummaryListApiResponse.class)
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
    public ResponseEntity<ApiResponse<List<AnalysisSpaceSummaryResponseDTO>>> getAnalysisSpacesByUser(
        HttpServletRequest httpRequest
    ) {
        Long userId = getAuthenticatedUserId();
        List<AnalysisSpaceSummaryResponseDTO> response = analysisSpaceQueryHandler.getAnalysisSpacesByUser(userId);
        return ResponseUtil.ok(httpRequest, SuccessCode.RETRIEVED, "Consulta exitosa", response);
    }

    /**
     * Obtiene el detalle completo de un espacio de análisis con sus baterías.
     */
    @Operation(
        summary = "Obtener detalle de un espacio de análisis",
        description = "Retorna el espacio de análisis con todas sus baterías asociadas."
    )
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Consulta exitosa",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = AnalysisSpaceApiResponse.class)
            )
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "404",
            description = "Espacio de análisis no encontrado",
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
    @GetMapping("/{spaceId}")
    public ResponseEntity<ApiResponse<AnalysisSpaceResponseDTO>> getAnalysisSpaceById(
        @PathVariable Long spaceId,
        HttpServletRequest httpRequest
    ) {
        Long userId = getAuthenticatedUserId();
        AnalysisSpaceResponseDTO response = analysisSpaceQueryHandler.getAnalysisSpaceById(spaceId, userId);
        return ResponseUtil.ok(httpRequest, SuccessCode.RETRIEVED, "Consulta exitosa", response);
    }

    /**
     * Obtiene el ID del usuario autenticado a partir del SecurityContext.
     */
    private Long getAuthenticatedUserId() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return systemUserQueryHandler.getSystemUserByEmail(email).getId();
    }
}

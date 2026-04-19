package com.unicuaca.asst.unicauca_asst.core.reports.infrastructure.adapters.input.controllers;

import com.unicuaca.asst.unicauca_asst.common.docs.ErrorResponseApiResponse;
import com.unicuaca.asst.unicauca_asst.common.response.ApiResponse;
import com.unicuaca.asst.unicauca_asst.common.response.ResponseUtil;
import com.unicuaca.asst.unicauca_asst.common.response.SuccessCode;
import com.unicuaca.asst.unicauca_asst.core.auth.application.query.SystemUserQueryHandler;
import com.unicuaca.asst.unicauca_asst.core.reports.application.dto.response.DomainsAndDimensionsResponseDTO;
import com.unicuaca.asst.unicauca_asst.core.reports.application.dto.response.GroupSummaryResponseDTO;
import com.unicuaca.asst.unicauca_asst.core.reports.application.dto.response.RiskMatrixResponseDTO;
import com.unicuaca.asst.unicauca_asst.core.reports.application.query.GroupReportQueryHandler;
import com.unicuaca.asst.unicauca_asst.core.reports.infrastructure.adapters.input.controllers.docs.DomainsAndDimensionsApiResponse;
import com.unicuaca.asst.unicauca_asst.core.reports.infrastructure.adapters.input.controllers.docs.GroupSummaryApiResponse;
import com.unicuaca.asst.unicauca_asst.core.reports.infrastructure.adapters.input.controllers.docs.RiskMatrixApiResponse;
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
 * Controlador REST para consultas de informes grupales de riesgo psicosocial.
 *
 * <p>Pertenece a la capa de infraestructura (adaptador de entrada) y delega
 * las solicitudes al handler de la capa de aplicación.</p>
 */
@RestController
@RequestMapping("/asst/reports/analysis-spaces")
@RequiredArgsConstructor
@Tag(name = "Informes Grupales", description = "Consultas de informes grupales de riesgo psicosocial")
@SecurityRequirement(name = "bearerAuth")
public class GroupReportQueryController {

    private final GroupReportQueryHandler groupReportQueryHandler;
    private final SystemUserQueryHandler systemUserQueryHandler;

    /**
     * Obtiene el resumen general grupal del espacio de análisis del usuario autenticado,
     * incluyendo totales globales y resúmenes por cuestionario.
     *
     * @param spaceId     ID del espacio de análisis
     * @param httpRequest solicitud HTTP entrante
     * @return respuesta API con el resumen general grupal
     */
    @Operation(
        summary = "Obtener resumen general del espacio de análisis",
        description = "Retorna el resumen grupal con distribución de riesgo, promedios y moda para cada cuestionario."
    )
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Consulta exitosa",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = GroupSummaryApiResponse.class)
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
    @GetMapping("/{spaceId}/summary")
    public ResponseEntity<ApiResponse<GroupSummaryResponseDTO>> getGroupSummary(
        @PathVariable Long spaceId,
        HttpServletRequest httpRequest
    ) {
        Long userId = getAuthenticatedUserId();
        GroupSummaryResponseDTO response = groupReportQueryHandler.getGroupSummary(spaceId, userId);
        return ResponseUtil.ok(httpRequest, SuccessCode.RETRIEVED, "Consulta exitosa", response);
    }

    /**
     * Obtiene el desglose grupal por dominios intralaborales, dimensiones intralaborales
     * y dimensiones extralaborales del espacio de análisis del usuario autenticado.
     *
     * @param spaceId     ID del espacio de análisis
     * @param httpRequest solicitud HTTP entrante
     * @return respuesta API con el desglose de dominios y dimensiones
     */
    @Operation(
        summary = "Obtener desglose por dominios y dimensiones del espacio",
        description = "Retorna la distribución de riesgo por dominio y dimensión intralaboral y extralaboral del grupo."
    )
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Consulta exitosa",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = DomainsAndDimensionsApiResponse.class)
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
    @GetMapping("/{spaceId}/domains-dimensions")
    public ResponseEntity<ApiResponse<DomainsAndDimensionsResponseDTO>> getDomainsAndDimensions(
        @PathVariable Long spaceId,
        HttpServletRequest httpRequest
    ) {
        Long userId = getAuthenticatedUserId();
        DomainsAndDimensionsResponseDTO response = groupReportQueryHandler.getDomainsAndDimensions(spaceId, userId);
        return ResponseUtil.ok(httpRequest, SuccessCode.RETRIEVED, "Consulta exitosa", response);
    }

    /**
     * Obtiene la matriz de riesgo grupal del espacio de análisis del usuario autenticado,
     * con magnitud de riesgo, índice de asociación con estrés y semáforos para dimensiones,
     * dominios y totales intralaboral y extralaboral.
     *
     * @param spaceId     ID del espacio de análisis
     * @param httpRequest solicitud HTTP entrante
     * @return respuesta API con la matriz de riesgo del espacio
     */
    @Operation(
        summary = "Obtener matriz de riesgo del espacio de análisis",
        description = "Retorna la matriz de riesgo con magnitud, índice de asociación y semáforos para cada dimensión y dominio."
    )
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Consulta exitosa",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = RiskMatrixApiResponse.class)
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
    @GetMapping("/{spaceId}/risk-matrix")
    public ResponseEntity<ApiResponse<RiskMatrixResponseDTO>> getRiskMatrix(
        @PathVariable Long spaceId,
        HttpServletRequest httpRequest
    ) {
        Long userId = getAuthenticatedUserId();
        RiskMatrixResponseDTO response = groupReportQueryHandler.getRiskMatrix(spaceId, userId);
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

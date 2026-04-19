package com.unicuaca.asst.unicauca_asst.core.reports.infrastructure.adapters.input.controllers;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import com.unicuaca.asst.unicauca_asst.common.docs.ErrorResponseApiResponse;
import com.unicuaca.asst.unicauca_asst.common.response.ApiResponse;
import com.unicuaca.asst.unicauca_asst.common.response.ResponseUtil;
import com.unicuaca.asst.unicauca_asst.common.response.SuccessCode;
import com.unicuaca.asst.unicauca_asst.core.auth.application.query.SystemUserQueryHandler;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.application.dto.response.BatteryManagementRecordInformationResponseDTO;
import com.unicuaca.asst.unicauca_asst.core.reports.application.dto.response.AnalysisSpaceResponseDTO;
import com.unicuaca.asst.unicauca_asst.core.reports.application.dto.response.AnalysisSpaceSummaryResponseDTO;
import com.unicuaca.asst.unicauca_asst.core.reports.application.query.AnalysisSpaceQueryHandler;
import com.unicuaca.asst.unicauca_asst.core.reports.infrastructure.adapters.input.controllers.docs.AnalysisSpaceApiResponse;
import com.unicuaca.asst.unicauca_asst.core.reports.infrastructure.adapters.input.controllers.docs.AnalysisSpaceSummaryListApiResponse;
import com.unicuaca.asst.unicauca_asst.core.reports.infrastructure.adapters.input.controllers.docs.BatteryManagementInformationPageApiResponse;
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
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
     * Lista los espacios de análisis creados por el usuario autenticado.
     *
     * @param httpRequest solicitud HTTP entrante
     * @return respuesta API con la lista de espacios de análisis del usuario en formato resumido
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
     *
     * @param spaceId     ID del espacio de análisis
     * @param httpRequest información de la petición HTTP
     * @return respuesta API con el detalle del espacio de análisis o error si no se encuentra
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
     * Lista de forma paginada las baterías asociadas a un espacio de análisis
     * aplicando múltiples filtros opcionales. Valida que el espacio exista y
     * pertenezca al usuario autenticado.
     *
     * @param spaceId              ID del espacio de análisis
     * @param identificationNumber prefijo del número de identificación (opcional)
     * @param workAreaName         contenido parcial del área de trabajo (opcional)
     * @param dateFrom             fecha inicial del rango en formato yyyy-MM-dd (opcional)
     * @param dateTo               fecha final del rango en formato yyyy-MM-dd (opcional)
     * @param identificationTypeId ID del tipo de identificación (opcional)
     * @param jobPositionTypeId    ID del tipo de cargo (opcional)
     * @param intralaboralForm     forma intralaboral "A" o "B" (opcional)
     * @param page                 número de página (0-indexado)
     * @param size                 tamaño de página
     * @param httpRequest          información de la petición HTTP
     * @return respuesta API con la página de baterías del espacio filtradas
     */
    @Operation(
        summary = "Listar baterías de un espacio con multifiltro",
        description = "Lista paginada de baterías de un espacio de análisis filtradas por identificación, área, fechas, tipo de identificación, tipo de cargo y forma intralaboral. Todos los filtros son opcionales."
    )
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Consulta exitosa",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = BatteryManagementInformationPageApiResponse.class)
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
    @GetMapping("/{spaceId}/batteries/multifilter")
    public ResponseEntity<ApiResponse<Page<BatteryManagementRecordInformationResponseDTO>>> getSpaceBatteriesWithMultifilter(
        @PathVariable Long spaceId,
        @Parameter(description = "Prefijo del número de identificación")
        @RequestParam(required = false) String identificationNumber,
        @Parameter(description = "Área de trabajo (contenido parcial)")
        @RequestParam(required = false) String workAreaName,
        @Parameter(description = "Fecha inicial (yyyy-MM-dd)")
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateFrom,
        @Parameter(description = "Fecha final (yyyy-MM-dd)")
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateTo,
        @Parameter(description = "ID del tipo de identificación")
        @RequestParam(required = false) Long identificationTypeId,
        @Parameter(description = "ID del tipo de cargo")
        @RequestParam(required = false) Long jobPositionTypeId,
        @Parameter(description = "Forma intralaboral (A o B)")
        @RequestParam(required = false) String intralaboralForm,
        @RequestParam(defaultValue = "0") Integer page,
        @RequestParam(defaultValue = "10") Integer size,
        HttpServletRequest httpRequest
    ) {
        Long userId = getAuthenticatedUserId();
        LocalDateTime dateTimeFrom = dateFrom != null ? dateFrom.atStartOfDay() : null;
        LocalDateTime dateTimeTo = dateTo != null ? dateTo.atTime(23, 59, 59) : null;

        Page<BatteryManagementRecordInformationResponseDTO> response =
            analysisSpaceQueryHandler.getSpaceBatteriesWithMultifilter(
                spaceId, userId, identificationNumber, workAreaName,
                dateTimeFrom, dateTimeTo, identificationTypeId, jobPositionTypeId,
                intralaboralForm, page, size
            );
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

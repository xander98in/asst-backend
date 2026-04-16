package com.unicuaca.asst.unicauca_asst.core.reports.infrastructure.adapters.input.controllers;

import com.unicuaca.asst.unicauca_asst.common.docs.ErrorResponseApiResponse;
import com.unicuaca.asst.unicauca_asst.common.response.ApiResponse;
import com.unicuaca.asst.unicauca_asst.common.response.ResponseUtil;
import com.unicuaca.asst.unicauca_asst.common.response.SuccessCode;
import com.unicuaca.asst.unicauca_asst.core.auth.application.query.SystemUserQueryHandler;
import com.unicuaca.asst.unicauca_asst.core.reports.application.dto.response.IndividualReportResponseDTO;
import com.unicuaca.asst.unicauca_asst.core.reports.application.query.IndividualReportQueryHandler;
import com.unicuaca.asst.unicauca_asst.core.reports.infrastructure.adapters.input.controllers.docs.IndividualReportApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controlador REST que expone los endpoints de consulta de informes de riesgo psicosocial.
 *
 * <p>Pertenece a la capa de infraestructura (adaptador de entrada) de la arquitectura hexagonal,
 * y delega las solicitudes al handler de la capa de aplicación.</p>
 */
@RestController
@RequestMapping("/asst/reports")
@RequiredArgsConstructor
@Tag(name = "Informes de Riesgo Psicosocial", description = "Endpoints para generar informes individuales de calificación de riesgo psicosocial")
@SecurityRequirement(name = "bearerAuth")
public class IndividualReportQueryController {

    private final IndividualReportQueryHandler individualReportQueryHandler;
    private final SystemUserQueryHandler systemUserQueryHandler;

    /**
     * Genera el informe individual de calificación de riesgo psicosocial
     * para una batería cerrada.
     *
     * @param batteryRecordId ID del registro de gestión de batería
     * @param request         solicitud HTTP entrante
     * @return una {@link ResponseEntity} con un {@link ApiResponse} que contiene el
     *         {@link IndividualReportResponseDTO}
     */
    @Operation(
        summary = "Obtener informe individual de riesgo psicosocial",
        description = "Genera el informe individual de calificación de riesgo psicosocial " +
            "para una batería cerrada, incluyendo resultados intralaboral, extralaboral, " +
            "total general y estrés."
    )
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Informe generado exitosamente",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = IndividualReportApiResponse.class)
            )
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "400",
            description = "La batería no cumple las precondiciones para generar el informe",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponseApiResponse.class)
            )
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "404",
            description = "Registro de gestión de batería no encontrado",
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
    @GetMapping("/individual/{batteryRecordId}")
    public ResponseEntity<ApiResponse<IndividualReportResponseDTO>> getIndividualReport(
        @PathVariable @Positive @Min(1) Long batteryRecordId,
        HttpServletRequest request
    ) {
        IndividualReportResponseDTO response =
            individualReportQueryHandler.getIndividualReport(batteryRecordId);
        return ResponseUtil.ok(request, SuccessCode.RETRIEVED, "Informe individual generado exitosamente", response);
    }

    /**
     * Descarga el PDF del informe individual de riesgo psicosocial asociado a un espacio
     * de análisis.
     *
     * @param batteryRecordId ID del registro de gestión de batería
     * @param spaceId         ID del espacio de análisis (para obtener el evaluador)
     * @param request         solicitud HTTP entrante
     * @return una {@link ResponseEntity} con el PDF como recurso descargable
     */
    @Operation(
        summary = "Descargar PDF del informe individual",
        description = "Genera y descarga el PDF del informe individual de riesgo psicosocial."
    )
    @GetMapping("/individual/{batteryRecordId}/pdf")
    public ResponseEntity<Resource> getIndividualReportPdf(
        @PathVariable @Positive @Min(1) Long batteryRecordId,
        @RequestParam @Positive @Min(1) Long spaceId,
        HttpServletRequest request
    ) {
        Long userId = getAuthenticatedUserId();
        byte[] pdfBytes = individualReportQueryHandler.getIndividualReportPdf(batteryRecordId, spaceId, userId);
        ByteArrayResource resource = new ByteArrayResource(pdfBytes);
        return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=Informe_Individual_" + batteryRecordId + ".pdf")
            .contentType(MediaType.APPLICATION_PDF)
            .contentLength(resource.contentLength())
            .body(resource);
    }

    /**
     * Obtiene el ID del usuario autenticado a partir del SecurityContext.
     */
    private Long getAuthenticatedUserId() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return systemUserQueryHandler.getSystemUserByEmail(email).getId();
    }
}

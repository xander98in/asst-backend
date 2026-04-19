package com.unicuaca.asst.unicauca_asst.core.reports.infrastructure.adapters.input.controllers;

import com.unicuaca.asst.unicauca_asst.common.docs.ErrorResponseApiResponse;
import com.unicuaca.asst.unicauca_asst.common.docs.VoidApiResponse;
import com.unicuaca.asst.unicauca_asst.common.response.ApiResponse;
import com.unicuaca.asst.unicauca_asst.common.response.ResponseUtil;
import com.unicuaca.asst.unicauca_asst.common.response.SuccessCode;
import com.unicuaca.asst.unicauca_asst.core.auth.application.query.SystemUserQueryHandler;
import com.unicuaca.asst.unicauca_asst.core.reports.application.command.AnalysisSpaceCommandHandler;
import com.unicuaca.asst.unicauca_asst.core.reports.application.dto.request.AnalysisSpaceAddBatteriesRequestDTO;
import com.unicuaca.asst.unicauca_asst.core.reports.application.dto.request.AnalysisSpaceCreateRequestDTO;
import com.unicuaca.asst.unicauca_asst.core.reports.application.dto.response.AnalysisSpaceResponseDTO;
import com.unicuaca.asst.unicauca_asst.core.reports.infrastructure.adapters.input.controllers.docs.AnalysisSpaceApiResponse;
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
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controlador REST para operaciones de escritura sobre espacios de análisis.
 *
 * <p>Pertenece a la capa de infraestructura (adaptador de entrada) y delega
 * las solicitudes al handler de la capa de aplicación.</p>
 */
@RestController
@RequestMapping("/asst/reports/analysis-spaces")
@RequiredArgsConstructor
@Tag(name = "Espacios de Análisis - Comandos", description = "Operaciones de escritura sobre espacios de análisis")
@SecurityRequirement(name = "bearerAuth")
public class AnalysisSpaceCommandController {

    private final AnalysisSpaceCommandHandler analysisSpaceCommandHandler;
    private final SystemUserQueryHandler systemUserQueryHandler;

    /**
     * Crea un nuevo espacio de análisis para el usuario autenticado.
     *
     * @param request     información del espacio de análisis a crear
     * @param httpRequest solicitud HTTP entrante
     * @return respuesta API con el espacio de análisis creado
     */
    @Operation(
        summary = "Crear un espacio de análisis",
        description = "Crea un nuevo espacio de análisis asociado al usuario autenticado."
    )
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "201",
            description = "Espacio de análisis creado exitosamente",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = AnalysisSpaceApiResponse.class)
            )
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "400",
            description = "Solicitud inválida o regla de negocio violada",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponseApiResponse.class)
            )
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "409",
            description = "Ya existe un espacio con ese nombre para el usuario",
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
    public ResponseEntity<ApiResponse<AnalysisSpaceResponseDTO>> createAnalysisSpace(
        @Valid @RequestBody AnalysisSpaceCreateRequestDTO request,
        HttpServletRequest httpRequest
    ) {
        Long userId = getAuthenticatedUserId();
        AnalysisSpaceResponseDTO response = analysisSpaceCommandHandler.createAnalysisSpace(request, userId);
        return ResponseUtil.created(
            httpRequest,
            "/asst/reports/analysis-spaces",
            SuccessCode.CREATED,
            "Espacio de análisis creado exitosamente",
            response
        );
    }

    /**
     * Agrega baterías cerradas a un espacio de análisis existente del usuario autenticado.
     *
     * @param spaceId     ID del espacio de análisis
     * @param request     identificadores de las baterías a asociar
     * @param httpRequest solicitud HTTP entrante
     * @return respuesta API vacía confirmando la asociación
     */
    @Operation(
        summary = "Agregar baterías a un espacio de análisis",
        description = "Agrega una o más baterías cerradas a un espacio de análisis del usuario autenticado."
    )
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Baterías agregadas exitosamente",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = VoidApiResponse.class)
            )
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "400",
            description = "Solicitud inválida o batería no cerrada",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponseApiResponse.class)
            )
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "404",
            description = "Espacio de análisis o batería no encontrado",
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
    @PostMapping("/{spaceId}/batteries")
    public ResponseEntity<ApiResponse<Void>> addBatteriesToSpace(
        @PathVariable Long spaceId,
        @Valid @RequestBody AnalysisSpaceAddBatteriesRequestDTO request,
        HttpServletRequest httpRequest
    ) {
        Long userId = getAuthenticatedUserId();
        analysisSpaceCommandHandler.addBatteriesToSpace(spaceId, request, userId);
        return ResponseUtil.ok(httpRequest, SuccessCode.OPERATION_COMPLETED, "Baterías agregadas exitosamente", null);
    }

    /**
     * Remueve una batería individual de un espacio de análisis del usuario autenticado.
     *
     * @param spaceId          ID del espacio de análisis
     * @param batteryRecordId  ID del registro de gestión de batería a remover
     * @param httpRequest      solicitud HTTP entrante
     * @return respuesta API vacía confirmando la remoción
     */
    @Operation(
        summary = "Remover una batería de un espacio de análisis",
        description = "Remueve una batería individual de un espacio de análisis del usuario autenticado."
    )
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Batería removida exitosamente",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = VoidApiResponse.class)
            )
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "400",
            description = "Regla de negocio violada (mínimo 1 batería)",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponseApiResponse.class)
            )
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "404",
            description = "Espacio o batería no encontrado",
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
    @DeleteMapping("/{spaceId}/batteries/{batteryRecordId}")
    public ResponseEntity<ApiResponse<Void>> removeBatteryFromSpace(
        @PathVariable Long spaceId,
        @PathVariable Long batteryRecordId,
        HttpServletRequest httpRequest
    ) {
        Long userId = getAuthenticatedUserId();
        analysisSpaceCommandHandler.removeBatteryFromSpace(spaceId, batteryRecordId, userId);
        return ResponseUtil.ok(httpRequest, SuccessCode.DELETED, "Batería removida del espacio exitosamente", null);
    }

    /**
     * Elimina un espacio de análisis completo del usuario autenticado junto con todas
     * sus asociaciones.
     *
     * @param spaceId     ID del espacio de análisis a eliminar
     * @param httpRequest solicitud HTTP entrante
     * @return respuesta API vacía confirmando la eliminación
     */
    @Operation(
        summary = "Eliminar un espacio de análisis",
        description = "Elimina un espacio de análisis del usuario autenticado y todas sus asociaciones."
    )
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Espacio de análisis eliminado exitosamente",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = VoidApiResponse.class)
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
    @DeleteMapping("/{spaceId}")
    public ResponseEntity<ApiResponse<Void>> deleteAnalysisSpace(
        @PathVariable Long spaceId,
        HttpServletRequest httpRequest
    ) {
        Long userId = getAuthenticatedUserId();
        analysisSpaceCommandHandler.deleteAnalysisSpace(spaceId, userId);
        return ResponseUtil.ok(httpRequest, SuccessCode.DELETED, "Espacio de análisis eliminado exitosamente", null);
    }

    /**
     * Obtiene el ID del usuario autenticado a partir del SecurityContext.
     */
    private Long getAuthenticatedUserId() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return systemUserQueryHandler.getSystemUserByEmail(email).getId();
    }
}

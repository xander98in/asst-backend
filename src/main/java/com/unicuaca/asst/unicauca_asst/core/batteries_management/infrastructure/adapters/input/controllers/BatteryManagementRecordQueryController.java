package com.unicuaca.asst.unicauca_asst.core.batteries_management.infrastructure.adapters.input.controllers;

import com.unicuaca.asst.unicauca_asst.common.docs.ErrorResponseApiResponse;
import com.unicuaca.asst.unicauca_asst.common.response.ApiResponse;
import com.unicuaca.asst.unicauca_asst.common.response.ResponseUtil;
import com.unicuaca.asst.unicauca_asst.common.response.SuccessCode;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.application.dto.response.BatteryManagementRecordInformationResponseDTO;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.application.query.BatteryManagementRecordQueryHandler;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.infrastructure.adapters.input.controllers.docs.BatteryManagementRecordInformationApiResponse;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.infrastructure.adapters.input.controllers.docs.BatteryManagementRecordInformationPageApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controlador REST que expone los endpoints de consulta relacionados con registros de gestión de baterías.
 * .
 * Esta clase pertenece a la capa de infraestructura (adaptador de entrada) de la arquitectura hexagonal,
 * y se encarga de recibir las solicitudes HTTP desde el exterior y delegarlas a los casos de uso
 * definidos en la capa de aplicación.
 *
 * <p>Utiliza {@link BatteryManagementRecordQueryHandler} como puerto de entrada para ejecutar la lógica de consulta
 * relacionada con registros de gestión de baterías.</p>
 */
@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/asst/battery-management-record")
@RequiredArgsConstructor
public class BatteryManagementRecordQueryController {

    private final BatteryManagementRecordQueryHandler batteryManagementRecordQueryHandler;

    /**
     * Endpoint para listar registros de gestión de baterías de forma paginada. (Excepto el estado "Cerrado")
     *
     * @param page número de página (0-indexado)
     * @param size cantidad de registros por página
     * @param request solicitud HTTP entrante
     * @return una {@link ResponseEntity} con un {@link ApiResponse} que contiene una página de
     *         {@link BatteryManagementRecordInformationResponseDTO}
     */
    @Operation(
        summary = "Listar registros de gestión de baterías paginados",
        description = "Retorna una lista paginada de registros de gestión de baterías. (Excepto con el estado" +
            " 'Cerrado')"
    )
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Consulta exitosa (puede retornar página vacía)",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = BatteryManagementRecordInformationPageApiResponse.class)
            )
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "400",
            description = "Parámetros inválidos",
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
    @GetMapping("/list-paged")
    public ResponseEntity<ApiResponse<Page<BatteryManagementRecordInformationResponseDTO>>> listPaged(
        @Parameter(description = "Página (0-indexado)", example = "0")
        @RequestParam(defaultValue = "0") Integer page,

        @Parameter(description = "Cantidad de registros por página", example = "10")
        @RequestParam(defaultValue = "10") Integer size,

        HttpServletRequest request
    ) {
        Page<BatteryManagementRecordInformationResponseDTO> response = batteryManagementRecordQueryHandler.listPaginatedRecords(page, size);
        return ResponseUtil.ok(request, SuccessCode.RETRIEVED, "Consulta exitosa", response);
    }

    /**
     * Endpoint para listar registros de gestión de baterías de forma paginada,
     * filtrando por prefijo de identificación. (Excepto el estado "Cerrado")
     *
     * @param page número de página (0-indexado)
     * @param size cantidad de registros por página
     * @param term prefijo del número de identificación para filtrar (opcional)
     * @param request solicitud HTTP entrante
     * @return una {@link ResponseEntity} con un {@link ApiResponse} que contiene una página de
     *         {@link BatteryManagementRecordInformationResponseDTO}
     */
    @Operation(
        summary = "Listar registros de gestión de baterías paginados por prefijo de identificación",
        description = "Retorna una lista paginada de registros de gestión de baterías filtrados por prefijo de" +
            " número de identificación. (Excepto con el estado 'Cerrado')"
    )
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Consulta exitosa (puede retornar página vacía)",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = BatteryManagementRecordInformationPageApiResponse.class)
            )
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "400",
            description = "Parámetros inválidos",
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
    @GetMapping("/list-paged-by-identification-prefix")
    public ResponseEntity<ApiResponse<Page<BatteryManagementRecordInformationResponseDTO>>> listPagedByIdentificationPrefix(
        @Parameter(description = "Página (0-indexado)", example = "0")
        @RequestParam(defaultValue = "0") Integer page,

        @Parameter(description = "Cantidad de registros por página", example = "10")
        @RequestParam(defaultValue = "10") Integer size,

        @Parameter(description = "Prefijo del número de identificación para filtrar", example = "12345")
        @RequestParam(required = false) String term,

        HttpServletRequest request
    ) {
        Page<BatteryManagementRecordInformationResponseDTO> response =
            batteryManagementRecordQueryHandler.listPaginatedByIdentificationPrefix(page, size, term);

        return ResponseUtil.ok(request, SuccessCode.RETRIEVED, "Consulta exitosa", response);
    }

    /**
     * Endpoint para listar registros de gestión de baterías de forma paginada,
     * filtrando por término de búsqueda en número de identificación o nombre del área de trabajo.
     * (Excepto el estado "Cerrado")
     *
     * @param page número de página (0-indexado)
     * @param size cantidad de registros por página
     * @param searchTerm término de búsqueda para filtrar (opcional)
     * @param request solicitud HTTP entrante
     * @return una {@link ResponseEntity} con un {@link ApiResponse} que contiene una página de
     *         {@link BatteryManagementRecordInformationResponseDTO}
     */
    @Operation(
        summary = "Listar registros de gestión de baterías paginados con término de búsqueda",
        description = "Retorna una lista paginada de registros de gestión de baterías filtrados por término de búsqueda" +
            " en número de identificación o nombre del área de trabajo. (Excepto con el estado 'Cerrado')"
    )
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Consulta exitosa (puede retornar página vacía)",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = BatteryManagementRecordInformationPageApiResponse.class)
            )
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "400",
            description = "Parámetros inválidos",
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
    @GetMapping("/list-paged-filtered")
    public ResponseEntity<ApiResponse<Page<BatteryManagementRecordInformationResponseDTO>>> listPagedWithSearchTerm(
        @Parameter(description = "Página (0-indexado)", example = "0")
        @RequestParam(defaultValue = "0") Integer page,

        @Parameter(description = "Cantidad de registros por página", example = "10")
        @RequestParam(defaultValue = "10") Integer size,

        @Parameter(description = "Término de búsqueda para filtrar", example = "12345 o Área de Trabajo")
        @RequestParam(required = false) String searchTerm,

        HttpServletRequest request
    ) {
        Page<BatteryManagementRecordInformationResponseDTO> response =
            batteryManagementRecordQueryHandler.listPagedWithSearchTerm(page, size, searchTerm);
        return ResponseUtil.ok(request, SuccessCode.RETRIEVED, "Consulta exitosa", response);
    }

    /**
     * Endpoint para obtener la información detallada de un registro de gestión de baterías por su ID.
     *
     * @param id ID del registro de gestión de baterías
     * @param request solicitud HTTP entrante
     * @return una {@link ResponseEntity} con un {@link ApiResponse} que contiene un
     *         {@link BatteryManagementRecordInformationResponseDTO}
     */
    @Operation(
        summary = "Obtener información detallada de un registro de gestión de baterías por ID",
        description = "Retorna la información detallada de un registro de gestión de baterías a partir de su ID."
    )
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Consulta exitosa",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = BatteryManagementRecordInformationApiResponse.class)
            )
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "404",
            description = "Registro no encontrado",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponseApiResponse.class)
            )
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "400",
            description = "Parámetros inválidos",
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
    public ResponseEntity<ApiResponse<BatteryManagementRecordInformationResponseDTO>> getById(@PathVariable @Positive @Min(1) Long id, HttpServletRequest request) {
        BatteryManagementRecordInformationResponseDTO response =
            batteryManagementRecordQueryHandler.getRecordInformationById(id);

        return ResponseUtil.ok(request, SuccessCode.RETRIEVED, "Consulta exitosa", response);
    }
}

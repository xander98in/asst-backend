package com.unicuaca.asst.unicauca_asst.core.batteries_management.infrastructure.adapters.input.controllers;

import com.unicuaca.asst.unicauca_asst.common.docs.ErrorResponseApiResponse;
import com.unicuaca.asst.unicauca_asst.common.response.ResponseUtil;
import com.unicuaca.asst.unicauca_asst.common.response.SuccessCode;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.application.dto.response.PersonEvaluatedInformationResponseDTO;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.infrastructure.adapters.input.controllers.docs.PersonEvaluatedInformationApiResponse;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.infrastructure.adapters.input.controllers.docs.PersonEvaluatedInformationListApiResponse;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.unicuaca.asst.unicauca_asst.common.response.ApiResponse;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.application.dto.response.PersonEvaluatedResponseDTO;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.application.query.PersonEvaluatedQueryHandler;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;

/**
 * Controlador REST que expone los endpoints de consulta relacionados con el agregado {@code Person}.
 *
 * <p>Esta clase pertenece a la capa de infraestructura (adaptador de entrada) de la arquitectura hexagonal,
 * y se encarga de recibir las solicitudes HTTP desde el exterior y delegarlas a los casos de uso
 * definidos en la capa de aplicación.</p>
 *
 * <p>Utiliza {@link PersonEvaluatedQueryHandler} como puerto de entrada para ejecutar la lógica de consulta
 * relacionada con personas.</p>
 */
@RestController
@RequestMapping("/asst/person-evaluated")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class PersonEvaluatedQueryController {

    private final PersonEvaluatedQueryHandler personEvaluatedQueryHandler;

    /**
     * Endpoint para consultar la información de una persona por su identificador.
     *
     * @param idPersonEvaluated identificador único de la persona a consultar.
     * @return una {@link ResponseEntity} con un {@link ApiResponse} que contiene los datos de la persona
     *         en un objeto {@link PersonEvaluatedResponseDTO}, o un mensaje de error si no se encuentra.
     */
    @Operation(
        summary = "Consultar persona evaluada por ID",
        description = "Retorna los detalles de la persona evaluada buscando por su ID."
    )
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Consulta exitosa",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = PersonEvaluatedInformationApiResponse.class)
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
            description = "Persona evaluada no encontrada",
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
    @GetMapping("/query-by-id/{idPersonEvaluated}")
    public ResponseEntity<ApiResponse<PersonEvaluatedResponseDTO>> queryPersonById(@PathVariable Long idPersonEvaluated, HttpServletRequest request) {
        PersonEvaluatedResponseDTO response = personEvaluatedQueryHandler.getPersonEvaluatedById(idPersonEvaluated);
        return ResponseUtil.ok(request, SuccessCode.RETRIEVED, "Consulta exitosa", response);
    }

    /**
     * Endpoint para consultar personas evaluadas por tipo y número de identificación.
     *
     * @param abbreviation          la abreviatura del tipo de identificación
     * @param identificationNumber  el número de identificación
     * @param page                el número de página (0-indexado)
     * @param size    la cantidad de registros por página
     * @return una {@link ResponseEntity} con un {@link ApiResponse} que contiene una lista paginada de
     *         {@link PersonEvaluatedInformationResponseDTO}, o un mensaje de error si no se encuentran resultados.
     */
     @Operation(
        summary = "Consultar personas evaluadas por tipo y número de identificación",
        description = "Retorna una lista paginada de personas evaluadas. Si no hay resultados, retorna una página vacía."
    )
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Consulta exitosa (puede retornar página vacía)",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = PersonEvaluatedInformationListApiResponse.class)
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
            description = "Tipo de identificación no encontrado",
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
    @GetMapping("/query-by-identity")
    public ResponseEntity<ApiResponse<Page<PersonEvaluatedInformationResponseDTO>>> queryByIdentity(
        @Parameter(description = "Abreviatura del tipo de identificación (ej: CC, CE, TI)", example = "CC", required = true)
        @RequestParam String abbreviation,

        @Parameter(description = "Número de identificación", example = "1234567890", required = true)
        @RequestParam String identificationNumber,

        @Parameter(description = "Página (0-indexado)", example = "0")
        @RequestParam(defaultValue = "0") Integer page,

        @Parameter(description = "Cantidad de registros por página", example = "10")
        @RequestParam(defaultValue = "10") Integer size,
        HttpServletRequest request) {

        Page<PersonEvaluatedInformationResponseDTO> response = personEvaluatedQueryHandler.queryByIdentity(abbreviation, identificationNumber, page, size);
        return ResponseUtil.ok(request, SuccessCode.RETRIEVED, "Consulta exitosa", response);
    }

    /**
     * Endpoint para listar personas evaluadas de forma paginada.
     *
     * @param page número de página (0-indexado)
     * @param size cantidad de registros por página
     * @param request solicitud HTTP entrante
     * @return una {@link ResponseEntity} con un {@link ApiResponse} que contiene una página de
     *         {@link PersonEvaluatedInformationResponseDTO}
     */
    @Operation(
        summary = "Listar personas evaluadas de forma paginada",
        description = "Retorna una lista paginada de todas las personas evaluadas registradas en el sistema."
    )
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Consulta exitosa",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = PersonEvaluatedInformationListApiResponse.class)
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
    @GetMapping("/list-paged")
    public ResponseEntity<ApiResponse<Page<PersonEvaluatedInformationResponseDTO>>> listPaged(
        @Parameter(description = "Página (0-indexado)", example = "0")
        @RequestParam(defaultValue = "0") Integer page,

        @Parameter(description = "Cantidad de registros por página", example = "10")
        @RequestParam(defaultValue = "10") Integer size,
        HttpServletRequest request
    ) {
        Page<PersonEvaluatedInformationResponseDTO> response = personEvaluatedQueryHandler.listPaginatedPersons(page, size);
        return ResponseUtil.ok(request, SuccessCode.RETRIEVED, "Consulta exitosa", response);
    }

    /**
     * Endpoint para listar personas evaluadas de forma paginada filtrando por término de búsqueda.
     *
     * @param page número de página (0-indexado)
     * @param size cantidad de registros por página
     * @param searchTerm término de búsqueda (identificación, nombre o apellido)
     * @param request solicitud HTTP entrante
     * @return una {@link ResponseEntity} con un {@link ApiResponse} que contiene una página de
     *         {@link PersonEvaluatedInformationResponseDTO}
     */
    @Operation(
        summary = "Listar personas evaluadas paginadas con término de búsqueda",
        description = "Retorna una lista paginada de personas evaluadas filtradas por término de búsqueda en " +
            "número de identificación, nombre o apellido."
    )
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Consulta exitosa",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = PersonEvaluatedInformationListApiResponse.class)
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
    @GetMapping("/list-paged-filtered")
    public ResponseEntity<ApiResponse<Page<PersonEvaluatedInformationResponseDTO>>> listPagedFiltered(
        @Parameter(description = "Página (0-indexado)", example = "0")
        @RequestParam(defaultValue = "0") Integer page,

        @Parameter(description = "Cantidad de registros por página", example = "10")
        @RequestParam(defaultValue = "10") Integer size,

        @Parameter(description = "Término de búsqueda para filtrar (número de identificación, nombre o apellido)", example = "Juan o 12345")
        @RequestParam(required = false) String searchTerm,
        HttpServletRequest request
    ) {
        Page<PersonEvaluatedInformationResponseDTO> response = personEvaluatedQueryHandler.listPaginatedWithSearchTerm(page, size, searchTerm);
        return ResponseUtil.ok(request, SuccessCode.RETRIEVED, "Consulta exitosa", response);
    }
}

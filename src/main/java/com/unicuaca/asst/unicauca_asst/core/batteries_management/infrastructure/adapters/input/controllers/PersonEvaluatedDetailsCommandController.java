package com.unicuaca.asst.unicauca_asst.core.batteries_management.infrastructure.adapters.input.controllers;

import com.unicuaca.asst.unicauca_asst.common.docs.ErrorResponseApiResponse;
import com.unicuaca.asst.unicauca_asst.common.docs.VoidApiResponse;
import com.unicuaca.asst.unicauca_asst.common.response.ApiResponse;
import com.unicuaca.asst.unicauca_asst.common.response.ResponseUtil;
import com.unicuaca.asst.unicauca_asst.common.response.SuccessCode;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.application.command.PersonEvaluatedDetailsCommandHandler;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.application.dto.request.PersonEvaluatedDetailsCreateRequestDTO;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.application.dto.request.PersonEvaluatedDetailsUpdateRequestDTO;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.application.dto.response.PersonEvaluatedDetailsResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controlador REST para gestionar operaciones de creación de detalles
 * de una persona evaluada (PersonEvaluatedDetails).
 *
 * Forma parte del adaptador de entrada (Input Adapter) en la arquitectura hexagonal.
 * Delega la orquestación al {@link PersonEvaluatedDetailsCommandHandler}.
 */
@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/asst/person-evaluated-details")
@RequiredArgsConstructor
public class PersonEvaluatedDetailsCommandController {

    private final PersonEvaluatedDetailsCommandHandler personEvaluatedDetailsCommandHandler;

    /**
     * Crea los detalles adicionales de una persona evaluada de un registro de gestión de baterías.
     *
     * @param dto datos de entrada validados para la creación
     */
    @Operation(
        summary = "Crear detalles de persona evaluada de un registro de gestión de baterías.",
        description = "Crea los detalles demográficos y laborales para una persona evaluada asociada a un registro de gestión de baterías."
    )
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "201",
            description = "Detalles creados con éxito",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = VoidApiResponse.class)
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
        )
    })
    @PostMapping("/create")
    public ResponseEntity<ApiResponse<PersonEvaluatedDetailsResponseDTO>> create(@Valid @RequestBody PersonEvaluatedDetailsCreateRequestDTO dto, HttpServletRequest request) {
        personEvaluatedDetailsCommandHandler.createPersonEvaluatedDetails(dto);
        return ResponseUtil.created(
            request, 
            "/asst/person-evaluated-details/create", 
            SuccessCode.CREATED, 
            "Detalles creados con éxito.",
            null
        );
    }

    /**
     * Actualiza los detalles adicionales de una persona evaluada por ID.
     *
     * @param id ID de los detalles de la persona evaluada
     * @param dto datos de entrada validados para la actualización
     * @return respuesta estandarizada indicando el éxito de la operación
     */
    @Operation(
        summary = "Actualizar detalles de persona evaluada por ID",
        description = "Actualiza los detalles demográficos y laborales de una persona evaluada a partir del ID del detalle."
    )
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Detalles actualizados con éxito",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = VoidApiResponse.class)
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
            description = "Detalle no encontrado",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponseApiResponse.class)
            )
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "409",
            description = "Conflicto al actualizar",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponseApiResponse.class)
            )
        )
    })
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> update(
        @PathVariable Long id,
        @Valid @RequestBody PersonEvaluatedDetailsUpdateRequestDTO dto,
        HttpServletRequest request
    ) {
        personEvaluatedDetailsCommandHandler.updatePersonEvaluatedDetails(id, dto);
        return ResponseUtil.ok(request, SuccessCode.UPDATED, "Detalles actualizados con éxito.", null);
    }

    /**
     * Elimina los detalles adicionales de una persona evaluada por ID.
     *
     * @param id ID de los detalles de la persona evaluada
     * @return respuesta estandarizada indicando el éxito de la operación
     */
    @Operation(
        summary = "Eliminar detalles de persona evaluada por ID",
        description = "Elimina los detalles adicionales de una persona evaluada por el ID del detalle."
    )
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Detalles eliminados con éxito",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = VoidApiResponse.class)
            )
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "404",
            description = "Detalle no encontrado",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponseApiResponse.class)
            )
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "409",
            description = "Conflicto al eliminar",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponseApiResponse.class)
            )
        )
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(
        @PathVariable Long id,
        HttpServletRequest request
    ) {
        personEvaluatedDetailsCommandHandler.deletePersonEvaluatedDetails(id);
        return ResponseUtil.ok(request, SuccessCode.DELETED, "Detalles eliminados con éxito.", null);
    }
}

package com.unicuaca.asst.unicauca_asst.core.batteries_management.infrastructure.adapters.input.controllers;

import com.unicuaca.asst.unicauca_asst.common.docs.ErrorResponseApiResponse;
import com.unicuaca.asst.unicauca_asst.common.docs.VoidApiResponse;
import com.unicuaca.asst.unicauca_asst.common.response.ResponseUtil;
import com.unicuaca.asst.unicauca_asst.common.response.SuccessCode;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.infrastructure.adapters.input.controllers.docs.PersonEvaluatedApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.unicuaca.asst.unicauca_asst.common.response.ApiResponse;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.application.command.PersonEvaluatedCommandHandler;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.application.dto.request.PersonEvaluatedCreateRequestDTO;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.application.dto.request.PersonEvaluatedUpdateRequestDTO;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.application.dto.response.PersonEvaluatedResponseDTO;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

/**
 * Controlador REST para gestionar operaciones de creación de personas evaluadas.
 * 
 * Forma parte de la capa de entrada (Input Adapter) en la arquitectura hexagonal.
 * Delega la lógica al {@link PersonEvaluatedCommandHandler}.
 */
@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/asst/person-evaluated")
@RequiredArgsConstructor
public class PersonEvaluatedCommandController {

    private final PersonEvaluatedCommandHandler personEvaluatedCommandHandler;

    /**
     * Crea una nueva persona evaluada en el sistema.
     *
     * @param dto Datos de entrada validados para la creación de una persona.
     * @return Respuesta con el objeto creado y estado HTTP 201 (Created).
     */
    @Operation(
        summary = "Crear nueva persona evaluada",
        description = "Este endpoint permite crear una nueva persona evaluada en el sistema."
    )
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "201",
            description = "Persona evaluada creada con éxito",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = PersonEvaluatedApiResponse.class)
            )
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "400",
            description = "Datos inválidos proporcionados para la creación",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponseApiResponse.class)
            )
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "409",
            description = "Conflicto al crear la entidad",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponseApiResponse.class)
            )
        )
    })
    @PostMapping
    public ResponseEntity<ApiResponse<PersonEvaluatedResponseDTO>> create(@Valid @RequestBody PersonEvaluatedCreateRequestDTO dto, HttpServletRequest request) {
        PersonEvaluatedResponseDTO response = personEvaluatedCommandHandler.createPersonEvaluated(dto);
        return ResponseUtil.created(request, "/asst/person-evaluated/create", SuccessCode.CREATED, "Persona evaluada creada con éxito", response);
    }

    /**
     * Endpoint REST para actualizar una persona evaluada.
     *
     * @param id ID de la persona evaluada
     * @param dto cuerpo de la solicitud con los datos nuevos
     * @return respuesta HTTP con el objeto actualizado
     */
    @Operation(
        summary = "Actualizar persona evaluada",
        description = "Este endpoint permite actualizar la información de una persona evaluada existente."
    )
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Persona evaluada actualizada correctamente",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = PersonEvaluatedApiResponse.class)
            )
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "400",
            description = "Datos inválidos proporcionados para la actualización",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponseApiResponse.class)
            )
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "404",
            description = "Entidad no encontrada para el ID proporcionado",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponseApiResponse.class)
            )
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "409",
            description = "Conflicto al actualizar la entidad",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponseApiResponse.class)
            )
        )
    })
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<PersonEvaluatedResponseDTO>> update(@PathVariable Long id, @Valid @RequestBody PersonEvaluatedUpdateRequestDTO dto, HttpServletRequest request) {
        PersonEvaluatedResponseDTO response = personEvaluatedCommandHandler.updatePersonEvaluated(id, dto);
        return ResponseUtil.ok(request, SuccessCode.UPDATED, "Persona evaluada actualizada exitosamente.", response);
    }

    @Operation(
        summary = "Eliminar persona evaluada",
        description = "Este endpoint permite eliminar una persona evaluada existente por su ID."
    )
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Persona evaluada eliminada correctamente",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = VoidApiResponse.class)
            )
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "404",
            description = "Entidad no encontrada para el ID proporcionado",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponseApiResponse.class)
            )
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "409",
            description = "Conflicto al eliminar la entidad",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponseApiResponse.class)
            )
        )
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id, HttpServletRequest request) {
        personEvaluatedCommandHandler.deletePersonEvaluated(id);
        return ResponseUtil.ok(request, SuccessCode.DELETED, "Persona evaluada eliminada exitosamente.", null);
    }
}

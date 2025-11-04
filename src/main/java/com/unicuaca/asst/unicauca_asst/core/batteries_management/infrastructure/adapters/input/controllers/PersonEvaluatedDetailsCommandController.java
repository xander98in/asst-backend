package com.unicuaca.asst.unicauca_asst.core.batteries_management.infrastructure.adapters.input.controllers;

import com.unicuaca.asst.unicauca_asst.common.docs.ErrorResponseApiResponse;
import com.unicuaca.asst.unicauca_asst.common.response.ApiResponse;
import com.unicuaca.asst.unicauca_asst.common.response.ResponseUtil;
import com.unicuaca.asst.unicauca_asst.common.response.SuccessCode;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.application.command.PersonEvaluatedDetailsCommandHandler;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.application.dto.request.PersonEvaluatedDetailsCreateRequestDTO;
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
     * @return respuesta estandarizada con el recurso creado
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
                schema = @Schema(implementation = ApiResponse.class)
            )
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "400",
            description = "Solicitud inválida",
            content = @Content(schema = @Schema(implementation = ErrorResponseApiResponse.class))
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "404",
            description = "Persona evaluada no encontrada",
            content = @Content(schema = @Schema(implementation = ErrorResponseApiResponse.class))
        )
    })
    @PostMapping("/create")
    public ResponseEntity<ApiResponse<PersonEvaluatedDetailsResponseDTO>> create(@Valid @RequestBody PersonEvaluatedDetailsCreateRequestDTO dto, HttpServletRequest request) {
        PersonEvaluatedDetailsResponseDTO response = personEvaluatedDetailsCommandHandler.createPersonEvaluatedDetails(dto);
        return ResponseUtil.created(request, "/asst/person-evaluated-details/create", SuccessCode.CREATED, "Detalles creados con éxito.", response);
    }
}

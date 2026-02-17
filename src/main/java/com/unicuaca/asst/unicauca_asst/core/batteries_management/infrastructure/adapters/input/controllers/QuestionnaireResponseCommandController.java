package com.unicuaca.asst.unicauca_asst.core.batteries_management.infrastructure.adapters.input.controllers;

import com.unicuaca.asst.unicauca_asst.common.docs.ErrorResponseApiResponse;
import com.unicuaca.asst.unicauca_asst.common.docs.VoidApiResponse;
import com.unicuaca.asst.unicauca_asst.common.response.ApiResponse;
import com.unicuaca.asst.unicauca_asst.common.response.ResponseUtil;
import com.unicuaca.asst.unicauca_asst.common.response.SuccessCode;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.application.command.QuestionnaireResponseCommandHandler;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.application.dto.request.QuestionnaireResponseBatchCreateRequestDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controlador REST para gestionar la recepción de respuestas de los cuestionarios.
 *
 * Forma parte de la capa de entrada (Input Adapter).
 * Delega la orquestación al {@link QuestionnaireResponseCommandHandler}.
 */
@Tag(
    name = "Respuestas de cuestionarios",
    description = "Endpoints para gestionar respuestas de cuestionarios."
)
@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/asst/questionnaire-response")
@RequiredArgsConstructor
public class QuestionnaireResponseCommandController {

    private final QuestionnaireResponseCommandHandler questionnaireResponseCommandHandler;

    /**
     * Registra un lote de respuestas para un registro de gestión de cuestionario específico.
     * Cada respuesta debe contener el ID de la pregunta y el valor de la respuesta.
     * El proceso se realiza en una sola transacción para garantizar la consistencia de los datos.
     *
     * @param dto DTO que contiene el ID del registro y la lista de respuestas.
     * @param request Objeto HttpServletRequest para metadatos de la respuesta.
     * @return Respuesta estandarizada 201 Created sin cuerpo de datos.
     */
    @Operation(
        summary = "Registrar respuestas de cuestionario (Batch)",
        description = "Recibe un conjunto de respuestas para un registro de gestión de cuestionario específico y las almacena en una sola transacción."
    )
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "201",
            description = "Respuestas registradas con éxito",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = VoidApiResponse.class)
            )
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "400",
            description = "Datos de entrada inválidos (IDs nulos, valores fuera de rango, lista vacía)",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponseApiResponse.class)
            )
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "404",
            description = "Registro de gestión o Pregunta no encontrada",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponseApiResponse.class)
            )
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "409",
            description = "Conflicto (ej. la pregunta ya fue respondida anteriormente)",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponseApiResponse.class)
            )
        )
    })
    @PostMapping("/batch-create")
    public ResponseEntity<ApiResponse<Void>> createBatch(
        @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "DTO con el ID del registro de gestión de cuestionario y la lista de respuestas a registrar.",
            required = true,
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = QuestionnaireResponseBatchCreateRequestDTO.class)
            )
        )
        @Valid @RequestBody QuestionnaireResponseBatchCreateRequestDTO dto,
        HttpServletRequest request
    ) {
        questionnaireResponseCommandHandler.createQuestionnaireResponseBatch(dto);

        return ResponseUtil.created(
            request,
            "/asst/questionnaire-response/batch-create",
            SuccessCode.CREATED,
            "Respuestas del cuestionario registradas con éxito.",
            null
        );
    }
}

package com.unicuaca.asst.unicauca_asst.core.batteries_management.infrastructure.adapters.input.controllers;

import com.unicuaca.asst.unicauca_asst.common.docs.ErrorResponseApiResponse;
import com.unicuaca.asst.unicauca_asst.common.response.ApiResponse;
import com.unicuaca.asst.unicauca_asst.common.response.ResponseUtil;
import com.unicuaca.asst.unicauca_asst.common.response.SuccessCode;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.application.dto.response.QuestionnaireResponseBatchResponseDTO;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.application.query.QuestionnaireResponseQueryHandler;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.infrastructure.adapters.input.controllers.docs.QuestionnaireResponseBatchApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controlador REST para consultas relacionadas con las respuestas de los cuestionarios.
 *
 * Forma parte de la capa de entrada (Input Adapter) en la arquitectura hexagonal.
 * Delega la lógica al {@link QuestionnaireResponseQueryHandler}.
 */
@Tag(
    name = "Consulta de respuestas de cuestionarios",
    description = "Endpoints para consultar las respuestas registradas en los cuestionarios."
)
@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/asst/questionnaire-response")
@RequiredArgsConstructor
public class QuestionnaireResponseQueryController {

    private final QuestionnaireResponseQueryHandler questionnaireResponseQueryHandler;

    /**
     * Obtiene las respuestas asociadas a un registro de gestión de cuestionario.
     *
     * @param recordId ID del registro de gestión de cuestionario.
     * @return DTO con la información del registro y la lista de respuestas.
     */
    @Operation(
        summary = "Consultar respuestas por ID de registro de gestión",
        description = "Retorna todas las respuestas asociadas a un registro de gestión de cuestionario específico, incluyendo los detalles del cuestionario y su estado."
    )
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Consulta exitosa",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = QuestionnaireResponseBatchApiResponse.class)
            )
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "404",
            description = "Registro de gestión no encontrado o sin respuestas",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponseApiResponse.class)
            )
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "400",
            description = "ID inválido",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponseApiResponse.class)
            )
        )
    })
    @GetMapping("/by-record/{recordId}")
    public ResponseEntity<ApiResponse<QuestionnaireResponseBatchResponseDTO>> getByRecordId(
        @Parameter(description = "ID del registro de gestión de cuestionario", example = "123")
        @PathVariable @Positive @Min(1) Long recordId,
        HttpServletRequest request
    ) {
        QuestionnaireResponseBatchResponseDTO response = questionnaireResponseQueryHandler.getResponsesByRecordId(recordId);

        return ResponseUtil.ok(
            request,
            SuccessCode.RETRIEVED,
            "Respuestas consultadas exitosamente.",
            response
        );
    }
}

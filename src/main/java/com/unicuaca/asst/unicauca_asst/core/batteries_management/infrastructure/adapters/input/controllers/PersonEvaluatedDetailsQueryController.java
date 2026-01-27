package com.unicuaca.asst.unicauca_asst.core.batteries_management.infrastructure.adapters.input.controllers;

import com.unicuaca.asst.unicauca_asst.common.docs.ErrorResponseApiResponse;
import com.unicuaca.asst.unicauca_asst.common.response.ApiResponse;
import com.unicuaca.asst.unicauca_asst.common.response.ResponseUtil;
import com.unicuaca.asst.unicauca_asst.common.response.SuccessCode;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.application.dto.response.PersonEvaluatedDetailsMetaResponseDTO;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.application.query.PersonEvaluatedDetailsQueryHandler;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.infrastructure.adapters.input.controllers.docs.PersonEvaluatedDetailsMetaApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controlador REST para consultas relacionadas con los detalles de las personas evaluadas.
 */
@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/asst/person-evaluated-details")
@RequiredArgsConstructor
public class PersonEvaluatedDetailsQueryController {

    private final PersonEvaluatedDetailsQueryHandler personEvaluatedDetailsQueryHandler;

    /**
     * Obtiene metadata del detalle de una persona evaluada asociado a un registro de gestión de batería.
     *
     * @param batteryManagementRecordId ID del registro de gestión de batería
     * @param request                   solicitud HTTP entrante
     * @return respuesta con metadata del detalle de la persona evaluada
     */
    @Operation(
        summary = "Consultar metadata de detalles por ID de registro de gestión de batería",
        description = "Retorna id, createdAt y updatedAt del detalle PersonEvaluatedDetails asociado al recordId."
    )
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Consulta exitosa",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = PersonEvaluatedDetailsMetaApiResponse.class)
            )
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "404",
            description = "Detalle no encontrado",
            content = @Content(schema = @Schema(implementation = ErrorResponseApiResponse.class))
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "400",
            description = "Parámetros inválidos",
            content = @Content(schema = @Schema(implementation = ErrorResponseApiResponse.class))
        )
    })
    @GetMapping("/meta/by-record/{batteryManagementRecordId}")
    public ResponseEntity<ApiResponse<PersonEvaluatedDetailsMetaResponseDTO>> getMetaByRecordId(
        @Parameter(description = "ID del registro de gestión de batería", example = "1")
        @PathVariable @Positive @Min(1) Long batteryManagementRecordId,
        HttpServletRequest request
    ) {
        PersonEvaluatedDetailsMetaResponseDTO response =
            personEvaluatedDetailsQueryHandler.getMetaByBatteryManagementRecordId(batteryManagementRecordId);

        return ResponseUtil.ok(request, SuccessCode.RETRIEVED, "Consulta exitosa", response);
    }
}

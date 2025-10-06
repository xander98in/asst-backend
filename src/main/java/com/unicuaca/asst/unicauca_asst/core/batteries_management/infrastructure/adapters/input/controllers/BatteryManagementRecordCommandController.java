package com.unicuaca.asst.unicauca_asst.core.batteries_management.infrastructure.adapters.input.controllers;

import com.unicuaca.asst.unicauca_asst.common.docs.ErrorResponseApiResponse;
import com.unicuaca.asst.unicauca_asst.common.response.ApiResponse;
import com.unicuaca.asst.unicauca_asst.common.response.ResponseUtil;
import com.unicuaca.asst.unicauca_asst.common.response.SuccessCode;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.application.command.BatteryManagementRecordCommandHandler;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.application.dto.response.BatteryManagementRecordResponseDTO;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.infrastructure.adapters.input.controllers.docs.BatteryManagementRecordApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/asst/battery-management-record")
@RequiredArgsConstructor
@Tag(name = "Battery Management Records", description = "Gestión de registros de baterías")
public class BatteryManagementRecordCommandController {

    private final BatteryManagementRecordCommandHandler batteryManagementRecordCommandHandler;

    /**
     * Crea un nuevo registro de gestión de baterías para la persona evaluada indicada.
     *
     * @param personEvaluatedId ID de la persona evaluada para la cual se crea el registro.
     * @param request           Objeto HttpServletRequest para construir la URL del recurso creado.
     * @return Respuesta con el objeto creado y estado HTTP 201 (Created).
     */
    @Operation(
        summary = "Crear registro de gestión de baterías",
        description = "Crea un nuevo registro de gestión de baterías para la persona evaluada indicada."
    )
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "201",
            description = "Registro de gestión de baterías creado exitosamente",
            content = @Content(
                schema = @Schema(implementation = BatteryManagementRecordApiResponse.class)
            )
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "404",
            description = "Persona evaluada no encontrada",
            content = @Content(
                schema = @Schema(implementation = ErrorResponseApiResponse.class)
            )
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "409",
            description = "La persona ya tiene un registro",
            content = @Content(
                schema = @Schema(implementation = ErrorResponseApiResponse.class)
            )
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "400",
            description = "Solicitud inválida",
            content = @Content(
                schema = @Schema(implementation = ErrorResponseApiResponse.class)
            )
        )
    })
    @PostMapping(value = "/{personEvaluatedId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse<BatteryManagementRecordResponseDTO>> createBatteryManagementRecord(
        @PathVariable @Positive @Min(1) Long personEvaluatedId,
        HttpServletRequest request
    ) {
        BatteryManagementRecordResponseDTO dto = batteryManagementRecordCommandHandler.createBatteryManagementRecord(personEvaluatedId);
        return ResponseUtil.created(
            request,
            "/asst/battery-management-record/" + dto.getId(),
            SuccessCode.CREATED,
            "Registro de gestión de baterías creado exitosamente",
            dto
        );
    }

}

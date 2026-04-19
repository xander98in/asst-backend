package com.unicuaca.asst.unicauca_asst.core.batteries_management.infrastructure.adapters.input.controllers;

import com.unicuaca.asst.unicauca_asst.common.docs.ErrorResponseApiResponse;
import com.unicuaca.asst.unicauca_asst.common.docs.VoidApiResponse;
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
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * Controlador REST para gestionar operaciones de escritura sobre los registros de gestión de baterías.
 *
 * <p>Forma parte del adaptador de entrada (Input Adapter) en la arquitectura hexagonal y delega la
 * orquestación al {@link BatteryManagementRecordCommandHandler}. Expone endpoints para crear, eliminar
 * y cerrar registros de gestión de baterías.</p>
 */
@Tag(
    name = "Registros de Gestión de Baterías - Comandos",
    description = "Operaciones de escritura sobre los registros de gestión de baterías"
)
@Validated
@RestController
@RequestMapping("/asst/battery-management-record")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
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
            description = "Registro de gestión de baterías creado con éxito",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = BatteryManagementRecordApiResponse.class)
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
            description = "Persona evaluada o estado no encontrado",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponseApiResponse.class)
            )
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "409",
            description = "Ya existe un registro de gestión de baterías para la persona evaluada",
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

    /**
     * Elimina un registro de gestión de baterías a partir de su ID.
     *
     * @param recordId ID del registro de gestión de baterías a eliminar
     * @param request objeto HttpServletRequest para construir la respuesta
     * @return respuesta estandarizada indicando el éxito de la operación
     */
    @Operation(
        summary = "Eliminar registro de gestión de baterías",
        description = "Elimina un registro de gestión de baterías a partir de su ID."
    )
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Registro de gestión de baterías eliminado con éxito",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = VoidApiResponse.class)
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
            responseCode = "404",
            description = "Registro de gestión de baterías no encontrado",
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
    @DeleteMapping(value = "/{recordId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse<Void>> deleteBatteryManagementRecord(
        @PathVariable @Positive @Min(1) Long recordId,
        HttpServletRequest request
    ) {
        batteryManagementRecordCommandHandler.deleteBatteryManagementRecord(recordId);
        return ResponseUtil.ok(request, SuccessCode.DELETED, "Registro de gestión de baterías eliminado exitosamente", null);
    }

    /**
     * Cierra un registro de gestión de baterías a partir de su ID, cambiando su estado a 'Cerrado'.
     *
     * @param recordId ID del registro de gestión de baterías a cerrar.
     * @param request  Objeto HttpServletRequest para construir la URL del recurso actualizado.
     * @return Respuesta con el objeto actualizado y estado HTTP 200 (OK).
     */
    @Operation(
        summary = "Cerrar registro de gestión de baterías",
        description = "Cambia el estado de un registro de gestión de baterías a 'Cerrado' a partir de su ID."
    )
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Registro de gestión de baterías cerrado con éxito",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = BatteryManagementRecordApiResponse.class)
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
            responseCode = "404",
            description = "Registro de gestión de baterías no encontrado",
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
    @PatchMapping(value = "/close/{recordId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse<BatteryManagementRecordResponseDTO>> closeBatteryManagementRecord(
        @PathVariable @Positive @Min(1) Long recordId,
        HttpServletRequest request
    ) {
        BatteryManagementRecordResponseDTO dto = batteryManagementRecordCommandHandler.closeBatteryManagementRecord(recordId);
        return ResponseUtil.ok(
            request,
            SuccessCode.UPDATED,
            "Registro de gestión de baterías cerrado exitosamente",
            dto
        );
    }
}

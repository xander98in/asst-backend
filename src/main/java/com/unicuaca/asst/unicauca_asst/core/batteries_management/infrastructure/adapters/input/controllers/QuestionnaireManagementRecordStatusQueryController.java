package com.unicuaca.asst.unicauca_asst.core.batteries_management.infrastructure.adapters.input.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.unicuaca.asst.unicauca_asst.common.response.ApiResponse;
import com.unicuaca.asst.unicauca_asst.common.response.ResponseUtil;
import com.unicuaca.asst.unicauca_asst.common.response.SuccessCode;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.application.dto.response.QuestionnaireManagementRecordStatusResponseDTO;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.application.query.QuestionnaireManagementRecordStatusHandler;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.infrastructure.adapters.input.controllers.docs.QuestionnaireManagementRecordStatusApiResponse;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.infrastructure.adapters.input.controllers.docs.QuestionnaireManagementRecordStatusListApiResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

/**
 * Controlador REST para consultas de estados de registros de gestión de cuestionarios.
 * 
 * Actúa como adaptador de entrada, delegando en {@link QuestionnaireManagementRecordStatusHandler} las operaciones de solo lectura.
 */
@Tag(
    name = "Estados de registros de gestión de cuestionarios",
    description = "Operaciones de consulta (lectura) sobre el catálogo de estados de los registros de gestión de cuestionarios."
)
@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/asst/questionnaire-management-record-statuses")
@RequiredArgsConstructor
public class QuestionnaireManagementRecordStatusQueryController {

    private final QuestionnaireManagementRecordStatusHandler questionnaireManagementRecordStatusHandler;

    /**
     * Lista todos los estados de los registros de gestión de cuestionarios.
     *
     * @return {@link ApiResponse} con la lista de estados.
     */
    @Operation(
        summary = "Listar estados de registros de gestión de cuestionarios",
        description = "Retorna todos los estados configurados para los registros de gestión de cuestionarios."
    )
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Consulta exitosa",
            content = @Content(schema = @Schema(implementation = QuestionnaireManagementRecordStatusListApiResponse.class))
        )
    })
    @GetMapping
    public ResponseEntity<ApiResponse<List<QuestionnaireManagementRecordStatusResponseDTO>>> getAllStatuses(HttpServletRequest request) {

        List<QuestionnaireManagementRecordStatusResponseDTO> response = questionnaireManagementRecordStatusHandler.getAllStatuses();
        return ResponseUtil.ok(request, SuccessCode.RETRIEVED, "Consulta exitosa", response);
    }

    /**
     * Obtiene un estado de registro de gestión de cuestionarios por su identificador.
     *
     * @param id identificador del estado.
     * @return {@link ApiResponse} con el estado encontrado.
     */
    @Operation(
        summary = "Obtener estado de registro de gestión de cuestionarios por ID",
        description = "Retorna el estado asociado al ID indicado."
    )
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Consulta exitosa",
            content = @Content(schema = @Schema(implementation = QuestionnaireManagementRecordStatusApiResponse.class))
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "404",
            description = "Estado no encontrado",
            content = @Content(schema = @Schema(implementation = ApiResponse.class))
        )
    })
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<QuestionnaireManagementRecordStatusResponseDTO>> getById(
            @PathVariable Long id,
            HttpServletRequest request) {

        QuestionnaireManagementRecordStatusResponseDTO response =
            questionnaireManagementRecordStatusHandler.getById(id);

        return ResponseUtil.ok(request, SuccessCode.RETRIEVED, "Consulta exitosa", response);
    }

    /**
     * Obtiene un estado de registro de gestión de cuestionarios por su nombre.
     *
     * @param name nombre del estado (por ejemplo, "Creado", "En diligenciamiento", "Diligenciado", "Cerrado").
     * @return {@link ApiResponse} con el estado encontrado.
     */
    @Operation(
        summary = "Obtener estado de registro de gestión de cuestionarios por nombre",
        description = "Retorna el estado asociado al nombre indicado."
    )
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Consulta exitosa",
            content = @Content(schema = @Schema(implementation = QuestionnaireManagementRecordStatusApiResponse.class))
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "404",
            description = "Estado no encontrado",
            content = @Content(schema = @Schema(implementation = ApiResponse.class))
        )
    })
    @GetMapping("/by-name/{name}")
    public ResponseEntity<ApiResponse<QuestionnaireManagementRecordStatusResponseDTO>> getByName(
            @PathVariable String name,
            HttpServletRequest request) {

        QuestionnaireManagementRecordStatusResponseDTO response =
            questionnaireManagementRecordStatusHandler.getByName(name);

        return ResponseUtil.ok(request, SuccessCode.RETRIEVED, "Consulta exitosa", response);
    }

}

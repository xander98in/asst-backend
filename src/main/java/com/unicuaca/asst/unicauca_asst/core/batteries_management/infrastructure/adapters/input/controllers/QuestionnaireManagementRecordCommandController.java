package com.unicuaca.asst.unicauca_asst.core.batteries_management.infrastructure.adapters.input.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.unicuaca.asst.unicauca_asst.common.docs.ErrorResponseApiResponse;
import com.unicuaca.asst.unicauca_asst.common.response.ApiResponse;
import com.unicuaca.asst.unicauca_asst.common.response.ResponseUtil;
import com.unicuaca.asst.unicauca_asst.common.response.SuccessCode;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.application.command.QuestionnaireManagementRecordCommandHandler;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.application.dto.request.QuestionnaireManagementRecordCreateRequestDTO;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

/**
 * Controlador REST para gestionar operaciones de creación o actualización
 * de registros de gestión de cuestionarios.
 *
 * Forma parte del adaptador de entrada (Input Adapter) en la arquitectura hexagonal.
 * Delega la orquestación al {@link QuestionnaireManagementRecordCommandHandler}.
 */
@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/asst/questionnaire-management-records")
@RequiredArgsConstructor
public class QuestionnaireManagementRecordCommandController {

    private final QuestionnaireManagementRecordCommandHandler questionnaireManagementRecordCommandHandler;

    /**
     * Crea un registro de gestión de cuestionario asociado a un registro de gestión
     * de baterías y a un cuestionario específico.
     *
     * @param dto datos de entrada validados para la creación
     * @return respuesta estandarizada sin cuerpo específico por ahora (solo mensaje)
     */
    @Operation(
        summary = "Crear registro de gestión de cuestionario.",
        description = "Crea un registro de gestión de cuestionario asociado a un registro de gestión de baterías y a un cuestionario."
    )
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "201",
            description = "Registro de gestión de cuestionario creado con éxito",
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
            description = "Recurso asociado no encontrado",
            content = @Content(schema = @Schema(implementation = ErrorResponseApiResponse.class))
        )
    })
    @PostMapping("/create")
    public ResponseEntity<ApiResponse<Void>> create(
            @Valid @RequestBody QuestionnaireManagementRecordCreateRequestDTO dto,
            HttpServletRequest request
    ) {
        questionnaireManagementRecordCommandHandler.createQuestionnaireManagementRecord(dto);
        return ResponseUtil.created(
            request,
            "/asst/questionnaire-management-records/create",
            SuccessCode.CREATED,
            "Registro de gestión de cuestionario creado con éxito.",
            null // por ahora no devolvemos un DTO específico
        );
    }

}

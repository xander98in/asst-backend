package com.unicuaca.asst.unicauca_asst.core.batteries_management.infrastructure.adapters.input.controllers;

import com.unicuaca.asst.unicauca_asst.core.batteries_management.application.dto.response.QuestionnaireManagementRecordResponseDTO;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.infrastructure.adapters.input.controllers.docs.QuestionnaireManagementRecordApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.unicuaca.asst.unicauca_asst.common.docs.ErrorResponseApiResponse;
import com.unicuaca.asst.unicauca_asst.common.response.ApiResponse;
import com.unicuaca.asst.unicauca_asst.common.response.ResponseUtil;
import com.unicuaca.asst.unicauca_asst.common.response.SuccessCode;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.application.command.QuestionnaireManagementRecordCommandHandler;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.application.dto.request.QuestionnaireManagementRecordCreateRequestDTO;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

/**
 * Controlador REST para gestionar operaciones de creación o actualización o eliminación (CRUD) de los
 * registros de gestión de cuestionarios.

 * Forma parte del adaptador de entrada (Input Adapter) en la arquitectura hexagonal.
 * Delega la orquestación al {@link QuestionnaireManagementRecordCommandHandler}.
 */
@Tag(
    name = "Gestión de registros de cuestionarios",
    description = "Endpoints para gestionar los registros de gestión de cuestionarios."
)
@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/asst/questionnaire-management-record")
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
                schema = @Schema(implementation = QuestionnaireManagementRecordApiResponse.class)
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
            description = "Recurso asociado no encontrado",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponseApiResponse.class)
            )
        )
    })
    @PostMapping("/create")
    public ResponseEntity<ApiResponse<QuestionnaireManagementRecordResponseDTO>> create(
        @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Datos necesarios para crear un registro de gestión de cuestionario",
            required = true,
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = QuestionnaireManagementRecordCreateRequestDTO.class)
            )
        )
        @Valid @RequestBody QuestionnaireManagementRecordCreateRequestDTO dto,
        HttpServletRequest request
    ) {

        QuestionnaireManagementRecordResponseDTO response = questionnaireManagementRecordCommandHandler.createQuestionnaireManagementRecord(dto);
        return ResponseUtil.created(
            request,
            "/asst/questionnaire-management-records/create",
            SuccessCode.CREATED,
            "Registro de gestión de cuestionario creado con éxito.",
            response
        );
    }

}

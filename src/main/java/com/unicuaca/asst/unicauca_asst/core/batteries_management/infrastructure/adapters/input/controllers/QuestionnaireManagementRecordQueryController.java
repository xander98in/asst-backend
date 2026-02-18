package com.unicuaca.asst.unicauca_asst.core.batteries_management.infrastructure.adapters.input.controllers;

import com.unicuaca.asst.unicauca_asst.common.docs.ErrorResponseApiResponse;
import com.unicuaca.asst.unicauca_asst.common.response.ApiResponse;
import com.unicuaca.asst.unicauca_asst.common.response.ResponseUtil;
import com.unicuaca.asst.unicauca_asst.common.response.SuccessCode;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.application.dto.response.QuestionnaireManagementRecordResponseDTO;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.application.query.QuestionnaireManagementRecordQueryHandler;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.infrastructure.adapters.input.controllers.docs.QuestionnaireManagementRecordApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controlador REST para consultas relacionadas con los registros de gestión de cuestionarios.
 */
@Tag(
    name = "Consulta de registros de cuestionarios",
    description = "Endpoints para consultar registros de gestión de cuestionarios."
)
@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/asst/questionnaire-management-record")
@RequiredArgsConstructor
public class QuestionnaireManagementRecordQueryController {

    private final QuestionnaireManagementRecordQueryHandler questionnaireManagementRecordQueryHandler;

    /**
     * Obtiene el registro de gestión de cuestionario asociado a un ID de registro de gestión de batería
     * y a la abreviatura del cuestionario.
     *
     * @param batteryManagementRecordId ID del registro de gestión de batería
     * @param abbreviation              Abreviatura del cuestionario (ILA, ILB, EXT, EST)
     * @param request                   solicitud HTTP entrante
     * @return respuesta con el registro de gestión de cuestionario
     */
    @Operation(
        summary = "Consultar registro de gestión de cuestionario por recordId y abreviatura",
        description = "Retorna el registro de gestión de cuestionario asociado al ID del registro de gestión de batería y a la abreviatura del cuestionario (ILA, ILB, EXT, EST)."
    )
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Consulta exitosa",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = QuestionnaireManagementRecordApiResponse.class)
            )
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "404",
            description = "Registro no encontrado",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponseApiResponse.class)
            )
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "400",
            description = "Parámetros inválidos",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponseApiResponse.class)
            )
        )
    })
    @GetMapping("/by-record/{batteryManagementRecordId}/questionnaire/{abbreviation}")
    public ResponseEntity<ApiResponse<QuestionnaireManagementRecordResponseDTO>> getByRecordAndQuestionnaire(
        @Parameter(description = "ID del registro de gestión de batería", example = "15")
        @PathVariable @Positive @Min(1) Long batteryManagementRecordId,

        @Parameter(description = "Abreviatura del cuestionario (ILA, ILB, EXT, EST)", example = "ILA")
        @PathVariable
        @NotBlank
        String abbreviation,

        HttpServletRequest request
    ) {
        QuestionnaireManagementRecordResponseDTO response =
            questionnaireManagementRecordQueryHandler.getByBatteryRecordIdAndQuestionnaireAbbreviation(
                batteryManagementRecordId,
                abbreviation
            );

        return ResponseUtil.ok(request, SuccessCode.RETRIEVED, "Consulta exitosa", response);
    }
}

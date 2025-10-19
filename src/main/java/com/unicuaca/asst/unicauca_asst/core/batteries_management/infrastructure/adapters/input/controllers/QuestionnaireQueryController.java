package com.unicuaca.asst.unicauca_asst.core.batteries_management.infrastructure.adapters.input.controllers;

import com.unicuaca.asst.unicauca_asst.common.response.ApiResponse;
import com.unicuaca.asst.unicauca_asst.common.response.ResponseUtil;
import com.unicuaca.asst.unicauca_asst.common.response.SuccessCode;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.application.dto.response.QuestionnaireResponseDTO;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.application.query.QuestionnaireQueryHandler;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.infrastructure.adapters.input.controllers.docs.QuestionnaireApiResponse;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.infrastructure.adapters.input.controllers.docs.QuestionnaireListApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para consultas de cuestionarios.
 * <p>
 * Actúa como adaptador de entrada, delegando en {@link QuestionnaireQueryHandler} las operaciones de solo lectura.
 */
@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/asst/questionnaires")
@RequiredArgsConstructor
public class QuestionnaireQueryController {

    private final QuestionnaireQueryHandler questionnaireQueryHandler;

    /**
     * Lista todos los cuestionarios disponibles.
     *
     * @return {@link ApiResponse} con la lista de cuestionarios.
     */
    @Operation(
        summary = "Listar cuestionarios",
        description = "Retorna todos los cuestionarios configurados en el sistema."
    )
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Consulta exitosa",
            content = @Content(schema = @Schema(implementation = QuestionnaireListApiResponse.class))
        )
    })
    @GetMapping
    public ResponseEntity<ApiResponse<List<QuestionnaireResponseDTO>>> getAllQuestionnaires(HttpServletRequest request) {
        List<QuestionnaireResponseDTO> response = questionnaireQueryHandler.getAllQuestionnaires();
        return ResponseUtil.ok(request, SuccessCode.RETRIEVED, "Consulta exitosa", response);
    }

    /**
     * Obtiene un cuestionario por su abreviatura exacta.
     *
     * @param abbreviation abreviatura del cuestionario (por ejemplo, "ILA", "ILB", "EXT", "EST").
     * @return {@link ApiResponse} con el cuestionario encontrado.
     */
    @Operation(
        summary = "Obtener cuestionario por abreviatura",
        description = "Retorna el cuestionario asociado a la abreviatura indicada."
    )
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Consulta exitosa",
            content = @Content(schema = @Schema(implementation = QuestionnaireApiResponse.class))
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "404",
            description = "Cuestionario no encontrado",
            content = @Content(schema = @Schema(implementation = ApiResponse.class))
        )
    })
    @GetMapping("/{abbreviation}")
    public ResponseEntity<ApiResponse<QuestionnaireResponseDTO>> getByAbbreviation(@PathVariable String abbreviation, HttpServletRequest request) {
        QuestionnaireResponseDTO response = questionnaireQueryHandler.getByAbbreviation(abbreviation);
        return ResponseUtil.ok(request, SuccessCode.RETRIEVED, "Consulta exitosa", response);
    }
}

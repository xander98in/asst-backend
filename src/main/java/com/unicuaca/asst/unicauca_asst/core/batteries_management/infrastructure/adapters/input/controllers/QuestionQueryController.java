package com.unicuaca.asst.unicauca_asst.core.batteries_management.infrastructure.adapters.input.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.unicuaca.asst.unicauca_asst.common.docs.ErrorResponseApiResponse;
import com.unicuaca.asst.unicauca_asst.common.response.ApiResponse;
import com.unicuaca.asst.unicauca_asst.common.response.ResponseUtil;
import com.unicuaca.asst.unicauca_asst.common.response.SuccessCode;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.application.dto.response.QuestionResponseDTO;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.application.dto.response.QuestionWithQuestionnaireResponseDTO;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.application.query.QuestionQueryHandler;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.infrastructure.adapters.input.controllers.docs.QuestionApiResponse;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.infrastructure.adapters.input.controllers.docs.QuestionListApiResponse;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.infrastructure.adapters.input.controllers.docs.QuestionWithQuestionnaireApiResponse;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.infrastructure.adapters.input.controllers.docs.QuestionWithQuestionnaireListApiResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

/**
 * Controlador REST para consultas de preguntas de cuestionarios.
 *
 * Actúa como adaptador de entrada, delegando en {@link QuestionQueryHandler}
 * las operaciones de solo lectura.
 */
@Tag(
    name = "Preguntas de cuestionarios",
    description = "Operaciones de consulta (lectura) sobre las preguntas asociadas a los cuestionarios."
)
@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/asst/questions")
@RequiredArgsConstructor
public class QuestionQueryController {

    private final QuestionQueryHandler questionQueryHandler;

    /**
     * Lista todas las preguntas sin información del cuestionario asociado.
     *
     * @return {@link ApiResponse} con la lista de preguntas.
     */
    @Operation(
        summary = "Listar preguntas",
        description = "Retorna todas las preguntas registradas sin incluir información del cuestionario asociado."
    )
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Consulta exitosa",
            content = @Content(schema = @Schema(implementation = QuestionListApiResponse.class))
        )
    })
    @GetMapping
    public ResponseEntity<ApiResponse<List<QuestionResponseDTO>>> getAllQuestions(HttpServletRequest request) {
        List<QuestionResponseDTO> response = questionQueryHandler.getAllQuestions();
        return ResponseUtil.ok(request, SuccessCode.RETRIEVED, "Consulta exitosa", response);
    }

    /**
     * Lista todas las preguntas incluyendo información del cuestionario asociado.
     *
     * @return {@link ApiResponse} con la lista de preguntas y sus cuestionarios.
     */
    @Operation(
        summary = "Listar preguntas con información del cuestionario",
        description = "Retorna todas las preguntas registradas incluyendo información básica del cuestionario al que pertenecen."
    )
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Consulta exitosa",
            content = @Content(schema = @Schema(implementation = QuestionWithQuestionnaireListApiResponse.class))
        )
    })
    @GetMapping("/with-questionnaire")
    public ResponseEntity<ApiResponse<List<QuestionWithQuestionnaireResponseDTO>>> getAllQuestionsWithQuestionnaire(
            HttpServletRequest request) {

        List<QuestionWithQuestionnaireResponseDTO> response =
            questionQueryHandler.getAllQuestionsWithQuestionnaire();

        return ResponseUtil.ok(request, SuccessCode.RETRIEVED, "Consulta exitosa", response);
    }

    /**
     * Obtiene una pregunta por su ID sin información del cuestionario asociado.
     *
     * @param id identificador de la pregunta.
     * @return {@link ApiResponse} con la pregunta encontrada.
     */
    @Operation(
        summary = "Obtener pregunta por ID",
        description = "Retorna una pregunta específica sin incluir la información del cuestionario asociado."
    )
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Consulta exitosa",
            content = @Content(schema = @Schema(implementation = QuestionApiResponse.class))
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "404",
            description = "Pregunta no encontrada",
            content = @Content(schema = @Schema(implementation = ErrorResponseApiResponse.class))
        )
    })
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<QuestionResponseDTO>> getById(
            @PathVariable Long id,
            HttpServletRequest request) {

        QuestionResponseDTO response = questionQueryHandler.getById(id);
        return ResponseUtil.ok(request, SuccessCode.RETRIEVED, "Consulta exitosa", response);
    }

    /**
     * Obtiene una pregunta por su ID incluyendo la información del cuestionario asociado.
     *
     * @param id identificador de la pregunta.
     * @return {@link ApiResponse} con la pregunta y su cuestionario.
     */
    @Operation(
        summary = "Obtener pregunta por ID con información del cuestionario",
        description = "Retorna una pregunta específica incluyendo la información básica del cuestionario asociado."
    )
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Consulta exitosa",
            content = @Content(schema = @Schema(implementation = QuestionWithQuestionnaireApiResponse.class))
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "404",
            description = "Pregunta no encontrada",
            content = @Content(schema = @Schema(implementation = ErrorResponseApiResponse.class))
        )
    })
    @GetMapping("/{id}/with-questionnaire")
    public ResponseEntity<ApiResponse<QuestionWithQuestionnaireResponseDTO>> getByIdWithQuestionnaire(
            @PathVariable Long id,
            HttpServletRequest request) {

        QuestionWithQuestionnaireResponseDTO response =
            questionQueryHandler.getByIdWithQuestionnaire(id);

        return ResponseUtil.ok(request, SuccessCode.RETRIEVED, "Consulta exitosa", response);
    }

    /**
     * Obtiene una pregunta por su orden dentro de un cuestionario específico,
     * incluyendo la información del cuestionario asociado.
     *
     * @param questionnaireId identificador del cuestionario.
     * @param order           orden de la pregunta dentro de ese cuestionario.
     * @return {@link ApiResponse} con la pregunta y su cuestionario.
     */
    @Operation(
        summary = "Obtener pregunta por orden y cuestionario",
        description = "Retorna una pregunta específica identificada por su orden dentro de un cuestionario, "
                    + "incluyendo la información básica del cuestionario asociado."
    )
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Consulta exitosa",
            content = @Content(schema = @Schema(implementation = QuestionWithQuestionnaireApiResponse.class))
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "404",
            description = "Pregunta no encontrada",
            content = @Content(schema = @Schema(implementation = ErrorResponseApiResponse.class))
        )
    })
    @GetMapping("/by-order/{questionnaireId}/{order}")
    public ResponseEntity<ApiResponse<QuestionWithQuestionnaireResponseDTO>> getByOrderAndQuestionnaire(
            @PathVariable Long questionnaireId,
            @PathVariable Integer order,
            HttpServletRequest request) {

        QuestionWithQuestionnaireResponseDTO response =
            questionQueryHandler.getByOrderAndQuestionnaireIdWithQuestionnaire(order, questionnaireId);

        return ResponseUtil.ok(request, SuccessCode.RETRIEVED, "Consulta exitosa", response);
    }

    /**
     * Obtiene las preguntas asociadas a un cuestionario por su ID.
     *
     * @param questionnaireId identificador del cuestionario para el cual se desean obtener las preguntas.
     * @param request objeto de solicitud HTTP para construir la respuesta adecuada.
     * @return {@link ResponseEntity} con un {@link ApiResponse} que contiene la lista de preguntas asociadas al cuestionario especificado.
     */
    @Operation(
        summary = "Listar preguntas por ID de Cuestionario",
        description = "Retorna la lista de preguntas pertenecientes al cuestionario especificado por su ID."
    )
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Consulta exitosa",
            content = @Content(schema = @Schema(implementation = QuestionListApiResponse.class))
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "404",
            description = "Cuestionario no encontrado o sin preguntas",
            content = @Content(schema = @Schema(implementation = ErrorResponseApiResponse.class))
        )
    })
    @GetMapping("/by-questionnaire/{questionnaireId}")
    public ResponseEntity<ApiResponse<List<QuestionResponseDTO>>> getByQuestionnaireId(
        @PathVariable Long questionnaireId,
        HttpServletRequest request) {

        List<QuestionResponseDTO> response = questionQueryHandler.getByQuestionnaireId(questionnaireId);
        return ResponseUtil.ok(request, SuccessCode.RETRIEVED, "Consulta exitosa", response);
    }

    /**
     * Obtiene las preguntas asociadas a un cuestionario por su abreviatura.
     *
     * @param abbreviation abreviatura del cuestionario para el cual se desean obtener las preguntas.
     * @param request objeto de solicitud HTTP para construir la respuesta adecuada.
     * @return {@link ResponseEntity} con un {@link ApiResponse} que contiene la lista de preguntas asociadas al cuestionario especificado.
     */
    @Operation(
        summary = "Listar preguntas por Abreviatura de Cuestionario",
        description = "Retorna la lista de preguntas pertenecientes al cuestionario especificado por su abreviatura (ej: ILA, EXT)."
    )
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Consulta exitosa",
            content = @Content(schema = @Schema(implementation = QuestionListApiResponse.class))
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "404",
            description = "Cuestionario no encontrado o sin preguntas",
            content = @Content(schema = @Schema(implementation = ErrorResponseApiResponse.class))
        )
    })
    @GetMapping("/by-questionnaire-abbr/{abbreviation}")
    public ResponseEntity<ApiResponse<List<QuestionResponseDTO>>> getByQuestionnaireAbbreviation(
        @PathVariable String abbreviation,
        HttpServletRequest request) {

        List<QuestionResponseDTO> response = questionQueryHandler.getByQuestionnaireAbbreviation(abbreviation);
        return ResponseUtil.ok(request, SuccessCode.RETRIEVED, "Consulta exitosa", response);
    }
}

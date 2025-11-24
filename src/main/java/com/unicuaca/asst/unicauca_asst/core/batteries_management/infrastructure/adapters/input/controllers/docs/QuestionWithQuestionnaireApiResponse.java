package com.unicuaca.asst.unicauca_asst.core.batteries_management.infrastructure.adapters.input.controllers.docs;

import com.unicuaca.asst.unicauca_asst.common.response.ApiResponse;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.application.dto.response.QuestionWithQuestionnaireResponseDTO;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Wrapper SOLO para documentación OpenAPI del genérico ApiResponse
 * con QuestionWithQuestionnaireResponseDTO.
 */
@Schema(
    name = "QuestionWithQuestionnaireApiResponse",
    description = "Respuesta API para una pregunta con información del cuestionario asociado"
)
public class QuestionWithQuestionnaireApiResponse extends ApiResponse<QuestionWithQuestionnaireResponseDTO> {
}

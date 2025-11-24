package com.unicuaca.asst.unicauca_asst.core.batteries_management.infrastructure.adapters.input.controllers.docs;

import com.unicuaca.asst.unicauca_asst.common.response.ApiResponse;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.application.dto.response.QuestionResponseDTO;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Wrapper SOLO para documentación OpenAPI del genérico ApiResponse
 * con QuestionResponseDTO.
 */
@Schema(
    name = "QuestionApiResponse",
    description = "Respuesta API para una pregunta sin información del cuestionario asociado"
)
public class QuestionApiResponse extends ApiResponse<QuestionResponseDTO> {
}

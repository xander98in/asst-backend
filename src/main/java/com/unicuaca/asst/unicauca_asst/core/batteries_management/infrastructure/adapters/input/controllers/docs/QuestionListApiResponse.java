package com.unicuaca.asst.unicauca_asst.core.batteries_management.infrastructure.adapters.input.controllers.docs;

import java.util.List;

import com.unicuaca.asst.unicauca_asst.common.response.ApiResponse;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.application.dto.response.QuestionResponseDTO;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Wrapper SOLO para documentación OpenAPI del genérico ApiResponse
 * que contiene una lista de preguntas sin información del cuestionario asociado.
 */
@Schema(
    name = "QuestionListApiResponse",
    description = "Respuesta API que contiene una lista de preguntas sin información del cuestionario asociado"
)
public class QuestionListApiResponse extends ApiResponse<List<QuestionResponseDTO>> {
}

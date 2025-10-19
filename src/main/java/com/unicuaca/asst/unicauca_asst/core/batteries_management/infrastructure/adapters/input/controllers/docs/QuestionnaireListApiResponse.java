package com.unicuaca.asst.unicauca_asst.core.batteries_management.infrastructure.adapters.input.controllers.docs;

import com.unicuaca.asst.unicauca_asst.common.response.ApiResponse;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.application.dto.response.QuestionnaireResponseDTO;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

/**
 * Wrapper SOLO para documentación OpenAPI del genérico ApiResponse que contiene una lista de cuestionarios.
 */
@Schema(name = "QuestionnaireListApiResponse", description = "Respuesta API que contiene una lista de cuestionarios")
public class QuestionnaireListApiResponse extends ApiResponse<List<QuestionnaireResponseDTO>> {
}

package com.unicuaca.asst.unicauca_asst.core.batteries_management.infrastructure.adapters.input.controllers.docs;

import com.unicuaca.asst.unicauca_asst.common.response.ApiResponse;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.application.dto.response.QuestionnaireResponseBatchResponseDTO;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Wrapper de respuesta API que contiene los detalles de las respuestas del lote de un cuestionario.
 */
@Schema(name = "QuestionnaireResponseBatchApiResponse", description = "Respuesta API que contiene los detalles de las respuestas del lote de un cuestionario.")
public class QuestionnaireResponseBatchApiResponse extends ApiResponse<QuestionnaireResponseBatchResponseDTO> {
}

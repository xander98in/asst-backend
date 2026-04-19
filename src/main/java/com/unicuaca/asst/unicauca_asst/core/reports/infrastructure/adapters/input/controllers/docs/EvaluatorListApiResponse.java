package com.unicuaca.asst.unicauca_asst.core.reports.infrastructure.adapters.input.controllers.docs;

import java.util.List;

import com.unicuaca.asst.unicauca_asst.common.response.ApiResponse;
import com.unicuaca.asst.unicauca_asst.core.reports.application.dto.response.EvaluatorResponseDTO;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Wrapper SOLO para la documentación OpenAPI del genérico ApiResponse
 * que contiene una lista de evaluadores.
 */
@Schema(name = "EvaluatorListApiResponse", description = "Respuesta API que contiene una lista de evaluadores")
public class EvaluatorListApiResponse extends ApiResponse<List<EvaluatorResponseDTO>> {
}

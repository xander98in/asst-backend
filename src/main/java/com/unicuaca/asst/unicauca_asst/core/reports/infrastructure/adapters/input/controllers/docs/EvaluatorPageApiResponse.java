package com.unicuaca.asst.unicauca_asst.core.reports.infrastructure.adapters.input.controllers.docs;

import com.unicuaca.asst.unicauca_asst.common.response.ApiResponse;
import com.unicuaca.asst.unicauca_asst.core.reports.application.dto.response.EvaluatorResponseDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.data.domain.Page;

/**
 * Wrapper SOLO para documentación OpenAPI del genérico ApiResponse
 * que contiene una lista paginada de evaluadores.
 */
@Schema(name = "EvaluatorPageApiResponse", description = "Respuesta API que contiene una lista paginada de evaluadores")
public class EvaluatorPageApiResponse extends ApiResponse<Page<EvaluatorResponseDTO>> {
}

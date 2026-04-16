package com.unicuaca.asst.unicauca_asst.core.reports.infrastructure.adapters.input.controllers.docs;

import com.unicuaca.asst.unicauca_asst.common.response.ApiResponse;
import com.unicuaca.asst.unicauca_asst.core.reports.application.dto.response.EvaluatorResponseDTO;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "EvaluatorApiResponse", description = "Respuesta API que contiene un evaluador")
public class EvaluatorApiResponse extends ApiResponse<EvaluatorResponseDTO> {
}

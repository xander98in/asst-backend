package com.unicuaca.asst.unicauca_asst.core.reports.infrastructure.adapters.input.controllers.docs;

import com.unicuaca.asst.unicauca_asst.common.response.ApiResponse;
import com.unicuaca.asst.unicauca_asst.core.reports.application.dto.response.RiskMatrixResponseDTO;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Wrapper SOLO para la documentación OpenAPI del genérico ApiResponse
 * que contiene la matriz de riesgo del grupo.
 */
@Schema(name = "RiskMatrixApiResponse", description = "Respuesta API que contiene la matriz de riesgo")
public class RiskMatrixApiResponse extends ApiResponse<RiskMatrixResponseDTO> {
}

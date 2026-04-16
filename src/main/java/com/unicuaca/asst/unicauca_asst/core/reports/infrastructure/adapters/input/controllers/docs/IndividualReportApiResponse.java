package com.unicuaca.asst.unicauca_asst.core.reports.infrastructure.adapters.input.controllers.docs;

import com.unicuaca.asst.unicauca_asst.common.response.ApiResponse;
import com.unicuaca.asst.unicauca_asst.core.reports.application.dto.response.IndividualReportResponseDTO;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Wrapper SOLO para la documentación OpenAPI del genérico ApiResponse
 * que contiene el informe individual de riesgo psicosocial.
 */
@Schema(name = "IndividualReportApiResponse", description = "Respuesta API que contiene el informe individual de riesgo psicosocial")
public class IndividualReportApiResponse extends ApiResponse<IndividualReportResponseDTO> {
}

package com.unicuaca.asst.unicauca_asst.core.reports.infrastructure.adapters.input.controllers.docs;

import java.util.List;

import com.unicuaca.asst.unicauca_asst.common.response.ApiResponse;
import com.unicuaca.asst.unicauca_asst.core.reports.application.dto.response.AnalysisSpaceSummaryResponseDTO;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Wrapper SOLO para la documentación OpenAPI del genérico ApiResponse
 * que contiene la lista de resúmenes de espacios de análisis.
 */
@Schema(name = "AnalysisSpaceSummaryListApiResponse", description = "Respuesta API que contiene la lista de espacios de análisis")
public class AnalysisSpaceSummaryListApiResponse extends ApiResponse<List<AnalysisSpaceSummaryResponseDTO>> {
}

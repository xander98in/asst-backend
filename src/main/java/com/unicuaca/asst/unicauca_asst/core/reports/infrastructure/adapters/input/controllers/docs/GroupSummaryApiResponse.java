package com.unicuaca.asst.unicauca_asst.core.reports.infrastructure.adapters.input.controllers.docs;

import com.unicuaca.asst.unicauca_asst.common.response.ApiResponse;
import com.unicuaca.asst.unicauca_asst.core.reports.application.dto.response.GroupSummaryResponseDTO;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Wrapper SOLO para la documentación OpenAPI del genérico ApiResponse
 * que contiene el resumen general grupal del espacio de análisis.
 */
@Schema(name = "GroupSummaryApiResponse", description = "Respuesta API que contiene el resumen grupal")
public class GroupSummaryApiResponse extends ApiResponse<GroupSummaryResponseDTO> {
}

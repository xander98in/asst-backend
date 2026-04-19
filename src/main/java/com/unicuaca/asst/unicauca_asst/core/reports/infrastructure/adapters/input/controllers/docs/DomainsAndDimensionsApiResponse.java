package com.unicuaca.asst.unicauca_asst.core.reports.infrastructure.adapters.input.controllers.docs;

import com.unicuaca.asst.unicauca_asst.common.response.ApiResponse;
import com.unicuaca.asst.unicauca_asst.core.reports.application.dto.response.DomainsAndDimensionsResponseDTO;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Wrapper SOLO para la documentación OpenAPI del genérico ApiResponse
 * que contiene el desglose de dominios y dimensiones del grupo.
 */
@Schema(name = "DomainsAndDimensionsApiResponse", description = "Respuesta API que contiene los dominios y dimensiones")
public class DomainsAndDimensionsApiResponse extends ApiResponse<DomainsAndDimensionsResponseDTO> {
}

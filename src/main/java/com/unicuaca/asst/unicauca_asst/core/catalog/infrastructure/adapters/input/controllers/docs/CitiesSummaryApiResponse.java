package com.unicuaca.asst.unicauca_asst.core.catalog.infrastructure.adapters.input.controllers.docs;

import com.unicuaca.asst.unicauca_asst.core.catalog.application.dto.response.CitySummaryResponseDTO;
import com.unicuaca.asst.unicauca_asst.common.response.ApiResponse;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

/**
 * Wrapper solo para documentación OpenAPI del genérico
 * {@code ApiResponse<List<CitySummaryResponseDTO>>}.
 *
 * <p>Permite que Swagger resuelva el tipo genérico y muestre correctamente el esquema
 * de respuesta que contiene la lista resumen de ciudades (sin departamento).</p>
 */
@Schema(name = "CitiesSummaryApiResponse", description = "Respuesta API que contiene una lista resumen de ciudades (sin departamento)")
public class CitiesSummaryApiResponse extends ApiResponse<List<CitySummaryResponseDTO>> {
}

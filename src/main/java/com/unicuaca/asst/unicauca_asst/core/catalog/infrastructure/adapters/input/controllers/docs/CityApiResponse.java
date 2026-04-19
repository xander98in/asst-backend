package com.unicuaca.asst.unicauca_asst.core.catalog.infrastructure.adapters.input.controllers.docs;

import com.unicuaca.asst.unicauca_asst.core.catalog.application.dto.response.CityResponseDTO;
import com.unicuaca.asst.unicauca_asst.common.response.ApiResponse;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Wrapper solo para documentación OpenAPI del genérico {@code ApiResponse<CityResponseDTO>}.
 *
 * <p>Permite que Swagger resuelva el tipo genérico y muestre correctamente el esquema
 * de respuesta que contiene una ciudad con su departamento.</p>
 */
@Schema(name = "CityApiResponse", description = "Respuesta API que contiene una ciudad")
public class CityApiResponse extends ApiResponse<CityResponseDTO> {
}

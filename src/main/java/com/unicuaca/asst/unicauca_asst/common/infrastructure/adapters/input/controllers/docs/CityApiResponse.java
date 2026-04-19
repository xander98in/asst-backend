package com.unicuaca.asst.unicauca_asst.common.infrastructure.adapters.input.controllers.docs;

import com.unicuaca.asst.unicauca_asst.common.application.dto.response.CityResponseDTO;
import com.unicuaca.asst.unicauca_asst.common.response.ApiResponse;

/**
 * Wrapper solo para documentación OpenAPI del genérico {@code ApiResponse<CityResponseDTO>}.
 *
 * <p>Permite que Swagger resuelva el tipo genérico y muestre correctamente el esquema
 * de respuesta que contiene una ciudad con su departamento.</p>
 */
public class CityApiResponse extends ApiResponse<CityResponseDTO> {
}

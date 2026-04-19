package com.unicuaca.asst.unicauca_asst.core.catalog.infrastructure.adapters.input.controllers.docs;

import com.unicuaca.asst.unicauca_asst.core.catalog.application.dto.response.CivilStatusResponseDTO;
import com.unicuaca.asst.unicauca_asst.common.response.ApiResponse;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

/**
 * Wrapper solo para documentación OpenAPI del genérico
 * {@code ApiResponse<List<CivilStatusResponseDTO>>}.
 *
 * <p>Permite que Swagger resuelva el tipo genérico y muestre correctamente el esquema
 * de respuesta que contiene la lista de estados civiles.</p>
 */
@Schema(name = "CivilStatusesApiResponse", description = "Respuesta API que contiene una lista de estados civiles")
public class CivilStatusesApiResponse extends ApiResponse<List<CivilStatusResponseDTO>> {
}

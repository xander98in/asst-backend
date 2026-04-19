package com.unicuaca.asst.unicauca_asst.common.infrastructure.adapters.input.controllers.docs;

import com.unicuaca.asst.unicauca_asst.common.application.dto.response.HousingTypeResponseDTO;
import com.unicuaca.asst.unicauca_asst.common.response.ApiResponse;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

/**
 * Wrapper solo para documentación OpenAPI del genérico
 * {@code ApiResponse<List<HousingTypeResponseDTO>>}.
 *
 * <p>Permite que Swagger resuelva el tipo genérico y muestre correctamente el esquema
 * de respuesta que contiene la lista de tipos de vivienda.</p>
 */
@Schema(name = "HousingTypesApiResponse", description = "Respuesta API que contiene una lista de tipos de vivienda")
public class HousingTypesApiResponse extends ApiResponse<List<HousingTypeResponseDTO>> {
}

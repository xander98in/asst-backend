package com.unicuaca.asst.unicauca_asst.common.infrastructure.adapters.input.controllers.docs;

import com.unicuaca.asst.unicauca_asst.common.application.dto.response.SocioeconomicLevelResponseDTO;
import com.unicuaca.asst.unicauca_asst.common.response.ApiResponse;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

/**
 * Wrapper solo para documentación OpenAPI del genérico
 * {@code ApiResponse<List<SocioeconomicLevelResponseDTO>>}.
 *
 * <p>Permite que Swagger resuelva el tipo genérico y muestre correctamente el esquema
 * de respuesta que contiene la lista de niveles socioeconómicos.</p>
 */
@Schema(name = "SocioeconomicLevelsApiResponse", description = "Respuesta API que contiene una lista de niveles socioeconómicos")
public class SocioeconomicLevelsApiResponse extends ApiResponse<List<SocioeconomicLevelResponseDTO>> {
}

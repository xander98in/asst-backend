package com.unicuaca.asst.unicauca_asst.common.infrastructure.adapters.input.controllers.docs;

import com.unicuaca.asst.unicauca_asst.common.application.dto.response.IdentificationTypeResponseDTO;
import com.unicuaca.asst.unicauca_asst.common.response.ApiResponse;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

/**
 * Wrapper SOLO para documentación OpenAPI del genérico ApiResponse que contiene una lista de tipos de identificación.
 */
@Schema(name = "IdentificationTypesApiResponse", description = "Respuesta API que contiene una lista de tipos de identificación")
public class IdentificationTypesApiResponse extends ApiResponse<List<IdentificationTypeResponseDTO>> {
}

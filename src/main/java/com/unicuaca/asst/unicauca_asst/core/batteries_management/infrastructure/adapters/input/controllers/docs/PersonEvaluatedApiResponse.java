package com.unicuaca.asst.unicauca_asst.core.batteries_management.infrastructure.adapters.input.controllers.docs;

import com.unicuaca.asst.unicauca_asst.common.response.ApiResponse;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.application.dto.response.PersonEvaluatedResponseDTO;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Wrapper SOLO para documentación OpenAPI del genérico ApiResponse que contiene los datos de una persona evaluada.
 */
@Schema(name = "PersonEvaluatedApiResponse", description = "Respuesta API que contiene los datos de una persona evaluada")
public class PersonEvaluatedApiResponse extends ApiResponse<PersonEvaluatedResponseDTO> {
}

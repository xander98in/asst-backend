package com.unicuaca.asst.unicauca_asst.core.batteries_management.infrastructure.adapters.input.controllers.docs;

import com.unicuaca.asst.unicauca_asst.common.response.ApiResponse;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.application.dto.response.PersonEvaluatedInformationResponseDTO;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Wrapper SOLO para documentación OpenAPI del genérico ApiResponse que contiene la información de una persona evaluada.
 */
@Schema(name = "PersonEvaluatedInformationApiResponse", description = "Respuesta API que contiene la información de una persona evaluada")
public class PersonEvaluatedInformationApiResponse extends ApiResponse<PersonEvaluatedInformationResponseDTO> {
}

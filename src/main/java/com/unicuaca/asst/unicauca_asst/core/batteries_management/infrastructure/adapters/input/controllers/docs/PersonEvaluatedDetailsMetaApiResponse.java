package com.unicuaca.asst.unicauca_asst.core.batteries_management.infrastructure.adapters.input.controllers.docs;

import com.unicuaca.asst.unicauca_asst.common.response.ApiResponse;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.application.dto.response.PersonEvaluatedDetailsMetaResponseDTO;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Wrapper SOLO para documentación OpenAPI del ApiResponse genérico que contiene metadata de PersonEvaluatedDetails.
 */
@Schema(
    name = "PersonEvaluatedDetailsMetaApiResponse",
    description = "Respuesta API que contiene metadata de PersonEvaluatedDetails."
)
public class PersonEvaluatedDetailsMetaApiResponse extends ApiResponse<PersonEvaluatedDetailsMetaResponseDTO> {
}

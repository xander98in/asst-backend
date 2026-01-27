package com.unicuaca.asst.unicauca_asst.core.batteries_management.infrastructure.adapters.input.controllers.docs;

import com.unicuaca.asst.unicauca_asst.common.response.ApiResponse;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.application.dto.response.PersonEvaluatedInformationResponseDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.data.domain.Page;

/**
 * Wrapper SOLO para documentación OpenAPI del genérico ApiResponse que contiene una lista paginada de información de personas evaluadas.
 */
@Schema(name = "PersonEvaluatedInformationListApiResponse", description = "Respuesta API que contiene una lista paginada de información de personas evaluadas")
public class PersonEvaluatedInformationListApiResponse extends ApiResponse<Page<PersonEvaluatedInformationResponseDTO>> {
}

package com.unicuaca.asst.unicauca_asst.core.batteries_management.infrastructure.adapters.input.controllers.docs;

import com.unicuaca.asst.unicauca_asst.common.response.ApiResponse;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.application.dto.response.QuestionnaireManagementRecordStatusResponseDTO;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Wrapper SOLO para documentación OpenAPI del genérico ApiResponse con QuestionnaireManagementRecordStatusResponseDTO.
 */
@Schema(
    name = "QuestionnaireManagementRecordStatusApiResponse",
    description = "Respuesta API para el estado de un registro de gestión de cuestionarios"
)
public class QuestionnaireManagementRecordStatusApiResponse extends ApiResponse<QuestionnaireManagementRecordStatusResponseDTO> {

}

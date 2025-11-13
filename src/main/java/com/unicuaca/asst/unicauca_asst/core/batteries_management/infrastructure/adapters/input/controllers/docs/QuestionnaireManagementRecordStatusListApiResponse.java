package com.unicuaca.asst.unicauca_asst.core.batteries_management.infrastructure.adapters.input.controllers.docs;

import java.util.List;

import com.unicuaca.asst.unicauca_asst.common.response.ApiResponse;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.application.dto.response.QuestionnaireManagementRecordStatusResponseDTO;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Wrapper SOLO para documentación OpenAPI del genérico ApiResponse
 * que contiene una lista de estados de registro de gestión de cuestionarios.
 */
@Schema(
    name = "QuestionnaireManagementRecordStatusListApiResponse",
    description = "Respuesta API que contiene una lista de estados de registros de gestión de cuestionarios"
)
public class QuestionnaireManagementRecordStatusListApiResponse extends ApiResponse<List<QuestionnaireManagementRecordStatusResponseDTO>>{

}

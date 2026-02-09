package com.unicuaca.asst.unicauca_asst.core.batteries_management.infrastructure.adapters.input.controllers.docs;

import com.unicuaca.asst.unicauca_asst.common.response.ApiResponse;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.application.dto.response.QuestionnaireManagementRecordResponseDTO;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Wrapper de respuesta API que contiene los detalles del registro de gestión del cuestionario.
 */
@Schema(name = "QuestionnaireManagementRecordApiResponse", description = "Respuesta API que contiene los detalles del registro de gestión del cuestionario.")
public class QuestionnaireManagementRecordApiResponse extends ApiResponse<QuestionnaireManagementRecordResponseDTO> {
}

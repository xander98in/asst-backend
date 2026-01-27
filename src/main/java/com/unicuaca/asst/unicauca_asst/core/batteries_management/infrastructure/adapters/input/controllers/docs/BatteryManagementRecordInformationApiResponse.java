package com.unicuaca.asst.unicauca_asst.core.batteries_management.infrastructure.adapters.input.controllers.docs;

import com.unicuaca.asst.unicauca_asst.common.response.ApiResponse;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.application.dto.response.BatteryManagementRecordInformationResponseDTO;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Wrapper SOLO para la documentación OpenAPI del genérico ApiResponse que contiene la información de un registro de gestión de baterías.
 */
@Schema(name = "BatteryManagementRecordInformationApiResponse", description = "Respuesta API que contiene la información de un registro de gestión de baterías")
public class BatteryManagementRecordInformationApiResponse extends ApiResponse<BatteryManagementRecordInformationResponseDTO> {
}

package com.unicuaca.asst.unicauca_asst.core.batteries_management.infrastructure.adapters.input.controllers.docs;

import com.unicuaca.asst.unicauca_asst.common.response.ApiResponse;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.application.dto.response.BatteryManagementRecordResponseDTO;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Wrapper SOLO para documentación OpenAPI del genérico ApiResponse<BatteryManagementRecordResponseDTO>.
 */
@Schema(name = "BatteryManagementRecordApiResponse")
public final class BatteryManagementRecordApiResponse extends ApiResponse<BatteryManagementRecordResponseDTO> {
}

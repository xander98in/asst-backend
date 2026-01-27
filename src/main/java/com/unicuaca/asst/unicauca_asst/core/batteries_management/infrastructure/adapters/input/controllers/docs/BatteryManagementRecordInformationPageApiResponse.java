package com.unicuaca.asst.unicauca_asst.core.batteries_management.infrastructure.adapters.input.controllers.docs;

import com.unicuaca.asst.unicauca_asst.common.response.ApiResponse;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.application.dto.response.BatteryManagementRecordInformationResponseDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.data.domain.Page;

/**
 * Wrapper SOLO para documentación OpenAPI del genérico ApiResponse que contiene una lista paginada de información de registros de gestión de baterías.
 */
@Schema(name = "BatteryManagementRecordInformationPageApiResponse", description = "Respuesta API que contiene una lista paginada de información de registros de gestión de baterías")
public class BatteryManagementRecordInformationPageApiResponse extends ApiResponse<Page<BatteryManagementRecordInformationResponseDTO>> {
}
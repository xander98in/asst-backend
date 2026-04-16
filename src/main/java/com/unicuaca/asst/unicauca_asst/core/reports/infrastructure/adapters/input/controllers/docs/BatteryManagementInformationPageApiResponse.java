package com.unicuaca.asst.unicauca_asst.core.reports.infrastructure.adapters.input.controllers.docs;

import org.springframework.data.domain.Page;

import com.unicuaca.asst.unicauca_asst.common.response.ApiResponse;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.application.dto.response.BatteryManagementRecordInformationResponseDTO;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Wrapper SOLO para documentación OpenAPI del genérico ApiResponse
 * que contiene una página de baterías con información enriquecida.
 */
@Schema(
    name = "BatteryManagementInformationPageApiResponse",
    description = "Respuesta API que contiene una página de baterías con información enriquecida"
)
public class BatteryManagementInformationPageApiResponse extends ApiResponse<Page<BatteryManagementRecordInformationResponseDTO>> {
}

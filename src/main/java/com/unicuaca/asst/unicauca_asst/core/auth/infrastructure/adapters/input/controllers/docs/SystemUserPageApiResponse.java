package com.unicuaca.asst.unicauca_asst.core.auth.infrastructure.adapters.input.controllers.docs;

import com.unicuaca.asst.unicauca_asst.common.response.ApiResponse;
import com.unicuaca.asst.unicauca_asst.core.auth.application.dto.response.SystemUserResponseDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.data.domain.Page;

/**
 * Wrapper SOLO para documentación OpenAPI del genérico ApiResponse que contiene una lista paginada de usuarios del sistema.
 */
@Schema(name = "SystemUserPageApiResponse", description = "Respuesta API que contiene una lista paginada de usuarios del sistema")
public class SystemUserPageApiResponse extends ApiResponse<Page<SystemUserResponseDTO>> {
}

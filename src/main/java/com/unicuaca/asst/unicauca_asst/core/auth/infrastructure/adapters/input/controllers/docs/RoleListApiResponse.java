package com.unicuaca.asst.unicauca_asst.core.auth.infrastructure.adapters.input.controllers.docs;

import java.util.List;

import com.unicuaca.asst.unicauca_asst.common.response.ApiResponse;
import com.unicuaca.asst.unicauca_asst.core.auth.application.dto.response.RoleResponseDTO;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Wrapper SOLO para documentación OpenAPI del genérico ApiResponse que contiene la lista de roles del sistema.
 */
@Schema(name = "RoleListApiResponse", description = "Respuesta API con lista de roles del sistema")
public class RoleListApiResponse extends ApiResponse<List<RoleResponseDTO>> {
}

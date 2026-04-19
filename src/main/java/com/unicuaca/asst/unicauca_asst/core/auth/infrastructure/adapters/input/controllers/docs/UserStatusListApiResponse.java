package com.unicuaca.asst.unicauca_asst.core.auth.infrastructure.adapters.input.controllers.docs;

import java.util.List;

import com.unicuaca.asst.unicauca_asst.common.response.ApiResponse;
import com.unicuaca.asst.unicauca_asst.core.auth.application.dto.response.UserStatusResponseDTO;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Wrapper de documentación OpenAPI para {@link ApiResponse} parametrizado con {@link List} de {@link UserStatusResponseDTO}.
 *
 * <p>Permite a Swagger renderizar correctamente el tipo genérico en la especificación;
 * no se instancia en tiempo de ejecución.</p>
 */
@Schema(name = "UserStatusListApiResponse", description = "Respuesta API con lista de estados de usuario del sistema")
public class UserStatusListApiResponse extends ApiResponse<List<UserStatusResponseDTO>> {
}

package com.unicuaca.asst.unicauca_asst.core.auth.infrastructure.adapters.input.controllers.docs;

import com.unicuaca.asst.unicauca_asst.common.response.ApiResponse;
import com.unicuaca.asst.unicauca_asst.core.auth.application.dto.response.AuthResponseDTO;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Wrapper de documentación OpenAPI para {@link ApiResponse} parametrizado con {@link AuthResponseDTO}.
 *
 * <p>Permite a Swagger renderizar correctamente el tipo genérico en la especificación;
 * no se instancia en tiempo de ejecución.</p>
 */
@Schema(name = "AuthResponseApiResponse", description = "Respuesta API que contiene los datos de autenticación")
public class AuthResponseApiResponse extends ApiResponse<AuthResponseDTO> {
}

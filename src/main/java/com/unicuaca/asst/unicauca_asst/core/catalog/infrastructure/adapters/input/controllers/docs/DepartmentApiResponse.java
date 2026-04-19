package com.unicuaca.asst.unicauca_asst.core.catalog.infrastructure.adapters.input.controllers.docs;

import com.unicuaca.asst.unicauca_asst.core.catalog.application.dto.response.DepartmentResponseDTO;
import com.unicuaca.asst.unicauca_asst.common.response.ApiResponse;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Wrapper solo para documentación OpenAPI del genérico {@code ApiResponse<DepartmentResponseDTO>}.
 *
 * <p>Permite que Swagger resuelva el tipo genérico y muestre correctamente el esquema
 * de respuesta que contiene un departamento con sus ciudades embebidas.</p>
 */
@Schema(name = "DepartmentApiResponse", description = "Respuesta API que contiene un departamento")
public class DepartmentApiResponse extends ApiResponse<DepartmentResponseDTO> {
}

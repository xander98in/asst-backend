package com.unicuaca.asst.unicauca_asst.common.docs;

import com.unicuaca.asst.unicauca_asst.common.response.ApiResponse;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Wrapper SOLO para documentación OpenAPI del genérico ApiResponse que no contiene datos en el cuerpo de la respuesta.
 */
@Schema(name = "VoidApiResponse", description = "Respuesta API que no contiene datos en el cuerpo de la respuesta")
public class VoidApiResponse extends ApiResponse<Void> {
}

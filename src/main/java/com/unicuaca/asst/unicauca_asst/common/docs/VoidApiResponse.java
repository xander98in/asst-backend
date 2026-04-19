package com.unicuaca.asst.unicauca_asst.common.docs;

import com.unicuaca.asst.unicauca_asst.common.response.ApiResponse;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Wrapper usado exclusivamente para la documentación OpenAPI.
 *
 * <p>Representa {@code ApiResponse<Void>} como un tipo concreto, utilizado en los endpoints
 * cuyas respuestas no transportan datos en el cuerpo.</p>
 */
@Schema(name = "VoidApiResponse", description = "Respuesta API que no contiene datos en el cuerpo de la respuesta")
public class VoidApiResponse extends ApiResponse<Void> {
}

package com.unicuaca.asst.unicauca_asst.common.docs;

import com.unicuaca.asst.unicauca_asst.common.exceptions.structure.ErrorResponse;
import com.unicuaca.asst.unicauca_asst.common.response.ApiResponse;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Wrapper usado exclusivamente para la documentación OpenAPI.
 *
 * <p>Representa {@code ApiResponse<ErrorResponse<Void>>} como un tipo concreto, permitiendo
 * a SpringDoc generar un esquema estable para las respuestas de error estandarizadas.</p>
 */
@Schema(name = "ErrorResponseApiResponse", description = "Respuesta de API que contiene un ErrorResponse")
public class ErrorResponseApiResponse extends ApiResponse<ErrorResponse<Void>> {
}

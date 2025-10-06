package com.unicuaca.asst.unicauca_asst.common.docs;

import com.unicuaca.asst.unicauca_asst.common.exceptions.structure.ErrorResponse;
import com.unicuaca.asst.unicauca_asst.common.response.ApiResponse;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Wrapper SOLO para documentación OpenAPI del ErrorResponse dentro de un ApiResponse.
 */
@Schema(name = "ErrorResponseApiResponse", description = "Respuesta de API que contiene un ErrorResponse")
public class ErrorResponseApiResponse extends ApiResponse<ErrorResponse<Void>> {
}

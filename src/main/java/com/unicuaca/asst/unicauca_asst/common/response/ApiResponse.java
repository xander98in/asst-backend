package com.unicuaca.asst.unicauca_asst.common.response;

import org.springframework.http.HttpStatus;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Clase genérica para encapsular respuestas estándar de la API REST.
 * Puede representar tanto respuestas exitosas como errores estructurados.
 *
 * @param <T> tipo de dato que contiene el campo 'data' (puede ser DTO o ErrorResponse)
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "ApiResponse")
public class ApiResponse<T> {

    /**
     * Código de estado HTTP de la respuesta (por ejemplo: 200, 404, 500).
     */
    @Schema(example = "200")
    private int httpStatus;

    /**
     * Mensaje descriptivo de la respuesta, ya sea de éxito o de error.
     */
    @Schema(example = "Mensaje de información")
    private String message;

    /**
     * Cuerpo de la respuesta. En caso de éxito, contiene el resultado (DTO).
     * En caso de error, puede contener una estructura con detalles técnicos (por ejemplo, {@code ErrorResponse}).
     */
    @Schema(example = "Datos de la respuesta", type = "object")
    private T data;

    // ✅ Éxito simple con datos
    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(HttpStatus.OK.value(), "Éxito", data);
    }

    // ✅ Éxito con mensaje personalizado
    public static <T> ApiResponse<T> success(String message, T data) {
        return new ApiResponse<>(HttpStatus.OK.value(), message, data);
    }

    // ✅ Éxito sin datos
    public static <T> ApiResponse<T> success(String message) {
        return new ApiResponse<>(HttpStatus.OK.value(), message, null);
    }

    // ✅ Error con datos personalizados (como ErrorResponse)
    public static <T> ApiResponse<T> error(String message, HttpStatus httpStatus, T errorData) {
        return new ApiResponse<>(httpStatus.value(), message, errorData);
    }

    // ✅ Error genérico 500
    public static <T> ApiResponse<T> internalError(String message) {
        return new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), message, null);
    }

    // ✅ Error 404
    public static <T> ApiResponse<T> notFound(String message) {
        return new ApiResponse<>(HttpStatus.NOT_FOUND.value(), message, null);
    }

    // ✅ Error personalizado sin body
    public static <T> ApiResponse<T> customError(String message, HttpStatus httpStatus) {
        return new ApiResponse<>(httpStatus.value(), message, null);
    }
}

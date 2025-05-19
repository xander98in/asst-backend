package com.unicuaca.asst.unicauca_asst.common.response;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse<T> {

    private HttpStatus status;
    private String message;
    private T data;
    private Integer code;

    // 🔹 Método para respuestas exitosas (200 OK)
    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(HttpStatus.OK, "Éxito", data, HttpStatus.OK.value());
    }

    // 🔹 Método para respuestas exitosas con mensaje personalizado
    public static <T> ApiResponse<T> success(String message, T data) {
        return new ApiResponse<>(HttpStatus.OK, message, data, HttpStatus.OK.value());
    }

    // 🔹 Método para errores
    public static <T> ApiResponse<T> error(String message, HttpStatus status) {
        return new ApiResponse<>(status, message, null, status.value());
    }

    // 🔹 Método para error genérico 500
    public static <T> ApiResponse<T> internalError(String message) {
        return new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR, message, null, HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

    // 🔹 Método para error 404
    public static <T> ApiResponse<T> notFound(String message) {
        return new ApiResponse<>(HttpStatus.NOT_FOUND, message, null, HttpStatus.NOT_FOUND.value());
    }

    // 🔹 Método para errores completamente personalizados
    public static <T> ApiResponse<T> customError(String message, HttpStatus status, int code) {
    return new ApiResponse<>(status, message, null, code);
}
}

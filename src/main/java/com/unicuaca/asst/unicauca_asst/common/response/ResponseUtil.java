package com.unicuaca.asst.unicauca_asst.common.response;

import com.unicuaca.asst.unicauca_asst.common.exceptions.structure.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

/**
 * Utilidades para construir respuestas HTTP con body JSON estandarizado ({@link ApiResponse}).
 *
 * <p>Ejemplos de uso:</p>
 * <pre>
 *     // Respuesta 200 OK
 *     return ResponseUtil.ok(request, SuccessCode.OPERATION_SUCCESS, payload);
 *
 *     // Respuesta 201 Created
 *     return ResponseUtil.created(request, "/asst/items/{id}", SuccessCode.ITEM_CREATED, createdItem);
 * </pre>
 */
public final class ResponseUtil {

    private ResponseUtil() {}

    /**
     * Respuesta 200 OK con body JSON estandarizado.
     *
     * @param req   request para extraer la ruta (path) de la solicitud.
     * @param code  código funcional de éxito (enum {@link SuccessCode}).
     * @param body  payload de respuesta (DTO o colección).
     * @return {@code 200 OK} con {@link ApiResponse}.
     */
    public static <T> ResponseEntity<ApiResponse<T>> ok(HttpServletRequest req, SuccessCode code, String message, T body) {
        if(message == null || message.isBlank()) {
            message = code.getMessage();
        }
        var api = ApiResponse.of(code.getCode(), message, body, req.getRequestURI());
        return ResponseEntity.ok()
            .contentType(MediaType.APPLICATION_JSON)
            .body(api);
    }

    /**
     * Respuesta 201 Created con header Location y body JSON estandarizado.
     *
     * <p>La URI de Location se construye a partir del contexto actual + {@code locationPath}.</p>
     *
     * @param req           request para obtener el path de la llamada (trazabilidad).
     * @param locationPath  ruta absoluta/relativa del nuevo recurso (p. ej. "/asst/items/{id}").
     * @param code          código funcional de éxito.
     * @param message       mensaje humano (usualmente igual a {@code code.getMessage()}).
     * @param body          payload del recurso creado.
     * @return {@code 201 Created} con header Location y {@link ApiResponse}.
     */
    public static <T> ResponseEntity<ApiResponse<T>> created(HttpServletRequest req, String locationPath, SuccessCode code, String message, T body) {
        URI location = ServletUriComponentsBuilder.fromCurrentContextPath()
            .path(locationPath)
            .build()
            .toUri();

        if(message == null || message.isBlank()) {
            message = code.getMessage();
        }

        var api = ApiResponse.of(code.getCode(), message, body, req.getRequestURI());
        return ResponseEntity.created(location)
            .contentType(MediaType.APPLICATION_JSON)
            .body(api);
    }

    /**
     * Respuesta de error con status arbitrario y body JSON estandarizado.
     *
     * @param req        request para el path.
     * @param errorCode  código funcional de error (p. ej. "ASST-ERR-400").
     * @param status     HTTP status a retornar.
     * @param message    mensaje humano del error.
     * @param details    detalles adicionales del error (puede ser {@code ErrorResponse<T>}).
     * @return {@code status} con {@link ApiResponse}.
     */
    public static <T> ResponseEntity<ApiResponse<ErrorResponse<T>>> error(HttpServletRequest req, String errorCode, HttpStatus status, String message, ErrorResponse<T> details) {
        var api = ApiResponse.of(errorCode, message, details, req.getRequestURI());
        return ResponseEntity.status(status)
            .contentType(MediaType.APPLICATION_JSON)
            .body(api);
    }

    public static <T> ResponseEntity<ApiResponse<ErrorResponse<T>>> badRequest(HttpServletRequest req, String message, ErrorResponse<T> details) {
        return error(req, "ASST-ERR-400", HttpStatus.BAD_REQUEST, message, details);
    }
    public static <T> ResponseEntity<ApiResponse<ErrorResponse<T>>> notFound(HttpServletRequest req, String message) {
        return error(req, "ASST-ERR-404", HttpStatus.NOT_FOUND, message, null);
    }
    public static <T> ResponseEntity<ApiResponse<ErrorResponse<T>>> conflict(HttpServletRequest req, String message) {
        return error(req, "ASST-ERR-409", HttpStatus.CONFLICT, message, null);
    }
}

package com.unicuaca.asst.unicauca_asst.common.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.unicuaca.asst.unicauca_asst.common.exceptions.structure.ErrorCode;
import com.unicuaca.asst.unicauca_asst.common.exceptions.structure.ErrorResponse;
import com.unicuaca.asst.unicauca_asst.common.exceptions.structure.ErrorUtils;
import com.unicuaca.asst.unicauca_asst.common.response.ApiResponse;

import jakarta.servlet.http.HttpServletRequest;

/**
 * Manejador global de excepciones para los controladores REST de la aplicación.
 *
 * Esta clase intercepta excepciones personalizadas lanzadas desde cualquier capa
 * del backend (generalmente desde los casos de uso o infraestructura), y construye
 * una respuesta uniforme del tipo {@link ApiResponse} que envuelve un {@link ErrorResponse}.
 *
 * <p>Su propósito es entregar al cliente errores estructurados con trazabilidad,
 * respetando el contrato de la API REST, y evitando respuestas técnicas genéricas.</p>
 *
 * <p>Utiliza {@link ErrorCode} como catálogo de códigos de error definidos por la aplicación
 * y {@link ErrorUtils} para construir respuestas enriquecidas con metadatos (URL, método HTTP, etc.).</p>
 */
@RestControllerAdvice
public class RestApiExceptionHandler {

    /**
     * Maneja excepciones del tipo {@link EntityNotFoundPersException} lanzadas cuando
     * no se encuentra una entidad solicitada.
     *
     * <p>Devuelve una respuesta HTTP con código <strong>404 NOT FOUND</strong> y una estructura
     * de error detallada que incluye el código interno de error, mensaje descriptivo,
     * URL solicitada y método HTTP.</p>
     *
     * @param req objeto que representa la solicitud HTTP original
     * @param ex  la excepción lanzada desde el dominio o infraestructura
     * @return    una {@link ResponseEntity} con código 404 y cuerpo estructurado usando {@link ApiResponse} y {@link ErrorResponse}
     */
    @ExceptionHandler(EntityNotFoundPersException.class)
    public ResponseEntity<ApiResponse<ErrorResponse>> handleEntityNotFoundException(HttpServletRequest req, EntityNotFoundPersException ex) {
        ErrorResponse error = ErrorUtils.createError(
                ex.getCode(),
                ex.getMessage(),
                HttpStatus.NOT_FOUND.value())
            .setUrl(req.getRequestURL().toString())
            .setMethod(req.getMethod());

        return ResponseEntity
            .status(HttpStatus.NOT_FOUND)
            .body(ApiResponse.error(ex.getMessageKey(), HttpStatus.NOT_FOUND, error));
    }
}

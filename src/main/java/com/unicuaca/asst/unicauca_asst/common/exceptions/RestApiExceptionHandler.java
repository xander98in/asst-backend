package com.unicuaca.asst.unicauca_asst.common.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.unicuaca.asst.unicauca_asst.common.exceptions.structure.ErrorResponse;
import com.unicuaca.asst.unicauca_asst.common.exceptions.structure.ErrorUtils;

import jakarta.servlet.http.HttpServletRequest;

/**
 * Manejador global de excepciones para los controladores REST de la aplicación.
 *
 * Esta clase intercepta las excepciones personalizadas lanzadas desde cualquier
 * controlador y construye una respuesta estandarizada del tipo {@link ErrorResponse},
 * permitiendo entregar al cliente información estructurada sobre el error ocurrido.
 *
 * <p>Esta clase hace uso de {@link ErrorCode} para representar los códigos de error definidos
 * por la aplicación y de {@link ErrorUtils} para construir respuestas con trazabilidad.</p>
 */
@ControllerAdvice
public class RestApiExceptionHandler {

    /**
     * Maneja excepciones del tipo {@link EntityNotFoundException}, devolviendo una respuesta
     * con código HTTP 404 (NOT FOUND) y un objeto {@link ErrorResponse} estructurado.
     *
     * @param req     objeto que contiene información de la petición HTTP original
     * @param ex      la excepción capturada
     * @return        una respuesta HTTP con código 404 y detalle del error
     */
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleEntityNotFound(final HttpServletRequest req, final EntityNotFoundException ex) {
        
        ErrorResponse error = ErrorUtils.createError(
                ex.getCode(),
                ex.getMessageKey(),
                HttpStatus.NOT_FOUND.value())
            .setUrl(req.getRequestURL().toString())
            .setMethod(req.getMethod());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }
}

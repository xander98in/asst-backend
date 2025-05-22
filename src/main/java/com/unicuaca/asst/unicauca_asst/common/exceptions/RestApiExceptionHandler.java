package com.unicuaca.asst.unicauca_asst.common.exceptions;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
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
 * <p>Esta clase centraliza la gestión de errores lanzados desde cualquier capa del backend,
 * convirtiéndolos en respuestas estructuradas del tipo {@link ApiResponse} que encapsulan un {@link ErrorResponse}.
 * Esto garantiza consistencia, trazabilidad y claridad para el cliente consumidor de la API.</p>
 *
 * <p>Utiliza {@link ErrorUtils} para la creación fluida de errores enriquecidos, y hace uso de
 * {@link ErrorCode} como catálogo de errores definidos por la aplicación.</p>
 */
@RestControllerAdvice
public class RestApiExceptionHandler {

    @Autowired
    private MessageSource messageSource;

    /**
     * Maneja excepciones lanzadas cuando una entidad no se encuentra en el sistema.
     *
     * @param req la solicitud HTTP que generó la excepción
     * @param ex  la excepción específica {@link EntityNotFoundPersException}
     * @return una respuesta con estado 404 (Not Found) y cuerpo estructurado con {@link ErrorResponse}
     */
    @ExceptionHandler(EntityNotFoundPersException.class)
    public ResponseEntity<ApiResponse<ErrorResponse<Void>>> handleEntityNotFoundException(HttpServletRequest req, EntityNotFoundPersException ex) {
        ErrorResponse<Void> error = ErrorUtils.createSimpleError(
                ex.getCode(),
                ex.getMessage(),
                HttpStatus.NOT_FOUND.value())
            .setUrl(req.getRequestURL().toString())
            .setMethod(req.getMethod());

        return ResponseEntity
            .status(HttpStatus.NOT_FOUND)
            .body(ApiResponse.error(ex.getMessageKey(), HttpStatus.NOT_FOUND, error));
    }

    /**
     * Maneja excepciones lanzadas cuando una entidad ya existe en el sistema
     * y no puede crearse de nuevo (violación de unicidad o duplicado lógico).
     *
     * @param req la solicitud HTTP que generó la excepción
     * @param ex  la excepción específica {@link EntityAlreadyExistsException}
     * @return una respuesta con estado 409 (Conflict) y cuerpo estructurado con {@link ErrorResponse}
     */
    @ExceptionHandler(EntityAlreadyExistsException.class)
    public ResponseEntity<ApiResponse<ErrorResponse<Void>>> handleEntityAlreadyExistsException(HttpServletRequest req, EntityAlreadyExistsException ex) {
        ErrorResponse<Void> error = ErrorUtils.createSimpleError(
                ex.getCode(),
                ex.getMessage(),
                HttpStatus.CONFLICT.value())
            .setUrl(req.getRequestURL().toString())
            .setMethod(req.getMethod());

        return ResponseEntity
            .status(HttpStatus.CONFLICT.value())
            .body(ApiResponse.error(ex.getMessageKey(), HttpStatus.NOT_FOUND, error));    
    }

    /**
     * Maneja errores de validación cuando el cuerpo de la solicitud no cumple con las reglas definidas
     * en las anotaciones de validación (como {@code @NotBlank}, {@code @Size}, etc.).
     *
     * <p>Extrae los errores campo a campo y los devuelve dentro del campo {@code data} de {@link ErrorResponse},
     * permitiendo al cliente saber qué atributos no cumplen con las restricciones.</p>
     *
     * @param req la solicitud HTTP que generó la excepción
     * @param ex  la excepción de validación lanzada por Spring
     * @return una respuesta con estado 400 (Bad Request) y cuerpo detallado con los errores por campo
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<ErrorResponse<?>>> handleValidationExceptions(
            HttpServletRequest req, MethodArgumentNotValidException ex) {

        Map<String, String> fieldErrors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String field = ((FieldError) error).getField();
            String message = error.getDefaultMessage();
            fieldErrors.put(field, message);
        });

        // Resolver mensajes desde el archivo error-messages.properties
        String internalMessage = messageSource.getMessage("error.validation.fields", null, req.getLocale());
        String clientMessage = messageSource.getMessage("error.validation.client", null, req.getLocale());

        ErrorResponse<Map<String, String>> error = ErrorUtils.<Map<String, String>>createError(
                "VALIDATION_ERROR",
                internalMessage,
                HttpStatus.BAD_REQUEST.value())
            .setUrl(req.getRequestURL().toString())
            .setMethod(req.getMethod())
            .setData(fieldErrors);

        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(ApiResponse.error(clientMessage, HttpStatus.BAD_REQUEST, error));
    }

}

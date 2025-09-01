package com.unicuaca.asst.unicauca_asst.common.exceptions;

import java.sql.SQLSyntaxErrorException;
import java.util.HashMap;
import java.util.Map;

import org.hibernate.exception.JDBCConnectionException;
import org.hibernate.exception.SQLGrammarException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.dao.InvalidDataAccessResourceUsageException;
import org.springframework.dao.QueryTimeoutException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.CannotCreateTransactionException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import com.unicuaca.asst.unicauca_asst.common.exceptions.structure.ErrorCode;
import com.unicuaca.asst.unicauca_asst.common.exceptions.structure.ErrorResponse;
import com.unicuaca.asst.unicauca_asst.common.exceptions.structure.ErrorUtils;
import com.unicuaca.asst.unicauca_asst.common.response.ApiResponse;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;

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
            .body(ApiResponse.error(ex.getMessageKey(), HttpStatus.CONFLICT, error));    
    }

    /**
     * Maneja excepciones de tipo {@link EntityCreationException} cuando falla la creación de una entidad.
     *
     * <p>Retorna una respuesta HTTP 500 con detalles del error, incluyendo código, mensaje, URL y método HTTP.</p>
     *
     * @param req solicitud HTTP que originó la excepción
     * @param ex  excepción lanzada al fallar la creación de la entidad
     * @return respuesta con estado 500 y cuerpo estructurado de error
     */
    @ExceptionHandler(EntityCreationException.class)
    public ResponseEntity<ApiResponse<ErrorResponse<Void>>> handleEntityCreationException(HttpServletRequest req, EntityCreationException ex) {
        ErrorResponse<Void> error = ErrorUtils.createSimpleError(
                ex.getCode(),
                ex.getMessage(),
                HttpStatus.INTERNAL_SERVER_ERROR.value())
            .setUrl(req.getRequestURL().toString())
            .setMethod(req.getMethod());

        return ResponseEntity
            .status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(ApiResponse.error(ex.getMessageKey(), HttpStatus.INTERNAL_SERVER_ERROR, error));
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

    /**
     * Maneja excepciones de catálogo vacío.
     *
     * @param req la solicitud HTTP que generó la excepción
     * @param ex  la excepción lanzada al acceder a un catálogo vacío
     * @return una respuesta con estado 404 (Not Found) y cuerpo detallado del error
     */
    @ExceptionHandler(CatalogEmptyException.class)
    public ResponseEntity<ApiResponse<ErrorResponse<Void>>> handleCatalogEmpty(
            HttpServletRequest req, CatalogEmptyException ex) {

        ErrorResponse<Void> error = ErrorUtils.createSimpleError(
                ex.getCode(),
                ex.getMessage(),
                HttpStatus.INTERNAL_SERVER_ERROR.value())
            .setUrl(req.getRequestURL().toString())
            .setMethod(req.getMethod());

        return ResponseEntity
            .status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(ApiResponse.error(ex.getMessageKey(), HttpStatus.INTERNAL_SERVER_ERROR, error));
    }

    /**
     * ------------------------------------------------------
     * Sección de manejo de excepciones relacionadas con la base de datos y otras más excepciones
     * ------------------------------------------------------
     */

    // --- TIMEOUT de consulta (ej. SELECT tardó demasiado) -> 504
    @ExceptionHandler(QueryTimeoutException.class)
    public ResponseEntity<ApiResponse<ErrorResponse<Void>>> handleQueryTimeout(
            HttpServletRequest req, QueryTimeoutException ex) {

        ErrorResponse<Void> error = ErrorUtils.createSimpleError(
                ErrorCode.DB_TIMEOUT.getCode(),
                ErrorCode.DB_TIMEOUT.getMessageKey(),
                HttpStatus.GATEWAY_TIMEOUT.value())
            .setUrl(req.getRequestURL().toString())
            .setMethod(req.getMethod());

        return ResponseEntity
            .status(HttpStatus.GATEWAY_TIMEOUT)
            .body(ApiResponse.error(ErrorCode.DB_TIMEOUT.getMessageKey(), HttpStatus.GATEWAY_TIMEOUT, error));
    }

    // --- Recurso de datos no disponible / conexión caída -> 503
    @ExceptionHandler({
        DataAccessResourceFailureException.class,
        CannotCreateTransactionException.class,
        JDBCConnectionException.class,
        java.sql.SQLTransientConnectionException.class
    })
    public ResponseEntity<ApiResponse<ErrorResponse<Void>>> handleDbUnavailable(
            HttpServletRequest req, Exception ex) {

        ErrorResponse<Void> error = ErrorUtils.createSimpleError(
                ErrorCode.DB_UNAVAILABLE.getCode(),
                ErrorCode.DB_UNAVAILABLE.getMessageKey(),
                HttpStatus.SERVICE_UNAVAILABLE.value())
            .setUrl(req.getRequestURL().toString())
            .setMethod(req.getMethod());

        return ResponseEntity
            .status(HttpStatus.SERVICE_UNAVAILABLE)
            .body(ApiResponse.error(ErrorCode.DB_UNAVAILABLE.getMessageKey(), HttpStatus.SERVICE_UNAVAILABLE, error));
    }

    // --- Errores de gramática SQL / tabla/columna inexistente -> 500
    @ExceptionHandler({
        InvalidDataAccessResourceUsageException.class,
        SQLGrammarException.class,
        SQLSyntaxErrorException.class
    })
    public ResponseEntity<ApiResponse<ErrorResponse<Void>>> handleSqlGrammar(
            HttpServletRequest req, Exception ex) {

        ErrorResponse<Void> error = ErrorUtils.createSimpleError(
                ErrorCode.SQL_GRAMMAR_ERROR.getCode(),
                ErrorCode.SQL_GRAMMAR_ERROR.getMessageKey(),
                HttpStatus.INTERNAL_SERVER_ERROR.value())
            .setUrl(req.getRequestURL().toString())
            .setMethod(req.getMethod());

        return ResponseEntity
            .status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(ApiResponse.error(ErrorCode.SQL_GRAMMAR_ERROR.getMessageKey(), HttpStatus.INTERNAL_SERVER_ERROR, error));
    }

    // --- Fallback para errores de acceso a datos -> 500
    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<ApiResponse<ErrorResponse<Void>>> handleDataAccess(
            HttpServletRequest req, DataAccessException ex) {

        ErrorResponse<Void> error = ErrorUtils.createSimpleError(
                ErrorCode.DATA_ACCESS_ERROR.getCode(),
                ErrorCode.DATA_ACCESS_ERROR.getMessageKey(),
                HttpStatus.INTERNAL_SERVER_ERROR.value())
            .setUrl(req.getRequestURL().toString())
            .setMethod(req.getMethod());

        return ResponseEntity
            .status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(ApiResponse.error(ErrorCode.DATA_ACCESS_ERROR.getMessageKey(), HttpStatus.INTERNAL_SERVER_ERROR, error));
    }

    // --- Parámetros de request inválidos (query/path) -> 400
    @ExceptionHandler({
        MissingServletRequestParameterException.class,
        MethodArgumentTypeMismatchException.class,
        ConstraintViolationException.class
    })
    public ResponseEntity<ApiResponse<ErrorResponse<Void>>> handleBadRequest(
            HttpServletRequest req, Exception ex) {

        ErrorResponse<Void> error = ErrorUtils.createSimpleError(
                ErrorCode.BAD_REQUEST.getCode(),
                ErrorCode.BAD_REQUEST.getMessageKey(),
                HttpStatus.BAD_REQUEST.value())
            .setUrl(req.getRequestURL().toString())
            .setMethod(req.getMethod());

        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(ApiResponse.error(ErrorCode.BAD_REQUEST.getMessageKey(), HttpStatus.BAD_REQUEST, error));
    }

    // --- Método HTTP no soportado -> 405
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ApiResponse<ErrorResponse<Void>>> handleMethodNotAllowed(
            HttpServletRequest req, HttpRequestMethodNotSupportedException ex) {

        ErrorResponse<Void> error = ErrorUtils.createSimpleError(
                ErrorCode.METHOD_NOT_ALLOWED.getCode(),
                ErrorCode.METHOD_NOT_ALLOWED.getMessageKey(),
                HttpStatus.METHOD_NOT_ALLOWED.value())
            .setUrl(req.getRequestURL().toString())
            .setMethod(req.getMethod());

        return ResponseEntity
            .status(HttpStatus.METHOD_NOT_ALLOWED)
            .body(ApiResponse.error(ErrorCode.METHOD_NOT_ALLOWED.getMessageKey(), HttpStatus.METHOD_NOT_ALLOWED, error));
    }

    // --- Media type no soportado / no aceptable -> 415 / 406
    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<ApiResponse<ErrorResponse<Void>>> handleUnsupportedMediaType(
            HttpServletRequest req, HttpMediaTypeNotSupportedException ex) {

        ErrorResponse<Void> error = ErrorUtils.createSimpleError(
                ErrorCode.UNSUPPORTED_MEDIA_TYPE.getCode(),
                ErrorCode.UNSUPPORTED_MEDIA_TYPE.getMessageKey(),
                HttpStatus.UNSUPPORTED_MEDIA_TYPE.value())
            .setUrl(req.getRequestURL().toString())
            .setMethod(req.getMethod());

        return ResponseEntity
            .status(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
            .body(ApiResponse.error(ErrorCode.UNSUPPORTED_MEDIA_TYPE.getMessageKey(), HttpStatus.UNSUPPORTED_MEDIA_TYPE, error));
    }

    @ExceptionHandler(HttpMediaTypeNotAcceptableException.class)
    public ResponseEntity<ApiResponse<ErrorResponse<Void>>> handleNotAcceptable(
            HttpServletRequest req, HttpMediaTypeNotAcceptableException ex) {

        ErrorResponse<Void> error = ErrorUtils.createSimpleError(
                ErrorCode.NOT_ACCEPTABLE.getCode(),
                ErrorCode.NOT_ACCEPTABLE.getMessageKey(),
                HttpStatus.NOT_ACCEPTABLE.value())
            .setUrl(req.getRequestURL().toString())
            .setMethod(req.getMethod());

        return ResponseEntity
            .status(HttpStatus.NOT_ACCEPTABLE)
            .body(ApiResponse.error(ErrorCode.NOT_ACCEPTABLE.getMessageKey(), HttpStatus.NOT_ACCEPTABLE, error));   
    }

    // --- Seguridad: 401 / 403
    /* @ExceptionHandler(org.springframework.security.core.AuthenticationException.class)
    public ResponseEntity<ApiResponse<ErrorResponse<Void>>> handleUnauthorized(
            HttpServletRequest req, org.springframework.security.core.AuthenticationException ex) {

        ErrorResponse<Void> error = ErrorUtils.createSimpleError(
                ErrorCode.UNAUTHORIZED.getCode(),
                ErrorCode.UNAUTHORIZED.getMessageKey(),
                HttpStatus.UNAUTHORIZED.value())
            .setUrl(req.getRequestURL().toString())
            .setMethod(req.getMethod());

        return ResponseEntity
            .status(HttpStatus.UNAUTHORIZED)
            .body(ApiResponse.error(ErrorCode.UNAUTHORIZED.getMessageKey(), HttpStatus.UNAUTHORIZED, error));
    } */

    /* @ExceptionHandler(org.springframework.security.access.AccessDeniedException.class)
    public ResponseEntity<ApiResponse<ErrorResponse<Void>>> handleForbidden(
            HttpServletRequest req, org.springframework.security.access.AccessDeniedException ex) {

        ErrorResponse<Void> error = ErrorUtils.createSimpleError(
                ErrorCode.FORBIDDEN.getCode(),
                ErrorCode.FORBIDDEN.getMessageKey(),
                HttpStatus.FORBIDDEN.value())
            .setUrl(req.getRequestURL().toString())
            .setMethod(req.getMethod());

        return ResponseEntity
            .status(HttpStatus.FORBIDDEN)
            .body(ApiResponse.error(ErrorCode.FORBIDDEN.getMessageKey(), HttpStatus.FORBIDDEN, error));
    } */

    // --- Bean de mapper o dependencia no encontrada (cableado) -> 500
    @ExceptionHandler(NoSuchBeanDefinitionException.class)
    public ResponseEntity<ApiResponse<ErrorResponse<Void>>> handleNoBean(
            HttpServletRequest req, NoSuchBeanDefinitionException ex) {

        ErrorResponse<Void> error = ErrorUtils.createSimpleError(
                ErrorCode.MAPPER_ERROR.getCode(),
                ErrorCode.MAPPER_ERROR.getMessageKey(),
                HttpStatus.INTERNAL_SERVER_ERROR.value())
            .setUrl(req.getRequestURL().toString())
            .setMethod(req.getMethod());

        return ResponseEntity
            .status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(ApiResponse.error(ErrorCode.MAPPER_ERROR.getMessageKey(), HttpStatus.INTERNAL_SERVER_ERROR, error));
    }

    // --- Fallback genérico -> 500
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<ErrorResponse<Void>>> handleGeneric(
            HttpServletRequest req, Exception ex) {

        ErrorResponse<Void> error = ErrorUtils.createSimpleError(
                ErrorCode.GENERIC_ERROR.getCode(),
                ErrorCode.GENERIC_ERROR.getMessageKey(),
                HttpStatus.INTERNAL_SERVER_ERROR.value())
            .setUrl(req.getRequestURL().toString())
            .setMethod(req.getMethod());

        return ResponseEntity
            .status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(ApiResponse.error(ErrorCode.GENERIC_ERROR.getMessageKey(), HttpStatus.INTERNAL_SERVER_ERROR, error));
    }

}

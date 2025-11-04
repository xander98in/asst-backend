package com.unicuaca.asst.unicauca_asst.common.exceptions;

import java.sql.SQLSyntaxErrorException;
import java.util.HashMap;
import java.util.Map;

import com.unicuaca.asst.unicauca_asst.common.response.ResponseUtil;
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
     * Maneja la excepción {@link EntityNotFoundPersException} cuando una entidad solicitada no existe.
     *
     * @param req solicitud HTTP que originó la excepción
     * @param ex  excepción lanzada al no encontrar la entidad
     * @return {@link ResponseEntity} con estado 404 y cuerpo estandarizado de error
     */
    @ExceptionHandler(EntityNotFoundPersException.class)
    public ResponseEntity<ApiResponse<ErrorResponse<Void>>> handleEntityNotFoundException(HttpServletRequest req, EntityNotFoundPersException ex) {
        var details = ErrorUtils.of(
            ex.getCode(),
            ex.getMessage(),
            req.getMethod()
        );

        return ResponseUtil.error(
            req,
            ErrorCode.DATA_ERROR.getCode(),
            HttpStatus.NOT_FOUND,
            ErrorCode.DATA_ERROR.getMessageKey(),
            details
        );
    }

    /**
     * Maneja la excepción {@link EntityAlreadyExistsException} cuando se intenta crear
     * una entidad que ya existe (violación de unicidad o duplicado lógico).
     *
     * @param req solicitud HTTP que originó la excepción
     * @param ex  excepción lanzada al detectar duplicidad
     * @return {@link ResponseEntity} con estado 409 y cuerpo estandarizado de error
     */
    @ExceptionHandler(EntityAlreadyExistsException.class)
    public ResponseEntity<ApiResponse<ErrorResponse<Void>>> handleEntityAlreadyExistsException(HttpServletRequest req, EntityAlreadyExistsException ex) {
        var details = ErrorUtils.of(
            ex.getCode(),
            ex.getMessage(),
            req.getMethod()
        );
        return ResponseUtil.error(
            req,
            ErrorCode.DATA_ERROR.getCode(),
            HttpStatus.CONFLICT,
            ErrorCode.DATA_ERROR.getMessageKey(),
            details
        );
    }

    /**
     * Maneja la excepción {@link EntityCreationException} cuando falla la creación de una entidad.
     *
     * @param req solicitud HTTP que originó la excepción
     * @param ex  excepción de fallo en creación de entidad
     * @return {@link ResponseEntity} con estado 500 y cuerpo estandarizado de error
     */
    @ExceptionHandler(EntityCreationException.class)
    public ResponseEntity<ApiResponse<ErrorResponse<Void>>> handleEntityCreationException(HttpServletRequest req, EntityCreationException ex) {
        var details = ErrorUtils.of(
            ex.getCode(),
            ex.getMessage(),
            req.getMethod()
        );

        return ResponseUtil.error(
            req,
            ErrorCode.DATA_ERROR.getCode(),
            HttpStatus.INTERNAL_SERVER_ERROR,
            ErrorCode.DATA_ERROR.getMessageKey(),
            details
        );
    }

    /**
     * Maneja errores de validación de entrada lanzados por Spring (p. ej., anotaciones
     * {@code @NotBlank}, {@code @Size}, {@code @Email}, etc.).
     *
     * @param req solicitud HTTP que originó la excepción
     * @param ex  excepción de validación con los errores de binding
     * @return {@link ResponseEntity} con estado 400 y detalle de errores por campo
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<ErrorResponse<Map<String, String>>>> handleValidationExceptions(
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

        var details = ErrorUtils.createError(
            ErrorCode.FIELD_VALIDATION,
            req.getMethod(),
            fieldErrors
        );

        return ResponseUtil.error(
            req,
            ErrorCode.VALIDATION_ERROR.getCode(),
            HttpStatus.BAD_REQUEST,
            ErrorCode.VALIDATION_ERROR.getMessageKey(),
            details
        );
    }

    /**
     * Maneja la excepción {@link CatalogEmptyException} cuando un catálogo solicitado no
     * contiene elementos.
     *
     * @param req solicitud HTTP que originó la excepción
     * @param ex  excepción lanzada al encontrar un catálogo vacío
     * @return {@link ResponseEntity} con estado 404 y cuerpo estandarizado de error
     */
    @ExceptionHandler(CatalogEmptyException.class)
    public ResponseEntity<ApiResponse<ErrorResponse<Void>>> handleCatalogEmpty(
            HttpServletRequest req, CatalogEmptyException ex) {

        var details = ErrorUtils.of(
            ex.getCode(),
            ex.getMessage(),
            req.getMethod()
        );

        return ResponseUtil.error(
            req,
            ErrorCode.CATALOG_ERROR.getCode(),
            HttpStatus.NOT_FOUND,
            ErrorCode.CATALOG_ERROR.getMessageKey(),
            details
        );
    }

    /**
     * ------------------------------------------------------
     * Sección de manejo de excepciones relacionadas con la base de datos y otras más excepciones
     * ------------------------------------------------------
     */

    /**
     * Maneja {@link QueryTimeoutException} cuando una consulta a la base
     * de datos excede el tiempo de espera configurado.
     *
     * @param req solicitud HTTP que originó la excepción
     * @param ex  excepción de timeout en consulta a base de datos
     * @return {@link ResponseEntity} con estado 504 y cuerpo estandarizado de error
     */
    @ExceptionHandler(QueryTimeoutException.class)
    public ResponseEntity<ApiResponse<ErrorResponse<Void>>> handleQueryTimeout(
            HttpServletRequest req, QueryTimeoutException ex) {

        var details = ErrorUtils.createSimpleError(
            ErrorCode.DB_TIMEOUT,
            req.getMethod()
        );

        return ResponseUtil.error(
            req,
            ErrorCode.DATA_ERROR.getCode(),
            HttpStatus.GATEWAY_TIMEOUT,
            ErrorCode.DATA_ERROR.getMessageKey(),
            details
        );
    }

    /**
     * Maneja errores cuando la base de datos no está disponible o no es posible abrir una transacción.
     *
     * <p>Atrapa: {@link DataAccessResourceFailureException},
     * {@link CannotCreateTransactionException},
     * {@link JDBCConnectionException},
     * {@link java.sql.SQLTransientConnectionException}.</p>
     *
     * <p>Responde con <b>503 Service Unavailable</b> y cuerpo:
     * @param req solicitud HTTP que originó la excepción
     * @param ex  excepción de base de datos no disponible
     * {@code ApiResponse<ErrorResponse<Void>>}.</p>
     */
    @ExceptionHandler({
        DataAccessResourceFailureException.class,
        CannotCreateTransactionException.class,
        JDBCConnectionException.class,
        java.sql.SQLTransientConnectionException.class
    })
    public ResponseEntity<ApiResponse<ErrorResponse<Void>>> handleDbUnavailable(
            HttpServletRequest req, Exception ex) {

        var details = ErrorUtils.createSimpleError(
            ErrorCode.DB_UNAVAILABLE,
            req.getMethod()
        );

        return ResponseUtil.error(
            req,
            ErrorCode.DATA_ERROR.getCode(),
            HttpStatus.SERVICE_UNAVAILABLE,
            ErrorCode.DATA_ERROR.getMessageKey(),
            details
        );
    }

    /**
     * Maneja errores de sintaxis/uso de SQL (gramática SQL inválida).
     *
     * <p>Atrapa: {@link InvalidDataAccessResourceUsageException},
     * {@link SQLGrammarException},
     * {@link java.sql.SQLSyntaxErrorException}.</p>
     *
     * <p>Responde con <b>500 Internal Server Error</b> y cuerpo:
     * {@code ApiResponse<ErrorResponse<Void>>}.</p>
     */
    @ExceptionHandler({
        InvalidDataAccessResourceUsageException.class,
        SQLGrammarException.class,
        SQLSyntaxErrorException.class
    })
    public ResponseEntity<ApiResponse<ErrorResponse<Void>>> handleSqlGrammar(HttpServletRequest req, Exception ex) {
        var details = ErrorUtils.createSimpleError(
            ErrorCode.SQL_GRAMMAR,
            req.getMethod()
        );

        return ResponseUtil.error(
            req,
            ErrorCode.DATA_ERROR.getCode(),
            HttpStatus.INTERNAL_SERVER_ERROR,
            ErrorCode.DATA_ERROR.getMessageKey(),
            details
        );
    }

    /**
     * Maneja errores generales de acceso a datos (consultas, actualizaciones, etc.).
     *
     * <p>Atrapa: {@link DataAccessException}.</p>
     *
     * <p>Responde con <b>500 Internal Server Error</b> y cuerpo:
     * {@code ApiResponse<ErrorResponse<Void>>}.</p>
     */
    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<ApiResponse<ErrorResponse<Void>>> handleDataAccess(
            HttpServletRequest req, DataAccessException ex) {

        var details = ErrorUtils.createSimpleError(
            ErrorCode.DATA_ACCESS,
            req.getMethod()
        );

        return ResponseUtil.error(
            req,
            ErrorCode.DATA_ERROR.getCode(),
            HttpStatus.INTERNAL_SERVER_ERROR,
            ErrorCode.DATA_ERROR.getMessageKey(),
            details
        );
    }

    /**
     * Maneja solicitudes mal formadas o con parámetros inválidos.
     *
     * <p>Atrapa: {@link MissingServletRequestParameterException},
     * {@link MethodArgumentTypeMismatchException},
     * {@link ConstraintViolationException}.</p>
     *
     * <p>Responde con <b>400 Bad Request</b> y cuerpo:
     * {@code ApiResponse<ErrorResponse<Void>>}.</p>
     */
    @ExceptionHandler({
        MissingServletRequestParameterException.class,
        MethodArgumentTypeMismatchException.class,
        ConstraintViolationException.class
    })
    public ResponseEntity<ApiResponse<ErrorResponse<Void>>> handleBadRequest(HttpServletRequest req, Exception ex) {
        var details = ErrorUtils.createSimpleError(
            ErrorCode.BAD_REQUEST,
            req.getMethod()
        );

        return ResponseUtil.error(
            req,
            ErrorCode.VALIDATION_ERROR.getCode(),
            HttpStatus.BAD_REQUEST,
            ErrorCode.VALIDATION_ERROR.getMessageKey(),
            details
        );
    }

    /**
     * Maneja invocaciones con un método HTTP no soportado por el endpoint.
     *
     * <p>Atrapa: {@link HttpRequestMethodNotSupportedException}.</p>
     *
     * <p>Responde con <b>405 Method Not Allowed</b> y cuerpo:
     * {@code ApiResponse<ErrorResponse<Void>>}.</p>
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ApiResponse<ErrorResponse<Void>>> handleMethodNotAllowed(
            HttpServletRequest req, HttpRequestMethodNotSupportedException ex) {

        var details = ErrorUtils.createSimpleError(
            ErrorCode.METHOD_NOT_ALLOWED,
            req.getMethod()
        );

        return ResponseUtil.error(
            req,
            ErrorCode.GENERIC_ERROR.getCode(),
            HttpStatus.METHOD_NOT_ALLOWED,
            ErrorCode.GENERIC_ERROR.getMessageKey(),
            details
        );
    }

    /**
     * Maneja peticiones con tipo de contenido no soportado por el servidor.
     *
     * <p>Atrapa: {@link HttpMediaTypeNotSupportedException}.</p>
     *
     * <p>Responde con <b>415 Unsupported Media Type</b> y cuerpo:
     * {@code ApiResponse<ErrorResponse<Void>>}.</p>
     */
    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<ApiResponse<ErrorResponse<Void>>> handleUnsupportedMediaType(
            HttpServletRequest req, HttpMediaTypeNotSupportedException ex) {

        var details = ErrorUtils.createSimpleError(
            ErrorCode.UNSUPPORTED_MEDIA_TYPE,
            req.getMethod()
        );

        return ResponseUtil.error(
            req,
            ErrorCode.GENERIC_ERROR.getCode(),
            HttpStatus.UNSUPPORTED_MEDIA_TYPE,
            ErrorCode.GENERIC_ERROR.getMessageKey(),
            details
        );
    }

    /**
     * Maneja solicitudes cuyo formato de respuesta solicitado por el cliente
     * (header {@code Accept}) no es aceptable.
     *
     * <p>Atrapa: {@link HttpMediaTypeNotAcceptableException}.</p>
     *
     * <p>Responde con <b>406 Not Acceptable</b> y cuerpo:
     * {@code ApiResponse<ErrorResponse<Void>>}.</p>
     */
    @ExceptionHandler(HttpMediaTypeNotAcceptableException.class)
    public ResponseEntity<ApiResponse<ErrorResponse<Void>>> handleNotAcceptable(
            HttpServletRequest req, HttpMediaTypeNotAcceptableException ex) {

        var details = ErrorUtils.createSimpleError(
            ErrorCode.NOT_ACCEPTABLE,
            req.getMethod()
        );

        return ResponseUtil.error(
            req,
            ErrorCode.GENERIC_ERROR.getCode(),
            HttpStatus.NOT_ACCEPTABLE,
            ErrorCode.GENERIC_ERROR.getMessageKey(),
            details
        );
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

    /**
     * Maneja la ausencia de beans en el contexto de Spring (p. ej., cuando un mapper
     * o componente requerido no está definido o no pudo inyectarse).
     *
     * <p>Atrapa: {@link NoSuchBeanDefinitionException}.</p>
     *
     * @param req solicitud HTTP que originó la excepción
     * @param ex  excepción lanzada por Spring al no encontrar el bean
     * @return {@link ResponseEntity} con estado 500 y cuerpo estandarizado de error
     */
    @ExceptionHandler(NoSuchBeanDefinitionException.class)
    public ResponseEntity<ApiResponse<ErrorResponse<Void>>> handleNoBean(
            HttpServletRequest req, NoSuchBeanDefinitionException ex) {

        var details = ErrorUtils.createSimpleError(
            ErrorCode.MAPPER_ERROR,
            req.getMethod()
        );

        return ResponseUtil.error(
            req,
            ErrorCode.MAPPER_ERROR.getCode(),
            HttpStatus.INTERNAL_SERVER_ERROR,
            ErrorCode.MAPPER_ERROR.getMessageKey(),
            details
        );
    }

    /**
     * Handler genérico para cualquier excepción no controlada por otros manejadores
     * específicos del {@code @RestControllerAdvice}.
     *
     * <p>Responde con <b>500 Internal Server Error</b>. El cuerpo sigue el contrato:
     * {@code ApiResponse<ErrorResponse<Void>>}. Para evitar filtrar detalles sensibles,
     * se envía un mensaje genérico al cliente (puedes loguear {@code ex} para diagnóstico).</p>
     *
     * @param req solicitud HTTP que originó la excepción
     * @param ex  excepción no controlada
     * @return {@link ResponseEntity} con estado 500 y cuerpo estandarizado de error
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<ErrorResponse<Void>>> handleGeneric(
            HttpServletRequest req, Exception ex) {

        System.out.println("\n\n[ERROR] Excepción no controlada capturada por RestApiExceptionHandler:");
        var details = ErrorUtils.of(
            ErrorCode.GENERIC_ERROR.getCode(),
            ex.getMessage(),
            req.getMethod()
        );

        return ResponseUtil.error(
            req,
            ErrorCode.GENERIC_ERROR.getCode(),
            HttpStatus.INTERNAL_SERVER_ERROR,
            ErrorCode.GENERIC_ERROR.getMessageKey(),
            details
        );
    }

}

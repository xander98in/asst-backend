package com.unicuaca.asst.unicauca_asst.common.exceptions;

import java.util.HashMap;
import java.util.Map;

import com.unicuaca.asst.unicauca_asst.common.response.ResponseUtil;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.dao.InvalidDataAccessResourceUsageException;
import org.springframework.dao.QueryTimeoutException;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
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
import lombok.RequiredArgsConstructor;

/**
 * Manejador global de excepciones para los controladores REST de la aplicación.
 *
 * <p>Esta clase centraliza la gestión de errores lanzados desde cualquier capa del backend,
 * convirtiéndolos en respuestas estructuradas del tipo {@link ApiResponse} que encapsulan un {@link ErrorResponse}.
 * Esto garantiza consistencia, trazabilidad y claridad para el cliente consumidor de la API.</p>
 */
@RestControllerAdvice
@RequiredArgsConstructor
public class RestApiExceptionHandler {

    private final MessageSource messageSource;

    /**
     * Resuelve un mensaje para el usuario a partir de una posible clave o mensaje directo.
     *
     * @param input clave de mensaje o mensaje directo
     * @param args argumentos para el mensaje
     * @param req solicitud actual para obtener el locale
     * @param defaultMsg mensaje por defecto si la resolución falla
     * @return el mensaje final traducido
     */
    private String resolveUserMessage(String input, Object[] args, HttpServletRequest req, String defaultMsg) {
        if (input == null) return defaultMsg;
        try {
            return messageSource.getMessage(input, args, req.getLocale());
        } catch (NoSuchMessageException e) {
            // Si no es una clave válida, se asume que es un mensaje directo
            return input;
        }
    }

    /**
     * Resuelve un mensaje técnico a partir de un ErrorCode.
     *
     * @param errorCode el código de error que contiene la clave técnica
     * @param req solicitud actual
     * @return el mensaje técnico traducido o el valor por defecto
     */
    private String resolveTechMessage(ErrorCode errorCode, HttpServletRequest req) {
        try {
            return messageSource.getMessage(errorCode.getMessageKey(), null, req.getLocale());
        } catch (NoSuchMessageException e) {
            return "Error interno del sistema";
        }
    }

    /**
     * Maneja la excepción {@link EntityNotFoundPersException} cuando una entidad solicitada no existe.
     *
     * @param req solicitud HTTP que originó la excepción
     * @param ex  excepción lanzada al no encontrar la entidad
     * @return {@link ResponseEntity} con estado 404 y cuerpo estandarizado de error
     */
    @ExceptionHandler(EntityNotFoundPersException.class)
    public ResponseEntity<ApiResponse<ErrorResponse<Void>>> handleEntityNotFoundException(HttpServletRequest req, EntityNotFoundPersException ex) {
        String defaultMsg = resolveUserMessage("user.default.entity_not_found", null, req, "Registro no encontrado.");
        String userMsg = resolveUserMessage(ex.getUserMessage(), ex.getArgs(), req, defaultMsg);
        String techDetail = resolveUserMessage(ex.getMessage(), ex.getArgs(), req, ex.getMessage());
        
        var details = ErrorUtils.of(
            ex.getCode(),
            techDetail,
            userMsg,
            req.getMethod()
        );

        return ResponseUtil.error(
            req,
            ErrorCode.DATA_ERROR.getCode(),
            HttpStatus.NOT_FOUND,
            resolveTechMessage(ErrorCode.DATA_ERROR, req),
            details
        );
    }

    /**
     * Maneja la excepción {@link BusinessRuleViolationException} cuando se viola
     * una regla de negocio definida en el dominio.
     *
     * @param req solicitud HTTP que originó la excepción
     * @param ex  excepción lanzada al violar una regla de negocio
     * @return {@link ResponseEntity} con estado 400 y cuerpo estandarizado de error
     */
    @ExceptionHandler(BusinessRuleViolationException.class)
    public ResponseEntity<ApiResponse<ErrorResponse<Void>>> handleBusinessRuleViolationException(HttpServletRequest req, BusinessRuleViolationException ex) {
        String defaultMsg = resolveUserMessage("user.default.business_rule_violation", null, req, "Regla de negocio violada.");
        String userMsg = resolveUserMessage(ex.getUserMessage(), ex.getArgs(), req, defaultMsg);
        String techDetail = resolveUserMessage(ex.getMessage(), ex.getArgs(), req, ex.getMessage());
        
        var details = ErrorUtils.of(
            ex.getCode(),
            techDetail,
            userMsg,
            req.getMethod()
        );

        return ResponseUtil.error(
            req,
            ErrorCode.BUSINESS_RULE_VIOLATION.getCode(),
            HttpStatus.BAD_REQUEST,
            resolveTechMessage(ErrorCode.BUSINESS_RULE_VIOLATION, req),
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
        String defaultMsg = resolveUserMessage("user.default.entity_already_exists", null, req, "Entidad existente.");
        String userMsg = resolveUserMessage(ex.getUserMessage(), ex.getArgs(), req, defaultMsg);
        String techDetail = resolveUserMessage(ex.getMessage(), ex.getArgs(), req, ex.getMessage());
        
        var details = ErrorUtils.of(
            ex.getCode(),
            techDetail,
            userMsg,
            req.getMethod()
        );
        return ResponseUtil.error(
            req,
            ErrorCode.DATA_ERROR.getCode(),
            HttpStatus.CONFLICT,
            resolveTechMessage(ErrorCode.DATA_ERROR, req),
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
        String defaultMsg = resolveUserMessage("user.default.entity_creation_error", null, req, "Error al crear registro.");
        String userMsg = resolveUserMessage(ex.getUserMessage(), ex.getArgs(), req, defaultMsg);
        String techDetail = resolveUserMessage(ex.getMessage(), ex.getArgs(), req, ex.getMessage());

        var details = ErrorUtils.of(
            ex.getCode(),
            techDetail,
            userMsg,
            req.getMethod()
        );

        return ResponseUtil.error(
            req,
            ErrorCode.DATA_ERROR.getCode(),
            HttpStatus.INTERNAL_SERVER_ERROR,
            resolveTechMessage(ErrorCode.DATA_ERROR, req),
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

        String userMsg = resolveUserMessage("user.generic.validation_error", null, req, "Error de validación.");
        String techDetail = resolveTechMessage(ErrorCode.FIELD_VALIDATION, req);

        var details = ErrorResponse.<Map<String, String>>builder()
            .errorCode(ErrorCode.FIELD_VALIDATION.getCode())
            .message(techDetail)
            .userMessage(userMsg)
            .method(req.getMethod())
            .data(fieldErrors)
            .build();

        return ResponseUtil.error(
            req,
            ErrorCode.VALIDATION_ERROR.getCode(),
            HttpStatus.BAD_REQUEST,
            resolveTechMessage(ErrorCode.VALIDATION_ERROR, req),
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

        String defaultMsg = resolveUserMessage("user.default.catalog_empty", null, req, "Catálogo vacío.");
        String userMsg = resolveUserMessage(ex.getUserMessage(), ex.getArgs(), req, defaultMsg);
        String techDetail = resolveUserMessage(ex.getMessage(), ex.getArgs(), req, ex.getMessage());
        
        var details = ErrorUtils.of(
            ex.getCode(),
            techDetail,
            userMsg,
            req.getMethod()
        );

        return ResponseUtil.error(
            req,
            ErrorCode.CATALOG_ERROR.getCode(),
            HttpStatus.NOT_FOUND,
            resolveTechMessage(ErrorCode.CATALOG_ERROR, req),
            details
        );
    }

    /**
     * Maneja la excepción {@link GoogleTokenException} cuando falla la verificación
     * del token de Google OAuth (token inválido, expirado o dominio no autorizado).
     *
     * @param req solicitud HTTP que originó la excepción
     * @param ex  excepción lanzada al verificar el token de Google
     * @return {@link ResponseEntity} con estado 401 y cuerpo estandarizado de error
     */
    @ExceptionHandler(GoogleTokenException.class)
    public ResponseEntity<ApiResponse<ErrorResponse<Void>>> handleGoogleTokenException(
            HttpServletRequest req, GoogleTokenException ex) {

        String defaultMsg = resolveUserMessage("user.auth.invalid_google_token", null, req, "Error de autenticación con Google.");
        String userMsg = resolveUserMessage(ex.getUserMessage(), ex.getArgs(), req, defaultMsg);
        String techDetail = resolveUserMessage(ex.getMessage(), ex.getArgs(), req, ex.getMessage());

        var details = ErrorUtils.of(
            ex.getCode(),
            techDetail,
            userMsg,
            req.getMethod()
        );

        String wrapperMessage;
        try {
            wrapperMessage = messageSource.getMessage(
                ErrorCode.AUTHENTICATION_ERROR.getMessageKey(),
                new Object[]{techDetail},
                req.getLocale()
            );
        } catch (NoSuchMessageException e) {
            wrapperMessage = techDetail;
        }

        return ResponseUtil.error(
            req,
            ErrorCode.AUTHENTICATION_ERROR.getCode(),
            HttpStatus.UNAUTHORIZED,
            wrapperMessage,
            details
        );
    }

    /**
     * Maneja la excepción {@link InvalidJwtException} cuando falla la validación
     * del JWT propio de la aplicación (expirado, firma inválida, malformado).
     *
     * <p>Segunda línea de defensa: normalmente capturada por {@code JwtAuthFilter},
     * pero garantiza consistencia si algún servicio la lanza directamente.</p>
     *
     * @param req solicitud HTTP que originó la excepción
     * @param ex  excepción lanzada al validar el JWT
     * @return {@link ResponseEntity} con estado 401 y cuerpo estandarizado de error
     */
    @ExceptionHandler(InvalidJwtException.class)
    public ResponseEntity<ApiResponse<ErrorResponse<Void>>> handleInvalidJwtException(
            HttpServletRequest req, InvalidJwtException ex) {

        String defaultMsg = resolveUserMessage("user.auth.jwt_malformed", null, req, "Token de seguridad inválido.");
        String userMsg = resolveUserMessage(ex.getUserMessage(), ex.getArgs(), req, defaultMsg);
        String techDetail = resolveUserMessage(ex.getMessage(), ex.getArgs(), req, ex.getMessage());

        var details = ErrorUtils.of(
            ex.getCode(),
            techDetail,
            userMsg,
            req.getMethod()
        );

        String wrapperMessage;
        try {
            wrapperMessage = messageSource.getMessage(
                ErrorCode.AUTHENTICATION_ERROR.getMessageKey(),
                new Object[]{techDetail},
                req.getLocale()
            );
        } catch (NoSuchMessageException e) {
            wrapperMessage = techDetail;
        }

        return ResponseUtil.error(
            req,
            ErrorCode.AUTHENTICATION_ERROR.getCode(),
            HttpStatus.UNAUTHORIZED,
            wrapperMessage,
            details
        );
    }

    /**
     * Maneja {@link QueryTimeoutException} cuando una consulta a la base
     * de datos excede el tiempo de espera configurado.
     *
     * @param req solicitud HTTP que originó la excepción
     * @param ex  excepción de timeout en consulta a base de datos
     * @return {@link ResponseEntity} con estado 504 y cuerpo estandarizado de error
     */
    @ExceptionHandler(QueryTimeoutException.class)
    public ResponseEntity<ApiResponse<ErrorResponse<Void>>> handleQueryTimeout(HttpServletRequest req, QueryTimeoutException ex) {

        String userMsg = resolveUserMessage("user.generic.query_timeout", null, req, "Timeout en consulta.");
        String techDetail = resolveTechMessage(ErrorCode.DB_TIMEOUT, req);

        var details = ErrorUtils.of(
            ErrorCode.DB_TIMEOUT.getCode(),
            techDetail,
            userMsg,
            req.getMethod()
        );

        return ResponseUtil.error(
            req,
            ErrorCode.DATA_ERROR.getCode(),
            HttpStatus.GATEWAY_TIMEOUT,
            resolveTechMessage(ErrorCode.DATA_ERROR, req),
            details
        );
    }

    /**
     * Maneja errores cuando la base de datos no está disponible o no es posible abrir una transacción.
     *
     * @param req solicitud HTTP que originó la excepción
     * @param ex excepción de recurso de datos no disponible
     * @return {@link ResponseEntity} con estado 503 y cuerpo estandarizado de error
     */
    @ExceptionHandler({
        DataAccessResourceFailureException.class,
        CannotCreateTransactionException.class
    })
    public ResponseEntity<ApiResponse<ErrorResponse<Void>>> handleDbUnavailable(HttpServletRequest req, Exception ex) {

        String userMsg = resolveUserMessage("user.generic.db_unavailable", null, req, "Servicio no disponible.");
        String techDetail = resolveTechMessage(ErrorCode.DB_UNAVAILABLE, req);

        var details = ErrorUtils.of(
            ErrorCode.DB_UNAVAILABLE.getCode(),
            techDetail,
            userMsg,
            req.getMethod()
        );

        return ResponseUtil.error(
            req,
            ErrorCode.DATA_ERROR.getCode(),
            HttpStatus.SERVICE_UNAVAILABLE,
            resolveTechMessage(ErrorCode.DATA_ERROR, req),
            details
        );
    }

    /**
     * Maneja errores de sintaxis/uso de SQL (gramática SQL inválida).
     *
     * @param req solicitud HTTP que originó la excepción
     * @param ex excepción de uso de recurso de datos inválido, típicamente por errores en la consulta SQL
     * @return {@link ResponseEntity} con estado 500 y cuerpo estandarizado de error
     */
    @ExceptionHandler({
        InvalidDataAccessResourceUsageException.class
    })
    public ResponseEntity<ApiResponse<ErrorResponse<Void>>> handleSqlGrammar(HttpServletRequest req, Exception ex) {

        String userMsg = resolveUserMessage("user.generic.sql_grammar", null, req, "Error interno.");
        String techDetail = resolveTechMessage(ErrorCode.SQL_GRAMMAR, req);

        var details = ErrorUtils.of(
            ErrorCode.SQL_GRAMMAR.getCode(),
            techDetail,
            userMsg,
            req.getMethod()
        );

        return ResponseUtil.error(
            req,
            ErrorCode.DATA_ERROR.getCode(),
            HttpStatus.INTERNAL_SERVER_ERROR,
            resolveTechMessage(ErrorCode.DATA_ERROR, req),
            details
        );
    }

    /**
     * Maneja errores generales de acceso a datos (consultas, actualizaciones, etc.).
     *
     * @param req solicitud HTTP que originó la excepción
     * @param ex excepción general de acceso a datos, que puede incluir errores de conexión, violaciones de integridad, etc.
     * @return {@link ResponseEntity} con estado 500 y cuerpo estandarizado de error
     */
    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<ApiResponse<ErrorResponse<Void>>> handleDataAccess(
            HttpServletRequest req, DataAccessException ex) {

        String userMsg = resolveUserMessage("user.generic.data_access", null, req, "Error de acceso a datos.");
        String techDetail = resolveTechMessage(ErrorCode.DATA_ACCESS, req);

        var details = ErrorUtils.of(
            ErrorCode.DATA_ACCESS.getCode(),
            techDetail,
            userMsg,
            req.getMethod()
        );

        return ResponseUtil.error(
            req,
            ErrorCode.DATA_ERROR.getCode(),
            HttpStatus.INTERNAL_SERVER_ERROR,
            resolveTechMessage(ErrorCode.DATA_ERROR, req),
            details
        );
    }

    /**
     * Maneja solicitudes mal formadas o con parámetros inválidos.
     *
     * @param req solicitud HTTP que originó la excepción
     * @param ex excepción que indica un error de sintaxis o tipo en los parámetros de la solicitud, como falta de parámetros requeridos o tipos incompatibles
     * @return {@link ResponseEntity} con estado 400 y cuerpo estandarizado de error indicando el problema de la solicitud
     */
    @ExceptionHandler({
        MissingServletRequestParameterException.class,
        MethodArgumentTypeMismatchException.class,
        ConstraintViolationException.class
    })
    public ResponseEntity<ApiResponse<ErrorResponse<Void>>> handleBadRequest(HttpServletRequest req, Exception ex) {

        String userMsg = resolveUserMessage("user.generic.bad_request", null, req, "Solicitud inválida.");
        String techDetail = resolveTechMessage(ErrorCode.BAD_REQUEST, req);

        var details = ErrorUtils.of(
            ErrorCode.BAD_REQUEST.getCode(),
            techDetail,
            userMsg,
            req.getMethod()
        );

        return ResponseUtil.error(
            req,
            ErrorCode.VALIDATION_ERROR.getCode(),
            HttpStatus.BAD_REQUEST,
            resolveTechMessage(ErrorCode.VALIDATION_ERROR, req),
            details
        );
    }

    /**
     * Maneja invocaciones con un método HTTP no soportado por el endpoint.
     *
     * @param req solicitud HTTP que originó la excepción
     * @param ex excepción que indica que el método HTTP utilizado no es compatible con el endpoint solicitado, como usar POST en un endpoint que solo acepta GET
     * @return {@link ResponseEntity} con estado 405 y cuerpo estandarizado de error indicando que el método no es permitido
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ApiResponse<ErrorResponse<Void>>> handleMethodNotAllowed(
            HttpServletRequest req, HttpRequestMethodNotSupportedException ex) {

        String userMsg = resolveUserMessage("user.generic.method_not_allowed", null, req, "Método no permitido.");
        String techDetail = resolveTechMessage(ErrorCode.METHOD_NOT_ALLOWED, req);

        var details = ErrorUtils.of(
            ErrorCode.METHOD_NOT_ALLOWED.getCode(),
            techDetail,
            userMsg,
            req.getMethod()
        );

        return ResponseUtil.error(
            req,
            ErrorCode.GENERIC_ERROR.getCode(),
            HttpStatus.METHOD_NOT_ALLOWED,
            resolveTechMessage(ErrorCode.GENERIC_ERROR, req),
            details
        );
    }

    /**
     * Maneja peticiones con tipo de contenido no soportado por el servidor.
     *
     * @param req solicitud HTTP que originó la excepción
     * @param ex excepción que indica que el tipo de contenido de la solicitud no es compatible con lo que el servidor puede procesar, como enviar JSON a un endpoint que solo acepta XML
     * @return {@link ResponseEntity} con estado 415 y cuerpo estandarizado de error indicando que el tipo de contenido no es soportado
     */
    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<ApiResponse<ErrorResponse<Void>>> handleUnsupportedMediaType(
            HttpServletRequest req, HttpMediaTypeNotSupportedException ex) {

        String userMsg = resolveUserMessage("user.generic.unsupported_media_type", null, req, "Tipo de contenido no soportado.");
        String techDetail = resolveTechMessage(ErrorCode.UNSUPPORTED_MEDIA_TYPE, req);

        var details = ErrorUtils.of(
            ErrorCode.UNSUPPORTED_MEDIA_TYPE.getCode(),
            techDetail,
            userMsg,
            req.getMethod()
        );

        return ResponseUtil.error(
            req,
            ErrorCode.GENERIC_ERROR.getCode(),
            HttpStatus.UNSUPPORTED_MEDIA_TYPE,
            resolveTechMessage(ErrorCode.GENERIC_ERROR, req),
            details
        );
    }

    /**
     * Maneja solicitudes cuyo formato de respuesta solicitado por el cliente no es aceptable.
     *
     * @param req solicitud HTTP que originó la excepción
     * @param ex excepción que indica que el formato de respuesta solicitado por el cliente (a través del encabezado Accept) no es compatible con lo que el servidor puede producir, como solicitar XML cuando el servidor solo puede producir JSON
     * @return {@link ResponseEntity} con estado 406 y cuerpo estandarizado de error indicando que el formato solicitado no es aceptable
     */
    @ExceptionHandler(HttpMediaTypeNotAcceptableException.class)
    public ResponseEntity<ApiResponse<ErrorResponse<Void>>> handleNotAcceptable(
            HttpServletRequest req, HttpMediaTypeNotAcceptableException ex) {

        String userMsg = resolveUserMessage("user.generic.not_acceptable", null, req, "Formato no aceptable.");
        String techDetail = resolveTechMessage(ErrorCode.NOT_ACCEPTABLE, req);

        var details = ErrorUtils.of(
            ErrorCode.NOT_ACCEPTABLE.getCode(),
            techDetail,
            userMsg,
            req.getMethod()
        );

        return ResponseUtil.error(
            req,
            ErrorCode.GENERIC_ERROR.getCode(),
            HttpStatus.NOT_ACCEPTABLE,
            resolveTechMessage(ErrorCode.GENERIC_ERROR, req),
            details
        );
    }

    /**
     * Maneja la ausencia de beans en el contexto de Spring.
     *
     * @param req solicitud HTTP que originó la excepción
     * @param ex excepción que indica que el contenedor de Spring no pudo encontrar un bean requerido para procesar la solicitud, lo que generalmente indica un error de configuración o una dependencia faltante
     * @return {@link ResponseEntity} con estado 500 y cuerpo estandarizado de error indicando un problema de configuración interna del servidor
     */
    @ExceptionHandler(NoSuchBeanDefinitionException.class)
    public ResponseEntity<ApiResponse<ErrorResponse<Void>>> handleNoBean(
            HttpServletRequest req, NoSuchBeanDefinitionException ex) {

        String userMsg = resolveUserMessage("user.generic.config_error", null, req, "Error de configuración.");
        String techDetail = resolveTechMessage(ErrorCode.MAPPER_ERROR, req);

        var details = ErrorUtils.of(
            ErrorCode.MAPPER_ERROR.getCode(),
            techDetail,
            userMsg,
            req.getMethod()
        );

        return ResponseUtil.error(
            req,
            ErrorCode.MAPPER_ERROR.getCode(),
            HttpStatus.INTERNAL_SERVER_ERROR,
            resolveTechMessage(ErrorCode.MAPPER_ERROR, req),
            details
        );
    }

    /**
     * Handler genérico para cualquier excepción no controlada.
     *
     * @param req solicitud HTTP que originó la excepción
     * @param ex excepción no controlada que se propagó hasta el controlador
     * @return {@link ResponseEntity} con estado 500 y cuerpo estandarizado de error indicando un error interno del servidor
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<ErrorResponse<Void>>> handleGeneric(
            HttpServletRequest req, Exception ex) {

        System.out.println("\n\n[ERROR] Excepción no controlada capturada por RestApiExceptionHandler:");
        ex.printStackTrace();

        String userMsg = resolveUserMessage("user.generic.error", null, req, "Error inesperado.");

        var details = ErrorUtils.of(
            ErrorCode.GENERIC_ERROR.getCode(),
            ex.getMessage(),
            userMsg,
            req.getMethod()
        );

        return ResponseUtil.error(
            req,
            ErrorCode.GENERIC_ERROR.getCode(),
            HttpStatus.INTERNAL_SERVER_ERROR,
            resolveTechMessage(ErrorCode.GENERIC_ERROR, req),
            details
        );
    }
}

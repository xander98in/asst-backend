package com.unicuaca.asst.unicauca_asst.common.exceptions.structure;

import java.util.Map;

/**
 * Utilidad para crear instancias de {@link ErrorResponse} de forma centralizada y estandarizada.
 * Esta clase contiene métodos estáticos que facilitan la construcción de errores formateados
 * para ser devueltos desde controladores o manejadores globales de excepciones.
 */
public final class ErrorUtils {

    /** Clase de utilidades; evita instanciación. */
    private ErrorUtils() {}

    // ---------- Helpers internos ----------

    /**
     * Formatea el mensaje usando la plantilla del {@link ErrorCode} si se proveen parámetros.
     * Si no hay parámetros, retorna la plantilla tal cual.
     *
     * @param error  código de error cuya plantilla se utilizará como base del mensaje
     * @param params parámetros opcionales para interpolar en la plantilla del mensaje
     * @return el mensaje formateado si hay parámetros, o la plantilla original en caso contrario
     */
    private static String formatMessage(ErrorCode error, Object... params) {
        String template = error.getMessageKey();
        return (params != null && params.length > 0) ? String.format(template, params) : template;
    }

    // ---------- Fábricas genéricas ----------

    /**
     * Crea un {@link ErrorResponse} simple (sin datos adicionales).
     *
     * @param error       código de error de la aplicación
     * @param userMessage mensaje amigable para el usuario final
     * @param method      método HTTP que provocó el error (GET/POST/PUT/DELETE)
     * @param params      parámetros opcionales para formatear la plantilla del mensaje técnico
     * @return instancia de {@link ErrorResponse} sin datos adicionales
     */
    public static ErrorResponse<Void> createSimpleError(ErrorCode error, String userMessage, String method, Object... params) {
        return ErrorResponse.<Void>builder()
            .errorCode(error.getCode())
            .message(formatMessage(error, params))
            .userMessage(userMessage)
            .method(method)
            .build();
    }

    /**
     * Sobrecarga de {@link #createSimpleError} que usa el mensaje del {@link ErrorCode} como mensaje de usuario.
     *
     * @param error  código de error de la aplicación
     * @param method método HTTP que provocó el error
     * @param params parámetros opcionales para formatear la plantilla del mensaje técnico
     * @return instancia de {@link ErrorResponse} sin datos adicionales
     */
    public static ErrorResponse<Void> createSimpleError(ErrorCode error, String method, Object... params) {
        return createSimpleError(error, error.getMessageKey(), method, params);
    }

    /**
     * Crea un {@link ErrorResponse} genérico con datos adicionales.
     *
     * @param <T>         tipo de los datos adicionales incluidos en la respuesta
     * @param error       código de error de la aplicación
     * @param userMessage mensaje amigable para el usuario final
     * @param method      método HTTP que provocó el error
     * @param data        datos adicionales
     * @param params      parámetros opcionales para formatear la plantilla del mensaje técnico
     * @return instancia de {@link ErrorResponse} con datos adicionales
     */
    public static <T> ErrorResponse<T> createError(ErrorCode error, String userMessage, String method, T data, Object... params) {
        return ErrorResponse.<T>builder()
            .errorCode(error.getCode())
            .message(formatMessage(error, params))
            .userMessage(userMessage)
            .method(method)
            .data(data)
            .build();
    }

    /**
     * Sobrecarga de {@link #createError} que usa el mensaje del {@link ErrorCode} como mensaje de usuario.
     *
     * @param <T>    tipo de los datos adicionales incluidos en la respuesta
     * @param error  código de error de la aplicación
     * @param method método HTTP que provocó el error
     * @param data   datos adicionales
     * @param params parámetros opcionales para formatear la plantilla del mensaje técnico
     * @return instancia de {@link ErrorResponse} con datos adicionales
     */
    public static <T> ErrorResponse<T> createError(ErrorCode error, String method, T data, Object... params) {
        return createError(error, error.getMessageKey(), method, data, params);
    }

    // ---------- Atajos frecuentes ----------

    /**
     * Atajo para errores de validación por campos.
     *
     * @param fieldErrors mapa de campo a mensaje de error con los errores de validación
     * @param method      método HTTP que provocó el error
     * @return instancia de {@link ErrorResponse} con el detalle de campos inválidos
     */
    public static ErrorResponse<Map<String, String>> fieldValidation(Map<String, String> fieldErrors, String method) {
        return createError(ErrorCode.FIELD_VALIDATION, ErrorCode.FIELD_VALIDATION.getMessageKey(), method, fieldErrors);
    }

    /**
     * Crea un error a partir de un código y mensaje arbitrarios.
     *
     * @param errorCode   código de error a incluir en la respuesta
     * @param message     mensaje técnico del error
     * @param userMessage mensaje amigable destinado al usuario final
     * @param method      método HTTP que provocó el error
     * @return instancia de {@link ErrorResponse} sin datos adicionales
     */
    public static ErrorResponse<Void> of(String errorCode, String message, String userMessage, String method) {
        return ErrorResponse.<Void>builder()
            .errorCode(errorCode)
            .message(message)
            .userMessage(userMessage)
            .method(method)
            .build();
    }
}

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
     */
    private static String formatMessage(ErrorCode error, Object... params) {
        String template = error.getMessageKey();
        return (params != null && params.length > 0) ? String.format(template, params) : template;
    }

    // ---------- Fábricas genéricas ----------

    /**
     * Crea un {@link ErrorResponse} simple (sin datos adicionales).
     *
     * @param error  código de error de la aplicación.
     * @param method método HTTP que provocó el error (GET/POST/PUT/DELETE).
     * @param params parámetros opcionales para formatear la plantilla del mensaje.
     */
    public static ErrorResponse<Void> createSimpleError(ErrorCode error, String method, Object... params) {
        return ErrorResponse.<Void>builder()
            .errorCode(error.getCode())
            .message(formatMessage(error, params))
            .method(method)
            .build();
    }

    /**
     * Crea un {@link ErrorResponse} genérico con datos adicionales (por ejemplo, mapa de errores de validación).
     *
     * @param error  código de error de la aplicación.
     * @param method método HTTP que provocó el error.
     * @param data   datos adicionales (p. ej., Map&lt;String, String&gt; con errores por campo).
     * @param params parámetros opcionales para formatear la plantilla del mensaje.
     */
    public static <T> ErrorResponse<T> createError(ErrorCode error, String method, T data, Object... params) {
        return ErrorResponse.<T>builder()
            .errorCode(error.getCode())
            .message(formatMessage(error, params))
            .method(method)
            .data(data)
            .build();
    }

    // ---------- Atajos frecuentes ----------

    /**
     * Atajo para errores de validación por campos.
     *
     * @param fieldErrors mapa campo → mensaje de error.
     * @param method      método HTTP de la solicitud.
     */
    public static ErrorResponse<Map<String, String>> fieldValidation(Map<String, String> fieldErrors, String method) {
        return createError(ErrorCode.FIELD_VALIDATION, method, fieldErrors);
    }

    /**
     * Crea un error a partir de un código y mensaje arbitrarios (cuando no hay un {@link ErrorCode} adecuado).
     */
    public static ErrorResponse<Void> of(String errorCode, String message, String method) {
        return ErrorResponse.<Void>builder()
            .errorCode(errorCode)
            .message(message)
            .method(method)
            .build();
    }
}

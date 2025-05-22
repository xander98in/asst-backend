package com.unicuaca.asst.unicauca_asst.common.exceptions.structure;

/**
 * Utilidad para crear instancias de {@link ErrorResponse} de forma centralizada y estandarizada.
 * Esta clase contiene métodos estáticos que facilitan la construcción de errores formateados
 * para ser devueltos desde controladores o manejadores globales de excepciones.
 */
public final class ErrorUtils {

    // Constructor privado para prevenir instanciación
    private ErrorUtils() {
    }

    /**
     * Crea un error con datos adicionales opcionales (por ejemplo: errores de validación).
     *
     * @param <T> tipo de datos adicionales (usualmente Map<String, String> o un DTO)
     * @param errorCode código de error definido por la app
     * @param message mensaje descriptivo
     * @param httpStatus código HTTP asociado
     * @return un {@link ErrorResponse<T>} genérico
     */
    public static <T> ErrorResponse<T> createError(final String errorCode, final String message, final Integer httpStatus) {
        return ErrorResponse.<T>builder()
            .errorCode(errorCode)
            .message(message)
            .httpStatus(httpStatus)
            .build();
    }

    /**
     * Crea un error simple sin campo `data`. Útil para errores 404, 403, 500, etc.
     *
     * @param errorCode código de error definido por la app
     * @param message mensaje descriptivo
     * @param httpStatus código HTTP asociado
     * @return un {@link ErrorResponse<Void>} sin campo `data`
     */
    public static ErrorResponse<Void> createSimpleError(final String errorCode, final String message, final Integer httpStatus) {
        return createError(errorCode, message, httpStatus);
    }
}

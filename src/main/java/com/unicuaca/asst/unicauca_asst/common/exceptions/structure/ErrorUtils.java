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
     * Crea un nuevo objeto {@link ErrorResponse} con los campos básicos.
     * 
     * @param errorCode   código de error (ej: BAT-BUS-001)
     * @param messageKey  mensaje del error (ej: "entity.not.found")
     * @param httpStatus  código HTTP correspondiente (ej: 404, 500)
     * @return una instancia de {@link ErrorResponse} completamente construida
     */
    public static ErrorResponse createError(final String errorCode, final String messageKey, final Integer httpStatus) {
        return ErrorResponse.builder()
            .errorCode(errorCode)
            .message(messageKey)
            .httpStatus(httpStatus)
            .build();
    }

}

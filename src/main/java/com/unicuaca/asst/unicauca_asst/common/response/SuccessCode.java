package com.unicuaca.asst.unicauca_asst.common.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Enum que define los códigos y mensajes genéricos de éxito
 * utilizados en las respuestas del sistema ASST.
 *
 * <p>Permite estandarizar las respuestas exitosas de la API,
 * facilitando la trazabilidad y consistencia entre módulos.</p>
 */
@Getter
@RequiredArgsConstructor
public enum SuccessCode {

    /** Recurso creado exitosamente. */
    CREATED("ASST-0001", "Recurso creado exitosamente."),

    /** Recurso obtenido exitosamente. */
    RETRIEVED("ASST-0002", "Recurso obtenido exitosamente."),

    /** Recurso actualizado exitosamente. */
    UPDATED("ASST-0003", "Recurso actualizado exitosamente."),

    /** Recurso eliminado exitosamente. */
    DELETED("ASST-0004", "Recurso eliminado exitosamente."),

    /** Operación general realizada con éxito. */
    OPERATION_COMPLETED("ASST-0005", "Operación realizada con éxito.");

    /** Código de éxito asociado a la operación. */
    private final String code;

    /** Mensaje descriptivo del resultado exitoso. */
    private final String message;
}

package com.unicuaca.asst.unicauca_asst.common.exceptions.structure;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    /**
     * Error genérico no clasificado.
     */
    GENERIC_ERROR("ASST-0001", "ERROR GENERICO"),

    /**
     * La entidad que se intenta crear ya existe.
     */
    ENTITY_ALREADY_EXISTS("ASST-0002", "ERROR ENTIDAD YA EXISTE"),

    /**
     * La entidad solicitada no fue encontrada.
     */
    ENTITY_NOT_FOUND("ASST-0003", "Entidad no encontrada"),

    /**
     * Se ha violado una regla de negocio definida por el sistema.
     */
    BUSINESS_RULE_VIOLATION("ASST-0004", "Regla de negocio violada"),

    /**
     * Error al autenticar, credenciales inválidas.
     */
    INVALID_CREDENTIALS("ASST-0005", "Error al iniciar sesión, compruebe sus credenciales y vuelva a intentarlo"),

    /**
     * El usuario existe pero no está habilitado o verificado.
     */
    USER_DISABLED("ASST-0006", "El usuario no ha sido verificado, por favor revise su correo para verificar su cuenta");

    /**
     * Código único del error (para trazabilidad y estandarización).
     */
    private final String code;

    /**
     * Mensaje clave o descripción estándar del error.
     * Puede ser traducido o utilizado como plantilla de respuesta.
     */
    private final String messageKey;
}

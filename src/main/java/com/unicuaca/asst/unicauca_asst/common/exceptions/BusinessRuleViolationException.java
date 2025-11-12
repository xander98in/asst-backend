package com.unicuaca.asst.unicauca_asst.common.exceptions;

import lombok.Getter;

/**
 * Excepción para indicar la violación de una regla de negocio.
 * Suele mapear a 400 Bad Request.
 */
@Getter
public class BusinessRuleViolationException extends RuntimeException {

    /** Código funcional del error (ASST-CAT-0001, etc.). */
    private final String code;
    /** Mensaje legible del error. */
    private final String message;

    /**
     * Constructor que inicializa la excepción con un mensaje personalizado.
     *
     * @param message el mensaje descriptivo del error
     */
    public BusinessRuleViolationException(String code, String message) {
        this.code = code;
        this.message = message;
    }
}

package com.unicuaca.asst.unicauca_asst.common.exceptions;

import lombok.Getter;

/**
 * Excepción lanzada cuando se viola una regla de negocio definida en el dominio.
 *
 * <p>Suele mapear a HTTP 400 Bad Request. Es utilizada por los servicios de dominio
 * para señalar condiciones funcionales que impiden completar la operación solicitada.</p>
 */
@Getter
public class BusinessRuleViolationException extends RuntimeException {

    /** Código funcional del error (ASST-CAT-0001, etc.). */
    private final String code;
    /** Mensaje legible del error. */
    private final String message;
    /** Mensaje amigable o clave de traducción destinado al usuario final. */
    private final String userMessage;
    /** Argumentos para el mensaje dinámico. */
    private final Object[] args;

    /**
     * Constructor que inicializa la excepción con un código y mensaje personalizado.
     *
     * @param code    código de error específico para trazabilidad
     * @param message el mensaje descriptivo del error
     */
    public BusinessRuleViolationException(String code, String message) {
        super(message);
        this.code = code;
        this.message = message;
        this.userMessage = null;
        this.args = null;
    }

    /**
     * Constructor que inicializa la excepción con un código, mensaje técnico y mensaje de usuario.
     *
     * @param code        código de error específico para trazabilidad
     * @param message     mensaje técnico del error
     * @param userMessage mensaje amigable destinado al usuario final
     */
    public BusinessRuleViolationException(String code, String message, String userMessage) {
        super(message);
        this.code = code;
        this.message = message;
        this.userMessage = userMessage;
        this.args = null;
    }

    /**
     * Constructor completo para soporte de i18n con argumentos.
     *
     * @param code        código de error
     * @param message     mensaje técnico
     * @param userMessage clave de traducción
     * @param args        argumentos dinámicos
     */
    public BusinessRuleViolationException(String code, String message, String userMessage, Object[] args) {
        super(message);
        this.code = code;
        this.message = message;
        this.userMessage = userMessage;
        this.args = args;
    }
}

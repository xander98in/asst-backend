package com.unicuaca.asst.unicauca_asst.common.exceptions;

import com.unicuaca.asst.unicauca_asst.common.exceptions.structure.ErrorCode;

import lombok.Getter;

/**
 * Excepción lanzada cuando falla la verificación del token de Google OAuth.
 * Indica que el token proporcionado no es válido, ha expirado o no pudo verificarse.
 */
@Getter
public class GoogleTokenException extends RuntimeException {

    /** Código funcional del error. */
    private final String code;
    /** Mensaje legible del error. */
    private final String message;
    /** Mensaje amigable o clave de traducción destinado al usuario final. */
    private final String userMessage;
    /** Argumentos para el mensaje dinámico. */
    private final Object[] args;

    /**
     * Constructor que permite lanzar la excepción con un código de error específico.
     *
     * @param code código de error definido en {@link ErrorCode}
     */
    public GoogleTokenException(ErrorCode code) {
        super(code.getMessageKey());
        this.code = code.getCode();
        this.message = code.getMessageKey();
        this.userMessage = null;
        this.args = null;
    }

    /**
     * Constructor que permite lanzar la excepción con un código y mensaje personalizados.
     *
     * @param code    código personalizado para la excepción
     * @param message mensaje técnico para la excepción
     */
    public GoogleTokenException(String code, String message) {
        super(message);
        this.code = code;
        this.message = message;
        this.userMessage = null;
        this.args = null;
    }

    /**
     * Constructor que permite lanzar la excepción con un código, mensaje técnico y mensaje de usuario.
     *
     * @param code        código personalizado para la excepción
     * @param message     mensaje técnico de la excepción
     * @param userMessage mensaje amigable o clave de traducción
     */
    public GoogleTokenException(String code, String message, String userMessage) {
        super(message);
        this.code = code;
        this.message = message;
        this.userMessage = userMessage;
        this.args = null;
    }

    /**
     * Constructor completo con soporte para argumentos dinámicos.
     *
     * @param code        código personalizado
     * @param message     mensaje técnico
     * @param userMessage clave de traducción
     * @param args        argumentos para el mensaje
     */
    public GoogleTokenException(String code, String message, String userMessage, Object[] args) {
        super(message);
        this.code = code;
        this.message = message;
        this.userMessage = userMessage;
        this.args = args;
    }
}

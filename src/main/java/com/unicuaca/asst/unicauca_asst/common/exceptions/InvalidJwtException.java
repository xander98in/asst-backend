package com.unicuaca.asst.unicauca_asst.common.exceptions;

import com.unicuaca.asst.unicauca_asst.common.exceptions.structure.ErrorCode;

import lombok.Getter;

/**
 * Excepción lanzada cuando falla la validación del JWT propio de la aplicación.
 * Indica que el token ha expirado, tiene una firma inválida, está malformado o no fue proporcionado.
 */
@Getter
public class InvalidJwtException extends RuntimeException {

    /** Código funcional del error. */
    private final String code;
    /** Mensaje legible del error. */
    private final String message;
    /** Mensaje amigable o clave de traducción destinado al usuario final. */
    private final String userMessage;
    /** Argumentos para el mensaje dinámico. */
    private final Object[] args;

    public InvalidJwtException(ErrorCode code) {
        super(code.getMessageKey());
        this.code = code.getCode();
        this.message = code.getMessageKey();
        this.userMessage = null;
        this.args = null;
    }

    public InvalidJwtException(String code, String message) {
        super(message);
        this.code = code;
        this.message = message;
        this.userMessage = null;
        this.args = null;
    }

    public InvalidJwtException(String code, String message, String userMessage) {
        super(message);
        this.code = code;
        this.message = message;
        this.userMessage = userMessage;
        this.args = null;
    }

    public InvalidJwtException(String code, String message, String userMessage, Object[] args) {
        super(message);
        this.code = code;
        this.message = message;
        this.userMessage = userMessage;
        this.args = args;
    }
}

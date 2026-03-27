package com.unicuaca.asst.unicauca_asst.common.exceptions;

import com.unicuaca.asst.unicauca_asst.common.exceptions.structure.ErrorCode;
import lombok.Getter;

/**
 * Excepción lanzada cuando falla la creación de una entidad.
 */
@Getter
public class EntityCreationException extends RuntimeException {

    /** Código funcional del error. */
    private final String code;
    /** Mensaje técnico legible. */
    private final String message;
    /** Clave de traducción o mensaje amigable para el usuario. */
    private final String userMessage;
    /** Argumentos para mensajes dinámicos. */
    private final Object[] args;

    /**
     * Constructor con ErrorCode y mensaje técnico.
     * @param code código de error
     * @param message mensaje técnico
     */
    public EntityCreationException(ErrorCode code, String message) {
        super(message);
        this.code = code.getCode();
        this.message = code.getMessageKey();
        this.userMessage = null;
        this.args = null;
    }

    /**
     * Constructor con mensaje técnico genérico.
     * @param message mensaje detallado del error
     */
    public EntityCreationException(String message) {
        super(message);
        this.code = ErrorCode.ENTITY_CREATION_ERROR.getCode();
        this.message = ErrorCode.ENTITY_CREATION_ERROR.getMessageKey();
        this.userMessage = null;
        this.args = null;
    }

    /**
     * Constructor con código y mensaje técnico personalizados.
     * @param code código personalizado
     * @param message mensaje técnico
     */
    public EntityCreationException(String code, String message) {
        super(message);
        this.code = code;
        this.message = message;
        this.userMessage = null;
        this.args = null;
    }

    /**
     * Constructor con código, mensaje técnico y mensaje de usuario.
     * @param code código de error
     * @param message mensaje técnico
     * @param userMessage mensaje para el usuario
     */
    public EntityCreationException(String code, String message, String userMessage) {
        super(message);
        this.code = code;
        this.message = message;
        this.userMessage = userMessage;
        this.args = null;
    }

    /**
     * Constructor completo para soporte de i18n con argumentos.
     * @param code código de error
     * @param message mensaje técnico
     * @param userMessage clave de traducción
     * @param args argumentos dinámicos
     */
    public EntityCreationException(String code, String message, String userMessage, Object[] args) {
        super(message);
        this.code = code;
        this.message = message;
        this.userMessage = userMessage;
        this.args = args;
    }
}

package com.unicuaca.asst.unicauca_asst.common.exceptions;

import com.unicuaca.asst.unicauca_asst.common.exceptions.structure.ErrorCode;
import lombok.Getter;

/**
 * Excepción lanzada cuando un catálogo solicitado no contiene elementos.
 */
@Getter
public class CatalogEmptyException extends RuntimeException {

    /** Código funcional del error. */
    private final String code;
    /** Mensaje técnico legible. */
    private final String message;
    /** Clave de traducción o mensaje amigable para el usuario. */
    private final String userMessage;
    /** Argumentos para mensajes dinámicos. */
    private final Object[] args;

    /**
     * Constructor con mensaje técnico genérico.
     * @param message mensaje detallado del error
     */
    public CatalogEmptyException(String message) {
        super(message);
        this.code = ErrorCode.CATALOG_ERROR.getCode();
        this.message = message;
        this.userMessage = null;
        this.args = null;
    }

    /**
     * Constructor con código y mensaje técnico personalizados.
     * @param code código personalizado
     * @param message mensaje técnico
     */
    public CatalogEmptyException(String code, String message) {
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
    public CatalogEmptyException(String code, String message, String userMessage) {
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
    public CatalogEmptyException(String code, String message, String userMessage, Object[] args) {
        super(message);
        this.code = code;
        this.message = message;
        this.userMessage = userMessage;
        this.args = args;
    }
}

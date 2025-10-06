package com.unicuaca.asst.unicauca_asst.common.exceptions;

import com.unicuaca.asst.unicauca_asst.common.exceptions.structure.ErrorCode;

import lombok.Getter;

/**
 * Excepción lanzada cuando se intenta crear una entidad que ya existe en el
 * sistema.
 */
@Getter
public class EntityAlreadyExistsException extends RuntimeException {

    /** Código funcional del error (ASST-CAT-0001, etc.). */
    private final String code;
    /** Mensaje legible del error. */
    private final String message;

    /**
     * Constructor que recibe un {@link ErrorCode} predefinido y un mensaje
     * personalizado.
     *
     * @param code    código de error predefinido
     * @param message mensaje detallado del error
     */
    public EntityAlreadyExistsException(ErrorCode code, String message) {
        super(message);
        this.code = code.getCode();
        this.message = code.getMessageKey();
    }

    /**
     * Constructor que recibe un mensaje personalizado, utilizando el código y
     * la llave de mensaje del {@link ErrorCode} predefinido.
     *
     * @param message mensaje detallado del error
     */
    public EntityAlreadyExistsException(String message) {
        super(message);
        this.code = ErrorCode.ENTITY_ALREADY_EXISTS.getCode();
        this.message = ErrorCode.ENTITY_ALREADY_EXISTS.getMessageKey();
    }

    /**
     * Constructor que recibe un código y un mensaje personalizados.
     *
     * @param code    código de error personalizado
     * @param message mensaje detallado del error
     */
    public  EntityAlreadyExistsException(String code, String message) {
        super(message);
        this.code = code;
        this.message = message;
    }

}

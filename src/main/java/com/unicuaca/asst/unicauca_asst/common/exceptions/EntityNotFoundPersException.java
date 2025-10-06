package com.unicuaca.asst.unicauca_asst.common.exceptions;

import com.unicuaca.asst.unicauca_asst.common.exceptions.structure.ErrorCode;

import lombok.Getter;

/**
 * Excepción lanzada cuando una entidad solicitada no existe en el sistema.
 * Es utilizada comúnmente por los casos de uso o servicios de dominio para indicar
 * la ausencia de un recurso solicitado (por ejemplo, una persona, usuario o entidad).
 */
@Getter
public class EntityNotFoundPersException extends RuntimeException {

    /** Código funcional del error (ASST-CAT-0001, etc.). */
    private final String code;
    /** Mensaje legible del error. */
    private final String message;
    
    /**
     * Constructor que permite lanzar la excepción con un código de error específico.
     *
     * @param code código de error definido en {@link ErrorCode}
     */
    public EntityNotFoundPersException(ErrorCode code) {
        super(code.getMessageKey());
        this.code = code.getCode();
        this.message = code.getMessageKey();
    }

    /**
     * Constructor que permite lanzar la excepción con un mensaje personalizado.
     *
     * @param message mensaje personalizado para la excepción
     */
    public EntityNotFoundPersException(String message) {
        super(message);
        this.code = ErrorCode.ENTITY_NOT_FOUND.getCode();
        this.message = ErrorCode.ENTITY_NOT_FOUND.getMessageKey();
    }

    /**
     * Constructor que permite lanzar la excepción con un código y mensaje personalizados.
     *
     * @param code    código personalizado para la excepción
     * @param message mensaje personalizado para la excepción
     */
    public EntityNotFoundPersException(String code, String message) {
        super(message);
        this.code = code;
        this.message = message;
    }
}

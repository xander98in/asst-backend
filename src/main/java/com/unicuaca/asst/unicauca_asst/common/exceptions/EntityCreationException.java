package com.unicuaca.asst.unicauca_asst.common.exceptions;

import com.unicuaca.asst.unicauca_asst.common.exceptions.structure.ErrorCode;

import lombok.Getter;

/**
 * Excepción lanzada cuando ocurre un error durante la creación de una entidad.
 *
 * <p>Incluye un código de error y una clave de mensaje definidos en {@link ErrorCode},
 * para facilitar la estandarización y trazabilidad de errores.</p>
 */
@Getter
public class EntityCreationException extends RuntimeException {

    private final String code;
    private final String messageKey;

    /**
     * Construye una excepción con un {@link ErrorCode} específico y un mensaje personalizado.
     *
     * @param code    código y clave del error
     * @param message mensaje descriptivo del error
     */
    public EntityCreationException(ErrorCode code, String message) {
        super(message);
        this.code = code.getCode();
        this.messageKey = code.getMessageKey();
    }

    /**
     * Construye una excepción con mensaje personalizado y código {@code ENTITY_CREATION_ERROR} por defecto.
     *
     * @param message mensaje descriptivo del error
     */
    public EntityCreationException(String message) {
        super(message);
        this.code = ErrorCode.ENTITY_CREATION_ERROR.getCode();
        this.messageKey = ErrorCode.ENTITY_CREATION_ERROR.getMessageKey();
    }
}

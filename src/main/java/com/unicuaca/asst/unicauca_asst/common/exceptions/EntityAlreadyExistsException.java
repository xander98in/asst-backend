package com.unicuaca.asst.unicauca_asst.common.exceptions;

import com.unicuaca.asst.unicauca_asst.common.exceptions.structure.ErrorCode;

import lombok.Getter;

@Getter
public class EntityAlreadyExistsException extends RuntimeException {

    /**
     * Código de error único, estructurado según convención (ej: BAT-BUS-001).
     */
    private final String code;

    /**
     * Clave de mensaje estándar para este tipo de error (ej: entity.not.found).
     * Puede ser usada para traducción o respuestas uniformes.
     */
    private final String messageKey;
    

    /**
     * Constructor que recibe un {@link ErrorCode} predefinido, el cual contiene
     * el código y la llave de mensaje asociados al error.
     *
     * @param code objeto {@link ErrorCode} con la información del error
     */
    public EntityAlreadyExistsException(ErrorCode code) {
        super(code.getMessageKey());
        this.code = code.getCode();
        this.messageKey = code.getMessageKey();
    }

    /**
     * Constructor que permite lanzar la excepción con un mensaje personalizado,
     * utilizando por defecto el código de error {@link ErrorCode#ENTITY_NOT_FOUND}.
     *
     * @param message mensaje de error específico
     */
    public EntityAlreadyExistsException(String message) {
        super(message);
        this.code = ErrorCode.ENTITY_NOT_FOUND.getCode();
        this.messageKey = ErrorCode.ENTITY_NOT_FOUND.getMessageKey();
    }

}

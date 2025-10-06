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
    private final String message;

    /**
     * Construye una excepción con un código y mensaje personalizados basados en un {@link ErrorCode}.
     *
     * @param code    el código funcional del error
     * @param message mensaje descriptivo del error
     */
    public EntityCreationException(ErrorCode code, String message) {
        super(message);
        this.code = code.getCode();
        this.message = code.getMessageKey();
    }

    /**
     * Constructor que inicializa la excepción con un mensaje personalizado
     * y utiliza los valores por defecto de {@link ErrorCode#ENTITY_CREATION_ERROR}.
     *
     * @param message el mensaje descriptivo del error
     */
    public EntityCreationException(String message) {
        super(message);
        this.code = ErrorCode.ENTITY_CREATION_ERROR.getCode();
        this.message = ErrorCode.ENTITY_CREATION_ERROR.getMessageKey();
    }

    /**
     * Constructor que inicializa la excepción con un código y mensaje personalizados.
     *
     * @param code el código funcional del error
     * @param message el mensaje descriptivo del error
     */
    public EntityCreationException(String code, String message) {
        super(message);
        this.code = code;
        this.message = message;
    }
}

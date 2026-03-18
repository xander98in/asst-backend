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
    /** Mensaje amigable destinado al usuario final. */
    private final String userMessage;

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
        this.userMessage = null;
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
        this.userMessage = null;
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
        this.userMessage = null;
    }

    /**
     * Constructor que inicializa la excepción con un código, mensaje técnico y mensaje de usuario.
     *
     * @param code        el código funcional del error
     * @param message     el mensaje técnico del error
     * @param userMessage mensaje amigable destinado al usuario final
     */
    public EntityCreationException(String code, String message, String userMessage) {
        super(message);
        this.code = code;
        this.message = message;
        this.userMessage = userMessage;
    }
}

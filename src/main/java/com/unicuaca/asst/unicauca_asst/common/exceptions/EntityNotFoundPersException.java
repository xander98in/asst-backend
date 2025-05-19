package com.unicuaca.asst.unicauca_asst.common.exceptions;

import com.unicuaca.asst.unicauca_asst.common.exceptions.structure.ErrorCode;

import lombok.Getter;


/**
 * Excepción lanzada cuando una entidad solicitada no existe en el sistema.
 * 
 * Esta excepción forma parte del manejo estructurado de errores. Contiene un código
 * de error único y una llave de mensaje estándar definidos en {@link ErrorCode}, los
 * cuales permiten una trazabilidad clara y facilitan la internacionalización del sistema.
 * 
 * Es utilizada comúnmente por los casos de uso o servicios de dominio para indicar
 * la ausencia de un recurso solicitado (por ejemplo, una persona, usuario o entidad).
 */
@Getter
public class EntityNotFoundPersException extends RuntimeException {

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
    public EntityNotFoundPersException(ErrorCode code) {
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
    public EntityNotFoundPersException(String message) {
        super(message);
        this.code = ErrorCode.ENTITY_NOT_FOUND.getCode();
        this.messageKey = ErrorCode.ENTITY_NOT_FOUND.getMessageKey();
    }
}

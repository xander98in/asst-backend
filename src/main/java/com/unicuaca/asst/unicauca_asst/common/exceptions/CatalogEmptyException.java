package com.unicuaca.asst.unicauca_asst.common.exceptions;

import com.unicuaca.asst.unicauca_asst.common.exceptions.structure.ErrorCode;

import lombok.Getter;

/**
 * Excepción para indicar que un catálogo esperado está vacío.
 * Suele mapear a 404 (o 200 con lista vacía en endpoints de listado).
 */
@Getter
public class CatalogEmptyException extends RuntimeException {

    /** Código funcional del error (ASST-CAT-0001, etc.). */
    private final String code;
    /** Mensaje legible del error. */
    private final String message;

    /**
     * Constructor que inicializa la excepción con un código de error predefinido.
     *
     * @param code el código de error que contiene el código y mensaje asociados
     */
    public CatalogEmptyException(ErrorCode code) {
        super(code.getMessageKey());
        this.code = code.getCode();
        this.message = code.getMessageKey();
    }

    /**
     * Constructor que inicializa la excepción con un mensaje personalizado.
     *
     * @param message el mensaje descriptivo del error
     */
    public CatalogEmptyException(String message) {
        super(message);
        this.code = ErrorCode.CATALOG_EMPTY.getCode();
        this.message = ErrorCode.CATALOG_EMPTY.getMessageKey();
    }

    /**
     * Constructor que inicializa la excepción con un código y mensaje personalizados.
     *
     * @param code el código funcional del error
     * @param message el mensaje descriptivo del error
     */
    public CatalogEmptyException(String code, String message) {
        super(message);
        this.code = code;
        this.message = message;
    }
}

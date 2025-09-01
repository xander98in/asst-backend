package com.unicuaca.asst.unicauca_asst.common.exceptions;

import com.unicuaca.asst.unicauca_asst.common.exceptions.structure.ErrorCode;

import lombok.Getter;

@Getter
public class CatalogEmptyException extends RuntimeException {

    private final String code;
    private final String messageKey;

    public CatalogEmptyException(ErrorCode code, String message) {
        super(message);
        this.code = code.getCode();
        this.messageKey = code.getMessageKey();
    }

    public CatalogEmptyException(String message) {
        super(message);
        this.code = ErrorCode.CATALOG_EMPTY.getCode();
        this.messageKey = ErrorCode.CATALOG_EMPTY.getMessageKey();
    }

}

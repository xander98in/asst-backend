package com.unicuaca.asst.unicauca_asst.common.infrastructure.exception;

import org.springframework.stereotype.Service;

import com.unicuaca.asst.unicauca_asst.common.application.output.ResultFormatterOutputPort;
import com.unicuaca.asst.unicauca_asst.common.exceptions.*;
import com.unicuaca.asst.unicauca_asst.common.exceptions.structure.ErrorCode;

/**
 * Implementación del puerto de salida {@link ResultFormatterOutputPort} encargado de lanzar
 * excepciones personalizadas relacionadas con errores de negocio.
 *
 * <p>Esta clase actúa como adaptador de salida en la arquitectura hexagonal y centraliza el
 * lanzamiento de excepciones, permitiendo el soporte de internacionalización (i18n) tanto
 * para mensajes técnicos como para mensajes destinados al usuario final.</p>
 */
@Service
public class ResultFormatterImpl implements ResultFormatterOutputPort {

    // --- Nuevos métodos recomendados (Uso de ErrorCode) ---

    @Override
    public void throwEntityAlreadyExists(ErrorCode errorCode, String userMessageKey, Object... args) {
        throw new EntityAlreadyExistsException(errorCode.getCode(), errorCode.getMessageKey(), userMessageKey, args);
    }

    @Override
    public void throwEntityNotFound(ErrorCode errorCode, String userMessageKey, Object... args) {
        throw new EntityNotFoundPersException(errorCode.getCode(), errorCode.getMessageKey(), userMessageKey, args);
    }

    @Override
    public void throwBusinessRuleViolation(ErrorCode errorCode, String userMessageKey, Object... args) {
        throw new BusinessRuleViolationException(errorCode.getCode(), errorCode.getMessageKey(), userMessageKey, args);
    }

    @Override
    public void throwEntityCreationFailed(ErrorCode errorCode, String userMessageKey, Object... args) {
        throw new EntityCreationException(errorCode.getCode(), errorCode.getMessageKey(), userMessageKey, args);
    }

    @Override
    public void throwCatalogEmptyException(ErrorCode errorCode, String userMessageKey, Object... args) {
        throw new CatalogEmptyException(errorCode.getCode(), errorCode.getMessageKey(), userMessageKey, args);
    }

    // --- Métodos de compatibilidad técnica ---

    @Override
    public void throwEntityAlreadyExists(String message) {
        throw new EntityAlreadyExistsException(message);
    }

    @Override
    public void throwEntityAlreadyExists(String errorCode, String message) {
        throw new EntityAlreadyExistsException(errorCode, message);
    }

    @Override
    public void throwEntityAlreadyExists(String errorCode, String message, String userMessage, Object... args) {
        throw new EntityAlreadyExistsException(errorCode, message, userMessage, args);
    }

    @Override
    public void throwEntityNotFound(String errorCode, String message) {
        throw new EntityNotFoundPersException(errorCode, message);
    }

    @Override
    public void throwEntityNotFound(String errorCode, String message, String userMessage, Object... args) {
        throw new EntityNotFoundPersException(errorCode, message, userMessage, args);
    }

    @Override
    public void throwBusinessRuleViolation(String message) {
        throw new BusinessRuleViolationException(null, message);
    }

    @Override
    public void throwBusinessRuleViolation(String errorCode, String message) {
        throw new BusinessRuleViolationException(errorCode, message);
    }

    @Override
    public void throwBusinessRuleViolation(String errorCode, String message, String userMessage, Object... args) {
        throw new BusinessRuleViolationException(errorCode, message, userMessage, args);
    }

    @Override
    public void throwEntityCreationFailed(String errorCode, String message) {
        throw new EntityCreationException(errorCode, message);
    }

    @Override
    public void throwEntityCreationFailed(String errorCode, String message, String userMessage, Object... args) {
        throw new EntityCreationException(errorCode, message, userMessage, args);
    }

    @Override
    public void throwCatalogEmptyException(String errorCode, String message) {
        throw new CatalogEmptyException(errorCode, message);
    }

    @Override
    public void throwCatalogEmptyException(String errorCode, String message, String userMessage, Object... args) {
        throw new CatalogEmptyException(errorCode, message, userMessage, args);
    }
}

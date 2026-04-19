package com.unicuaca.asst.unicauca_asst.common.infrastructure.adapters.output.exception;

import org.springframework.stereotype.Service;

import com.unicuaca.asst.unicauca_asst.common.domain.ports.output.ResultFormatterOutputPort;
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

    /**
     * Lanza una excepción cuando se detecta que la entidad ya existe, usando ErrorCode.
     *
     * @param errorCode código de error estandarizado
     * @param userMessageKey clave de traducción para el mensaje de usuario
     * @param args argumentos para formatear el mensaje dinámicamente
     * @throws EntityAlreadyExistsException siempre que se invoque
     */
    @Override
    public void throwEntityAlreadyExists(ErrorCode errorCode, String userMessageKey, Object... args) {
        throw new EntityAlreadyExistsException(errorCode.getCode(), errorCode.getMessageKey(), userMessageKey, args);
    }

    /**
     * Lanza una excepción cuando no se encuentra una entidad, usando ErrorCode.
     *
     * @param errorCode código de error estandarizado
     * @param userMessageKey clave de traducción para el mensaje de usuario
     * @param args argumentos dinámicos
     * @throws EntityNotFoundPersException siempre que se invoque
     */
    @Override
    public void throwEntityNotFound(ErrorCode errorCode, String userMessageKey, Object... args) {
        throw new EntityNotFoundPersException(errorCode.getCode(), errorCode.getMessageKey(), userMessageKey, args);
    }

    /**
     * Lanza una excepción cuando se viola una regla de negocio, usando ErrorCode.
     *
     * @param errorCode código de error estandarizado
     * @param userMessageKey clave de traducción para el mensaje de usuario
     * @param args argumentos dinámicos
     * @throws BusinessRuleViolationException siempre que se invoque
     */
    @Override
    public void throwBusinessRuleViolation(ErrorCode errorCode, String userMessageKey, Object... args) {
        throw new BusinessRuleViolationException(errorCode.getCode(), errorCode.getMessageKey(), userMessageKey, args);
    }

    /**
     * Lanza una excepción cuando falla la creación de una entidad, usando ErrorCode.
     *
     * @param errorCode código de error estandarizado
     * @param userMessageKey clave de traducción para el mensaje de usuario
     * @param args argumentos dinámicos
     * @throws EntityCreationException siempre que se invoque
     */
    @Override
    public void throwEntityCreationFailed(ErrorCode errorCode, String userMessageKey, Object... args) {
        throw new EntityCreationException(errorCode.getCode(), errorCode.getMessageKey(), userMessageKey, args);
    }

    /**
     * Lanza una excepción cuando un catálogo está vacío, usando ErrorCode.
     *
     * @param errorCode código de error estandarizado
     * @param userMessageKey clave de traducción para el mensaje de usuario
     * @param args argumentos dinámicos
     * @throws CatalogEmptyException siempre que se invoque
     */
    @Override
    public void throwCatalogEmptyException(ErrorCode errorCode, String userMessageKey, Object... args) {
        throw new CatalogEmptyException(errorCode.getCode(), errorCode.getMessageKey(), userMessageKey, args);
    }

    // --- Métodos de compatibilidad técnica ---

    /**
     * Lanza una excepción cuando se detecta que la entidad ya existe.
     *
     * @param message mensaje técnico explicativo
     * @throws EntityAlreadyExistsException siempre que se invoque
     */
    @Override
    public void throwEntityAlreadyExists(String message) {
        throw new EntityAlreadyExistsException(message);
    }

    /**
     * Lanza una excepción con código específico cuando se detecta duplicidad.
     *
     * @param errorCode código estructurado del error
     * @param message mensaje técnico explicativo
     * @throws EntityAlreadyExistsException siempre que se invoque
     */
    @Override
    public void throwEntityAlreadyExists(String errorCode, String message) {
        throw new EntityAlreadyExistsException(errorCode, message);
    }

    /**
     * Lanza una excepción con soporte completo para i18n manual.
     *
     * @param errorCode código estructurado del error
     * @param message mensaje técnico explicativo
     * @param userMessage clave de traducción para el mensaje de usuario
     * @param args argumentos dinámicos para formatear el mensaje de usuario
     * @throws EntityAlreadyExistsException siempre que se invoque
     */
    @Override
    public void throwEntityAlreadyExists(String errorCode, String message, String userMessage, Object... args) {
        throw new EntityAlreadyExistsException(errorCode, message, userMessage, args);
    }

    /**
     * Lanza una excepción cuando no se encuentra una entidad.
     *
     * @param errorCode código estructurado del error
     * @param message mensaje técnico explicativo
     * @throws EntityNotFoundPersException siempre que se invoque
     */
    @Override
    public void throwEntityNotFound(String errorCode, String message) {
        throw new EntityNotFoundPersException(errorCode, message);
    }

    /**
     * Lanza una excepción cuando no se encuentra una entidad, con soporte i18n manual.
     *
     * @param errorCode código estructurado del error
     * @param message mensaje técnico explicativo
     * @param userMessage clave de traducción para el mensaje de usuario
     * @param args argumentos dinámicos para formatear el mensaje de usuario
     * @throws EntityNotFoundPersException siempre que se invoque
     */
    @Override
    public void throwEntityNotFound(String errorCode, String message, String userMessage, Object... args) {
        throw new EntityNotFoundPersException(errorCode, message, userMessage, args);
    }

    /**
     * Lanza una excepción cuando se viola una regla de negocio.
     *
     * @param message mensaje técnico explicativo
     * @throws BusinessRuleViolationException siempre que se invoque
     */
    @Override
    public void throwBusinessRuleViolation(String message) {
        throw new BusinessRuleViolationException(null, message);
    }

    /**
     * Lanza una excepción cuando se viola una regla de negocio con código específico.
     *
     * @param errorCode código estructurado del error
     * @param message mensaje técnico explicativo
     * @throws BusinessRuleViolationException siempre que se invoque
     */
    @Override
    public void throwBusinessRuleViolation(String errorCode, String message) {
        throw new BusinessRuleViolationException(errorCode, message);
    }

    /**
     * Lanza una excepción cuando se viola una regla de negocio, con soporte i18n manual.
     *
     * @param errorCode código estructurado del error
     * @param message mensaje técnico explicativo
     * @param userMessage clave de traducción para el mensaje de usuario
     * @param args argumentos dinámicos para formatear el mensaje de usuario
     * @throws BusinessRuleViolationException siempre que se invoque
     */
    @Override
    public void throwBusinessRuleViolation(String errorCode, String message, String userMessage, Object... args) {
        throw new BusinessRuleViolationException(errorCode, message, userMessage, args);
    }

    /**
     * Lanza una excepción cuando falla la creación de una entidad.
     *
     * @param errorCode código estructurado del error
     * @param message mensaje técnico explicativo
     * @throws EntityCreationException siempre que se invoque
     */
    @Override
    public void throwEntityCreationFailed(String errorCode, String message) {
        throw new EntityCreationException(errorCode, message);
    }

    /**
     * Lanza una excepción cuando falla la creación de una entidad, con soporte i18n manual.
     *
     * @param errorCode código estructurado del error
     * @param message mensaje técnico explicativo
     * @param userMessage clave de traducción para el mensaje de usuario
     * @param args argumentos dinámicos para formatear el mensaje de usuario
     * @throws EntityCreationException siempre que se invoque
     */
    @Override
    public void throwEntityCreationFailed(String errorCode, String message, String userMessage, Object... args) {
        throw new EntityCreationException(errorCode, message, userMessage, args);
    }

    /**
     * Lanza una excepción cuando un catálogo está vacío.
     *
     * @param errorCode código estructurado del error
     * @param message mensaje técnico explicativo
     * @throws CatalogEmptyException siempre que se invoque
     */
    @Override
    public void throwCatalogEmptyException(String errorCode, String message) {
        throw new CatalogEmptyException(errorCode, message);
    }

    /**
     * Lanza una excepción cuando un catálogo está vacío, con soporte i18n manual.
     *
     * @param errorCode código estructurado del error
     * @param message mensaje técnico explicativo
     * @param userMessage clave de traducción para el mensaje de usuario
     * @param args argumentos dinámicos para formatear el mensaje de usuario
     * @throws CatalogEmptyException siempre que se invoque
     */
    @Override
    public void throwCatalogEmptyException(String errorCode, String message, String userMessage, Object... args) {
        throw new CatalogEmptyException(errorCode, message, userMessage, args);
    }
}

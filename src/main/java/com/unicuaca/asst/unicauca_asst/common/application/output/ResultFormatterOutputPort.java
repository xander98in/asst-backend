package com.unicuaca.asst.unicauca_asst.common.application.output;

import com.unicuaca.asst.unicauca_asst.common.exceptions.structure.ErrorCode;

/**
 * Puerto de salida para formatear o propagar errores de negocio desde los casos de uso.
 *
 * <p>Este contrato debe ser implementado por la infraestructura para lanzar excepciones
 * específicas como "Entidad ya existe", "Entidad no encontrada", o "Regla de negocio violada".
 * Soporta internacionalización (i18n) mediante claves de mensaje y argumentos dinámicos.</p>
 */
public interface ResultFormatterOutputPort {

    /**
     * Lanza una excepción cuando se detecta que la entidad ya existe, usando ErrorCode.
     *
     * @param errorCode código de error estandarizado
     * @param userMessageKey clave de traducción para el mensaje de usuario
     * @param args argumentos para formatear el mensaje dinámicamente
     */
    void throwEntityAlreadyExists(ErrorCode errorCode, String userMessageKey, Object... args);

    /**
     * Lanza una excepción cuando no se encuentra una entidad, usando ErrorCode.
     *
     * @param errorCode código de error estandarizado
     * @param userMessageKey clave de traducción para el mensaje de usuario
     * @param args argumentos dinámicos
     */
    void throwEntityNotFound(ErrorCode errorCode, String userMessageKey, Object... args);

    /**
     * Lanza una excepción cuando se viola una regla de negocio, usando ErrorCode.
     *
     * @param errorCode código de error estandarizado
     * @param userMessageKey clave de traducción para el mensaje de usuario
     * @param args argumentos dinámicos
     */
    void throwBusinessRuleViolation(ErrorCode errorCode, String userMessageKey, Object... args);

    /**
     * Lanza una excepción cuando falla la creación de una entidad, usando ErrorCode.
     *
     * @param errorCode código de error estandarizado
     * @param userMessageKey clave de traducción para el mensaje de usuario
     * @param args argumentos dinámicos
     */
    void throwEntityCreationFailed(ErrorCode errorCode, String userMessageKey, Object... args);

    /**
     * Lanza una excepción cuando un catálogo está vacío, usando ErrorCode.
     *
     * @param errorCode código de error estandarizado
     * @param userMessageKey clave de traducción para el mensaje de usuario
     * @param args argumentos dinámicos
     */
    void throwCatalogEmptyException(ErrorCode errorCode, String userMessageKey, Object... args);

    // --- Métodos de compatibilidad técnica ---

    /**
     * Lanza una excepción cuando se detecta que la entidad ya existe.
     *
     * @param message mensaje técnico explicativo
     */
    void throwEntityAlreadyExists(String message);

    /**
     * Lanza una excepción con código específico cuando se detecta duplicidad.
     *
     * @param errorCode código estructurado del error
     * @param message mensaje técnico explicativo
     */
    void throwEntityAlreadyExists(String errorCode, String message);

    /**
     * Lanza una excepción con soporte completo para i18n manual.
     *
     * @param errorCode código estructurado del error
     * @param message mensaje técnico explicativo
     * @param userMessage clave de traducción para el mensaje de usuario
     * @param args argumentos dinámicos para formatear el mensaje de usuario
     */
    void throwEntityAlreadyExists(String errorCode, String message, String userMessage, Object... args);

    /**
     * Lanza una excepción cuando no se encuentra una entidad.
     *
     * @param errorCode código estructurado del error
     * @param message mensaje técnico explicativo
     */
    void throwEntityNotFound(String errorCode, String message);

    /**
     * Lanza una excepción cuando no se encuentra una entidad, con soporte i18n manual
     *
     * @param errorCode
     * @param message
     * @param userMessage
     * @param args
     */
    void throwEntityNotFound(String errorCode, String message, String userMessage, Object... args);

    /**
     * Lanza una excepción cuando se viola una regla de negocio.
     *
     * @param message mensaje técnico explicativo
     */
    void throwBusinessRuleViolation(String message);

    /**
     * Lanza una excepción cuando se viola una regla de negocio con código específico.
     * @param errorCode código estructurado del error
     * @param message mensaje técnico explicativo
     */
    void throwBusinessRuleViolation(String errorCode, String message);

    /**
     * Lanza una excepción cuando se viola una regla de negocio, con soporte i18n manual.
     *
     * @param errorCode código estructurado del error
     * @param message mensaje técnico explicativo
     * @param userMessage clave de traducción para el mensaje de usuario
     * @param args argumentos dinámicos para formatear el mensaje de usuario
     */
    void throwBusinessRuleViolation(String errorCode, String message, String userMessage, Object... args);

    /**
     * Lanza una excepción cuando falla la creación de una entidad.
     *
     * @param errorCode código estructurado del error
     * @param message mensaje técnico explicativo
     */
    void throwEntityCreationFailed(String errorCode, String message);

    /**
     * Lanza una excepción cuando falla la creación de una entidad, con soporte i18n manual.
     *
     * @param errorCode código estructurado del error
     * @param message mensaje técnico explicativo
     * @param userMessage clave de traducción para el mensaje de usuario
     * @param args argumentos dinámicos para formatear el mensaje de usuario
     */
    void throwEntityCreationFailed(String errorCode, String message, String userMessage, Object... args);

    /**
     * Lanza una excepción cuando un catálogo está vacío.
     *
     * @param errorCode código estructurado del error
     * @param message mensaje técnico explicativo
     */
    void throwCatalogEmptyException(String errorCode, String message);

    /**
     * Lanza una excepción cuando un catálogo está vacío, con soporte i18n manual.
     *
     * @param errorCode código estructurado del error
     * @param message mensaje técnico explicativo
     * @param userMessage clave de traducción para el mensaje de usuario
     * @param args argumentos dinámicos para formatear el mensaje de usuario
     */
    void throwCatalogEmptyException(String errorCode, String message, String userMessage, Object... args);
}

package com.unicuaca.asst.unicauca_asst.common.application.output;

/**
 * Puerto de salida para formatear o propagar errores de negocio desde los casos de uso.
 *
 * Este contrato debe ser implementado por la infraestructura para lanzar excepciones
 * específicas como "Entidad ya existe", "Entidad no encontrada", o "Regla de negocio violada".
 *
 * Se ubica en el paquete common.application.output para permitir su reutilización transversal
 * desde múltiples dominios.
 */
public interface ResultFormatterOutputPort {

    /**
     * Lanza una excepción cuando se detecta que la entidad ya existe.
     *
     * @param message mensaje explicativo del error
     */
    void throwEntityAlreadyExists(String message);

    /**
     * Lanza una excepción cuando no se encuentra una entidad esperada.
     *
     * @param message mensaje explicativo del error
     */
    void throwEntityNotFound(String message);

    /**
     * Lanza una excepción cuando se viola una regla de negocio.
     *
     * @param message mensaje explicativo del error
     */
    void throwBusinessRuleViolation(String message);
}

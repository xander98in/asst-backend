package com.unicuaca.asst.unicauca_asst.common.application.output;

import com.unicuaca.asst.unicauca_asst.common.exceptions.EntityAlreadyExistsException;
import com.unicuaca.asst.unicauca_asst.common.exceptions.EntityCreationException;
import com.unicuaca.asst.unicauca_asst.common.exceptions.structure.ErrorCode;

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
     * Lanza una excepción cuando una entidad que se intenta crear ya existe.
     *
     * @param errorCode código de error específico para trazabilidad
     * @param message mensaje descriptivo del conflicto
     * @throws EntityAlreadyExistsException si la entidad ya está registrada
     */
    void throwEntityAlreadyExists(ErrorCode errorCode, String message);

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

    /**
     * Lanza una excepción cuando falla la creación de una entidad.
     *
     * @param message mensaje descriptivo del error ocurrido durante la creación
     * @throws EntityCreationException excepción que indica un fallo en la creación
     */
    void throwEntityCreationFailed(String message);

    /**
     * Lanza una excepción cuando se intenta acceder a un catálogo vacío.
     *
     * @param errorCode código de error específico para trazabilidad
     * @param message mensaje descriptivo del conflicto
     * @throws CatalogEmptyException si el catálogo está vacío
     */
    void throwCatalogEmptyException(ErrorCode errorCode, String message);
}

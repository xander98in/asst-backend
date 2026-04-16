package com.unicuaca.asst.unicauca_asst.core.reports.domain.ports.output;

import com.unicuaca.asst.unicauca_asst.core.reports.domain.models.AnalysisSpace;

/**
 * Puerto de salida para operaciones de escritura sobre espacios de análisis.
 */
public interface AnalysisSpaceCommandRepository {

    /**
     * Persiste un espacio de análisis (creación o actualización).
     *
     * @param analysisSpace espacio de análisis a guardar
     * @return el espacio de análisis persistido
     */
    AnalysisSpace save(AnalysisSpace analysisSpace);

    /**
     * Agrega una batería a un espacio de análisis.
     *
     * @param spaceId             ID del espacio de análisis
     * @param batteryRecordId     ID del registro de batería
     */
    void addBatteryToSpace(Long spaceId, Long batteryRecordId);

    /**
     * Remueve una batería de un espacio de análisis.
     *
     * @param spaceId             ID del espacio de análisis
     * @param batteryRecordId     ID del registro de batería
     */
    void removeBatteryFromSpace(Long spaceId, Long batteryRecordId);

    /**
     * Elimina un espacio de análisis por su ID (cascade elimina las asociaciones).
     *
     * @param spaceId ID del espacio de análisis
     */
    void deleteById(Long spaceId);

    /**
     * Verifica si existe un espacio con el mismo nombre para el usuario dado.
     *
     * @param name   nombre del espacio
     * @param userId ID del usuario creador
     * @return true si ya existe un espacio con ese nombre para el usuario
     */
    boolean existsByNameAndCreatorUserId(String name, Long userId);

    /**
     * Cuenta la cantidad de espacios de análisis de un usuario.
     *
     * @param userId ID del usuario creador
     * @return cantidad de espacios del usuario
     */
    int countByCreatorUserId(Long userId);
}

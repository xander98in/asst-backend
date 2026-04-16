package com.unicuaca.asst.unicauca_asst.core.reports.domain.ports.input;

import java.util.List;

import com.unicuaca.asst.unicauca_asst.core.reports.domain.models.AnalysisSpace;

/**
 * Puerto de entrada para operaciones de escritura sobre espacios de análisis.
 */
public interface AnalysisSpaceCommandCUInputPort {

    /**
     * Crea un nuevo espacio de análisis.
     *
     * @param name          nombre descriptivo del espacio
     * @param evaluatorId   ID del evaluador asociado al espacio
     * @param creatorUserId ID del usuario que crea el espacio
     * @return el espacio de análisis creado
     */
    AnalysisSpace createAnalysisSpace(String name, Long evaluatorId, Long creatorUserId);

    /**
     * Agrega baterías cerradas a un espacio de análisis existente.
     *
     * @param spaceId          ID del espacio de análisis
     * @param batteryRecordIds IDs de los registros de batería a agregar
     * @param userId           ID del usuario que realiza la operación
     */
    void addBatteriesToSpace(Long spaceId, List<Long> batteryRecordIds, Long userId);

    /**
     * Remueve una batería individual de un espacio de análisis.
     *
     * @param spaceId             ID del espacio de análisis
     * @param batteryRecordId     ID del registro de batería a remover
     * @param userId              ID del usuario que realiza la operación
     */
    void removeBatteryFromSpace(Long spaceId, Long batteryRecordId, Long userId);

    /**
     * Elimina un espacio de análisis completo con todas sus asociaciones.
     *
     * @param spaceId ID del espacio de análisis a eliminar
     * @param userId  ID del usuario que realiza la operación
     */
    void deleteAnalysisSpace(Long spaceId, Long userId);
}

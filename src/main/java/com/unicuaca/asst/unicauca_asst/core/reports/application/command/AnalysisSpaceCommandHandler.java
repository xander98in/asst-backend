package com.unicuaca.asst.unicauca_asst.core.reports.application.command;

import com.unicuaca.asst.unicauca_asst.core.reports.application.dto.request.AnalysisSpaceAddBatteriesRequestDTO;
import com.unicuaca.asst.unicauca_asst.core.reports.application.dto.request.AnalysisSpaceCreateRequestDTO;
import com.unicuaca.asst.unicauca_asst.core.reports.application.dto.response.AnalysisSpaceResponseDTO;

/**
 * Handler de comandos para operaciones de escritura sobre espacios de análisis.
 */
public interface AnalysisSpaceCommandHandler {

    /**
     * Crea un nuevo espacio de análisis.
     *
     * @param request datos de creación del espacio
     * @param userId  ID del usuario autenticado
     * @return DTO con el espacio creado
     */
    AnalysisSpaceResponseDTO createAnalysisSpace(AnalysisSpaceCreateRequestDTO request, Long userId);

    /**
     * Agrega baterías cerradas a un espacio de análisis.
     *
     * @param spaceId ID del espacio de análisis
     * @param request datos con los IDs de baterías a agregar
     * @param userId  ID del usuario autenticado
     */
    void addBatteriesToSpace(Long spaceId, AnalysisSpaceAddBatteriesRequestDTO request, Long userId);

    /**
     * Remueve una batería de un espacio de análisis.
     *
     * @param spaceId         ID del espacio de análisis
     * @param batteryRecordId ID del registro de batería a remover
     * @param userId          ID del usuario autenticado
     */
    void removeBatteryFromSpace(Long spaceId, Long batteryRecordId, Long userId);

    /**
     * Elimina un espacio de análisis completo.
     *
     * @param spaceId ID del espacio de análisis
     * @param userId  ID del usuario autenticado
     */
    void deleteAnalysisSpace(Long spaceId, Long userId);
}

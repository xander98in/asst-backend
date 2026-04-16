package com.unicuaca.asst.unicauca_asst.core.reports.domain.ports.input;

import java.util.List;

import com.unicuaca.asst.unicauca_asst.core.reports.domain.models.AnalysisSpace;

/**
 * Puerto de entrada para operaciones de lectura sobre espacios de análisis.
 */
public interface AnalysisSpaceQueryCUInputPort {

    /**
     * Lista todos los espacios de análisis creados por un usuario.
     *
     * @param userId ID del usuario
     * @return lista de espacios de análisis del usuario
     */
    List<AnalysisSpace> getAnalysisSpacesByUser(Long userId);

    /**
     * Obtiene un espacio de análisis con sus baterías asociadas.
     *
     * @param spaceId ID del espacio de análisis
     * @param userId  ID del usuario que realiza la consulta
     * @return el espacio de análisis con sus baterías
     */
    AnalysisSpace getAnalysisSpaceById(Long spaceId, Long userId);
}

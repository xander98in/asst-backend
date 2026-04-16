package com.unicuaca.asst.unicauca_asst.core.reports.application.query;

import java.util.List;

import com.unicuaca.asst.unicauca_asst.core.reports.application.dto.response.AnalysisSpaceResponseDTO;
import com.unicuaca.asst.unicauca_asst.core.reports.application.dto.response.AnalysisSpaceSummaryResponseDTO;

/**
 * Handler de consultas para operaciones de lectura sobre espacios de análisis.
 */
public interface AnalysisSpaceQueryHandler {

    /**
     * Lista los espacios de análisis del usuario autenticado.
     *
     * @param userId ID del usuario autenticado
     * @return lista de resúmenes de espacios de análisis
     */
    List<AnalysisSpaceSummaryResponseDTO> getAnalysisSpacesByUser(Long userId);

    /**
     * Obtiene el detalle completo de un espacio de análisis.
     *
     * @param spaceId ID del espacio de análisis
     * @param userId  ID del usuario autenticado
     * @return DTO con el espacio y sus baterías
     */
    AnalysisSpaceResponseDTO getAnalysisSpaceById(Long spaceId, Long userId);
}

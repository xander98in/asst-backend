package com.unicuaca.asst.unicauca_asst.core.reports.application.query;

import com.unicuaca.asst.unicauca_asst.core.reports.application.dto.response.DomainsAndDimensionsResponseDTO;
import com.unicuaca.asst.unicauca_asst.core.reports.application.dto.response.GroupSummaryResponseDTO;
import com.unicuaca.asst.unicauca_asst.core.reports.application.dto.response.RiskMatrixResponseDTO;

/**
 * Interfaz del manejador de consultas para informes grupales de riesgo psicosocial.
 */
public interface GroupReportQueryHandler {

    /**
     * Obtiene el resumen general grupal de un espacio de análisis.
     *
     * @param spaceId ID del espacio de análisis
     * @param userId  ID del usuario autenticado
     * @return DTO con el resumen grupal
     */
    GroupSummaryResponseDTO getGroupSummary(Long spaceId, Long userId);

    /**
     * Obtiene el desglose por dominios y dimensiones de un espacio de análisis.
     *
     * @param spaceId ID del espacio de análisis
     * @param userId  ID del usuario autenticado
     * @return DTO con las distribuciones por dominio y dimensión
     */
    DomainsAndDimensionsResponseDTO getDomainsAndDimensions(Long spaceId, Long userId);

    /**
     * Obtiene la matriz de riesgo de un espacio de análisis.
     *
     * @param spaceId ID del espacio de análisis
     * @param userId  ID del usuario autenticado
     * @return DTO con la matriz de riesgo
     */
    RiskMatrixResponseDTO getRiskMatrix(Long spaceId, Long userId);
}

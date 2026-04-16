package com.unicuaca.asst.unicauca_asst.core.reports.domain.ports.input;

import com.unicuaca.asst.unicauca_asst.core.reports.domain.services.GroupReportEngine;

/**
 * Puerto de entrada para la generación de informes grupales de riesgo psicosocial.
 */
public interface GroupReportQueryCUInputPort {

    /**
     * Genera el resumen general grupal de un espacio de análisis.
     *
     * @param spaceId ID del espacio de análisis
     * @param userId  ID del usuario que solicita el informe
     * @return resumen grupal con distribuciones, promedios y nivel por cuestionario
     */
    GroupReportEngine.GroupSummaryResult getGroupSummary(Long spaceId, Long userId);

    /**
     * Genera el desglose por dominios y dimensiones de un espacio de análisis.
     *
     * @param spaceId ID del espacio de análisis
     * @param userId  ID del usuario que solicita el informe
     * @return distribuciones de riesgo por dominio y dimensión
     */
    GroupReportEngine.DomainsAndDimensionsResult getDomainsAndDimensions(Long spaceId, Long userId);

    /**
     * Genera la matriz de riesgo de un espacio de análisis.
     *
     * @param spaceId ID del espacio de análisis
     * @param userId  ID del usuario que solicita el informe
     * @return matriz con magnitud del riesgo, índice de asociación y semáforos
     */
    GroupReportEngine.RiskMatrixResult getRiskMatrix(Long spaceId, Long userId);
}

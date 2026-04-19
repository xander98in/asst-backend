package com.unicuaca.asst.unicauca_asst.core.reports.application.query;

import com.unicuaca.asst.unicauca_asst.core.reports.application.dto.response.DomainsAndDimensionsResponseDTO;
import com.unicuaca.asst.unicauca_asst.core.reports.application.dto.response.GroupSummaryResponseDTO;
import com.unicuaca.asst.unicauca_asst.core.reports.application.dto.response.RiskMatrixResponseDTO;
import com.unicuaca.asst.unicauca_asst.core.reports.application.mappers.GroupReportMapper;
import com.unicuaca.asst.unicauca_asst.core.reports.domain.ports.input.GroupReportQueryCUInputPort;
import com.unicuaca.asst.unicauca_asst.core.reports.domain.services.GroupReportEngine;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementación del manejador de consultas para informes grupales de riesgo psicosocial.
 *
 * <p>Delega al puerto de entrada de dominio y transforma los resultados
 * calculados por {@link GroupReportEngine} a DTOs de respuesta.</p>
 */
@RequiredArgsConstructor
@Component
@Transactional(readOnly = true)
public class GroupReportQueryHandlerImpl implements GroupReportQueryHandler {

    private final GroupReportQueryCUInputPort groupReportQueryCUInputPort;
    private final GroupReportMapper groupReportMapper;

    /**
     * Obtiene el resumen general grupal de un espacio de análisis.
     *
     * @param spaceId ID del espacio de análisis
     * @param userId  ID del usuario autenticado
     * @return DTO con el resumen grupal
     */
    @Override
    public GroupSummaryResponseDTO getGroupSummary(Long spaceId, Long userId) {
        GroupReportEngine.GroupSummaryResult result =
            groupReportQueryCUInputPort.getGroupSummary(spaceId, userId);
        return groupReportMapper.toGroupSummaryDTO(result);
    }

    /**
     * Obtiene el desglose por dominios y dimensiones de un espacio de análisis.
     *
     * @param spaceId ID del espacio de análisis
     * @param userId  ID del usuario autenticado
     * @return DTO con las distribuciones por dominio y dimensión
     */
    @Override
    public DomainsAndDimensionsResponseDTO getDomainsAndDimensions(Long spaceId, Long userId) {
        GroupReportEngine.DomainsAndDimensionsResult result =
            groupReportQueryCUInputPort.getDomainsAndDimensions(spaceId, userId);
        return groupReportMapper.toDomainsAndDimensionsDTO(result);
    }

    /**
     * Obtiene la matriz de riesgo de un espacio de análisis.
     *
     * @param spaceId ID del espacio de análisis
     * @param userId  ID del usuario autenticado
     * @return DTO con la matriz de riesgo
     */
    @Override
    public RiskMatrixResponseDTO getRiskMatrix(Long spaceId, Long userId) {
        GroupReportEngine.RiskMatrixResult result =
            groupReportQueryCUInputPort.getRiskMatrix(spaceId, userId);
        return groupReportMapper.toRiskMatrixDTO(result);
    }
}

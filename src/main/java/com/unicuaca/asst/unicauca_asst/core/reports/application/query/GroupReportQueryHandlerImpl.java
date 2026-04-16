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
 */
@RequiredArgsConstructor
@Component
@Transactional(readOnly = true)
public class GroupReportQueryHandlerImpl implements GroupReportQueryHandler {

    private final GroupReportQueryCUInputPort groupReportQueryCUInputPort;
    private final GroupReportMapper groupReportMapper;

    @Override
    public GroupSummaryResponseDTO getGroupSummary(Long spaceId, Long userId) {
        GroupReportEngine.GroupSummaryResult result =
            groupReportQueryCUInputPort.getGroupSummary(spaceId, userId);
        return groupReportMapper.toGroupSummaryDTO(result);
    }

    @Override
    public DomainsAndDimensionsResponseDTO getDomainsAndDimensions(Long spaceId, Long userId) {
        GroupReportEngine.DomainsAndDimensionsResult result =
            groupReportQueryCUInputPort.getDomainsAndDimensions(spaceId, userId);
        return groupReportMapper.toDomainsAndDimensionsDTO(result);
    }

    @Override
    public RiskMatrixResponseDTO getRiskMatrix(Long spaceId, Long userId) {
        GroupReportEngine.RiskMatrixResult result =
            groupReportQueryCUInputPort.getRiskMatrix(spaceId, userId);
        return groupReportMapper.toRiskMatrixDTO(result);
    }
}

package com.unicuaca.asst.unicauca_asst.core.reports.application.query;

import com.unicuaca.asst.unicauca_asst.core.reports.application.dto.response.IndividualReportResponseDTO;
import com.unicuaca.asst.unicauca_asst.core.reports.application.mappers.IndividualReportMapper;
import com.unicuaca.asst.unicauca_asst.core.reports.domain.ports.input.IndividualReportQueryCUInputPort;
import com.unicuaca.asst.unicauca_asst.core.reports.domain.services.ScoringEngine;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * Implementación del manejador de consultas para informes individuales de riesgo psicosocial.
 */
@RequiredArgsConstructor
@Component
public class IndividualReportQueryHandlerImpl implements IndividualReportQueryHandler {

    private final IndividualReportQueryCUInputPort individualReportQueryCUInputPort;
    private final IndividualReportMapper individualReportMapper;

    /**
     * Genera el informe individual de calificación de riesgo psicosocial
     * para una batería cerrada.
     *
     * @param batteryManagementRecordId ID del registro de gestión de batería
     * @return DTO con el resultado consolidado del informe individual
     */
    @Override
    public IndividualReportResponseDTO getIndividualReport(Long batteryManagementRecordId) {
        ScoringEngine.IndividualScoringResult result =
            individualReportQueryCUInputPort.getIndividualReport(batteryManagementRecordId);
        return individualReportMapper.toResponseDTO(result);
    }

    @Override
    public byte[] getIndividualReportPdf(Long batteryManagementRecordId, Long spaceId, Long userId) {
        return individualReportQueryCUInputPort.getIndividualReportPdf(batteryManagementRecordId, spaceId, userId);
    }
}

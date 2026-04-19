package com.unicuaca.asst.unicauca_asst.core.reports.application.query;

import com.unicuaca.asst.unicauca_asst.core.reports.application.dto.response.IndividualReportResponseDTO;
import com.unicuaca.asst.unicauca_asst.core.reports.application.mappers.IndividualReportMapper;
import com.unicuaca.asst.unicauca_asst.core.reports.domain.ports.input.IndividualReportQueryCUInputPort;
import com.unicuaca.asst.unicauca_asst.core.reports.domain.services.ScoringEngine;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * Implementación del manejador de consultas para informes individuales de riesgo psicosocial.
 *
 * <p>Delega al puerto de entrada de dominio y transforma los resultados
 * a DTOs de respuesta. La generación del PDF se delega directamente al dominio
 * y se propaga el arreglo de bytes resultante.</p>
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

    /**
     * Genera el PDF del informe individual de riesgo psicosocial para una batería cerrada.
     *
     * @param batteryManagementRecordId ID del registro de gestión de batería
     * @param spaceId                   ID del espacio de análisis (para obtener el evaluador)
     * @param userId                    ID del usuario autenticado
     * @return arreglo de bytes con el contenido del PDF generado
     */
    @Override
    public byte[] getIndividualReportPdf(Long batteryManagementRecordId, Long spaceId, Long userId) {
        return individualReportQueryCUInputPort.getIndividualReportPdf(batteryManagementRecordId, spaceId, userId);
    }
}

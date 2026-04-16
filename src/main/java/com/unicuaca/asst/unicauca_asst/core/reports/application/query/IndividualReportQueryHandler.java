package com.unicuaca.asst.unicauca_asst.core.reports.application.query;

import com.unicuaca.asst.unicauca_asst.core.reports.application.dto.response.IndividualReportResponseDTO;

/**
 * Manejador de consultas para informes individuales de riesgo psicosocial.
 */
public interface IndividualReportQueryHandler {

    /**
     * Genera el informe individual de calificación de riesgo psicosocial
     * para una batería cerrada.
     *
     * @param batteryManagementRecordId ID del registro de gestión de batería
     * @return DTO con el resultado consolidado del informe individual
     */
    IndividualReportResponseDTO getIndividualReport(Long batteryManagementRecordId);

    /**
     * Genera el PDF del informe individual de riesgo psicosocial para una batería cerrada.
     *
     * @param batteryManagementRecordId ID del registro de gestión de batería
     * @param spaceId                   ID del espacio de análisis (para obtener el evaluador)
     * @param userId                    ID del usuario autenticado
     * @return arreglo de bytes con el contenido del PDF generado
     */
    byte[] getIndividualReportPdf(Long batteryManagementRecordId, Long spaceId, Long userId);
}

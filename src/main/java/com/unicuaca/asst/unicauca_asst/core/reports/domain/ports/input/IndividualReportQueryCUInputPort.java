package com.unicuaca.asst.unicauca_asst.core.reports.domain.ports.input;

import com.unicuaca.asst.unicauca_asst.core.reports.domain.services.ScoringEngine;

/**
 * Puerto de entrada para consultas de informes individuales de riesgo psicosocial.
 */
public interface IndividualReportQueryCUInputPort {

    /**
     * Genera el informe individual de calificación de riesgo psicosocial
     * para una batería cerrada.
     *
     * @param batteryManagementRecordId ID del registro de gestión de batería
     * @return resultado consolidado con intralaboral, extralaboral, total general y estrés
     */
    ScoringEngine.IndividualScoringResult getIndividualReport(Long batteryManagementRecordId);

    /**
     * Genera el PDF del informe individual de riesgo psicosocial para una batería cerrada,
     * incluyendo los datos del evaluador asociado al espacio de análisis.
     *
     * @param batteryManagementRecordId ID del registro de gestión de batería
     * @param spaceId                   ID del espacio de análisis (para obtener el evaluador)
     * @param userId                    ID del usuario que solicita el informe
     * @return arreglo de bytes con el contenido del PDF generado
     */
    byte[] getIndividualReportPdf(Long batteryManagementRecordId, Long spaceId, Long userId);
}

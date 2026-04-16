package com.unicuaca.asst.unicauca_asst.core.reports.domain.ports.output;

import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.models.BatteryManagementRecord;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.models.PersonEvaluatedDetails;
import com.unicuaca.asst.unicauca_asst.core.reports.domain.models.Evaluator;
import com.unicuaca.asst.unicauca_asst.core.reports.domain.services.ScoringEngine;

/**
 * Puerto de salida para la generación de PDFs de informes individuales
 * de riesgo psicosocial. Abstrae el motor de renderizado y la plantilla
 * utilizada por la infraestructura.
 */
public interface IndividualReportPdfOutputPort {

    /**
     * Genera el PDF del informe individual de riesgo psicosocial a partir
     * de los resultados calculados y los datos de contexto.
     *
     * @param scoringResult     resultados de calificación del {@link ScoringEngine}
     * @param personDetails     detalles sociodemográficos y laborales de la persona evaluada
     * @param batteryRecord     registro de batería (provee fecha de aplicación y datos personales)
     * @param evaluator         datos del evaluador responsable del informe
     * @param intralaboralForm  forma del cuestionario intralaboral aplicada: {@code "ILA"} o {@code "ILB"}
     * @return arreglo de bytes con el contenido del PDF generado
     */
    byte[] generatePdf(
        ScoringEngine.IndividualScoringResult scoringResult,
        PersonEvaluatedDetails personDetails,
        BatteryManagementRecord batteryRecord,
        Evaluator evaluator,
        String intralaboralForm
    );
}

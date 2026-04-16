package com.unicuaca.asst.unicauca_asst.core.reports.domain.ports.output;

import java.util.List;
import java.util.Optional;

import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.models.BatteryManagementRecord;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.models.PersonEvaluatedDetails;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.models.QuestionnaireManagementRecord;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.models.QuestionnaireResponse;

/**
 * Puerto de salida que abstrae la obtención de datos necesarios para calcular informes
 * de riesgo psicosocial. Define métodos específicos para el contexto de informes,
 * sin duplicar los repositorios del módulo de gestión de baterías.
 */
public interface ReportDataQueryRepository {

    /**
     * Obtiene un registro de batería por su ID.
     * Debe incluir la persona evaluada asociada.
     *
     * @param id identificador del registro de batería
     * @return Optional con el registro encontrado
     */
    Optional<BatteryManagementRecord> getBatteryRecordById(Long id);

    /**
     * Obtiene los detalles sociodemográficos y laborales de una persona
     * asociados a un registro de batería específico.
     *
     * @param batteryRecordId ID del registro de batería
     * @return Optional con los detalles encontrados
     */
    Optional<PersonEvaluatedDetails> getPersonDetailsByBatteryRecordId(Long batteryRecordId);

    /**
     * Obtiene todos los registros de gestión de cuestionarios de una batería.
     * Cada registro incluye el cuestionario asociado (con su abreviatura).
     *
     * @param batteryRecordId ID del registro de batería
     * @return lista de registros de gestión de cuestionarios
     */
    List<QuestionnaireManagementRecord> getQuestionnaireRecordsByBatteryId(Long batteryRecordId);

    /**
     * Obtiene todas las respuestas de un registro de gestión de cuestionario,
     * incluyendo las relaciones con pregunta (con orden) y opción de respuesta (con valor).
     *
     * @param questionnaireRecordId ID del registro de gestión de cuestionario
     * @return lista de respuestas con relaciones cargadas
     */
    List<QuestionnaireResponse> getResponsesByQuestionnaireRecordId(Long questionnaireRecordId);
}

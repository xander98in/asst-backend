package com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.ports.output;

import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.models.QuestionnaireManagementRecord;

/**
 * Puerto de salida para operaciones de escritura (Command) sobre registros de gestión de cuestionarios.
 */
public interface QuestionnaireManagementRecordCommandRepository {

    /**
     * Guarda un registro de gestión de cuestionario.
     *
     * @param record El modelo de dominio con toda la información hidratada (relaciones completas).
     * @return El modelo de dominio persistido con el ID generado y fechas de auditoría.
     */
    QuestionnaireManagementRecord save(QuestionnaireManagementRecord record);
}

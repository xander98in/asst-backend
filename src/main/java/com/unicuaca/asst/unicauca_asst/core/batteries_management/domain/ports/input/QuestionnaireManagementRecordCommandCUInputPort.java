package com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.ports.input;

import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.models.QuestionnaireManagementRecord;

/**
 * Puerto de entrada para los casos de uso relacionados con la gestión de registros de cuestionarios.
 * Define las operaciones command (crear, actualizar, eliminar).
 */
public interface QuestionnaireManagementRecordCommandCUInputPort {

    /**
     * Crea un nuevo registro de gestión de cuestionario.
     *
     * @param record modelo de dominio con los datos necesarios (IDs de batería y cuestionario)
     * @return el registro creado con sus datos completos (ID generado, fechas, estado, etc.)
     */
    QuestionnaireManagementRecord createQuestionnaireManagementRecord(QuestionnaireManagementRecord record);

    /**
     * Elimina un registro de gestión de cuestionario por su ID.
     * También se encarga de eliminar en cascada las respuestas asociadas a este registro de gestión de cuestionario, si existen.
     *
     * @param id identificador del registro a eliminar.
     */
    void deleteQuestionnaireManagementRecord(Long id);
}

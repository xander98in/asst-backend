package com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.ports.output;

import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.models.QuestionnaireResponse;

import java.util.List;

/**
 * Puerto de salida para operaciones de escritura (Command) sobre respuestas de cuestionarios
 * ({@link QuestionnaireResponse}).
 *
 * <p>Define el contrato que debe cumplir la infraestructura para persistir en lote o eliminar
 * respuestas asociadas a un registro de gestión de cuestionario, sin acoplar el dominio
 * a los detalles de persistencia.</p>
 */
public interface QuestionnaireResponseCommandRepository {

    /**
     * Guarda una lista de respuestas de cuestionario en la base de datos.
     *
     * @param responses Lista de modelos de dominio completamente validados y enriquecidos.
     */
    void saveAll(List<QuestionnaireResponse> responses);

    /**
     * Elimina todas las respuestas asociadas a un registro de gestión de cuestionario.
     *
     * @param recordId ID del registro de gestión de cuestionario.
     */
    void deleteByQuestionnaireManagementRecordId(Long recordId);
}

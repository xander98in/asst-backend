package com.unicuaca.asst.unicauca_asst.core.reports.domain.ports.output;

import com.unicuaca.asst.unicauca_asst.core.reports.domain.models.Evaluator;

/**
 * Puerto de salida para operaciones de escritura de evaluadores.
 */
public interface EvaluatorCommandRepository {

    /**
     * Persiste un evaluador (creación o actualización).
     *
     * @param evaluator evaluador a guardar
     * @return el evaluador persistido con su ID asignado
     */
    Evaluator save(Evaluator evaluator);

    /**
     * Elimina un evaluador por su ID.
     *
     * @param evaluatorId ID del evaluador a eliminar
     */
    void deleteById(Long evaluatorId);

    /**
     * Verifica si existe un evaluador con el ID y usuario creador indicados.
     *
     * @param evaluatorId ID del evaluador
     * @param userId      ID del usuario creador
     * @return true si existe un evaluador con esos criterios
     */
    boolean existsByIdAndCreatorUserId(Long evaluatorId, Long userId);
}

package com.unicuaca.asst.unicauca_asst.core.reports.domain.ports.input;

import com.unicuaca.asst.unicauca_asst.core.reports.domain.models.Evaluator;

/**
 * Puerto de entrada para las operaciones de escritura sobre evaluadores.
 */
public interface EvaluatorCommandCUInputPort {

    /**
     * Crea un nuevo evaluador asociado al usuario creador.
     *
     * @param evaluator datos del evaluador a registrar
     * @return el evaluador creado con su ID y fecha de creación asignados
     */
    Evaluator createEvaluator(Evaluator evaluator);

    /**
     * Actualiza los datos de un evaluador existente, validando que pertenezca
     * al usuario que realiza la operación.
     *
     * @param evaluatorId ID del evaluador a actualizar
     * @param evaluator   nuevos datos del evaluador
     * @param userId      ID del usuario que realiza la operación
     * @return el evaluador actualizado
     */
    Evaluator updateEvaluator(Long evaluatorId, Evaluator evaluator, Long userId);

    /**
     * Elimina un evaluador, validando que pertenezca al usuario y que no esté
     * siendo referenciado por ningún espacio de análisis.
     *
     * @param evaluatorId ID del evaluador a eliminar
     * @param userId      ID del usuario que realiza la operación
     */
    void deleteEvaluator(Long evaluatorId, Long userId);
}

package com.unicuaca.asst.unicauca_asst.core.reports.domain.ports.input;

import java.util.List;

import org.springframework.data.domain.Page;

import com.unicuaca.asst.unicauca_asst.core.reports.domain.models.Evaluator;

/**
 * Puerto de entrada para las operaciones de lectura sobre evaluadores.
 */
public interface EvaluatorQueryCUInputPort {

    /**
     * Lista los evaluadores registrados por un usuario específico.
     *
     * @param userId ID del usuario creador
     * @return lista de evaluadores asociados al usuario
     */
    List<Evaluator> getEvaluatorsByUser(Long userId);

    /**
     * Obtiene un evaluador por su ID validando que pertenezca al usuario.
     *
     * @param evaluatorId ID del evaluador
     * @param userId      ID del usuario que realiza la operación
     * @return el evaluador solicitado
     */
    Evaluator getEvaluatorById(Long evaluatorId, Long userId);

    /**
     * Lista evaluadores del usuario de forma paginada con término de búsqueda opcional.
     *
     * @param userId     ID del usuario
     * @param searchTerm término de búsqueda (puede ser null)
     * @param page       número de página
     * @param size       tamaño de página
     * @return página de evaluadores
     */
    Page<Evaluator> getEvaluatorsByUserPaged(Long userId, String searchTerm, Integer page, Integer size);
}

package com.unicuaca.asst.unicauca_asst.core.reports.domain.ports.output;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;

import com.unicuaca.asst.unicauca_asst.core.reports.domain.models.Evaluator;

/**
 * Puerto de salida para operaciones de lectura de evaluadores.
 */
public interface EvaluatorQueryRepository {

    /**
     * Lista todos los evaluadores creados por un usuario.
     *
     * @param userId ID del usuario creador
     * @return lista de evaluadores del usuario
     */
    List<Evaluator> findAllByCreatorUserId(Long userId);

    /**
     * Busca un evaluador por su ID.
     *
     * @param evaluatorId ID del evaluador
     * @return Optional con el evaluador encontrado
     */
    Optional<Evaluator> findById(Long evaluatorId);

    /**
     * Verifica si existe un evaluador con el ID indicado.
     *
     * @param evaluatorId ID del evaluador
     * @return true si existe
     */
    boolean existsById(Long evaluatorId);

    /**
     * Verifica si el evaluador está referenciado por al menos un espacio de análisis.
     *
     * @param evaluatorId ID del evaluador
     * @return true si algún espacio lo referencia, false en caso contrario
     */
    boolean isEvaluatorInUseByAnySpace(Long evaluatorId);

    /**
     * Lista evaluadores del usuario de forma paginada.
     *
     * @param userId ID del usuario creador
     * @param page   número de página
     * @param size   tamaño de página
     * @return página de evaluadores
     */
    Page<Evaluator> findAllByCreatorUserIdPaged(Long userId, Integer page, Integer size);

    /**
     * Lista evaluadores del usuario de forma paginada con término de búsqueda.
     * Busca por número de identificación, tarjeta profesional, licencia o nombre.
     *
     * @param userId     ID del usuario creador
     * @param searchTerm término de búsqueda
     * @param page       número de página
     * @param size       tamaño de página
     * @return página de evaluadores filtrados
     */
    Page<Evaluator> findAllByCreatorUserIdWithSearchTerm(Long userId, String searchTerm, Integer page, Integer size);
}

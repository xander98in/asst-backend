package com.unicuaca.asst.unicauca_asst.core.reports.application.query;

import java.util.List;

import org.springframework.data.domain.Page;

import com.unicuaca.asst.unicauca_asst.core.reports.application.dto.response.EvaluatorResponseDTO;

/**
 * Handler de la capa de aplicación para operaciones de lectura sobre evaluadores.
 */
public interface EvaluatorQueryHandler {

    /**
     * Lista los evaluadores registrados por un usuario.
     *
     * @param userId ID del usuario creador
     * @return lista de DTOs de evaluadores
     */
    List<EvaluatorResponseDTO> getEvaluatorsByUser(Long userId);

    /**
     * Obtiene un evaluador específico por su ID.
     *
     * @param evaluatorId ID del evaluador
     * @param userId      ID del usuario que realiza la operación
     * @return DTO del evaluador solicitado
     */
    EvaluatorResponseDTO getEvaluatorById(Long evaluatorId, Long userId);

    /**
     * Lista evaluadores del usuario de forma paginada con búsqueda opcional.
     *
     * @param userId     ID del usuario autenticado
     * @param searchTerm término de búsqueda (puede ser null)
     * @param page       número de página
     * @param size       tamaño de página
     * @return página de DTOs de evaluadores
     */
    Page<EvaluatorResponseDTO> getEvaluatorsByUserPaged(Long userId, String searchTerm, Integer page, Integer size);
}

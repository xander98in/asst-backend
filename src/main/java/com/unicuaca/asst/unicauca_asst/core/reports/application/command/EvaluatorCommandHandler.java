package com.unicuaca.asst.unicauca_asst.core.reports.application.command;

import com.unicuaca.asst.unicauca_asst.core.reports.application.dto.request.EvaluatorCreateRequestDTO;
import com.unicuaca.asst.unicauca_asst.core.reports.application.dto.request.EvaluatorUpdateRequestDTO;
import com.unicuaca.asst.unicauca_asst.core.reports.application.dto.response.EvaluatorResponseDTO;

/**
 * Handler de la capa de aplicación para operaciones de escritura sobre evaluadores.
 */
public interface EvaluatorCommandHandler {

    /**
     * Crea un nuevo evaluador asociado al usuario autenticado.
     *
     * @param request datos del evaluador a registrar
     * @param userId  ID del usuario creador
     * @return DTO de respuesta del evaluador creado
     */
    EvaluatorResponseDTO createEvaluator(EvaluatorCreateRequestDTO request, Long userId);

    /**
     * Actualiza un evaluador existente.
     *
     * @param evaluatorId ID del evaluador a actualizar
     * @param request     nuevos datos del evaluador
     * @param userId      ID del usuario que realiza la operación
     * @return DTO de respuesta del evaluador actualizado
     */
    EvaluatorResponseDTO updateEvaluator(Long evaluatorId, EvaluatorUpdateRequestDTO request, Long userId);

    /**
     * Elimina un evaluador existente.
     *
     * @param evaluatorId ID del evaluador a eliminar
     * @param userId      ID del usuario que realiza la operación
     */
    void deleteEvaluator(Long evaluatorId, Long userId);
}

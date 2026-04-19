package com.unicuaca.asst.unicauca_asst.core.reports.application.command;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.unicuaca.asst.unicauca_asst.core.reports.application.dto.request.EvaluatorCreateRequestDTO;
import com.unicuaca.asst.unicauca_asst.core.reports.application.dto.request.EvaluatorUpdateRequestDTO;
import com.unicuaca.asst.unicauca_asst.core.reports.application.dto.response.EvaluatorResponseDTO;
import com.unicuaca.asst.unicauca_asst.core.reports.application.mappers.EvaluatorMapper;
import com.unicuaca.asst.unicauca_asst.core.reports.domain.models.Evaluator;
import com.unicuaca.asst.unicauca_asst.core.reports.domain.ports.input.EvaluatorCommandCUInputPort;

import lombok.RequiredArgsConstructor;

/**
 * Implementación del handler de comandos para evaluadores.
 *
 * <p>Delega la lógica de negocio al puerto de entrada de dominio y transforma
 * las entradas y salidas entre DTOs y modelos de dominio.</p>
 */
@Component
@RequiredArgsConstructor
@Transactional
public class EvaluatorCommandHandlerImpl implements EvaluatorCommandHandler {

    private final EvaluatorCommandCUInputPort evaluatorCommandCUInputPort;
    private final EvaluatorMapper evaluatorMapper;

    /**
     * Crea un nuevo evaluador asociado al usuario autenticado.
     *
     * @param request datos del evaluador a registrar
     * @param userId  ID del usuario creador
     * @return DTO de respuesta del evaluador creado
     */
    @Override
    public EvaluatorResponseDTO createEvaluator(EvaluatorCreateRequestDTO request, Long userId) {
        Evaluator domain = evaluatorMapper.toDomain(request);
        domain.setCreatorUserId(userId);
        Evaluator created = evaluatorCommandCUInputPort.createEvaluator(domain);
        return evaluatorMapper.toResponseDTO(created);
    }

    /**
     * Actualiza un evaluador existente.
     *
     * @param evaluatorId ID del evaluador a actualizar
     * @param request     nuevos datos del evaluador
     * @param userId      ID del usuario que realiza la operación
     * @return DTO de respuesta del evaluador actualizado
     */
    @Override
    public EvaluatorResponseDTO updateEvaluator(Long evaluatorId, EvaluatorUpdateRequestDTO request, Long userId) {
        Evaluator domain = evaluatorMapper.toDomain(request);
        Evaluator updated = evaluatorCommandCUInputPort.updateEvaluator(evaluatorId, domain, userId);
        return evaluatorMapper.toResponseDTO(updated);
    }

    /**
     * Elimina un evaluador existente.
     *
     * @param evaluatorId ID del evaluador a eliminar
     * @param userId      ID del usuario que realiza la operación
     */
    @Override
    public void deleteEvaluator(Long evaluatorId, Long userId) {
        evaluatorCommandCUInputPort.deleteEvaluator(evaluatorId, userId);
    }
}

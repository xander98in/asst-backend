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
 */
@Component
@RequiredArgsConstructor
@Transactional
public class EvaluatorCommandHandlerImpl implements EvaluatorCommandHandler {

    private final EvaluatorCommandCUInputPort evaluatorCommandCUInputPort;
    private final EvaluatorMapper evaluatorMapper;

    @Override
    public EvaluatorResponseDTO createEvaluator(EvaluatorCreateRequestDTO request, Long userId) {
        Evaluator domain = evaluatorMapper.toDomain(request);
        domain.setCreatorUserId(userId);
        Evaluator created = evaluatorCommandCUInputPort.createEvaluator(domain);
        return evaluatorMapper.toResponseDTO(created);
    }

    @Override
    public EvaluatorResponseDTO updateEvaluator(Long evaluatorId, EvaluatorUpdateRequestDTO request, Long userId) {
        Evaluator domain = evaluatorMapper.toDomain(request);
        Evaluator updated = evaluatorCommandCUInputPort.updateEvaluator(evaluatorId, domain, userId);
        return evaluatorMapper.toResponseDTO(updated);
    }

    @Override
    public void deleteEvaluator(Long evaluatorId, Long userId) {
        evaluatorCommandCUInputPort.deleteEvaluator(evaluatorId, userId);
    }
}

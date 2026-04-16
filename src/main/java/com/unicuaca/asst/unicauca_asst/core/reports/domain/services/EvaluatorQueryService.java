package com.unicuaca.asst.unicauca_asst.core.reports.domain.services;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;

import com.unicuaca.asst.unicauca_asst.common.application.output.ResultFormatterOutputPort;
import com.unicuaca.asst.unicauca_asst.common.exceptions.structure.ErrorCode;
import com.unicuaca.asst.unicauca_asst.core.reports.domain.models.Evaluator;
import com.unicuaca.asst.unicauca_asst.core.reports.domain.ports.input.EvaluatorQueryCUInputPort;
import com.unicuaca.asst.unicauca_asst.core.reports.domain.ports.output.EvaluatorQueryRepository;

import lombok.RequiredArgsConstructor;

/**
 * Servicio de dominio para consultas de evaluadores.
 */
@RequiredArgsConstructor
public class EvaluatorQueryService implements EvaluatorQueryCUInputPort {

    private final EvaluatorQueryRepository evaluatorQueryRepository;
    private final ResultFormatterOutputPort resultFormatter;

    /**
     * Retorna los evaluadores creados por el usuario indicado.
     *
     * @param userId ID del usuario creador
     * @return lista de evaluadores
     */
    @Override
    public List<Evaluator> getEvaluatorsByUser(Long userId) {
        return evaluatorQueryRepository.findAllByCreatorUserId(userId);
    }

    /**
     * Obtiene un evaluador por su ID validando ownership.
     *
     * @param evaluatorId ID del evaluador
     * @param userId      ID del usuario que realiza la operación
     * @return el evaluador solicitado
     */
    @Override
    public Evaluator getEvaluatorById(Long evaluatorId, Long userId) {
        Optional<Evaluator> optional = evaluatorQueryRepository.findById(evaluatorId);
        if (optional.isEmpty()) {
            resultFormatter.throwEntityNotFound(
                ErrorCode.EVALUATOR_NOT_FOUND,
                "user.report.evaluator_not_found",
                evaluatorId
            );
        }
        Evaluator evaluator = optional.get();

        if (!evaluator.getCreatorUserId().equals(userId)) {
            resultFormatter.throwBusinessRuleViolation(
                ErrorCode.EVALUATOR_ACCESS_DENIED,
                "user.report.evaluator_access_denied",
                userId, evaluatorId
            );
        }

        return evaluator;
    }

    /** {@inheritDoc} */
    @Override
    public Page<Evaluator> getEvaluatorsByUserPaged(Long userId, String searchTerm, Integer page, Integer size) {
        if (searchTerm != null && !searchTerm.isBlank()) {
            return evaluatorQueryRepository.findAllByCreatorUserIdWithSearchTerm(userId, searchTerm.trim(), page, size);
        }
        return evaluatorQueryRepository.findAllByCreatorUserIdPaged(userId, page, size);
    }
}

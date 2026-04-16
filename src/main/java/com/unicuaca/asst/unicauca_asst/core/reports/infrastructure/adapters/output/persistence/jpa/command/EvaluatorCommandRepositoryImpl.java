package com.unicuaca.asst.unicauca_asst.core.reports.infrastructure.adapters.output.persistence.jpa.command;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.unicuaca.asst.unicauca_asst.core.reports.domain.models.Evaluator;
import com.unicuaca.asst.unicauca_asst.core.reports.domain.ports.output.EvaluatorCommandRepository;
import com.unicuaca.asst.unicauca_asst.core.reports.infrastructure.adapters.output.persistence.jpa.entities.EvaluatorEntity;
import com.unicuaca.asst.unicauca_asst.core.reports.infrastructure.adapters.output.persistence.jpa.repositories.EvaluatorSpringJpaRepository;
import com.unicuaca.asst.unicauca_asst.core.reports.infrastructure.adapters.output.persistence.mappers.EvaluatorPersistenceMapper;

import lombok.RequiredArgsConstructor;

/**
 * Implementación del puerto de salida {@link EvaluatorCommandRepository}.
 */
@Component
@RequiredArgsConstructor
@Transactional
public class EvaluatorCommandRepositoryImpl implements EvaluatorCommandRepository {

    private final EvaluatorSpringJpaRepository evaluatorJpaRepository;
    private final EvaluatorPersistenceMapper persistenceMapper;

    @Override
    public Evaluator save(Evaluator evaluator) {
        EvaluatorEntity entity = persistenceMapper.toEntity(evaluator);
        EvaluatorEntity saved = evaluatorJpaRepository.save(entity);
        return persistenceMapper.toDomain(saved);
    }

    @Override
    public void deleteById(Long evaluatorId) {
        evaluatorJpaRepository.deleteById(evaluatorId);
    }

    @Override
    public boolean existsByIdAndCreatorUserId(Long evaluatorId, Long userId) {
        return evaluatorJpaRepository.existsByIdAndCreatorUserId(evaluatorId, userId);
    }
}

package com.unicuaca.asst.unicauca_asst.core.reports.infrastructure.adapters.output.persistence.jpa.command;

import org.springframework.stereotype.Repository;

import com.unicuaca.asst.unicauca_asst.core.reports.domain.models.Evaluator;
import com.unicuaca.asst.unicauca_asst.core.reports.domain.ports.output.EvaluatorCommandRepository;
import com.unicuaca.asst.unicauca_asst.core.reports.infrastructure.adapters.output.persistence.jpa.entities.EvaluatorEntity;
import com.unicuaca.asst.unicauca_asst.core.reports.infrastructure.adapters.output.persistence.jpa.repositories.EvaluatorSpringJpaRepository;
import com.unicuaca.asst.unicauca_asst.core.reports.infrastructure.adapters.output.persistence.mappers.EvaluatorPersistenceMapper;

import lombok.RequiredArgsConstructor;

/**
 * Implementación del puerto de salida {@link EvaluatorCommandRepository}.
 *
 * <p>Gestiona las operaciones de escritura sobre evaluadores delegando en el
 * repositorio Spring Data JPA y traduciendo entre entidades JPA y modelos de dominio
 * mediante el mapper de persistencia.</p>
 */
@Repository
@RequiredArgsConstructor
public class EvaluatorCommandRepositoryImpl implements EvaluatorCommandRepository {

    private final EvaluatorSpringJpaRepository evaluatorJpaRepository;
    private final EvaluatorPersistenceMapper persistenceMapper;

    /**
     * Persiste un evaluador (creación o actualización).
     *
     * @param evaluator evaluador a guardar
     * @return el evaluador persistido con su ID asignado
     */
    @Override
    public Evaluator save(Evaluator evaluator) {
        EvaluatorEntity entity = persistenceMapper.toEntity(evaluator);
        EvaluatorEntity saved = evaluatorJpaRepository.save(entity);
        return persistenceMapper.toDomain(saved);
    }

    /**
     * Elimina un evaluador por su ID.
     *
     * @param evaluatorId ID del evaluador a eliminar
     */
    @Override
    public void deleteById(Long evaluatorId) {
        evaluatorJpaRepository.deleteById(evaluatorId);
    }

    /**
     * Verifica si existe un evaluador con el ID y usuario creador indicados.
     *
     * @param evaluatorId ID del evaluador
     * @param userId      ID del usuario creador
     * @return true si existe un evaluador con esos criterios
     */
    @Override
    public boolean existsByIdAndCreatorUserId(Long evaluatorId, Long userId) {
        return evaluatorJpaRepository.existsByIdAndCreatorUserId(evaluatorId, userId);
    }
}

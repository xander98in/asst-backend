package com.unicuaca.asst.unicauca_asst.core.reports.infrastructure.adapters.output.persistence.jpa.query;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import com.unicuaca.asst.unicauca_asst.core.reports.domain.models.Evaluator;
import com.unicuaca.asst.unicauca_asst.core.reports.domain.ports.output.EvaluatorQueryRepository;
import com.unicuaca.asst.unicauca_asst.core.reports.infrastructure.adapters.output.persistence.jpa.entities.EvaluatorEntity;
import com.unicuaca.asst.unicauca_asst.core.reports.infrastructure.adapters.output.persistence.jpa.repositories.AnalysisSpaceSpringJpaRepository;
import com.unicuaca.asst.unicauca_asst.core.reports.infrastructure.adapters.output.persistence.jpa.repositories.EvaluatorSpringJpaRepository;
import com.unicuaca.asst.unicauca_asst.core.reports.infrastructure.adapters.output.persistence.mappers.EvaluatorPersistenceMapper;

import lombok.RequiredArgsConstructor;

/**
 * Implementación del puerto de salida {@link EvaluatorQueryRepository}.
 *
 * <p>Gestiona las operaciones de lectura sobre evaluadores delegando en los
 * repositorios Spring Data JPA y traduciendo entre entidades JPA y modelos de dominio
 * mediante el mapper de persistencia.</p>
 */
@Component
@RequiredArgsConstructor
public class EvaluatorQueryRepositoryImpl implements EvaluatorQueryRepository {

    private final EvaluatorSpringJpaRepository evaluatorJpaRepository;
    private final AnalysisSpaceSpringJpaRepository analysisSpaceJpaRepository;
    private final EvaluatorPersistenceMapper persistenceMapper;

    /**
     * Lista todos los evaluadores creados por un usuario.
     *
     * @param userId ID del usuario creador
     * @return lista de evaluadores del usuario
     */
    @Override
    public List<Evaluator> findAllByCreatorUserId(Long userId) {
        return evaluatorJpaRepository.findAllByCreatorUserId(userId).stream()
            .map(persistenceMapper::toDomain)
            .toList();
    }

    /**
     * Busca un evaluador por su ID.
     *
     * @param evaluatorId ID del evaluador
     * @return Optional con el evaluador encontrado
     */
    @Override
    public Optional<Evaluator> findById(Long evaluatorId) {
        return evaluatorJpaRepository.findById(evaluatorId)
            .map(persistenceMapper::toDomain);
    }

    /**
     * Verifica si existe un evaluador con el ID indicado.
     *
     * @param evaluatorId ID del evaluador
     * @return true si existe
     */
    @Override
    public boolean existsById(Long evaluatorId) {
        return evaluatorJpaRepository.existsById(evaluatorId);
    }

    /**
     * Verifica si el evaluador está referenciado por al menos un espacio de análisis.
     *
     * @param evaluatorId ID del evaluador
     * @return true si algún espacio lo referencia, false en caso contrario
     */
    @Override
    public boolean isEvaluatorInUseByAnySpace(Long evaluatorId) {
        return analysisSpaceJpaRepository.existsByEvaluator_Id(evaluatorId);
    }

    /**
     * Lista evaluadores del usuario de forma paginada.
     *
     * @param userId ID del usuario creador
     * @param page   número de página
     * @param size   tamaño de página
     * @return página de evaluadores
     */
    @Override
    public Page<Evaluator> findAllByCreatorUserIdPaged(Long userId, Integer page, Integer size) {
        Page<EvaluatorEntity> entityPage = evaluatorJpaRepository.findAllByCreatorUserIdPaged(
            userId, PageRequest.of(page, size));
        List<Evaluator> domainList = entityPage.getContent().stream()
            .map(persistenceMapper::toDomain).toList();
        return new PageImpl<>(domainList, entityPage.getPageable(), entityPage.getTotalElements());
    }

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
    @Override
    public Page<Evaluator> findAllByCreatorUserIdWithSearchTerm(Long userId, String searchTerm, Integer page, Integer size) {
        Page<EvaluatorEntity> entityPage = evaluatorJpaRepository.findAllByCreatorUserIdWithSearchTerm(
            userId, searchTerm, PageRequest.of(page, size));
        List<Evaluator> domainList = entityPage.getContent().stream()
            .map(persistenceMapper::toDomain).toList();
        return new PageImpl<>(domainList, entityPage.getPageable(), entityPage.getTotalElements());
    }
}

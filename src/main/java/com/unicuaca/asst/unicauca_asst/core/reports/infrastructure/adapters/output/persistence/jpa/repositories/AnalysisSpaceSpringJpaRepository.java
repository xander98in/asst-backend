package com.unicuaca.asst.unicauca_asst.core.reports.infrastructure.adapters.output.persistence.jpa.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.unicuaca.asst.unicauca_asst.core.reports.infrastructure.adapters.output.persistence.jpa.entities.AnalysisSpaceEntity;

/**
 * Repositorio Spring Data JPA para la entidad {@link AnalysisSpaceEntity}.
 */
@Repository
public interface AnalysisSpaceSpringJpaRepository extends JpaRepository<AnalysisSpaceEntity, Long> {

    List<AnalysisSpaceEntity> findAllByCreatorUserId(Long creatorUserId);

    @Query("SELECT a FROM AnalysisSpaceEntity a LEFT JOIN FETCH a.batteries WHERE a.id = :id")
    Optional<AnalysisSpaceEntity> findByIdWithBatteries(@Param("id") Long id);

    boolean existsByNameAndCreatorUserId(String name, Long creatorUserId);

    int countByCreatorUserId(Long creatorUserId);

    boolean existsByEvaluator_Id(Long evaluatorId);
}

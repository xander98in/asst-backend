package com.unicuaca.asst.unicauca_asst.core.reports.infrastructure.adapters.output.persistence.jpa.repositories;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.unicuaca.asst.unicauca_asst.core.reports.infrastructure.adapters.output.persistence.jpa.entities.EvaluatorEntity;

/**
 * Repositorio Spring Data JPA para la entidad {@link EvaluatorEntity}.
 */
@Repository
public interface EvaluatorSpringJpaRepository extends JpaRepository<EvaluatorEntity, Long> {

    List<EvaluatorEntity> findAllByCreatorUserId(Long creatorUserId);

    boolean existsByIdAndCreatorUserId(Long id, Long creatorUserId);

    @Query("SELECT e FROM EvaluatorEntity e WHERE e.creatorUserId = :userId " +
           "ORDER BY e.createdAt DESC")
    Page<EvaluatorEntity> findAllByCreatorUserIdPaged(@Param("userId") Long userId, Pageable pageable);

    @Query("SELECT e FROM EvaluatorEntity e WHERE e.creatorUserId = :userId " +
           "AND (e.identificationNumber LIKE CONCAT(:term, '%') " +
           "OR e.professionalCardNumber LIKE CONCAT(:term, '%') " +
           "OR e.occupationalHealthLicense LIKE CONCAT(:term, '%') " +
           "OR LOWER(e.fullName) LIKE CONCAT('%', LOWER(:term), '%')) " +
           "ORDER BY e.createdAt DESC")
    Page<EvaluatorEntity> findAllByCreatorUserIdWithSearchTerm(
        @Param("userId") Long userId, @Param("term") String term, Pageable pageable);
}

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

    /**
     * Lista todos los evaluadores creados por un usuario.
     *
     * @param creatorUserId ID del usuario creador
     * @return lista de evaluadores del usuario
     */
    List<EvaluatorEntity> findAllByCreatorUserId(Long creatorUserId);

    /**
     * Verifica si existe un evaluador con el ID y usuario creador indicados.
     *
     * @param id            ID del evaluador
     * @param creatorUserId ID del usuario creador
     * @return {@code true} si existe un evaluador con esos criterios
     */
    boolean existsByIdAndCreatorUserId(Long id, Long creatorUserId);

    /**
     * Lista de forma paginada los evaluadores de un usuario, ordenados de manera
     * descendente por fecha de creación.
     *
     * @param userId   ID del usuario creador
     * @param pageable información de paginación
     * @return página de evaluadores del usuario
     */
    @Query("SELECT e FROM EvaluatorEntity e WHERE e.creatorUserId = :userId " +
           "ORDER BY e.createdAt DESC")
    Page<EvaluatorEntity> findAllByCreatorUserIdPaged(@Param("userId") Long userId, Pageable pageable);

    /**
     * Lista de forma paginada los evaluadores de un usuario filtrando por un término
     * de búsqueda aplicado a número de identificación, tarjeta profesional, licencia
     * de salud ocupacional o nombre completo.
     *
     * @param userId   ID del usuario creador
     * @param term     término de búsqueda
     * @param pageable información de paginación
     * @return página de evaluadores filtrados
     */
    @Query("SELECT e FROM EvaluatorEntity e WHERE e.creatorUserId = :userId " +
           "AND (e.identificationNumber LIKE CONCAT(:term, '%') " +
           "OR e.professionalCardNumber LIKE CONCAT(:term, '%') " +
           "OR e.occupationalHealthLicense LIKE CONCAT(:term, '%') " +
           "OR LOWER(e.fullName) LIKE CONCAT('%', LOWER(:term), '%')) " +
           "ORDER BY e.createdAt DESC")
    Page<EvaluatorEntity> findAllByCreatorUserIdWithSearchTerm(
        @Param("userId") Long userId, @Param("term") String term, Pageable pageable);
}

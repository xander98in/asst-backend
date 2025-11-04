package com.unicuaca.asst.unicauca_asst.core.batteries_management.infrastructure.adapters.output.persistence.jpa.repositories;

import com.unicuaca.asst.unicauca_asst.core.batteries_management.infrastructure.adapters.output.persistence.jpa.entities.PersonEvaluatedDetailsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repositorio JPA para {@link PersonEvaluatedDetailsEntity}.
 */
@Repository
public interface PersonEvaluatedDetailsSpringJpaRepository extends JpaRepository<PersonEvaluatedDetailsEntity, Long> {

    /**
     * Verifica si existe un registro de detalles por ID de persona evaluada.
     *
     * @param personEvaluatedId ID de la persona evaluada
     * @return true si existe un registro de detalles, false en caso contrario
     */
    boolean existsByPersonEvaluated_Id(Long personEvaluatedId);

    /**
     * Obtiene los detalles por ID de persona evaluada (sin forzar cargas).
     *
     * @param personEvaluatedId ID de la persona evaluada
     * @return Detalles de la persona evaluada envueltos en un Optional
     */
    Optional<PersonEvaluatedDetailsEntity> findByPersonEvaluated_Id(Long personEvaluatedId);

    /**
     * Variante JPQL para verificar existencia por ID de persona evaluada.
     *
     * @param personEvaluatedId ID de la persona evaluada
     * @return true si existe un registro de detalles, false en caso contrario
     */
    @Query("""
       SELECT CASE WHEN COUNT(d) > 0 THEN TRUE ELSE FALSE END
       FROM PersonEvaluatedDetailsEntity d
       WHERE d.personEvaluated.id = :personEvaluatedId
       """)
    boolean existsByPersonEvaluatedIdQuery(@Param("personEvaluatedId") Long personEvaluatedId);

    /**
     * Variante JPQL para obtener detalles por ID de persona evaluada.
     *
     * @param personEvaluatedId ID de la persona evaluada
     * @return Detalles de la persona evaluada envueltos en un Optional
     */
    @Query("""
       SELECT d
       FROM PersonEvaluatedDetailsEntity d
       WHERE d.personEvaluated.id = :personEvaluatedId
       """)
    Optional<PersonEvaluatedDetailsEntity> findByPersonEvaluatedIdQuery(@Param("personEvaluatedId") Long personEvaluatedId);

    /**
     * Variante JPQL con JOIN FETCH para traer TODA la información, incluyendo
     * los departamentos de las ciudades (residencia/trabajo).
     *
     * @param id ID del detalle de persona evaluada
     * @return Detalles completos de la persona evaluada
     */
    @Query("""
           SELECT d
           FROM PersonEvaluatedDetailsEntity d
           LEFT JOIN FETCH d.personEvaluated p
           LEFT JOIN FETCH d.civilStatus cs
           LEFT JOIN FETCH d.educationLevel el
           LEFT JOIN FETCH d.residenceCity rc
           LEFT JOIN FETCH rc.department rcd
           LEFT JOIN FETCH d.socioeconomicLevel sl
           LEFT JOIN FETCH d.housingType ht
           LEFT JOIN FETCH d.workCity wc
           LEFT JOIN FETCH wc.department wcd
           LEFT JOIN FETCH d.jobPositionType jpt
           LEFT JOIN FETCH d.contractType ct
           LEFT JOIN FETCH d.salaryType st
           WHERE d.id = :id
           """)
    Optional<PersonEvaluatedDetailsEntity> findByIdWithAll(@Param("id") Long id);

    /**
     * Variante JPQL con JOIN FETCH para traer por ID de persona evaluada.
     *
     * @param personEvaluatedId ID de la persona evaluada
     * @return Detalles completos de la persona evaluada
     */
    @Query("""
           SELECT d
           FROM PersonEvaluatedDetailsEntity d
           LEFT JOIN FETCH d.personEvaluated p
           LEFT JOIN FETCH d.civilStatus cs
           LEFT JOIN FETCH d.educationLevel el
           LEFT JOIN FETCH d.residenceCity rc
           LEFT JOIN FETCH rc.department rcd
           LEFT JOIN FETCH d.socioeconomicLevel sl
           LEFT JOIN FETCH d.housingType ht
           LEFT JOIN FETCH d.workCity wc
           LEFT JOIN FETCH wc.department wcd
           LEFT JOIN FETCH d.jobPositionType jpt
           LEFT JOIN FETCH d.contractType ct
           LEFT JOIN FETCH d.salaryType st
           WHERE p.id = :personEvaluatedId
           """)
    Optional<PersonEvaluatedDetailsEntity> findByPersonEvaluatedIdWithAll(@Param("personEvaluatedId") Long personEvaluatedId);
}

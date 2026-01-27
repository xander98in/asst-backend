package com.unicuaca.asst.unicauca_asst.core.batteries_management.infrastructure.adapters.output.persistence.jpa.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.unicuaca.asst.unicauca_asst.core.batteries_management.infrastructure.adapters.output.persistence.jpa.entities.QuestionnaireManagementRecordEntity;

/**
 * Repositorio JPA para {@link QuestionnaireManagementRecordEntity}.
 */
@Repository
public interface QuestionnaireManagementRecordSpringJpaRepository extends JpaRepository<QuestionnaireManagementRecordEntity, Long> {

    /**
     * Obtiene un registro de gestión de cuestionario por su ID.
     * NO fuerza la carga de las relaciones (batteryManagementRecord, questionnaire, status).
     * Se apoya en el lazy loading configurado en la entidad.
     *
     * @param id ID del registro de gestión de cuestionario.
     * @return Registro de gestión de cuestionario envuelto en un Optional.
     */
    Optional<QuestionnaireManagementRecordEntity> findById(Long id);

    /**
     * Obtiene un registro de gestión de cuestionario por su ID,
     * incluyendo la información del registro de gestión de batería,
     * del cuestionario y del estado mediante JOIN FETCH.
     *
     * @param id ID del registro de gestión de cuestionario.
     * @return Registro de gestión de cuestionario con todas sus relaciones, envuelto en un Optional.
     */
    @Query("""
           SELECT r
           FROM QuestionnaireManagementRecordEntity r
           LEFT JOIN FETCH r.batteryManagementRecord br
           LEFT JOIN FETCH r.questionnaire q
           LEFT JOIN FETCH r.status s
           WHERE r.id = :id
           """)
    Optional<QuestionnaireManagementRecordEntity> findByIdWithAll(@Param("id") Long id);

    /**
     * Obtiene un registro de gestión de cuestionario filtrando por:
     * - ID del registro de gestión de cuestionario.
     * - ID del cuestionario asociado.
     * (Sin forzar carga de relaciones)
     *
     * @param id               ID del registro de gestión de cuestionario.
     * @param questionnaireId  ID del cuestionario asociado.
     * @return Registro de gestión de cuestionario envuelto en un Optional.
     */
    Optional<QuestionnaireManagementRecordEntity> findByIdAndQuestionnaire_Id(
            Long id,
            Long questionnaireId
    );

    /**
     * Obtiene un registro de gestión de cuestionario filtrando por:
     * - ID del registro de gestión de cuestionario.
     * - ID del cuestionario asociado.
     * Incluye información de sus relaciones (registro de batería, cuestionario y estado).
     *
     * @param id               ID del registro de gestión de cuestionario.
     * @param questionnaireId  ID del cuestionario asociado.
     * @return Registro de gestión de cuestionario con sus relaciones, envuelto en un Optional.
     */
    @Query("""
           SELECT r
           FROM QuestionnaireManagementRecordEntity r
           LEFT JOIN FETCH r.batteryManagementRecord br
           LEFT JOIN FETCH r.questionnaire q
           LEFT JOIN FETCH r.status s
           WHERE r.id = :id
             AND q.id = :questionnaireId
           """)
    Optional<QuestionnaireManagementRecordEntity> findByIdAndQuestionnaireIdWithAll(
            @Param("id") Long id,
            @Param("questionnaireId") Long questionnaireId
    );

    /**
     * Obtiene un registro de gestión de cuestionario filtrando por:
     * - ID del registro de gestión de cuestionario.
     * - Abreviatura del cuestionario asociado.
     * (Sin forzar carga de relaciones)
     *
     * @param id             ID del registro de gestión de cuestionario.
     * @param abbreviation   Abreviatura del cuestionario (por ejemplo: "ILA", "ILB", "EXT", "EST").
     * @return Registro de gestión de cuestionario envuelto en un Optional.
     */
    Optional<QuestionnaireManagementRecordEntity> findByIdAndQuestionnaire_Abbreviation(
            Long id,
            String abbreviation
    );

    /**
     * Obtiene un registro de gestión de cuestionario filtrando por:
     * - ID del registro de gestión de cuestionario.
     * - Abreviatura del cuestionario asociado.
     * Incluye información de sus relaciones (registro de batería, cuestionario y estado).
     *
     * @param id             ID del registro de gestión de cuestionario.
     * @param abbreviation   Abreviatura del cuestionario (por ejemplo: "ILA", "ILB", "EXT", "EST").
     * @return Registro de gestión de cuestionario con sus relaciones, envuelto en un Optional.
     */
    @Query("""
           SELECT r
           FROM QuestionnaireManagementRecordEntity r
           LEFT JOIN FETCH r.batteryManagementRecord br
           LEFT JOIN FETCH r.questionnaire q
           LEFT JOIN FETCH r.status s
           WHERE r.id = :id
             AND q.abbreviation = :abbreviation
           """)
    Optional<QuestionnaireManagementRecordEntity> findByIdAndQuestionnaireAbbreviationWithAll(
            @Param("id") Long id,
            @Param("abbreviation") String abbreviation
    );
}

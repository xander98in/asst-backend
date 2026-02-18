package com.unicuaca.asst.unicauca_asst.core.batteries_management.infrastructure.adapters.output.persistence.jpa.repositories;

import com.unicuaca.asst.unicauca_asst.core.batteries_management.infrastructure.adapters.output.persistence.jpa.entities.QuestionnaireResponseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repositorio Spring Data JPA para la entidad QuestionnaireResponseEntity.
 *
 * Proporciona operacionea CRUD y de consulta personalizados para recuperar respuestas de cuestionarios.
 */
@Repository
public interface QuestionnaireResponseSpringJpaRepository extends JpaRepository<QuestionnaireResponseEntity,Long> {

    /**
     * Consulta por ID (Sin forzar relaciones, usa lazy loading por defecto).
     *
     * @param id Identificador de la respuesta.
     * @return Respuesta encontrada o vacío si no existe.
     */
    Optional<QuestionnaireResponseEntity> findById(Long id);

    /**
     * Consulta una respuesta por su ID obteniendo todas sus relaciones
     * en una sola consulta a la base de datos mediante JOIN FETCH.
     *
     * @param id Identificador de la respuesta.
     * @return Respuesta con relaciones cargadas.
     */
    @Query("""
           SELECT r
           FROM QuestionnaireResponseEntity r
           JOIN FETCH r.questionnaireManagementRecord qmr
           JOIN FETCH r.question q
           JOIN FETCH r.answerOption ao
           WHERE r.id = :id
           """)
    Optional<QuestionnaireResponseEntity> findByIdWithAll(@Param("id") Long id);

    /**
     * Consulta por ID de registro y pregunta (Sin forzar relaciones).
     */
    Optional<QuestionnaireResponseEntity> findByQuestionnaireManagementRecord_IdAndQuestion_Id(Long recordId, Long questionId);

    /**
     * Consulta una respuesta por el ID del registro de gestión de cuestionario
     * y el ID de la pregunta, obteniendo todas sus relaciones mediante JOIN FETCH.
     *
     * @param recordId   ID del registro de gestión de cuestionario.
     * @param questionId ID de la pregunta.
     * @return Respuesta con relaciones cargadas.
     */
    @Query("""
           SELECT r
           FROM QuestionnaireResponseEntity r
           JOIN FETCH r.questionnaireManagementRecord qmr
           JOIN FETCH r.question q
           JOIN FETCH r.answerOption ao
           WHERE qmr.id = :recordId
             AND q.id = :questionId
           """)
    Optional<QuestionnaireResponseEntity> findByRecordIdAndQuestionIdWithAll(
        @Param("recordId") Long recordId,
        @Param("questionId") Long questionId
    );

    /**
     * Verifica si existe una respuesta para un registro de gestión de cuestionario
     * y una pregunta específica. (No requiere JOIN FETCH al ser un booleano).
     *
     * @param recordId   ID del registro de gestión de cuestionario.
     * @param questionId ID de la pregunta.
     * @return true si existe, false en caso contrario.
     */
    boolean existsByQuestionnaireManagementRecord_IdAndQuestion_Id(Long recordId, Long questionId);

    /**
     * Verifica si existe una respuesta para un registro de gestión y una pregunta específica.
     *
     * @param recordId ID del registro de gestión de cuestionario.
     * @param questionId ID de la pregunta.
     * @return true si existe, false si no.
     */
    @Query("""
           SELECT CASE WHEN COUNT(r) > 0 THEN TRUE ELSE FALSE END
           FROM QuestionnaireResponseEntity r
           WHERE r.questionnaireManagementRecord.id = :recordId
             AND r.question.id = :questionId
           """)
    boolean existsByRecordIdAndQuestionId(
        @Param("recordId") Long recordId,
        @Param("questionId") Long questionId
    );

    /**
     * Elimina masivamente todas las respuestas de un registro de gestión.
     * Usa @Modifying para optimizar el rendimiento (1 sola consulta).
     *
     * @param recordId ID del registro de gestión de cuestionario.
     */
    @Modifying(clearAutomatically = true)
    @Query("DELETE FROM QuestionnaireResponseEntity r WHERE r.questionnaireManagementRecord.id = :recordId")
    void deleteByQuestionnaireManagementRecord_Id(@Param("recordId") Long recordId);

    /**
     * Obtiene todas las respuestas de un registro de gestión de cuestionario específico, cargando
     * las relaciones (Pregunta, Opción de respuesta) ansiosamente para optimizar la lectura.
     *
     * @param recordId ID del registro de gestión de cuestionario.
     * @return Lista de entidades de respuesta.
     */
    @Query("""
           SELECT r
           FROM QuestionnaireResponseEntity r
           JOIN FETCH r.question q
           JOIN FETCH r.answerOption ao
           JOIN FETCH r.questionnaireManagementRecord qmr
           WHERE qmr.id = :recordId
           ORDER BY q.order ASC
           """)
    List<QuestionnaireResponseEntity> findAllByQuestionnaireManagementRecordIdWithAll(@Param("recordId") Long recordId);
}

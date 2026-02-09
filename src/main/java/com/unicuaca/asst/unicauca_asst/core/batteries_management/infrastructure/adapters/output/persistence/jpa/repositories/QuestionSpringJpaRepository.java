package com.unicuaca.asst.unicauca_asst.core.batteries_management.infrastructure.adapters.output.persistence.jpa.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.unicuaca.asst.unicauca_asst.core.batteries_management.infrastructure.adapters.output.persistence.jpa.entities.QuestionEntity;

/**
 * Repositorio JPA para la entidad {@link QuestionEntity}.
 *
 * Proporciona operaciones CRUD básicas y consultas especializadas
 * para manejar preguntas asociadas a cuestionarios.
 */
@Repository
public interface QuestionSpringJpaRepository extends JpaRepository<QuestionEntity, Long> {

    // ================== SIN INFORMACIÓN DEL CUESTIONARIO ==================

    /**
     * Obtiene una pregunta por su ID sin hacer fetch explícito del cuestionario.
     * (Equivalente a {@link #findById(Long)} heredado de JpaRepository).
     *
     * @param id identificador de la pregunta.
     * @return pregunta encontrada o vacío si no existe.
     */
    @Query("SELECT q FROM QuestionEntity q WHERE q.id = :id")
    Optional<QuestionEntity> findByIdWithoutQuestionnaire(@Param("id") Long id);

    /**
     * Lista todas las preguntas sin hacer fetch explícito del cuestionario.
     * (Equivalente a {@link #findAll()} heredado de JpaRepository).
     *
     * @return lista de preguntas.
     */
    @Query("SELECT q FROM QuestionEntity q")
    List<QuestionEntity> findAllWithoutQuestionnaire();

    // ================== CON INFORMACIÓN DEL CUESTIONARIO (JOIN FETCH) ==================

    /**
     * Obtiene una pregunta por su ID cargando también la información del cuestionario asociado.
     *
     * @param id identificador de la pregunta.
     * @return pregunta encontrada con su cuestionario o vacío si no existe.
     */
    @Query("SELECT q FROM QuestionEntity q JOIN FETCH q.questionnaire WHERE q.id = :id")
    Optional<QuestionEntity> findByIdWithQuestionnaire(@Param("id") Long id);

    /**
     * Lista todas las preguntas cargando también la información del cuestionario asociado.
     *
     * @return lista de preguntas con sus cuestionarios.
     */
    @Query("SELECT q FROM QuestionEntity q JOIN FETCH q.questionnaire")
    List<QuestionEntity> findAllWithQuestionnaire();

    /**
     * Obtiene una pregunta por su orden y el ID del cuestionario,
     * cargando también la información del cuestionario asociado.
     *
     * @param order           orden de la pregunta dentro del cuestionario.
     * @param questionnaireId identificador del cuestionario.
     * @return pregunta encontrada con su cuestionario o vacío si no existe.
     */
    @Query("""
           SELECT q
           FROM QuestionEntity q
           JOIN FETCH q.questionnaire qu
           WHERE q.order = :order
             AND qu.id = :questionnaireId
           """)
    Optional<QuestionEntity> findByOrderAndQuestionnaireIdWithQuestionnaire(
            @Param("order") Integer order,
            @Param("questionnaireId") Long questionnaireId
    );

    /**
     * Busca preguntas por ID del cuestionario, ordenadas por el campo 'orden'.
     *
     * @param questionnaireId
     * @return lista de preguntas asociadas al cuestionario con el ID especificado, ordenadas por su campo 'orden' de forma ascendente.
     */
    @Query("SELECT q FROM QuestionEntity q WHERE q.questionnaire.id = :questionnaireId ORDER BY q.order ASC")
    List<QuestionEntity> findByQuestionnaireId(@Param("questionnaireId") Long questionnaireId);

    /**
     * Busca preguntas por Abreviatura del cuestionario, ordenadas por el campo 'orden'.
     *
     * @param abbreviation abreviatura del cuestionario a buscar.
     * @return lista de preguntas asociadas al cuestionario con la abreviatura especificada, ordenadas por su campo 'orden' de forma ascendente.
     */
    @Query("SELECT q FROM QuestionEntity q WHERE q.questionnaire.abbreviation = :abbreviation ORDER BY q.order ASC")
    List<QuestionEntity> findByQuestionnaireAbbreviation(@Param("abbreviation") String abbreviation);
}

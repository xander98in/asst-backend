package com.unicuaca.asst.unicauca_asst.core.batteries_management.infrastructure.adapters.output.persistence.jpa.repositories;

import java.util.Collection;
import java.util.List;
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

    /**
     * Verifica si existe al menos un registro de gestión de cuestionario
     * asociado a un registro de gestión de batería específico
     * y cuyo cuestionario tenga una abreviatura dentro del conjunto proporcionado.
     *
     * @param batteryManagementRecordId ID del registro de gestión de batería.
     * @param abbreviations             Colección de abreviaturas de cuestionarios.
     * @return {@code true} si existe al menos un registro que cumple los criterios, {@code false} en caso contrario.
     */
    @Query("""
        SELECT CASE WHEN COUNT(r) > 0 THEN TRUE ELSE FALSE END
        FROM QuestionnaireManagementRecordEntity r
        WHERE r.batteryManagementRecord.id = :batteryManagementRecordId
          AND r.questionnaire.abbreviation IN :abbreviations
    """)
    boolean existsByBatteryManagementRecordIdAndQuestionnaireAbbreviations(
        @Param("batteryManagementRecordId") Long batteryManagementRecordId,
        @Param("abbreviations") Collection<String> abbreviations
    );

    /**
     * Verifica si existe al menos un registro de gestión de cuestionario
     * asociado a un registro de gestión de batería específico.
     *
     * @param batteryManagementRecordId ID del registro de gestión de batería.
     * @return {@code true} si existe al menos un registro que cumple el criterio, {@code false} en caso contrario.
     */
    boolean existsByBatteryManagementRecord_Id(Long batteryManagementRecordId);

    /**
     * Obtiene un registro de gestión de cuestionario por el ID del registro de gestión de batería
     * y la abreviatura del cuestionario, incluyendo la información del registro de gestión de batería,
     * del cuestionario y del estado mediante JOIN FETCH.
     *
     * @param batteryManagementRecordId ID del registro de gestión de batería.
     * @param abbreviation              Abreviatura del cuestionario.
     * @return Registro de gestión de cuestionario con todas sus relaciones, envuelto en un Optional.
     */
    @Query("""
       SELECT r
       FROM QuestionnaireManagementRecordEntity r
       LEFT JOIN FETCH r.batteryManagementRecord br
       LEFT JOIN FETCH r.questionnaire q
       LEFT JOIN FETCH r.status s
       WHERE br.id = :batteryManagementRecordId
         AND q.abbreviation = :abbreviation
       """)
    Optional<QuestionnaireManagementRecordEntity> findByBatteryManagementRecordIdAndQuestionnaireAbbreviationWithAll(
        @Param("batteryManagementRecordId") Long batteryManagementRecordId,
        @Param("abbreviation") String abbreviation
    );

    /**
     * Verifica existencia por los IDs de las relaciones.
     * Spring Data JPA genera la query automáticamente por el nombre del método.
     */
    boolean existsByBatteryManagementRecord_IdAndQuestionnaire_Id(Long batteryId, Long questionnaireId);

    /**
     * Obtiene la lista de abreviaturas de los cuestionarios asociados a una batería
     * que se encuentren en un estado específico (ej: "Diligenciado").
     *
     * @param batteryId ID del registro de gestión de batería.
     * @param statusName Nombre del estado (por ejemplo: "Diligenciado").
     * @return Lista de abreviaturas de cuestionarios que cumplen los criterios.
     */
    @Query("""
        SELECT q.abbreviation
        FROM QuestionnaireManagementRecordEntity r
        JOIN r.questionnaire q
        JOIN r.status s
        WHERE r.batteryManagementRecord.id = :batteryId
          AND s.name = :statusName
    """)
    List<String> findAbbreviationsByBatteryIdAndStatusName(
        @Param("batteryId") Long batteryId,
        @Param("statusName") String statusName
    );
}

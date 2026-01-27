package com.unicuaca.asst.unicauca_asst.core.batteries_management.infrastructure.adapters.output.persistence.jpa.repositories;

import com.unicuaca.asst.unicauca_asst.core.batteries_management.infrastructure.adapters.output.persistence.jpa.entities.BatteryManagementRecordEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface BatteryManagementRecordSpringJpaRepository extends JpaRepository<BatteryManagementRecordEntity, Long> {

    @Query("""
       SELECT r
       FROM BatteryManagementRecordEntity r
       JOIN FETCH r.personEvaluated
       JOIN FETCH r.status
       WHERE r.id = :id
       """)
    Optional<BatteryManagementRecordEntity> findByIdWithRelations(@Param("id") Long id);

    @Query("""
       SELECT DISTINCT r
       FROM BatteryManagementRecordEntity r
       JOIN FETCH r.personEvaluated p
       JOIN FETCH r.status s
       WHERE p.id = :personId
       """)
    List<BatteryManagementRecordEntity> findAllByPersonEvaluatedId(@Param("personId") Long personId);

    /**
     * Verifica si existe un registro de gestión de baterías asociado a una persona evaluada específica.
     *
     * @param personId el ID de la persona evaluada
     * @return true si existe un registro asociado, false en caso contrario
     */
    boolean existsByPersonEvaluatedId(Long personId);

    /**
     * Lista registros de gestión de baterías paginados, excluyendo el estado "Cerrado",
     * ordenados por fecha de creación DESC y cargando relaciones principales.
     *
     * @param excludedStatus nombre del estado a excluir (ej: "Cerrado")
     * @param pageable paginación + orden
     */
    @Query(
        value = """
            SELECT r
            FROM BatteryManagementRecordEntity r
            JOIN FETCH r.personEvaluated p
            JOIN FETCH p.identificationType it
            JOIN FETCH r.status s
            WHERE LOWER(s.name) <> LOWER(:excludedStatus)
            """,
        countQuery = """
            SELECT COUNT(r)
            FROM BatteryManagementRecordEntity r
            JOIN r.personEvaluated p
            JOIN p.identificationType it
            JOIN r.status s
            WHERE LOWER(s.name) <> LOWER(:excludedStatus)
            """
    )
    Page<BatteryManagementRecordEntity> listPagedExcludingStatus(@Param("excludedStatus") String excludedStatus, Pageable pageable);

    /**
     * Lista registros de gestión de baterías paginados, excluyendo un estado específico
     * y filtrando por prefijo de número de identificación,
     * ordenados por fecha de creación DESC y cargando relaciones principales.
     *
     * @param excludedStatus nombre del estado a excluir
     * @param prefix prefijo del número de identificación para filtrar (puede ser nulo o vacío)
     * @param pageable paginación + orden
     */
    @Query(
        value = """
        SELECT r
        FROM BatteryManagementRecordEntity r
        JOIN FETCH r.personEvaluated p
        JOIN FETCH p.identificationType it
        JOIN FETCH r.status s
        WHERE LOWER(s.name) <> LOWER(:excludedStatus)
          AND (
                :prefix IS NULL
                OR :prefix = ''
                OR p.identificationNumber LIKE CONCAT(:prefix, '%')
          )
        """,
        countQuery = """
        SELECT COUNT(r)
        FROM BatteryManagementRecordEntity r
        JOIN r.personEvaluated p
        JOIN p.identificationType it
        JOIN r.status s
        WHERE LOWER(s.name) <> LOWER(:excludedStatus)
          AND (
                :prefix IS NULL
                OR :prefix = ''
                OR p.identificationNumber LIKE CONCAT(:prefix, '%')
          )
        """
    )
    Page<BatteryManagementRecordEntity> listPagedByIdentificationPrefixExcludingStatus(
        @Param("excludedStatus") String excludedStatus,
        @Param("prefix") String prefix,
        Pageable pageable
    );

    /**
     * Lista registros de gestión de baterías paginados, excluyendo un estado específico
     * y filtrando por término de búsqueda en número de identificación o nombre del área de trabajo,
     * ordenados por criterio (fecha creación) DESC y cargando relaciones principales.
     *
     * @param excludedStatus nombre del estado a excluir
     * @param term término de búsqueda para filtrar (puede ser nulo o vacío)
     * @param pageable paginación + orden
     */
    @Query(
        value = """
        SELECT DISTINCT r
        FROM BatteryManagementRecordEntity r
        JOIN FETCH r.personEvaluated p
        JOIN FETCH p.identificationType it
        JOIN FETCH r.status s
        LEFT JOIN PersonEvaluatedDetailsEntity d
               ON d.batteryManagementRecord = r
        WHERE LOWER(s.name) <> LOWER(:excludedStatus)
          AND (
                :term IS NULL OR :term = ''
                OR p.identificationNumber LIKE CONCAT(:term, '%')
                OR LOWER(d.workAreaName) LIKE CONCAT('%', LOWER(:term), '%')
          )
        """,
        countQuery = """
        SELECT COUNT(DISTINCT r)
        FROM BatteryManagementRecordEntity r
        JOIN r.personEvaluated p
        JOIN p.identificationType it
        JOIN r.status s
        LEFT JOIN PersonEvaluatedDetailsEntity d
               ON d.batteryManagementRecord = r
        WHERE LOWER(s.name) <> LOWER(:excludedStatus)
          AND (
                :term IS NULL OR :term = ''
                OR p.identificationNumber LIKE CONCAT(:term, '%')
                OR LOWER(d.workAreaName) LIKE CONCAT('%', LOWER(:term), '%')
          )
        """
    )
    Page<BatteryManagementRecordEntity> listPagedExcludingStatusWithSearchTerm(
        @Param("excludedStatus") String excludedStatus,
        @Param("term") String term,
        Pageable pageable
    );


}

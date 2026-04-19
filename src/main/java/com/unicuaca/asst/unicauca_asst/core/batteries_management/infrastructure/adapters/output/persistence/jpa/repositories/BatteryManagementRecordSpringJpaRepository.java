package com.unicuaca.asst.unicauca_asst.core.batteries_management.infrastructure.adapters.output.persistence.jpa.repositories;

import com.unicuaca.asst.unicauca_asst.core.batteries_management.infrastructure.adapters.output.persistence.jpa.entities.BatteryManagementRecordEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

/**
 * Repositorio JPA para la entidad {@link BatteryManagementRecordEntity}.
 *
 * <p>Expone operaciones de acceso a datos sobre la tabla {@code registros_gestion_baterias},
 * incluyendo consultas paginadas por estado, por término de búsqueda y por prefijo de
 * identificación. Extiende {@link JpaSpecificationExecutor} para soportar consultas dinámicas
 * basadas en especificaciones JPA.</p>
 */
public interface BatteryManagementRecordSpringJpaRepository
    extends JpaRepository<BatteryManagementRecordEntity, Long>,
            JpaSpecificationExecutor<BatteryManagementRecordEntity> {

    /**
     * Obtiene un registro de gestión de batería por su ID, cargando las relaciones con
     * la persona evaluada y el estado mediante JOIN FETCH.
     *
     * @param id ID del registro de gestión de batería.
     * @return registro con sus relaciones cargadas envuelto en un Optional, o vacío si no existe.
     */
    @Query("""
       SELECT r
       FROM BatteryManagementRecordEntity r
       JOIN FETCH r.personEvaluated
       JOIN FETCH r.status
       WHERE r.id = :id
       """)
    Optional<BatteryManagementRecordEntity> findByIdWithRelations(@Param("id") Long id);

    /**
     * Lista todos los registros de gestión de batería asociados a una persona evaluada,
     * cargando las relaciones con la persona y el estado mediante JOIN FETCH.
     *
     * @param personId ID de la persona evaluada.
     * @return lista de registros de gestión de batería asociados a la persona.
     */
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
     * Verifica si existe un registro de gestión de baterías asociado a una persona evaluada específica y en alguno de los estados indicados.
     *
     * @param personId el ID de la persona evaluada
     * @param statusNames lista de nombres de estados
     * @return true si existe un registro asociado con alguno de los estados, false en caso contrario
     */
    boolean existsByPersonEvaluatedIdAndStatusNameIn(Long personId, List<String> statusNames);

    /**
     * Lista registros de gestión de baterías paginados por su nombre de estado,
     * ordenados por fecha de creación DESC y cargando relaciones principales.
     *
     * @param statusName nombre del estado a filtrar (ej: "Cerrado")
     * @param pageable paginación + orden
     * @return página de registros con las relaciones cargadas
     */
    @Query(
        value = """
            SELECT r
            FROM BatteryManagementRecordEntity r
            JOIN FETCH r.personEvaluated p
            JOIN FETCH p.identificationType it
            JOIN FETCH r.status s
            WHERE LOWER(s.name) = LOWER(:statusName)
            """,
        countQuery = """
            SELECT COUNT(r)
            FROM BatteryManagementRecordEntity r
            JOIN r.personEvaluated p
            JOIN p.identificationType it
            JOIN r.status s
            WHERE LOWER(s.name) = LOWER(:statusName)
            """
    )
    Page<BatteryManagementRecordEntity> listPagedByStatus(@Param("statusName") String statusName, Pageable pageable);

    /**
     * Lista registros de gestión de baterías paginados por un estado específico y filtrando por término de búsqueda.
     *
     * @param statusName nombre del estado a filtrar
     * @param term término de búsqueda (identificación o área)
     * @param pageable paginación + orden
     * @return página de registros con las relaciones cargadas
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
        WHERE LOWER(s.name) = LOWER(:statusName)
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
        WHERE LOWER(s.name) = LOWER(:statusName)
          AND (
                :term IS NULL OR :term = ''
                OR p.identificationNumber LIKE CONCAT(:term, '%')
                OR LOWER(d.workAreaName) LIKE CONCAT('%', LOWER(:term), '%')
          )
        """
    )
    Page<BatteryManagementRecordEntity> listPagedByStatusWithSearchTerm(
        @Param("statusName") String statusName,
        @Param("term") String term,
        Pageable pageable
    );

    /**
     * Lista registros de gestión de baterías paginados, excluyendo el estado "Cerrado",
     * ordenados por fecha de creación DESC y cargando relaciones principales.
     *
     * @param excludedStatus nombre del estado a excluir (ej: "Cerrado")
     * @param pageable paginación + orden
     * @return página de registros con las relaciones cargadas
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
     * @return página de registros con las relaciones cargadas
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
     * @return página de registros con las relaciones cargadas
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

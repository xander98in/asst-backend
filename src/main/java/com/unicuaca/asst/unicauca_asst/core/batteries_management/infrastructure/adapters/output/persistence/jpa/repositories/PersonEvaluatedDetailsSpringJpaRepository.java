package com.unicuaca.asst.unicauca_asst.core.batteries_management.infrastructure.adapters.output.persistence.jpa.repositories;

import com.unicuaca.asst.unicauca_asst.core.batteries_management.infrastructure.adapters.output.persistence.jpa.entities.PersonEvaluatedDetailsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

/**
 * Repositorio JPA para {@link PersonEvaluatedDetailsEntity}.
 *
 * <p>Define operaciones CRUD básicas sobre la tabla {@code detalles_persona_evaluada},
 * junto con variantes JPQL con {@code JOIN FETCH} para cargar de forma ansiosa las
 * relaciones sociodemográficas (género, estado civil, nivel educativo, ciudades con
 * sus departamentos, estrato, tipo de vivienda, tipo de cargo, tipo de contrato y
 * tipo de salario) y, cuando aplica, el registro de gestión de batería y la persona
 * evaluada asociada.</p>
 */
public interface PersonEvaluatedDetailsSpringJpaRepository extends JpaRepository<PersonEvaluatedDetailsEntity, Long> {

    /**
     * Verifica si existe un registro de detalles por ID de registro de gestión de batería.
     *
     * @param BatteryManagementRecordId ID del registro de gestión de batería
     * @return true si existe un registro de detalles, false en caso contrario
     */
    boolean existsByBatteryManagementRecord_Id(Long BatteryManagementRecordId);

    /**
     * Obtiene los detalles por ID de registro de gestión de batería. (sin forzar carga de relaciones)
     *
     * @param batteryManagementRecordId ID del registro de gestión de batería
     * @return Detalles de la persona evaluada envueltos en un Optional
     */
    Optional<PersonEvaluatedDetailsEntity> findByBatteryManagementRecord_Id(Long batteryManagementRecordId);

    /**
     * Variante JPQL para verificar existencia por ID de registro de gestión de batería.
     *
     * @param batteryManagementRecordId ID del registro de gestión de batería
     * @return true si existe un registro de detalles, false en caso contrario
     */
    @Query("""
       SELECT CASE WHEN COUNT(d) > 0 THEN TRUE ELSE FALSE END
       FROM PersonEvaluatedDetailsEntity d
       WHERE d.batteryManagementRecord.id = :batteryManagementRecordId
       """)
    boolean existsByBatteryManagementRecordId(@Param("batteryManagementRecordId") Long batteryManagementRecordId);

    /**
     * Variante JPQL para obtener detalles por ID de registro de gestión de batería. (sin forzar carga de relaciones)
     *
     * @param batteryManagementRecordId ID del registro de gestión de batería
     * @return Detalles de la persona evaluada envueltos en un Optional
     */
    @Query("""
       SELECT d
       FROM PersonEvaluatedDetailsEntity d
       WHERE d.batteryManagementRecord.id = :batteryManagementRecordId
       """)
    Optional<PersonEvaluatedDetailsEntity> findByBatteryManagementRecordIdQuery(@Param("batteryManagementRecordId") Long batteryManagementRecordId);

    /**
     * Variante JPQL con JOIN FETCH para traer TODA la información, incluyendo los departamentos
     * de las ciudades (residencia/trabajo) a partir del ID del detalle de persona evaluada.
     *
     * @param id ID del detalle de persona evaluada
     * @return Detalles completos de la persona evaluada
     */
    @Query("""
           SELECT d
           FROM PersonEvaluatedDetailsEntity d
           LEFT JOIN FETCH d.batteryManagementRecord r
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
           LEFT JOIN FETCH d.gender g
           WHERE d.id = :id
           """)
    Optional<PersonEvaluatedDetailsEntity> findByIdWithAll(@Param("id") Long id);

    /**
     * Variante JPQL con JOIN FETCH para traer por ID de registro de gestión de batería
     *
     * @param batteryManagementRecordId ID de la persona evaluada
     * @return Detalles completos de la persona evaluada
     */
    @Query("""
           SELECT d
           FROM PersonEvaluatedDetailsEntity d
           LEFT JOIN FETCH d.batteryManagementRecord r
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
           LEFT JOIN FETCH d.gender g
           WHERE r.id = :batteryManagementRecordId
           """)
    Optional<PersonEvaluatedDetailsEntity> findByBatteryManagementRecordIdWithAll(@Param("batteryManagementRecordId") Long batteryManagementRecordId);

    /**
     * Obtiene el nombre del área de trabajo por ID de registro de gestión de batería.
     *
     * @param recordId ID del registro de gestión de batería
     * @return Nombre del área de trabajo envuelto en un Optional
     */
    @Query("""
           SELECT d.workAreaName
           FROM PersonEvaluatedDetailsEntity d
           WHERE d.batteryManagementRecord.id = :recordId
           """)
    Optional<String> findWorkAreaNameByBatteryManagementRecordId(@Param("recordId") Long recordId);

    /**
     * Variante JPQL con JOIN FETCH para traer los detalles y sus relaciones auxiliares
     * (género, estado civil, nivel de estudios, ciudades con sus departamentos, estrato,
     * tipo de vivienda, tipo de cargo, tipo de contrato y tipo de salario) a partir
     * del ID del registro de gestión de batería, sin cargar el registro mismo.
     *
     * @param recordId ID del registro de gestión de batería
     * @return detalles de la persona evaluada con sus relaciones auxiliares envueltos en un Optional
     */
    @Query("""
       SELECT d
       FROM PersonEvaluatedDetailsEntity d
       LEFT JOIN FETCH d.gender
       LEFT JOIN FETCH d.civilStatus
       LEFT JOIN FETCH d.educationLevel
       LEFT JOIN FETCH d.residenceCity rc
       LEFT JOIN FETCH rc.department
       LEFT JOIN FETCH d.socioeconomicLevel
       LEFT JOIN FETCH d.housingType
       LEFT JOIN FETCH d.workCity wc
       LEFT JOIN FETCH wc.department
       LEFT JOIN FETCH d.jobPositionType
       LEFT JOIN FETCH d.contractType
       LEFT JOIN FETCH d.salaryType
       WHERE d.batteryManagementRecord.id = :recordId
        """)
    Optional<PersonEvaluatedDetailsEntity> findDetailsWithRelationsWithoutRecordByBatteryManagementRecordId(@Param("recordId") Long recordId);

    /**
     * Variante JPQL con JOIN FETCH para traer los detalles junto con el registro de gestión
     * de batería, la persona evaluada, su tipo de identificación y el estado del registro,
     * además de todas las relaciones auxiliares, a partir del ID del registro de gestión de batería.
     *
     * @param recordId ID del registro de gestión de batería
     * @return detalles completos de la persona evaluada con persona y registro envueltos en un Optional
     */
    @Query("""
       SELECT d
       FROM PersonEvaluatedDetailsEntity d
       JOIN FETCH d.batteryManagementRecord r
       JOIN FETCH r.personEvaluated p
       JOIN FETCH p.identificationType
       JOIN FETCH r.status
       LEFT JOIN FETCH d.gender
       LEFT JOIN FETCH d.civilStatus
       LEFT JOIN FETCH d.educationLevel
       LEFT JOIN FETCH d.residenceCity rc
       LEFT JOIN FETCH rc.department
       LEFT JOIN FETCH d.socioeconomicLevel
       LEFT JOIN FETCH d.housingType
       LEFT JOIN FETCH d.workCity wc
       LEFT JOIN FETCH wc.department
       LEFT JOIN FETCH d.jobPositionType
       LEFT JOIN FETCH d.contractType
       LEFT JOIN FETCH d.salaryType
       WHERE r.id = :recordId
       """)
    Optional<PersonEvaluatedDetailsEntity> findDetailsWithRecordAndPersonByBatteryManagementRecordId(@Param("recordId") Long recordId);

    /**
     * Variante JPQL con JOIN FETCH para traer toda la información, incluyendo el registro de gestión de batería
     * y la persona evaluada, a partir del ID del detalle de persona evaluada.
     *
     * @param id ID del detalle de persona evaluada
     * @return Detalles completos de la persona evaluada
     */
    @Query("""
       SELECT d
       FROM PersonEvaluatedDetailsEntity d
       JOIN FETCH d.batteryManagementRecord r
       JOIN FETCH r.personEvaluated p
       JOIN FETCH p.identificationType
       JOIN FETCH r.status
       LEFT JOIN FETCH d.gender
       LEFT JOIN FETCH d.civilStatus
       LEFT JOIN FETCH d.educationLevel
       LEFT JOIN FETCH d.residenceCity rc
       LEFT JOIN FETCH rc.department
       LEFT JOIN FETCH d.socioeconomicLevel
       LEFT JOIN FETCH d.housingType
       LEFT JOIN FETCH d.workCity wc
       LEFT JOIN FETCH wc.department
       LEFT JOIN FETCH d.jobPositionType
       LEFT JOIN FETCH d.contractType
       LEFT JOIN FETCH d.salaryType
       WHERE d.id = :id
       """)
    Optional<PersonEvaluatedDetailsEntity> findDetailsWithRecordAndPersonById(@Param("id") Long id);
}

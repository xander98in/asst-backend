package com.unicuaca.asst.unicauca_asst.core.batteries_management.infrastructure.adapters.output.persistence.jpa.repositories;

import com.unicuaca.asst.unicauca_asst.core.batteries_management.infrastructure.adapters.output.persistence.jpa.entities.BatteryManagementRecordEntity;
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
}

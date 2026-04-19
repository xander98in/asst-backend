package com.unicuaca.asst.unicauca_asst.core.reports.infrastructure.adapters.output.persistence.jpa.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.unicuaca.asst.unicauca_asst.core.reports.infrastructure.adapters.output.persistence.jpa.entities.AnalysisSpaceBatteryEntity;
import com.unicuaca.asst.unicauca_asst.core.reports.infrastructure.adapters.output.persistence.jpa.entities.AnalysisSpaceBatteryId;

/**
 * Repositorio Spring Data JPA para la entidad {@link AnalysisSpaceBatteryEntity}.
 */
@Repository
public interface AnalysisSpaceBatterySpringJpaRepository extends JpaRepository<AnalysisSpaceBatteryEntity, AnalysisSpaceBatteryId> {

    /**
     * Verifica si una batería ya está asociada a un espacio de análisis.
     *
     * @param spaceId          ID del espacio de análisis
     * @param batteryRecordId  ID del registro de batería
     * @return {@code true} si la batería ya está asociada al espacio
     */
    boolean existsByAnalysisSpace_IdAndBatteryManagementRecordId(Long spaceId, Long batteryRecordId);

    /**
     * Elimina la asociación entre un espacio de análisis y una batería específica.
     *
     * @param spaceId          ID del espacio de análisis
     * @param batteryRecordId  ID del registro de batería a desasociar
     */
    @Modifying
    @Query("DELETE FROM AnalysisSpaceBatteryEntity b WHERE b.analysisSpace.id = :spaceId AND b.batteryManagementRecordId = :batteryRecordId")
    void deleteByAnalysisSpace_IdAndBatteryManagementRecordId(@Param("spaceId") Long spaceId, @Param("batteryRecordId") Long batteryRecordId);
}

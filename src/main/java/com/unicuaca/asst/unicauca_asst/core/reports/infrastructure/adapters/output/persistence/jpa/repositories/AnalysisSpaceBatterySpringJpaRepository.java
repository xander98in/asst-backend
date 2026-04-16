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

    boolean existsByAnalysisSpace_IdAndBatteryManagementRecordId(Long spaceId, Long batteryRecordId);

    @Modifying
    @Query("DELETE FROM AnalysisSpaceBatteryEntity b WHERE b.analysisSpace.id = :spaceId AND b.batteryManagementRecordId = :batteryRecordId")
    void deleteByAnalysisSpace_IdAndBatteryManagementRecordId(@Param("spaceId") Long spaceId, @Param("batteryRecordId") Long batteryRecordId);
}

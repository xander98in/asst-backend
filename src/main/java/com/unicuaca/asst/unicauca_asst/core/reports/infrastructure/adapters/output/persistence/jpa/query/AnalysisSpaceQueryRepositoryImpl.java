package com.unicuaca.asst.unicauca_asst.core.reports.infrastructure.adapters.output.persistence.jpa.query;

import java.util.List;
import java.util.Optional;

import com.unicuaca.asst.unicauca_asst.core.reports.domain.models.AnalysisSpace;
import com.unicuaca.asst.unicauca_asst.core.reports.domain.ports.output.AnalysisSpaceQueryRepository;
import com.unicuaca.asst.unicauca_asst.core.reports.infrastructure.adapters.output.persistence.jpa.repositories.AnalysisSpaceBatterySpringJpaRepository;
import com.unicuaca.asst.unicauca_asst.core.reports.infrastructure.adapters.output.persistence.jpa.repositories.AnalysisSpaceSpringJpaRepository;
import com.unicuaca.asst.unicauca_asst.core.reports.infrastructure.adapters.output.persistence.mappers.AnalysisSpacePersistenceMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementación del puerto de salida {@link AnalysisSpaceQueryRepository}.
 *
 * <p>Gestiona las operaciones de lectura sobre espacios de análisis.</p>
 */
@Component
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AnalysisSpaceQueryRepositoryImpl implements AnalysisSpaceQueryRepository {

    private final AnalysisSpaceSpringJpaRepository analysisSpaceJpaRepository;
    private final AnalysisSpaceBatterySpringJpaRepository batteryJpaRepository;
    private final AnalysisSpacePersistenceMapper persistenceMapper;

    @Override
    public List<AnalysisSpace> findAllByCreatorUserId(Long userId) {
        return analysisSpaceJpaRepository.findAllByCreatorUserId(userId)
            .stream()
            .map(persistenceMapper::toDomain)
            .toList();
    }

    @Override
    public Optional<AnalysisSpace> findByIdWithBatteries(Long spaceId) {
        return analysisSpaceJpaRepository.findByIdWithBatteries(spaceId)
            .map(persistenceMapper::toDomain);
    }

    @Override
    public boolean existsById(Long spaceId) {
        return analysisSpaceJpaRepository.existsById(spaceId);
    }

    @Override
    public boolean existsBatteryInSpace(Long spaceId, Long batteryRecordId) {
        return batteryJpaRepository.existsByAnalysisSpace_IdAndBatteryManagementRecordId(spaceId, batteryRecordId);
    }
}

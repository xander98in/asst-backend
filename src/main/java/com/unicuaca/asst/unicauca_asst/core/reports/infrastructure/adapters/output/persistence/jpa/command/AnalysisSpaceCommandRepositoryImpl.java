package com.unicuaca.asst.unicauca_asst.core.reports.infrastructure.adapters.output.persistence.jpa.command;

import java.time.LocalDateTime;

import com.unicuaca.asst.unicauca_asst.core.reports.domain.models.AnalysisSpace;
import com.unicuaca.asst.unicauca_asst.core.reports.domain.ports.output.AnalysisSpaceCommandRepository;
import com.unicuaca.asst.unicauca_asst.core.reports.infrastructure.adapters.output.persistence.jpa.entities.AnalysisSpaceBatteryEntity;
import com.unicuaca.asst.unicauca_asst.core.reports.infrastructure.adapters.output.persistence.jpa.entities.AnalysisSpaceEntity;
import com.unicuaca.asst.unicauca_asst.core.reports.infrastructure.adapters.output.persistence.jpa.repositories.AnalysisSpaceBatterySpringJpaRepository;
import com.unicuaca.asst.unicauca_asst.core.reports.infrastructure.adapters.output.persistence.jpa.repositories.AnalysisSpaceSpringJpaRepository;
import com.unicuaca.asst.unicauca_asst.core.reports.infrastructure.adapters.output.persistence.jpa.repositories.EvaluatorSpringJpaRepository;
import com.unicuaca.asst.unicauca_asst.core.reports.infrastructure.adapters.output.persistence.mappers.AnalysisSpacePersistenceMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementación del puerto de salida {@link AnalysisSpaceCommandRepository}.
 *
 * <p>Gestiona las operaciones de escritura sobre espacios de análisis
 * y sus asociaciones con baterías.</p>
 */
@Component
@RequiredArgsConstructor
@Transactional
public class AnalysisSpaceCommandRepositoryImpl implements AnalysisSpaceCommandRepository {

    private final AnalysisSpaceSpringJpaRepository analysisSpaceJpaRepository;
    private final AnalysisSpaceBatterySpringJpaRepository batteryJpaRepository;
    private final EvaluatorSpringJpaRepository evaluatorJpaRepository;
    private final AnalysisSpacePersistenceMapper persistenceMapper;

    @Override
    public AnalysisSpace save(AnalysisSpace analysisSpace) {
        AnalysisSpaceEntity entity = persistenceMapper.toEntity(analysisSpace);
        entity.setEvaluator(evaluatorJpaRepository.getReferenceById(analysisSpace.getEvaluatorId()));
        AnalysisSpaceEntity saved = analysisSpaceJpaRepository.save(entity);
        return persistenceMapper.toDomain(saved);
    }

    @Override
    public void addBatteryToSpace(Long spaceId, Long batteryRecordId) {
        AnalysisSpaceEntity spaceRef = analysisSpaceJpaRepository.getReferenceById(spaceId);
        AnalysisSpaceBatteryEntity batteryEntity = AnalysisSpaceBatteryEntity.builder()
            .analysisSpace(spaceRef)
            .batteryManagementRecordId(batteryRecordId)
            .addedAt(LocalDateTime.now())
            .build();
        batteryJpaRepository.save(batteryEntity);
    }

    @Override
    public void removeBatteryFromSpace(Long spaceId, Long batteryRecordId) {
        batteryJpaRepository.deleteByAnalysisSpace_IdAndBatteryManagementRecordId(spaceId, batteryRecordId);
    }

    @Override
    public void deleteById(Long spaceId) {
        analysisSpaceJpaRepository.deleteById(spaceId);
    }

    @Override
    public boolean existsByNameAndCreatorUserId(String name, Long userId) {
        return analysisSpaceJpaRepository.existsByNameAndCreatorUserId(name, userId);
    }

    @Override
    public int countByCreatorUserId(Long userId) {
        return analysisSpaceJpaRepository.countByCreatorUserId(userId);
    }
}

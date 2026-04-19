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
import org.springframework.stereotype.Repository;

/**
 * Implementación del puerto de salida {@link AnalysisSpaceCommandRepository}.
 *
 * <p>Gestiona las operaciones de escritura sobre espacios de análisis
 * y sus asociaciones con baterías.</p>
 */
@Repository
@RequiredArgsConstructor
public class AnalysisSpaceCommandRepositoryImpl implements AnalysisSpaceCommandRepository {

    private final AnalysisSpaceSpringJpaRepository analysisSpaceJpaRepository;
    private final AnalysisSpaceBatterySpringJpaRepository batteryJpaRepository;
    private final EvaluatorSpringJpaRepository evaluatorJpaRepository;
    private final AnalysisSpacePersistenceMapper persistenceMapper;

    /**
     * Persiste un espacio de análisis (creación o actualización).
     *
     * @param analysisSpace espacio de análisis a guardar
     * @return el espacio de análisis persistido
     */
    @Override
    public AnalysisSpace save(AnalysisSpace analysisSpace) {
        AnalysisSpaceEntity entity = persistenceMapper.toEntity(analysisSpace);
        entity.setEvaluator(evaluatorJpaRepository.getReferenceById(analysisSpace.getEvaluatorId()));
        AnalysisSpaceEntity saved = analysisSpaceJpaRepository.save(entity);
        return persistenceMapper.toDomain(saved);
    }

    /**
     * Agrega una batería a un espacio de análisis.
     *
     * @param spaceId             ID del espacio de análisis
     * @param batteryRecordId     ID del registro de batería
     */
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

    /**
     * Remueve una batería de un espacio de análisis.
     *
     * @param spaceId             ID del espacio de análisis
     * @param batteryRecordId     ID del registro de batería
     */
    @Override
    public void removeBatteryFromSpace(Long spaceId, Long batteryRecordId) {
        batteryJpaRepository.deleteByAnalysisSpace_IdAndBatteryManagementRecordId(spaceId, batteryRecordId);
    }

    /**
     * Elimina un espacio de análisis por su ID (cascade elimina las asociaciones).
     *
     * @param spaceId ID del espacio de análisis
     */
    @Override
    public void deleteById(Long spaceId) {
        analysisSpaceJpaRepository.deleteById(spaceId);
    }

    /**
     * Verifica si existe un espacio con el mismo nombre para el usuario dado.
     *
     * @param name   nombre del espacio
     * @param userId ID del usuario creador
     * @return true si ya existe un espacio con ese nombre para el usuario
     */
    @Override
    public boolean existsByNameAndCreatorUserId(String name, Long userId) {
        return analysisSpaceJpaRepository.existsByNameAndCreatorUserId(name, userId);
    }

    /**
     * Cuenta la cantidad de espacios de análisis de un usuario.
     *
     * @param userId ID del usuario creador
     * @return cantidad de espacios del usuario
     */
    @Override
    public int countByCreatorUserId(Long userId) {
        return analysisSpaceJpaRepository.countByCreatorUserId(userId);
    }
}

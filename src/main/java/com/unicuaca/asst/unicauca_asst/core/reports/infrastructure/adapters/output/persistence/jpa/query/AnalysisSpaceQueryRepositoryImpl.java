package com.unicuaca.asst.unicauca_asst.core.reports.infrastructure.adapters.output.persistence.jpa.query;

import java.util.List;
import java.util.Optional;

import com.unicuaca.asst.unicauca_asst.core.reports.domain.models.AnalysisSpace;
import com.unicuaca.asst.unicauca_asst.core.reports.domain.ports.output.AnalysisSpaceQueryRepository;
import com.unicuaca.asst.unicauca_asst.core.reports.infrastructure.adapters.output.persistence.jpa.repositories.AnalysisSpaceBatterySpringJpaRepository;
import com.unicuaca.asst.unicauca_asst.core.reports.infrastructure.adapters.output.persistence.jpa.repositories.AnalysisSpaceSpringJpaRepository;
import com.unicuaca.asst.unicauca_asst.core.reports.infrastructure.adapters.output.persistence.mappers.AnalysisSpacePersistenceMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

/**
 * Implementación del puerto de salida {@link AnalysisSpaceQueryRepository}.
 *
 * <p>Gestiona las operaciones de lectura sobre espacios de análisis.</p>
 */
@Repository
@RequiredArgsConstructor
public class AnalysisSpaceQueryRepositoryImpl implements AnalysisSpaceQueryRepository {

    private final AnalysisSpaceSpringJpaRepository analysisSpaceJpaRepository;
    private final AnalysisSpaceBatterySpringJpaRepository batteryJpaRepository;
    private final AnalysisSpacePersistenceMapper persistenceMapper;

    /**
     * Lista todos los espacios de análisis creados por un usuario.
     *
     * @param userId ID del usuario creador
     * @return lista de espacios de análisis
     */
    @Override
    public List<AnalysisSpace> findAllByCreatorUserId(Long userId) {
        return analysisSpaceJpaRepository.findAllByCreatorUserId(userId)
            .stream()
            .map(persistenceMapper::toDomain)
            .toList();
    }

    /**
     * Obtiene un espacio de análisis con sus baterías asociadas.
     *
     * @param spaceId ID del espacio de análisis
     * @return Optional con el espacio encontrado
     */
    @Override
    public Optional<AnalysisSpace> findByIdWithBatteries(Long spaceId) {
        return analysisSpaceJpaRepository.findByIdWithBatteries(spaceId)
            .map(persistenceMapper::toDomain);
    }

    /**
     * Verifica si existe un espacio de análisis por su ID.
     *
     * @param spaceId ID del espacio de análisis
     * @return true si existe
     */
    @Override
    public boolean existsById(Long spaceId) {
        return analysisSpaceJpaRepository.existsById(spaceId);
    }

    /**
     * Verifica si una batería ya está asociada a un espacio de análisis.
     *
     * @param spaceId             ID del espacio de análisis
     * @param batteryRecordId     ID del registro de batería
     * @return true si la batería ya está en el espacio
     */
    @Override
    public boolean existsBatteryInSpace(Long spaceId, Long batteryRecordId) {
        return batteryJpaRepository.existsByAnalysisSpace_IdAndBatteryManagementRecordId(spaceId, batteryRecordId);
    }
}

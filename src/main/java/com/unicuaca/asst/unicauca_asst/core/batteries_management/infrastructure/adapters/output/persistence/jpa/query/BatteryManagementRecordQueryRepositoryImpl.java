package com.unicuaca.asst.unicauca_asst.core.batteries_management.infrastructure.adapters.output.persistence.jpa.query;

import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.models.BatteryManagementRecord;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.models.BatteryManagementRecordStatus;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.ports.output.BatteryManagementRecordQueryRepository;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.infrastructure.adapters.output.persistence.jpa.entities.BatteryManagementRecordEntity;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.infrastructure.adapters.output.persistence.jpa.repositories.BatteryManagementRecordSpringJpaRepository;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.infrastructure.adapters.output.persistence.jpa.repositories.BatteryManagementRecordStatusSpringJpaRepository;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.infrastructure.adapters.output.persistence.mappers.BatteryManagementRecordPersistenceMapper;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.infrastructure.adapters.output.persistence.mappers.BatteryManagementRecordStatusPersistenceMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
@Transactional
public class BatteryManagementRecordQueryRepositoryImpl implements BatteryManagementRecordQueryRepository {

    private final BatteryManagementRecordSpringJpaRepository batteryManagementRecordSpringJpaRepository;
    private final BatteryManagementRecordPersistenceMapper batteryManagementRecordPersistenceMapper;
    private final BatteryManagementRecordStatusSpringJpaRepository batteryManagementRecordStatusSpringJpaRepository;
    private final BatteryManagementRecordStatusPersistenceMapper batteryManagementRecordStatusPersistenceMapper;

    /**
     * Busca un registro de gestión de batería por su identificador y lo transforma a su representación de dominio.
     * @param id identificador del registro de gestión de batería
     * @return un {@link Optional} con el registro encontrado o vacío si no existe
     */
    @Override
    public Optional<BatteryManagementRecord> getBatteryManagementRecordById(Long id) {
        return batteryManagementRecordSpringJpaRepository.findByIdWithRelations(id)
                .map(batteryManagementRecordPersistenceMapper::toDomain);
    }

    /**
     * Busca un estado de registro de gestión de batería por su nombre y lo transforma a su representación de dominio.
     * @param name nombre del estado del registro de gestión de batería
     * @return un {@link Optional} con el estado encontrado o vacío si no existe
     */
    @Override
    public Optional<BatteryManagementRecordStatus> getBatteryManagementRecordStatudByName(String name) {
        return batteryManagementRecordStatusSpringJpaRepository.findByName(name)
                .map(batteryManagementRecordStatusPersistenceMapper::toDomain);
    }

    /**
     * Verifica si existe un registro de gestión de batería con el ID proporcionado.
     * @param id identificador del registro de gestión de batería
     * @return true si el registro existe, false si no
     */
    @Override
    public boolean existsById(Long id) {
        return batteryManagementRecordSpringJpaRepository.existsById(id);
    }

    /**
     * Obtiene todos los registros de gestión de batería asociados a una persona evaluada específica.
     * @param personEvaluatedId ID de la persona evaluada
     * @return lista de registros de gestión de batería asociados a la persona evaluada
     */
    @Override
    public List<BatteryManagementRecord> getBatteryManagementRecordsByPersonEvaluatedId(Long personEvaluatedId) {
        List<BatteryManagementRecordEntity> entities = batteryManagementRecordSpringJpaRepository.findAllByPersonEvaluatedId(personEvaluatedId);

        return entities.stream()
                .map(batteryManagementRecordPersistenceMapper::toDomain)
                .toList();
    }

    /**
     * Verifica si existe un registro de gestión de batería asociado a una persona evaluada específica.
     * @param personEvaluatedId ID de la persona evaluada
     * @return true si existe un registro asociado, false en caso contrario
     */
    @Override
    public boolean existsByPersonEvaluatedId(Long personEvaluatedId) {
        return batteryManagementRecordSpringJpaRepository.existsByPersonEvaluatedId(personEvaluatedId);
    }
}

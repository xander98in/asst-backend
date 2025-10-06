package com.unicuaca.asst.unicauca_asst.core.batteries_management.infrastructure.adapters.output.persistence.jpa.command;

import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.models.BatteryManagementRecord;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.ports.output.BatteryManagementRecordCommandRepository;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.infrastructure.adapters.output.persistence.jpa.entities.BatteryManagementRecordEntity;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.infrastructure.adapters.output.persistence.jpa.repositories.BatteryManagementRecordSpringJpaRepository;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.infrastructure.adapters.output.persistence.mappers.BatteryManagementRecordPersistenceMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@RequiredArgsConstructor
@Service
@Transactional
public class BatteryManagementRecordCommandRepositoryImpl implements BatteryManagementRecordCommandRepository {

    private final BatteryManagementRecordSpringJpaRepository batteryManagementRecordSpringJpaRepository;
    private final BatteryManagementRecordPersistenceMapper batteryManagementRecordPersistenceMapper;

    @Override
    public Optional<BatteryManagementRecord> saveBatteryManagementRecord(BatteryManagementRecord record) {
        BatteryManagementRecordEntity entity = batteryManagementRecordPersistenceMapper.toEntity(record);
        BatteryManagementRecordEntity saved = batteryManagementRecordSpringJpaRepository.save(entity);
        BatteryManagementRecordEntity loaded = batteryManagementRecordSpringJpaRepository.findByIdWithRelations(saved.getId())
                .orElse(saved);
        return Optional.of(batteryManagementRecordPersistenceMapper.toDomain(loaded));
    }

    @Override
    public Optional<BatteryManagementRecord> updateBatteryManagementRecord(BatteryManagementRecord record) {
        return Optional.empty();
    }
}

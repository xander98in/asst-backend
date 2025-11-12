package com.unicuaca.asst.unicauca_asst.core.batteries_management.infrastructure.adapters.output.persistence.jpa.query;

import org.springframework.stereotype.Service;

import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.ports.output.PersonEvaluatedDetailsQueryRepository;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.infrastructure.adapters.output.persistence.jpa.repositories.PersonEvaluatedDetailsSpringJpaRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
@Transactional
public class PersonEvaluatedDetailsQueryRepositoryImpl implements PersonEvaluatedDetailsQueryRepository {

    private final PersonEvaluatedDetailsSpringJpaRepository personEvaluatedDetailsSpringJpaRepository;
    
    /**
     * Verifica si ya existe un PersonEvaluatedDetails asociado al registro de gestión de batería dado (relación 1:1).
     *
     * @param batteryManagementRecordId ID del registro de gestión de batería
     * @return true si existe, false si no
     */
    @Override
    public boolean existsByBatteryManagementRecordId(Long batteryManagementRecordId) {
        return personEvaluatedDetailsSpringJpaRepository.existsByBatteryManagementRecord_Id(batteryManagementRecordId);
    }

}

package com.unicuaca.asst.unicauca_asst.core.batteries_management.infrastructure.adapters.output.persistence.jpa.query;

import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.models.BatteryManagementRecordStatus;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.ports.output.BatteryManagementRecordStatusQueryRepository;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.infrastructure.adapters.output.persistence.jpa.repositories.BatteryManagementRecordStatusSpringJpaRepository;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.infrastructure.adapters.output.persistence.mappers.BatteryManagementRecordStatusPersistenceMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Implementación del puerto de salida {@link BatteryManagementRecordStatusQueryRepository}
 * utilizando JPA como tecnología de persistencia.
 *
 * Actúa como adaptador entre el dominio y la infraestructura.
 */
@RequiredArgsConstructor
@Service
@Transactional
public class BatteryManagementRecordStatusQueryRepositoryImpl implements BatteryManagementRecordStatusQueryRepository {

    private final BatteryManagementRecordStatusSpringJpaRepository batteryManagementRecordStatusSpringJpaRepository;
    private final BatteryManagementRecordStatusPersistenceMapper batteryManagementRecordStatusPersistenceMapper;

    /**
     * Consulta un estado de registro de gestión de baterías por su nombre.
     *
     * @param name nombre del estado
     * @return un {@link Optional} con el estado encontrado o vacío si no existe
     */
    @Override
    public Optional<BatteryManagementRecordStatus> getStatusByName(String name) {
        return batteryManagementRecordStatusSpringJpaRepository.findByName(name)
            .map(batteryManagementRecordStatusPersistenceMapper::toDomain);
    }
}

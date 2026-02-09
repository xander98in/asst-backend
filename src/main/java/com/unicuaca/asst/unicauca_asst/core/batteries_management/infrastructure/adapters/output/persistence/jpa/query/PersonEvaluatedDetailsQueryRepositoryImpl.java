package com.unicuaca.asst.unicauca_asst.core.batteries_management.infrastructure.adapters.output.persistence.jpa.query;

import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.models.PersonEvaluatedDetails;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.infrastructure.adapters.output.persistence.mappers.PersonEvaluatedDetailsPersistenceMapper;
import org.springframework.stereotype.Service;

import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.ports.output.PersonEvaluatedDetailsQueryRepository;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.infrastructure.adapters.output.persistence.jpa.repositories.PersonEvaluatedDetailsSpringJpaRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

/**
 * Implementación del puerto de salida {@link PersonEvaluatedDetailsQueryRepository} para la obtención de datos de detalles de personas evaluadas.
 *
 * Esta clase actúa como adaptador de salida en la arquitectura hexagonal y utiliza un repositorio JPA
 * para acceder a la base de datos.
 *
 * <p>Las operaciones definidas aquí son de solo lectura (consultas), y no incluyen lógica de negocio.</p>
 */
@RequiredArgsConstructor
@Service
@Transactional
public class PersonEvaluatedDetailsQueryRepositoryImpl implements PersonEvaluatedDetailsQueryRepository {

    private final PersonEvaluatedDetailsSpringJpaRepository personEvaluatedDetailsSpringJpaRepository;
    private final PersonEvaluatedDetailsPersistenceMapper personEvaluatedDetailsPersistenceMapper;
    
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

    /**
     * Obtiene el nombre del área de trabajo por ID de registro de gestión de batería.
     *
     * @param recordId ID del registro de gestión de batería
     * @return Nombre del área (Optional vacío si no existe detalle asociado)
     */
    @Override
    public Optional<String> getWorkAreaNameByBatteryManagementRecordId(Long recordId) {
        return personEvaluatedDetailsSpringJpaRepository
            .findWorkAreaNameByBatteryManagementRecordId(recordId);
    }

    /**
     * Obtiene el detalle de una persona evaluada asociado a un registro de gestión de batería.
     *
     * @param batteryManagementRecordId ID del registro de gestión de batería
     * @return Detalle de la persona evaluada (Optional vacío si no existe)
     */
    @Override
    public Optional<PersonEvaluatedDetails> getByBatteryManagementRecordId(Long batteryManagementRecordId) {
        return personEvaluatedDetailsSpringJpaRepository
            .findByBatteryManagementRecord_Id(batteryManagementRecordId)
            .map(personEvaluatedDetailsPersistenceMapper::toDomain);
    }

    /**
     * Obtiene los detalles completos por ID del detalle (con relaciones cargadas).
     *
     * @param personEvaluatedDetailsId ID del detalle
     * @return Optional con PersonEvaluatedDetails si existe
     */
    @Override
    public Optional<PersonEvaluatedDetails> getByIdWithAll(Long personEvaluatedDetailsId) {
        return personEvaluatedDetailsSpringJpaRepository
            .findByIdWithAll(personEvaluatedDetailsId)
            .map(personEvaluatedDetailsPersistenceMapper::toDomain);
    }

    /**
     * Verifica si existe un PersonEvaluatedDetails por su ID.
     *
     * @param id ID del detalle
     * @return true si existe, false si no
     */
    @Override
    public boolean existsById(Long id) {
        return this.personEvaluatedDetailsSpringJpaRepository.existsById(id);
    }
}

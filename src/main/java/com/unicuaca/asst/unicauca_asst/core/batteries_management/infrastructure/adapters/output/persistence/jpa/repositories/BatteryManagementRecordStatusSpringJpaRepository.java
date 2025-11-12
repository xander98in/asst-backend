package com.unicuaca.asst.unicauca_asst.core.batteries_management.infrastructure.adapters.output.persistence.jpa.repositories;

import com.unicuaca.asst.unicauca_asst.core.batteries_management.infrastructure.adapters.output.persistence.jpa.entities.BatteryManagementRecordStatusEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Repositorio JPA para la entidad {@link BatteryManagementRecordStatusEntity}.
 * Proporciona métodos para realizar operaciones CRUD y consultas personalizadas en la tabla
 * "estados_registro_gestion_baterias".
 */
public interface BatteryManagementRecordStatusSpringJpaRepository extends JpaRepository<BatteryManagementRecordStatusEntity, Long> {

    /**
     * Busca un estado de registro de gestión de baterías por su nombre.
     *
     * @param name el nombre del estado a buscar
     * @return un Optional que contiene el estado si se encuentra, o vacío si no
     */
    Optional<BatteryManagementRecordStatusEntity> findByName(String name);

    /**
     * Busca un estado de registro de gestión de baterías por su nombre, ignorando mayúsculas y minúsculas.
     *
     * @param name el nombre del estado a buscar
     * @return un Optional que contiene el estado si se encuentra, o vacío si no
     */
    Optional<BatteryManagementRecordStatusEntity> findByNameIgnoreCase(String name);

    /**
     * Verifica si existe un estado de registro de gestión de baterías con el nombre dado.
     *
     * @param name el nombre del estado a verificar
     * @return true si existe, false en caso contrario
     */
    boolean existsByName(String name);

    /**
     * Verifica si existe un estado de registro de gestión de baterías con el nombre dado, ignorando mayúsculas y minúsculas.
     *
     * @param name el nombre del estado a verificar
     * @return true si existe, false en caso contrario
     */
    boolean existsByNameIgnoreCase(String name);
}

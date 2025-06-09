package com.unicuaca.asst.unicauca_asst.core.batteries_management.infrastructure.adapters.output.persistence.jpa.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.unicuaca.asst.unicauca_asst.core.batteries_management.infrastructure.adapters.output.persistence.jpa.entities.GenderEntity;

/**
 * Repositorio JPA para la entidad {@link GenderEntity}.
 *
 * Gestiona el acceso a la información de sexos disponibles en la tabla "sexos".
 */
public interface GenderRepository extends JpaRepository<GenderEntity, Long> {

}

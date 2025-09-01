package com.unicuaca.asst.unicauca_asst.core.batteries_management.infrastructure.adapters.output.persistence.jpa.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.unicuaca.asst.unicauca_asst.core.batteries_management.infrastructure.adapters.output.persistence.jpa.entities.IdentificationTypeEntitys;

/**
 * Repositorio JPA para la entidad {@link IdentificationTypeEntitys}.
 *
 * Permite consultar y administrar los tipos de identificación registrados en la base de datos.
 */
public interface IdentificationTypeRepository extends JpaRepository<IdentificationTypeEntitys, Long> {

}

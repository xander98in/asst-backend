package com.unicuaca.asst.unicauca_asst.core.batteries_management.infrastructure.adapters.output.persistence.jpa.repositories;

import org.springframework.data.repository.CrudRepository;

import com.unicuaca.asst.unicauca_asst.core.batteries_management.infrastructure.adapters.output.persistence.jpa.entities.IdentificationTypeEntity;

/**
 * Repositorio JPA para la entidad {@link IdentificationTypeEntity}.
 *
 * Permite consultar y administrar los tipos de identificación registrados en la base de datos.
 */
public interface IdentificationTypeRepository extends CrudRepository<IdentificationTypeEntity, Long> {

}

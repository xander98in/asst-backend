package com.unicuaca.asst.unicauca_asst.core.batteries_management.infrastructure.adapters.output.persistence.jpa.repositories;

import org.springframework.data.repository.CrudRepository;

import com.unicuaca.asst.unicauca_asst.core.batteries_management.infrastructure.adapters.output.persistence.jpa.entities.PersonEntity;

/**
 * Repositorio JPA para la entidad {@link PersonEntity}.
 *
 * Proporciona operaciones CRUD sobre la tabla "personas".
 */
public interface PersonRepository extends CrudRepository<PersonEntity, Long> {

}

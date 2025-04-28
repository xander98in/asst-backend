package com.unicuaca.asst.unicauca_asst.core.batteries_management.infrastructure.adapters.output.persistence.jpa.repositories;

import org.springframework.data.repository.CrudRepository;

import com.unicuaca.asst.unicauca_asst.core.batteries_management.infrastructure.adapters.output.persistence.jpa.entities.SexoEntity;

public interface SexoRepository extends CrudRepository<SexoEntity, Long> {
    // No additional methods are needed for now, as we are using the default CRUD operations provided by CrudRepository.

}

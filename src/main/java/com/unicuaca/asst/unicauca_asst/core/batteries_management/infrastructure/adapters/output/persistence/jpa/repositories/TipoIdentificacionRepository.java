package com.unicuaca.asst.unicauca_asst.core.batteries_management.infrastructure.adapters.output.persistence.jpa.repositories;

import org.springframework.data.repository.CrudRepository;

import com.unicuaca.asst.unicauca_asst.core.batteries_management.infrastructure.adapters.output.persistence.jpa.entities.TipoIdentificacionEntity;

public interface TipoIdentificacionRepository extends CrudRepository<TipoIdentificacionEntity, Long> {
    // No additional methods are needed for now, as we are using the default CRUD operations provided by CrudRepository.

}

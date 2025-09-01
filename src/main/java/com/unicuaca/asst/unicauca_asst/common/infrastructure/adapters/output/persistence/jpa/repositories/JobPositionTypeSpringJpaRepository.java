package com.unicuaca.asst.unicauca_asst.common.infrastructure.adapters.output.persistence.jpa.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.unicuaca.asst.unicauca_asst.common.infrastructure.adapters.output.persistence.jpa.entities.JobPositionTypeEntity;

public interface JobPositionTypeSpringJpaRepository extends JpaRepository<JobPositionTypeEntity, Long>{
    
    /**
     * Busca todos los tipos de cargo y los ordena por nombre de forma ascendente.
     *
     * @return Lista de tipos de cargo ordenados por nombre.
     */
    List<JobPositionTypeEntity> findAllByOrderByNameAsc();

    /**
     * Verifica si existe un tipo de cargo con el nombre dado.
     *
     * @param name El nombre del tipo de cargo.
     * @return true si existe, false en caso contrario.
     */
    boolean existsByName(String name);
}

package com.unicuaca.asst.unicauca_asst.common.infrastructure.adapters.output.persistence.jpa.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.unicuaca.asst.unicauca_asst.common.infrastructure.adapters.output.persistence.jpa.entities.ContractTypeEntity;

public interface ContractTypeSpringJpaRepository extends JpaRepository<ContractTypeEntity, Long> {

    /**
     * Busca todos los tipos de contrato ordenados por nombre de forma ascendente
     *
     * @return Lista de tipos de contrato
     */
    List<ContractTypeEntity> findAllByOrderByNameAsc();

    /**
     * Verifica si existe un tipo de contrato con el nombre dado
     *
     * @param name Nombre del tipo de contrato
     * @return true si existe, false en caso contrario
     */
    boolean existsByName(String name);
}

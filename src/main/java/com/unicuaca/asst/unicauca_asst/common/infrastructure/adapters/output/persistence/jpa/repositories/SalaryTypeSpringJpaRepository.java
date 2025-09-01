package com.unicuaca.asst.unicauca_asst.common.infrastructure.adapters.output.persistence.jpa.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.unicuaca.asst.unicauca_asst.common.infrastructure.adapters.output.persistence.jpa.entities.SalaryTypeEntity;

public interface SalaryTypeSpringJpaRepository extends JpaRepository<SalaryTypeEntity, Long> {

    /**
     * Busca todos los tipos de salario ordenados por nombre de forma ascendente
     * @return Lista de tipos de salario
     */
    List<SalaryTypeEntity> findAllByOrderByNameAsc();

    /**
     * Verifica si existe un tipo de salario con el nombre dado
     * 
     * @param name Nombre del tipo de salario
     * @return true si existe, false en caso contrario
     */
    boolean existsByName(String name);
}

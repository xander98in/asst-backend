package com.unicuaca.asst.unicauca_asst.common.infrastructure.adapters.output.persistence.jpa.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.unicuaca.asst.unicauca_asst.common.infrastructure.adapters.output.persistence.jpa.entities.CivilStatusEntity;

public interface CivilStatusSpringJpaRepository extends JpaRepository<CivilStatusEntity, Long> {

    /**
     * Busca todos los estados civiles y los ordena por nombre de forma ascendente.
     *
     * @return una lista de entidades de estado civil ordenadas por nombre
     */
    List<CivilStatusEntity> findAllByOrderByNameAsc();

    /**
     * Verifica si existe un estado civil con el nombre dado.
     *
     * @param name el nombre del estado civil a buscar
     * @return true si existe un estado civil con el nombre dado, false en caso contrario
     */
    boolean existsByName(String name);

}

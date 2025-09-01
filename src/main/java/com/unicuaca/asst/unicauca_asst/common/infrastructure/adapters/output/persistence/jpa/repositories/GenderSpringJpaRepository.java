package com.unicuaca.asst.unicauca_asst.common.infrastructure.adapters.output.persistence.jpa.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.unicuaca.asst.unicauca_asst.common.infrastructure.adapters.output.persistence.jpa.entities.GenderEntity;

public interface GenderSpringJpaRepository extends JpaRepository<GenderEntity, Long> {

    /**
     * Busca todos los géneros y los ordena por nombre de forma ascendente.
     *
     * @return una lista de GenderEntity ordenada por nombre
     */
    List<GenderEntity> findAllByOrderByNameAsc();

    /**
     * Verifica si un género con el nombre dado existe.
     *
     * @param name el nombre del género a buscar
     * @return true si el género existe, false en caso contrario
     */
    boolean existsByName(String name);
}

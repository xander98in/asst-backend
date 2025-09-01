package com.unicuaca.asst.unicauca_asst.common.infrastructure.adapters.output.persistence.jpa.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.unicuaca.asst.unicauca_asst.common.infrastructure.adapters.output.persistence.jpa.entities.HousingTypeEntity;

public interface HousingTypeSpringJpaRepository extends JpaRepository<HousingTypeEntity, Long> {

    /**
     * Busca todos los tipos de vivienda y los ordena por nombre de forma ascendente.
     *
     * @return una lista de objetos HousingTypeEntity ordenados por nombre
     */
    List<HousingTypeEntity> findAllByOrderByNameAsc();

    /**
     * Verifica si un tipo de vivienda con el nombre dado existe.
     *
     * @param name el nombre del tipo de vivienda a buscar
     * @return true si existe, false en caso contrario
     */
    boolean existsByName(String name);
}

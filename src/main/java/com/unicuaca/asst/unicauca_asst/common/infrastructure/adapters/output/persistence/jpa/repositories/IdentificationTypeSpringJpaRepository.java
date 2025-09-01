package com.unicuaca.asst.unicauca_asst.common.infrastructure.adapters.output.persistence.jpa.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.unicuaca.asst.unicauca_asst.common.infrastructure.adapters.output.persistence.jpa.entities.IdentificationTypeEntity;

public interface IdentificationTypeSpringJpaRepository extends JpaRepository<IdentificationTypeEntity, Long> {

    /**
     * Obtiene todos los tipos de identificación ordenados por descripción de forma ascendente.
     *
     * @return una lista de entidades IdentificationTypeEntity
     */
    List<IdentificationTypeEntity> findAllByOrderByDescriptionAsc();

    /**
     * Verifica si existe un tipo de identificación con la abreviatura dada.
     *
     * @param abbreviation la abreviatura a verificar
     * @return true si existe, false en caso contrario
     */
    boolean existsByAbbreviation(String abbreviation);

}

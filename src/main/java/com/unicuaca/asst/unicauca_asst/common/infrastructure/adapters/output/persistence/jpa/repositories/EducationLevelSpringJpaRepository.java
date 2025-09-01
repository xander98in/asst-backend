package com.unicuaca.asst.unicauca_asst.common.infrastructure.adapters.output.persistence.jpa.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.unicuaca.asst.unicauca_asst.common.infrastructure.adapters.output.persistence.jpa.entities.EducationLevelEntity;

public interface EducationLevelSpringJpaRepository extends JpaRepository<EducationLevelEntity, Long> {

    /**
     * Obtiene una lista de todos los niveles educativos ordenados por nombre.
     *
     * @return una lista de objetos EducationLevelEntity
     */
    List<EducationLevelEntity> findAllByOrderByNameAsc();

    /**
     * Verifica si existe un nivel educativo con el nombre dado.
     *
     * @param name el nombre del nivel educativo a buscar
     * @return true si existe un nivel educativo con el nombre dado, false en caso contrario
     */
    boolean existsByName(String name);
}

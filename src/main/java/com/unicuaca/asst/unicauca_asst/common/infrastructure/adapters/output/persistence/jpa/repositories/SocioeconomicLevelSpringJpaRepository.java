package com.unicuaca.asst.unicauca_asst.common.infrastructure.adapters.output.persistence.jpa.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.unicuaca.asst.unicauca_asst.common.infrastructure.adapters.output.persistence.jpa.entities.SocioeconomicLevelEntity;

public interface SocioeconomicLevelSpringJpaRepository extends JpaRepository<SocioeconomicLevelEntity, Long> {

    /**
     * Busca todos los niveles socioeconómicos y los ordena por nombre de forma ascendente.
     *
     * @return una lista de entidades de nivel socioeconómico ordenadas por nombre
     */
    List<SocioeconomicLevelEntity> findAllByOrderByNameAsc();

    /**
     * Verifica si existe un nivel socioeconómico con el nombre dado.
     *
     * @param name el nombre del nivel socioeconómico a buscar
     * @return true si existe un nivel socioeconómico con el nombre dado, false en caso contrario
     */
    boolean existsByName(String name);
}

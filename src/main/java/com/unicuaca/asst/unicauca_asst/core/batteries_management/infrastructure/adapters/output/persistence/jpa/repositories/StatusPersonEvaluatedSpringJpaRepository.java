package com.unicuaca.asst.unicauca_asst.core.batteries_management.infrastructure.adapters.output.persistence.jpa.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.unicuaca.asst.unicauca_asst.core.batteries_management.infrastructure.adapters.output.persistence.jpa.entities.StatusPersonEvaluatedEntity;

/**
 * Repositorio JPA para la entidad {@link StatusPersonEvaluatedEntity}.
 *
 * <p>Proporciona operaciones CRUD básicas sobre la tabla {@code estados_persona_evaluada}
 * y una consulta de búsqueda por nombre del estado.</p>
 */
public interface StatusPersonEvaluatedSpringJpaRepository extends JpaRepository<StatusPersonEvaluatedEntity, Long> {

    /**
     * Busca un estado de persona evaluada por su nombre.
     *
     * @param name el nombre del estado a buscar
     * @return un Optional que contiene el estado encontrado, o vacío si no se encuentra
     */
    Optional<StatusPersonEvaluatedEntity> findByName(String name);

}

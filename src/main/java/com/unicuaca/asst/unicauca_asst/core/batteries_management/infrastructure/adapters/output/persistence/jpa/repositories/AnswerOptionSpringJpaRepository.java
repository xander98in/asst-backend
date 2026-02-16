package com.unicuaca.asst.unicauca_asst.core.batteries_management.infrastructure.adapters.output.persistence.jpa.repositories;

import com.unicuaca.asst.unicauca_asst.core.batteries_management.infrastructure.adapters.output.persistence.jpa.entities.AnswerOptionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repositorio Spring Data JPA para la entidad {@link AnswerOptionEntity}.
 *
 * Proporciona métodos para realizar operaciones CRUD y consultas personalizadas.
 * Para manejar el catálogo de opciones de respuesta.
 */
@Repository
public interface AnswerOptionSpringJpaRepository extends JpaRepository<AnswerOptionEntity, Long> {

    /**
     * Busca una opción de respuesta por su orden específico.
     *
     * @param order el orden a buscar.
     * @return un {@link Optional} con la entidad encontrada o vacío si no existe.
     */
    Optional<AnswerOptionEntity> findByOrder(Integer order);

    /**
     * Verifica si existe una opción de respuesta con el orden especificado.
     *
     * @param order el orden a verificar.
     * @return true si existe, false en caso contrario.
     */
    boolean existsByOrder(Integer order);

    /**
     * Busca una opción de respuesta por su valor numérico.
     *
     * @param value el valor a buscar.
     * @return un {@link Optional} con la entidad encontrada.
     */
    Optional<AnswerOptionEntity> findByValue(Integer value);
}

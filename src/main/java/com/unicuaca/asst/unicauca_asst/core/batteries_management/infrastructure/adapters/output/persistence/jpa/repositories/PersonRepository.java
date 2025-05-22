package com.unicuaca.asst.unicauca_asst.core.batteries_management.infrastructure.adapters.output.persistence.jpa.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.unicuaca.asst.unicauca_asst.core.batteries_management.infrastructure.adapters.output.persistence.jpa.entities.PersonEntity;

/**
 * Repositorio JPA para la entidad {@link PersonEntity}.
 *
 * Proporciona operaciones CRUD sobre la tabla "personas".
 */
public interface PersonRepository extends CrudRepository<PersonEntity, Long> {

    /**
     * Verifica si existe una persona con el número de identificación y tipo de documento especificado.
     *
     * @param identificationTypeId ID del tipo de identificación (ej. cédula, pasaporte)
     * @param identificationNumber número de identificación único
     * @return true si existe la persona, false en caso contrario
     */
    boolean existsByIdentificationTypeIdAndIdentificationNumber(Long identificationTypeId, String identificationNumber);

    /**
     * Consulta una persona por su ID e incluye las relaciones {@code identificationType} y {@code gender}
     * mediante {@code JOIN FETCH}, lo cual garantiza que estas entidades se cargan completamente.
     *
     * Este método se recomienda para evitar problemas de carga perezosa (lazy loading) cuando se necesita
     * retornar un objeto de dominio que contiene información anidada desde relaciones @ManyToOne.
     *
     * @param id identificador único de la persona
     * @return un {@link Optional} con la persona encontrada y sus relaciones cargadas, o vacío si no existe
     */
    @Query("SELECT p FROM PersonEntity p " +
       "JOIN FETCH p.identificationType " +
       "JOIN FETCH p.gender " +
       "WHERE p.id = :id")
    Optional<PersonEntity> findWithRelationsById(Long id);
}

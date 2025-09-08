package com.unicuaca.asst.unicauca_asst.core.batteries_management.infrastructure.adapters.output.persistence.jpa.repositories;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.unicuaca.asst.unicauca_asst.core.batteries_management.infrastructure.adapters.output.persistence.jpa.entities.PersonEvaluatedEntity;

/**
 * Repositorio JPA para la entidad {@link PersonEvaluatedEntity}.
 * 
 * Define operaciones de acceso a datos para la tabla {@code personas_evaluadas}, incluyendo
 * consultas personalizadas para verificar existencia y recuperación de relaciones.
 *
 * <p>Forma parte del adaptador de persistencia dentro de la arquitectura hexagonal, 
 * y se utiliza para interactuar con la base de datos relacional.</p>
 */
public interface PersonEvaluatedSpringJpaRepository extends JpaRepository<PersonEvaluatedEntity, Long> {

    /**
     * Verifica si existe una persona evaluada con el número de identificación y tipo de documento especificados.
     *
     * @param identificationTypeId ID del tipo de identificación (por ejemplo: 1 para Cédula)
     * @param identificationNumber número de identificación de la persona evaluada
     * @return {@code true} si existe una coincidencia, {@code false} en caso contrario
     */
    boolean existsByIdentificationTypeIdAndIdentificationNumber(Long identificationTypeId, String identificationNumber);

    /**
     * Verifica si existe una persona evaluada con el correo electrónico especificado.
     *
     * @param email correo electrónico a verificar
     * @return {@code true} si el correo ya está registrado, {@code false} si no
     */
    boolean existsByEmail(String email);

    /**
     * Verifica si el correo electrónico ya está asignado a otra persona evaluada distinta del ID dado.
     *
     * @param email correo a verificar
     * @param id ID de la persona que está intentando actualizar
     * @return true si el correo pertenece a otra persona, false si no
     */
    boolean existsByEmailAndIdNot(String email, Long id);

    /**
     * Recupera una persona evaluada por su ID, cargando además sus relaciones con
     * {@code identificationType} y {@code gender} mediante {@code JOIN FETCH}.
     *
     * <p>Esta consulta evita problemas de carga perezosa (lazy loading) al obtener entidades anidadas.</p>
     *
     * @param id identificador único de la persona evaluada
     * @return un {@link Optional} que contiene la entidad completa si se encuentra, o vacío si no existe
     */
    @Query("SELECT p FROM PersonEvaluatedEntity p " +
           "JOIN FETCH p.identificationType " +
           "JOIN FETCH p.gender " +
           "WHERE p.id = :id")
    Optional<PersonEvaluatedEntity> findWithRelationsById(Long id);

    /**
     * Consulta paginada de personas evaluadas por tipo de identificación y número de identificación.
     *
     * @param identificationTypeId el ID del tipo de identificación
     * @param identificationNumber el número de identificación
     * @param pageable objeto Pageable con la información de la página
     * @return una página de personas evaluadas
     */
    @Query("SELECT p FROM PersonEvaluatedEntity p " +
            "JOIN p.identificationType it " +
            "WHERE it.abbreviation = :abbreviation " +
            "AND p.identificationNumber LIKE CONCAT(:identificationNumber, '%')")
    Page<PersonEvaluatedEntity> queryByIdentity(String abbreviation, String identificationNumber, Pageable pageable);

}

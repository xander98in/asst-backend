package com.unicuaca.asst.unicauca_asst.core.batteries_management.infrastructure.adapters.output.persistence.jpa.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.unicuaca.asst.unicauca_asst.core.batteries_management.infrastructure.adapters.output.persistence.jpa.entities.QuestionnaireManagementRecordStatusEntity;

/**
 * Repositorio JPA para la entidad {@link QuestionnaireManagementRecordStatusEntity}.
 *
 * Proporciona operaciones CRUD básicas sobre la tabla {@code estados_registro_gestion_cuestionarios}.
 */
public interface QuestionnaireManagementRecordStatusSpringJpaRepository extends JpaRepository<QuestionnaireManagementRecordStatusEntity, Long> {

    /**
     * Verifica si existe un estado de registro de gestión de cuestionarios
     * con el nombre especificado.
     *
     * @param name nombre del estado.
     * @return {@code true} si existe, de lo contrario {@code false}.
     */
    boolean existsByName(String name);

    /**
     * Busca un estado de registro de gestión de cuestionarios por su nombre.
     *
     * @param name nombre del estado a buscar.
     * @return un {@link Optional} con la entidad encontrada, o vacío si no existe.
     */
    Optional<QuestionnaireManagementRecordStatusEntity> findByName(String name);

    /**
     * Obtiene un estado por su identificador usando una consulta JPQL.
     *
     * @param id identificador del estado.
     * @return un {@link Optional} con la entidad encontrada, o vacío si no existe.
     */
    @Query("SELECT q FROM QuestionnaireManagementRecordStatusEntity q WHERE q.id = :id")
    Optional<QuestionnaireManagementRecordStatusEntity> findByIdUsingQuery(@Param("id") Long id);

    /**
     * Obtiene un estado por su nombre usando una consulta JPQL.
     *
     * @param name nombre del estado.
     * @return un {@link Optional} con la entidad encontrada, o vacío si no existe.
     */
    @Query("SELECT q FROM QuestionnaireManagementRecordStatusEntity q WHERE q.name = :name")
    Optional<QuestionnaireManagementRecordStatusEntity> findByNameUsingQuery(@Param("name") String name);

}

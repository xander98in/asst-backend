package com.unicuaca.asst.unicauca_asst.core.batteries_management.infrastructure.adapters.output.persistence.jpa.repositories;

import com.unicuaca.asst.unicauca_asst.core.batteries_management.infrastructure.adapters.output.persistence.jpa.entities.QuestionnaireEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repositorio JPA para la entidad {@link QuestionnaireEntity}.
 *
 * Proporciona operaciones CRUD básicas (crear, leer, actualizar, eliminar)
 * sobre la tabla {@code cuestionarios} utilizando las abstracciones de {@link JpaRepository}.
 */
@Repository
public interface QuestionnaireSpringJpaRepository extends JpaRepository<QuestionnaireEntity, Long> {

    /**
     * Verifica si existe un cuestionario registrado con el nombre especificado.
     *
     * @param name nombre del cuestionario (por ejemplo, "Intralaboral - Forma A").
     * @return {@code true} si existe un cuestionario con ese nombre, de lo contrario {@code false}.
     */
    boolean existsByName(String name);

    /**
     * Busca un cuestionario por su nombre exacto.
     *
     * @param name nombre del cuestionario a buscar.
     * @return un {@link Optional} con la entidad encontrada, o vacío si no existe.
     */
    Optional<QuestionnaireEntity> findByName(String name);

    /**
     * Verifica si existe un cuestionario registrado con la abreviatura especificada.
     *
     * @param abbreviation abreviatura del cuestionario (por ejemplo, "ILA", "EXT").
     * @return {@code true} si existe un cuestionario con esa abreviatura, de lo contrario {@code false}.
     */
    boolean existsByAbbreviation(String abbreviation);

    /**
     * Busca un cuestionario por su abreviatura exacta.
     *
     * @param abbreviation abreviatura del cuestionario a buscar.
     * @return un {@link Optional} con la entidad encontrada, o vacío si no existe.
     */
    Optional<QuestionnaireEntity> findByAbbreviation(String abbreviation);
}

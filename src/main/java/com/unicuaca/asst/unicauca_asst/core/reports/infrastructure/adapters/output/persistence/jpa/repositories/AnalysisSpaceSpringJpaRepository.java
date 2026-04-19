package com.unicuaca.asst.unicauca_asst.core.reports.infrastructure.adapters.output.persistence.jpa.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.unicuaca.asst.unicauca_asst.core.reports.infrastructure.adapters.output.persistence.jpa.entities.AnalysisSpaceEntity;

/**
 * Repositorio Spring Data JPA para la entidad {@link AnalysisSpaceEntity}.
 */
@Repository
public interface AnalysisSpaceSpringJpaRepository extends JpaRepository<AnalysisSpaceEntity, Long> {

    /**
     * Lista todos los espacios de análisis creados por un usuario.
     *
     * @param creatorUserId ID del usuario creador
     * @return lista de espacios de análisis del usuario
     */
    List<AnalysisSpaceEntity> findAllByCreatorUserId(Long creatorUserId);

    /**
     * Obtiene un espacio de análisis por su ID cargando de forma anticipada
     * la colección de baterías asociadas.
     *
     * @param id ID del espacio de análisis
     * @return Optional con el espacio y sus baterías cargadas
     */
    @Query("SELECT a FROM AnalysisSpaceEntity a LEFT JOIN FETCH a.batteries WHERE a.id = :id")
    Optional<AnalysisSpaceEntity> findByIdWithBatteries(@Param("id") Long id);

    /**
     * Verifica si existe un espacio de análisis con el mismo nombre para el usuario dado.
     *
     * @param name          nombre del espacio
     * @param creatorUserId ID del usuario creador
     * @return {@code true} si ya existe un espacio con ese nombre para el usuario
     */
    boolean existsByNameAndCreatorUserId(String name, Long creatorUserId);

    /**
     * Cuenta la cantidad de espacios de análisis creados por un usuario.
     *
     * @param creatorUserId ID del usuario creador
     * @return cantidad de espacios del usuario
     */
    int countByCreatorUserId(Long creatorUserId);

    /**
     * Verifica si algún espacio de análisis referencia al evaluador indicado.
     *
     * @param evaluatorId ID del evaluador
     * @return {@code true} si al menos un espacio referencia al evaluador
     */
    boolean existsByEvaluator_Id(Long evaluatorId);
}

package com.unicuaca.asst.unicauca_asst.common.infrastructure.adapters.output.persistence.jpa.repositories;

import com.unicuaca.asst.unicauca_asst.common.infrastructure.adapters.output.persistence.jpa.entities.DepartmentEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repositorio JPA para la entidad {@link DepartmentEntity}.
 */
@Repository
public interface DepartmentSpringJpaRepository extends JpaRepository<DepartmentEntity, Long> {

    /**
     * Verifica si existe un departamento con el código especificado.
     *
     * @param code código del departamento a verificar.
     * @return true si existe un departamento con ese código, false en caso contrario.
     */
    boolean existsByCode(String code);

    /**
     * Verifica si existe un departamento con el nombre especificado.
     *
     * @param name nombre del departamento a verificar.
     * @return true si existe un departamento con ese nombre, false en caso contrario.
     */
    boolean existsByName(String name);

    /**
     * Obtiene un departamento por su código (NO carga ciudades).
     *
     * @param code código del departamento.
     * @return un {@link Optional} con la entidad encontrada, o vacío si no existe.
     */
    Optional<DepartmentEntity> findByCode(String code);

    /**
     * Obtiene un departamento por su nombre (NO carga ciudades).
     *
     * @param name nombre del departamento.
     * @return un {@link Optional} con la entidad encontrada, o vacío si no existe.
     */
    Optional<DepartmentEntity> findByName(String name);

    /**
     * Obtiene un departamento por su código incluyendo sus ciudades.
     * Se usa DISTINCT para evitar duplicados al hacer join con la colección.
     *
     * @param code código del departamento.
     * @return un {@link Optional} con la entidad encontrada, o vacío si no existe.
     */
    @EntityGraph(attributePaths = "cities")
    Optional<DepartmentEntity> findDistinctByCode(String code);

    /**
     * Obtiene un departamento por su nombre incluyendo sus ciudades.
     *
     * @param name nombre del departamento.
     * @return un {@link Optional} con la entidad encontrada, o vacío si no existe.
     */
    @EntityGraph(attributePaths = "cities")
    Optional<DepartmentEntity> findDistinctByName(String name);

    /**
     * Lista todos los departamentos (NO incluye ciudades).
     *
     * @return lista de todas las entidades de departamento.
     */
    List<DepartmentEntity> findAll();

    /**
     * Lista todos los departamentos incluyendo sus ciudades.
     * DISTINCT evita duplicados causados por el join con la colección.
     *
     * @return lista de todas las entidades de departamento con sus ciudades.
     */
    @EntityGraph(attributePaths = "cities")
    List<DepartmentEntity> findAllDistinctBy();

    /**
     * Carga un departamento por code + sus ciudades.
     * DISTINCT evita duplicados por el join a la colección.
     * LEFT JOIN FETCH para no fallar si no hay ciudades.
     *
     * @param code código del departamento.
     * @return un {@link Optional} con la entidad encontrada, o vacío si no existe.
     */
    @Query("""
           SELECT DISTINCT d
           FROM DepartmentEntity d
           LEFT JOIN FETCH d.cities c
           WHERE d.code = :code
           """)
    Optional<DepartmentEntity> findByCodeWithCities(@Param("code") String code);

    /**
     * Carga un departamento por name + sus ciudades.
     *
     * @param name nombre del departamento.
     * @return un {@link Optional} con la entidad encontrada, o vacío si no existe.
     */
    @Query("""
           SELECT DISTINCT d
           FROM DepartmentEntity d
           LEFT JOIN FETCH d.cities c
           WHERE d.name = :name
           """)
    Optional<DepartmentEntity> findByNameWithCities(@Param("name") String name);

    /**
     * Lista todos los departamentos + sus ciudades.
     * DISTINCT es clave para evitar resultados duplicados.
     *
     * @return lista de todas las entidades de departamento con sus ciudades.
     */
    @Query("""
           SELECT DISTINCT d
           FROM DepartmentEntity d
           LEFT JOIN FETCH d.cities c
           """)
    List<DepartmentEntity> findAllWithCitiesJoinFetch();
}
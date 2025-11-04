package com.unicuaca.asst.unicauca_asst.common.infrastructure.adapters.output.persistence.jpa.repositories;

import com.unicuaca.asst.unicauca_asst.common.infrastructure.adapters.output.persistence.jpa.entities.CityEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repositorio JPA para la entidad {@link CityEntity}.
 */
@Repository
public interface CitySpringJpaRepository extends JpaRepository<CityEntity, Long> {

    /**
     * Verifica si existe una ciudad con el código especificado.
     * @param code código de la ciudad a verificar.
     * @return true si existe una ciudad con ese código, false en caso contrario.
     */
    boolean existsByCode(String code);

    /**
     * Verifica si existe una ciudad con el nombre especificado.
     * @param name nombre de la ciudad a verificar.
     * @return true si existe una ciudad con ese nombre, false en caso contrario.
     */
    boolean existsByName(String name);

    /**
     * Obtiene una ciudad por su código (sin forzar carga del departamento).
     * Útil cuando no se requiere la asociación.
     * @param code código de la ciudad.
     * @return un {@link Optional} con la entidad encontrada, o vacío si no existe.
     */
    Optional<CityEntity> findByCode(String code);

    /**
     * Obtiene una ciudad por su nombre (sin forzar carga del departamento).
     * @param name nombre de la ciudad.
     * @return un {@link Optional} con la entidad encontrada, o vacío si no existe.
     */
    Optional<CityEntity> findByName(String name);

    /**
     * Lista todas las ciudades sin forzar carga del departamento.
     * @return lista de todas las entidades de ciudad.
     */
    List<CityEntity> findAll();

//    /**
//     * Obtiene una ciudad por su código cargando el departamento asociado
//     * (pero sin traer las ciudades del departamento).
//     * @param code código de la ciudad.
//     * @return un {@link Optional} con la entidad encontrada, o vacío si no existe.
//     */
//    @EntityGraph(attributePaths = "department")
//    Optional<CityEntity> findByCodeAndFetchDepartmentEagerly(String code);
//
//    /**
//     * Obtiene una ciudad por su nombre cargando el departamento asociado
//     * (pero sin traer las ciudades del departamento).
//     * @param name nombre de la ciudad.
//     * @return un {@link Optional} con la entidad encontrada, o vacío si no existe.
//     */
//    @EntityGraph(attributePaths = "department")
//    Optional<CityEntity> findByNameAndFetchDepartmentEagerly(String name);
//
//    /**
//     * Lista todas las ciudades cargando el departamento correspondiente
//     * (sin traer las ciudades del departamento).
//     * @return lista de todas las entidades de ciudad con su departamento.
//     */
//    @EntityGraph(attributePaths = "department")
//    List<CityEntity> findAllWithDepartment();

    /**
     * Variante JPQL con JOIN FETCH para traer la ciudad + su departamento.
     * Equivalente funcional a @EntityGraph(attributePaths = "department").
     * @param code código de la ciudad.
     * @return un {@link Optional} con la entidad encontrada, o vacío si no existe.
     */
    @Query("""
           SELECT c
           FROM CityEntity c
           JOIN FETCH c.department d
           WHERE c.code = :code
           """)
    Optional<CityEntity> findByCodeWithDepartment(@Param("code") String code);

    /**
     * Variante JPQL con JOIN FETCH para traer la ciudad + su departamento.
     * @param name nombre de la ciudad.
     * @return un {@link Optional} con la entidad encontrada, o vacío si no existe.
     */
    @Query("""
           SELECT c
           FROM CityEntity c
           JOIN FETCH c.department d
           WHERE c.name = :name
           """)
    Optional<CityEntity> findByNameWithDepartment(@Param("name") String name);

    /**
     * Variante JPQL con JOIN FETCH para listar todas las ciudades + su departamento.
     * @return lista de todas las entidades de ciudad con su departamento.
     */
    @Query("""
           SELECT c
           FROM CityEntity c
           JOIN FETCH c.department d
           """)
    List<CityEntity> findAllWithDepartmentJoinFetch();

}

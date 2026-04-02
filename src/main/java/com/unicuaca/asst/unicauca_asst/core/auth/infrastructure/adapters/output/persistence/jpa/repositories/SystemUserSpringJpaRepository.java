package com.unicuaca.asst.unicauca_asst.core.auth.infrastructure.adapters.output.persistence.jpa.repositories;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.unicuaca.asst.unicauca_asst.core.auth.infrastructure.adapters.output.persistence.jpa.entities.SystemUserEntity;

/**
 * Repositorio JPA para la entidad {@link SystemUserEntity}.
 *
 * <p>Define operaciones de acceso a datos para la tabla {@code usuarios_sistema},
 * incluyendo consultas personalizadas con JOIN FETCH para cargar relaciones.</p>
 *
 * <p>En consultas paginadas solo se hace JOIN FETCH de relaciones {@code @ManyToOne} (status)
 * para evitar el warning HHH000104 con {@code @ManyToMany} (roles).</p>
 */
public interface SystemUserSpringJpaRepository extends JpaRepository<SystemUserEntity, Long> {

    /**
     * Verifica si existe un usuario con el correo electrónico proporcionado.
     *
     * @param email correo electrónico a verificar
     * @return {@code true} si ya hay un usuario con ese correo
     */
    boolean existsByEmail(String email);

    /**
     * Verifica si el correo ya está asignado a un usuario distinto del ID dado.
     *
     * @param email correo a verificar
     * @param id    ID del usuario actual
     * @return {@code true} si el correo pertenece a otro usuario
     */
    boolean existsByEmailAndIdNot(String email, Long id);

    /**
     * Verifica si existe un usuario con el nombre de usuario proporcionado.
     *
     * @param username nombre de usuario a verificar
     * @return {@code true} si ya hay un usuario con ese username
     */
    boolean existsByUsername(String username);

    /**
     * Verifica si el nombre de usuario ya está asignado a un usuario distinto del ID dado.
     *
     * @param username nombre de usuario a verificar
     * @param id       ID del usuario actual
     * @return {@code true} si el username pertenece a otro usuario
     */
    boolean existsByUsernameAndIdNot(String username, Long id);

    /**
     * Recupera un usuario por su ID con sus relaciones (status y roles).
     *
     * @param id identificador del usuario
     * @return un {@link Optional} con la entidad completa o vacío si no existe
     */
    @Query("SELECT u FROM SystemUserEntity u " +
           "JOIN FETCH u.status " +
           "LEFT JOIN FETCH u.roles " +
           "WHERE u.id = :id")
    Optional<SystemUserEntity> findWithRelationsById(@Param("id") Long id);

    /**
     * Recupera un usuario por su correo electrónico con sus relaciones (status y roles).
     *
     * @param email correo electrónico del usuario
     * @return un {@link Optional} con la entidad completa o vacío si no existe
     */
    @Query("SELECT u FROM SystemUserEntity u " +
           "JOIN FETCH u.status " +
           "LEFT JOIN FETCH u.roles " +
           "WHERE u.email = :email")
    Optional<SystemUserEntity> findWithRelationsByEmail(@Param("email") String email);

    /**
     * Recupera todos los usuarios de forma paginada cargando solo la relación status.
     *
     * @param pageable objeto de paginación
     * @return página de usuarios del sistema
     */
    @Query(value = "SELECT u FROM SystemUserEntity u " +
                   "JOIN FETCH u.status",
           countQuery = "SELECT COUNT(u) FROM SystemUserEntity u")
    Page<SystemUserEntity> findAllWithRelations(Pageable pageable);

    /**
     * Recupera usuarios filtrados por término de búsqueda (correo, nombre de usuario o nombre completo).
     *
     * @param term     término de búsqueda
     * @param pageable objeto de paginación
     * @return página de usuarios filtrados
     */
    @Query(value = "SELECT u FROM SystemUserEntity u " +
                   "JOIN FETCH u.status " +
                   "WHERE (:term IS NULL OR :term = '' " +
                   "OR LOWER(u.email) LIKE CONCAT('%', LOWER(:term), '%') " +
                   "OR LOWER(u.username) LIKE CONCAT('%', LOWER(:term), '%') " +
                   "OR LOWER(u.fullName) LIKE CONCAT('%', LOWER(:term), '%'))",
           countQuery = "SELECT COUNT(u) FROM SystemUserEntity u " +
                        "WHERE (:term IS NULL OR :term = '' " +
                        "OR LOWER(u.email) LIKE CONCAT('%', LOWER(:term), '%') " +
                        "OR LOWER(u.username) LIKE CONCAT('%', LOWER(:term), '%') " +
                        "OR LOWER(u.fullName) LIKE CONCAT('%', LOWER(:term), '%'))")
    Page<SystemUserEntity> findAllWithSearchTerm(@Param("term") String term, Pageable pageable);
}

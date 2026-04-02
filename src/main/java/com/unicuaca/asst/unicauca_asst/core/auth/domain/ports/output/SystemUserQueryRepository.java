package com.unicuaca.asst.unicauca_asst.core.auth.domain.ports.output;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;

import com.unicuaca.asst.unicauca_asst.core.auth.domain.models.SystemUser;

/**
 * Puerto de salida para operaciones de consulta sobre el agregado {@link SystemUser}.
 *
 * Define las firmas de los métodos que deben implementar los adaptadores de infraestructura encargados
 * de recuperar información de usuarios desde fuentes externas, como bases de datos relacionales.
 *
 * <p>Hace parte de la arquitectura hexagonal, separando las dependencias externas
 * de la lógica del dominio.</p>
 */
public interface SystemUserQueryRepository {

    /**
     * Consulta un usuario del sistema a partir de su identificador único.
     *
     * @param id identificador del usuario
     * @return un {@link Optional} con el usuario encontrado o vacío si no existe
     */
    Optional<SystemUser> getSystemUserById(Long id);

    /**
     * Consulta un usuario del sistema a partir de su correo electrónico.
     *
     * @param email correo electrónico del usuario
     * @return un {@link Optional} con el usuario encontrado o vacío si no existe
     */
    Optional<SystemUser> getSystemUserByEmail(String email);

    /**
     * Verifica si existe un usuario con el correo electrónico proporcionado.
     *
     * @param email correo electrónico a verificar
     * @return {@code true} si ya hay un usuario registrado con ese correo, {@code false} si no
     */
    boolean existsByEmail(String email);

    /**
     * Verifica si existe un usuario con el ID proporcionado.
     *
     * @param id identificador del usuario
     * @return {@code true} si el usuario existe, {@code false} si no
     */
    boolean existsById(Long id);

    /**
     * Verifica si el correo ya está asignado a un usuario distinto del ID dado.
     *
     * @param email correo a verificar
     * @param id ID del usuario actual
     * @return {@code true} si el correo pertenece a otro usuario, {@code false} en caso contrario
     */
    boolean isEmailAssignedToDifferentUser(String email, Long id);

    /**
     * Verifica si existe un usuario con el nombre de usuario proporcionado.
     *
     * @param username nombre de usuario a verificar
     * @return {@code true} si ya hay un usuario registrado con ese username, {@code false} si no
     */
    boolean existsByUsername(String username);

    /**
     * Verifica si el nombre de usuario ya está asignado a un usuario distinto del ID dado.
     *
     * @param username nombre de usuario a verificar
     * @param id ID del usuario actual
     * @return {@code true} si el username pertenece a otro usuario, {@code false} en caso contrario
     */
    boolean isUsernameAssignedToDifferentUser(String username, Long id);

    /**
     * Lista todos los usuarios del sistema de forma paginada.
     *
     * @param page número de página
     * @param size tamaño de página
     * @param sort criterio de ordenamiento
     * @return una página de usuarios del sistema
     */
    Page<SystemUser> findAllPaged(Integer page, Integer size, Sort sort);

    /**
     * Lista usuarios del sistema de forma paginada filtrando por término de búsqueda.
     *
     * @param searchTerm término de búsqueda
     * @param page       número de página
     * @param size       tamaño de página
     * @param sort       criterio de ordenamiento
     * @return una página de usuarios del sistema
     */
    Page<SystemUser> findAllWithSearchTermPaged(String searchTerm, Integer page, Integer size, Sort sort);
}

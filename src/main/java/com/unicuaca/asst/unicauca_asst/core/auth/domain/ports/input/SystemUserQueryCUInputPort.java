package com.unicuaca.asst.unicauca_asst.core.auth.domain.ports.input;

import org.springframework.data.domain.Page;

import com.unicuaca.asst.unicauca_asst.core.auth.domain.models.SystemUser;

/**
 * Puerto de entrada para operaciones de consulta sobre el agregado {@link SystemUser}.
 *
 * Define las firmas de los métodos que deben implementar los casos de uso encargados
 * de recuperar información de usuarios del sistema desde el dominio.
 *
 * <p>Hace parte de la arquitectura hexagonal, separando las dependencias externas
 * de la lógica del dominio.</p>
 */
public interface SystemUserQueryCUInputPort {

    /**
     * Consulta un usuario del sistema a partir de su identificador único.
     *
     * @param id el identificador del usuario
     * @return una instancia del modelo de dominio {@link SystemUser}
     */
    SystemUser getSystemUserById(Long id);

    /**
     * Consulta un usuario del sistema a partir de su correo electrónico.
     *
     * @param email correo electrónico del usuario
     * @return una instancia del modelo de dominio {@link SystemUser}
     */
    SystemUser getSystemUserByEmail(String email);

    /**
     * Lista todos los usuarios del sistema de forma paginada.
     *
     * @param page número de página (0-indexado)
     * @param size cantidad de registros por página
     * @return una página de usuarios del sistema
     */
    Page<SystemUser> listPaginatedUsers(Integer page, Integer size);

    /**
     * Lista usuarios del sistema de forma paginada filtrando por término de búsqueda.
     *
     * @param page       número de página (0-indexado)
     * @param size       cantidad de registros por página
     * @param searchTerm término de búsqueda
     * @return una página de usuarios del sistema
     */
    Page<SystemUser> listPaginatedWithSearchTerm(Integer page, Integer size, String searchTerm);
}

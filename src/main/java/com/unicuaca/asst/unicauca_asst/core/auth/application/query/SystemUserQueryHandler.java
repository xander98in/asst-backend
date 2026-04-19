package com.unicuaca.asst.unicauca_asst.core.auth.application.query;

import org.springframework.data.domain.Page;

import com.unicuaca.asst.unicauca_asst.core.auth.application.dto.response.SystemUserResponseDTO;

/**
 * Manejador de consultas para operaciones de lectura sobre usuarios del sistema.
 *
 * <p>Define el contrato para consultar usuarios por identificador o correo electrónico
 * y para listarlos de forma paginada con soporte para términos de búsqueda.</p>
 */
public interface SystemUserQueryHandler {

    /**
     * Consulta los datos de un usuario a partir de su identificador único.
     *
     * @param id el ID del usuario que se desea consultar
     * @return un {@link SystemUserResponseDTO} con la información del usuario
     */
    SystemUserResponseDTO getSystemUserById(Long id);

    /**
     * Consulta los datos de un usuario a partir de su correo electrónico.
     *
     * @param email correo electrónico del usuario
     * @return un {@link SystemUserResponseDTO} con la información del usuario
     */
    SystemUserResponseDTO getSystemUserByEmail(String email);

    /**
     * Lista usuarios del sistema de forma paginada.
     *
     * @param page número de página (0-indexado)
     * @param size cantidad de registros por página
     * @return una página de {@link SystemUserResponseDTO}
     */
    Page<SystemUserResponseDTO> listPaginatedUsers(Integer page, Integer size);

    /**
     * Lista usuarios del sistema de forma paginada filtrando por término de búsqueda.
     *
     * @param page       número de página (0-indexado)
     * @param size       cantidad de registros por página
     * @param searchTerm término de búsqueda
     * @return una página de {@link SystemUserResponseDTO}
     */
    Page<SystemUserResponseDTO> listPaginatedWithSearchTerm(Integer page, Integer size, String searchTerm);
}

package com.unicuaca.asst.unicauca_asst.core.auth.application.query;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.unicuaca.asst.unicauca_asst.core.auth.application.dto.response.SystemUserResponseDTO;
import com.unicuaca.asst.unicauca_asst.core.auth.application.mappers.SystemUserMapper;
import com.unicuaca.asst.unicauca_asst.core.auth.domain.models.SystemUser;
import com.unicuaca.asst.unicauca_asst.core.auth.domain.ports.input.SystemUserQueryCUInputPort;

import lombok.RequiredArgsConstructor;

/**
 * Implementación del manejador de consultas de usuarios del sistema.
 *
 * <p>Delega la lógica de negocio al puerto de entrada y transforma
 * el modelo de dominio en un DTO para la respuesta.</p>
 */
@RequiredArgsConstructor
@Component
@Transactional(readOnly = true)
public class SystemUserQueryHandlerImpl implements SystemUserQueryHandler {

    private final SystemUserQueryCUInputPort systemUserQueryCUInputPort;
    private final SystemUserMapper systemUserMapper;

    /**
     * Consulta un usuario por su ID y devuelve su representación.
     *
     * @param id ID único del usuario
     * @return respuesta con los datos del usuario consultado
     */
    @Override
    public SystemUserResponseDTO getSystemUserById(Long id) {
        SystemUser systemUser = systemUserQueryCUInputPort.getSystemUserById(id);
        return systemUserMapper.toResponseDTO(systemUser);
    }

    /**
     * Consulta un usuario por su correo electrónico y devuelve su representación.
     *
     * @param email correo electrónico del usuario
     * @return respuesta con los datos del usuario consultado
     */
    @Override
    public SystemUserResponseDTO getSystemUserByEmail(String email) {
        SystemUser systemUser = systemUserQueryCUInputPort.getSystemUserByEmail(email);
        return systemUserMapper.toResponseDTO(systemUser);
    }

    /**
     * Lista usuarios de forma paginada.
     *
     * @param page número de página (0-indexado)
     * @param size cantidad de registros por página
     * @return una página de {@link SystemUserResponseDTO}
     */
    @Override
    public Page<SystemUserResponseDTO> listPaginatedUsers(Integer page, Integer size) {
        Page<SystemUser> domainPage = systemUserQueryCUInputPort.listPaginatedUsers(page, size);
        return domainPage.map(systemUserMapper::toResponseDTO);
    }

    /**
     * Lista usuarios de forma paginada filtrando por término de búsqueda.
     *
     * @param page       número de página (0-indexado)
     * @param size       cantidad de registros por página
     * @param searchTerm término de búsqueda
     * @return una página de {@link SystemUserResponseDTO}
     */
    @Override
    public Page<SystemUserResponseDTO> listPaginatedWithSearchTerm(Integer page, Integer size, String searchTerm) {
        Page<SystemUser> domainPage = systemUserQueryCUInputPort.listPaginatedWithSearchTerm(page, size, searchTerm);
        return domainPage.map(systemUserMapper::toResponseDTO);
    }
}

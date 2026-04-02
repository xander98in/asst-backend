package com.unicuaca.asst.unicauca_asst.core.auth.application.command;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.unicuaca.asst.unicauca_asst.core.auth.application.dto.request.SystemUserCreateRequestDTO;
import com.unicuaca.asst.unicauca_asst.core.auth.application.dto.request.SystemUserStatusUpdateRequestDTO;
import com.unicuaca.asst.unicauca_asst.core.auth.application.dto.request.SystemUserUpdateRequestDTO;
import com.unicuaca.asst.unicauca_asst.core.auth.application.dto.response.SystemUserResponseDTO;
import com.unicuaca.asst.unicauca_asst.core.auth.application.mappers.SystemUserMapper;
import com.unicuaca.asst.unicauca_asst.core.auth.domain.models.SystemUser;
import com.unicuaca.asst.unicauca_asst.core.auth.domain.ports.input.SystemUserCommandCUInputPort;

import lombok.RequiredArgsConstructor;

/**
 * Implementación del manejador de comandos de usuarios del sistema.
 *
 * <p>Convierte los DTOs de entrada a modelos de dominio, delega la ejecución
 * al puerto de entrada correspondiente y transforma la respuesta del dominio
 * en un DTO de salida.</p>
 */
@RequiredArgsConstructor
@Component
@Transactional
public class SystemUserCommandHandlerImpl implements SystemUserCommandHandler {

    private final SystemUserCommandCUInputPort systemUserCommandCUInputPort;
    private final SystemUserMapper systemUserMapper;

    /**
     * Crea un usuario y devuelve su representación.
     *
     * @param dto datos de entrada
     * @return respuesta con el DTO del usuario creado
     */
    @Override
    public SystemUserResponseDTO createSystemUser(SystemUserCreateRequestDTO dto) {
        SystemUser domain = systemUserMapper.toDomain(dto);
        SystemUser created = systemUserCommandCUInputPort.createSystemUser(domain);
        return systemUserMapper.toResponseDTO(created);
    }

    /**
     * Actualiza un usuario y devuelve su representación.
     *
     * @param id  identificador del usuario a actualizar
     * @param dto datos nuevos validados
     * @return respuesta con el DTO del usuario actualizado
     */
    @Override
    public SystemUserResponseDTO updateSystemUser(Long id, SystemUserUpdateRequestDTO dto) {
        SystemUser domain = systemUserMapper.toDomain(id, dto);
        SystemUser updated = systemUserCommandCUInputPort.updateSystemUser(domain);
        return systemUserMapper.toResponseDTO(updated);
    }

    /**
     * Cambia el estado de un usuario.
     *
     * @param id  identificador del usuario
     * @param dto datos con el nuevo estado
     * @return respuesta con el DTO del usuario actualizado
     */
    @Override
    public SystemUserResponseDTO changeUserStatus(Long id, SystemUserStatusUpdateRequestDTO dto) {
        SystemUser updated = systemUserCommandCUInputPort.changeUserStatus(id, dto.getStatusName());
        return systemUserMapper.toResponseDTO(updated);
    }

    /**
     * Elimina un usuario por su ID.
     *
     * @param id identificador del usuario a eliminar
     */
    @Override
    public void deleteSystemUser(Long id) {
        systemUserCommandCUInputPort.deleteSystemUser(id);
    }
}

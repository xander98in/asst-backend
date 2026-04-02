package com.unicuaca.asst.unicauca_asst.core.auth.application.command;

import com.unicuaca.asst.unicauca_asst.core.auth.application.dto.request.SystemUserCreateRequestDTO;
import com.unicuaca.asst.unicauca_asst.core.auth.application.dto.request.SystemUserStatusUpdateRequestDTO;
import com.unicuaca.asst.unicauca_asst.core.auth.application.dto.request.SystemUserUpdateRequestDTO;
import com.unicuaca.asst.unicauca_asst.core.auth.application.dto.response.SystemUserResponseDTO;

/**
 * Manejador de comandos para operaciones de escritura sobre usuarios del sistema.
 */
public interface SystemUserCommandHandler {

    /**
     * Registra un nuevo usuario en el sistema.
     *
     * @param dto datos de entrada
     * @return respuesta con el usuario creado
     */
    SystemUserResponseDTO createSystemUser(SystemUserCreateRequestDTO dto);

    /**
     * Actualiza los datos de un usuario existente.
     *
     * @param id  identificador del usuario
     * @param dto datos a actualizar
     * @return respuesta con el usuario actualizado
     */
    SystemUserResponseDTO updateSystemUser(Long id, SystemUserUpdateRequestDTO dto);

    /**
     * Cambia el estado de un usuario del sistema.
     *
     * @param id  identificador del usuario
     * @param dto datos con el nuevo estado
     * @return respuesta con el usuario actualizado
     */
    SystemUserResponseDTO changeUserStatus(Long id, SystemUserStatusUpdateRequestDTO dto);

    /**
     * Elimina un usuario del sistema por su identificador.
     *
     * @param id identificador del usuario a eliminar
     */
    void deleteSystemUser(Long id);
}

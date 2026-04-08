package com.unicuaca.asst.unicauca_asst.core.auth.application.query;

import java.util.List;

import com.unicuaca.asst.unicauca_asst.core.auth.application.dto.response.UserStatusResponseDTO;

/**
 * Manejador de consultas para operaciones de lectura sobre estados de usuario del sistema.
 */
public interface UserStatusQueryHandler {

    /**
     * Lista todos los estados de usuario disponibles en el sistema.
     *
     * @return lista de {@link UserStatusResponseDTO} con todos los estados de usuario
     */
    List<UserStatusResponseDTO> getAllUserStatuses();
}

package com.unicuaca.asst.unicauca_asst.core.auth.application.query;

import java.util.List;

import com.unicuaca.asst.unicauca_asst.core.auth.application.dto.response.UserStatusResponseDTO;

/**
 * Manejador de consultas para operaciones de lectura sobre estados de usuario del sistema.
 *
 * <p>Define el contrato para recuperar el catálogo de estados de usuario disponibles,
 * delegando la lógica de consulta al puerto de entrada de dominio.</p>
 */
public interface UserStatusQueryHandler {

    /**
     * Lista todos los estados de usuario disponibles en el sistema.
     *
     * @return lista de {@link UserStatusResponseDTO} con todos los estados de usuario
     */
    List<UserStatusResponseDTO> getAllUserStatuses();
}

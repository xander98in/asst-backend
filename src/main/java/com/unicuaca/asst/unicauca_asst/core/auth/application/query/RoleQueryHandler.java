package com.unicuaca.asst.unicauca_asst.core.auth.application.query;

import java.util.List;

import com.unicuaca.asst.unicauca_asst.core.auth.application.dto.response.RoleResponseDTO;

/**
 * Manejador de consultas para operaciones de lectura sobre roles del sistema.
 */
public interface RoleQueryHandler {

    /**
     * Lista todos los roles disponibles en el sistema.
     *
     * @return lista de {@link RoleResponseDTO} con todos los roles
     */
    List<RoleResponseDTO> getAllRoles();
}

package com.unicuaca.asst.unicauca_asst.core.auth.application.query;

import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.unicuaca.asst.unicauca_asst.core.auth.application.dto.response.RoleResponseDTO;
import com.unicuaca.asst.unicauca_asst.core.auth.application.mappers.RoleMapper;
import com.unicuaca.asst.unicauca_asst.core.auth.domain.ports.input.RoleQueryCUInputPort;

import lombok.RequiredArgsConstructor;

/**
 * Implementación del manejador de consultas de roles del sistema.
 *
 * <p>Delega la lógica de negocio al puerto de entrada y transforma
 * el modelo de dominio en un DTO para la respuesta.</p>
 */
@RequiredArgsConstructor
@Component
@Transactional(readOnly = true)
public class RoleQueryHandlerImpl implements RoleQueryHandler {

    private final RoleQueryCUInputPort roleQueryCUInputPort;
    private final RoleMapper roleMapper;

    /**
     * Lista todos los roles disponibles en el sistema.
     *
     * @return lista de {@link RoleResponseDTO} con todos los roles
     */
    @Override
    public List<RoleResponseDTO> getAllRoles() {
        return roleQueryCUInputPort.getAllRoles().stream()
            .map(roleMapper::toResponseDTO)
            .toList();
    }
}

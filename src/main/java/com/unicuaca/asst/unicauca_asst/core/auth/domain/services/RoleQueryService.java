package com.unicuaca.asst.unicauca_asst.core.auth.domain.services;

import java.util.List;
import java.util.Optional;

import com.unicuaca.asst.unicauca_asst.common.application.output.ResultFormatterOutputPort;
import com.unicuaca.asst.unicauca_asst.common.exceptions.structure.ErrorCode;
import com.unicuaca.asst.unicauca_asst.core.auth.domain.models.Role;
import com.unicuaca.asst.unicauca_asst.core.auth.domain.ports.input.RoleQueryCUInputPort;
import com.unicuaca.asst.unicauca_asst.core.auth.domain.ports.output.RoleQueryRepository;

import lombok.RequiredArgsConstructor;

/**
 * Servicio de dominio para la consulta de roles del sistema.
 *
 * <p>Proporciona capacidades de búsqueda por clave técnica
 * y listado de todos los roles disponibles.</p>
 */
@RequiredArgsConstructor
public class RoleQueryService implements RoleQueryCUInputPort {

    private final RoleQueryRepository roleQueryRepository;
    private final ResultFormatterOutputPort resultFormatter;

    /**
     * Lista todos los roles disponibles en el sistema.
     *
     * @return lista de todos los roles
     */
    @Override
    public List<Role> getAllRoles() {
        return roleQueryRepository.getAllRoles();
    }

    /**
     * Consulta un rol por su clave técnica.
     *
     * @param keyName clave técnica del rol (por ejemplo: ADMIN, PROFESIONAL_ASST)
     * @return el rol encontrado
     */
    @Override
    public Role getRoleByKeyName(String keyName) {
        Optional<Role> roleOpt = roleQueryRepository.getRoleByKeyName(keyName);
        if (roleOpt.isEmpty()) {
            resultFormatter.throwEntityNotFound(
                ErrorCode.ROLE_NOT_FOUND,
                "tech.role.not_found",
                keyName
            );
        }
        return roleOpt.get();
    }
}

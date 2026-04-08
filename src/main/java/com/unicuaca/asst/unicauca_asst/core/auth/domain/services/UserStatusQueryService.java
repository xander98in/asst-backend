package com.unicuaca.asst.unicauca_asst.core.auth.domain.services;

import java.util.List;

import com.unicuaca.asst.unicauca_asst.core.auth.domain.models.UserStatus;
import com.unicuaca.asst.unicauca_asst.core.auth.domain.ports.input.UserStatusQueryCUInputPort;
import com.unicuaca.asst.unicauca_asst.core.auth.domain.ports.output.UserStatusQueryRepository;

import lombok.RequiredArgsConstructor;

/**
 * Servicio de dominio para la consulta de estados de usuario del sistema.
 *
 * <p>Proporciona la capacidad de listar todos los estados de usuario disponibles,
 * delegando la recuperación de datos al repositorio de consulta.</p>
 */
@RequiredArgsConstructor
public class UserStatusQueryService implements UserStatusQueryCUInputPort {

    private final UserStatusQueryRepository userStatusQueryRepository;

    /**
     * Lista todos los estados de usuario disponibles en el sistema.
     *
     * @return lista de todos los estados de usuario
     */
    @Override
    public List<UserStatus> getAllUserStatuses() {
        return userStatusQueryRepository.getAllUserStatuses();
    }
}

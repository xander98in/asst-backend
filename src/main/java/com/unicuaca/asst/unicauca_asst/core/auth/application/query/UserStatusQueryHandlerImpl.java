package com.unicuaca.asst.unicauca_asst.core.auth.application.query;

import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.unicuaca.asst.unicauca_asst.core.auth.application.dto.response.UserStatusResponseDTO;
import com.unicuaca.asst.unicauca_asst.core.auth.application.mappers.UserStatusMapper;
import com.unicuaca.asst.unicauca_asst.core.auth.domain.ports.input.UserStatusQueryCUInputPort;

import lombok.RequiredArgsConstructor;

/**
 * Implementación del manejador de consultas de estados de usuario del sistema.
 *
 * <p>Delega la lógica de negocio al puerto de entrada y transforma
 * el modelo de dominio en un DTO para la respuesta.</p>
 */
@RequiredArgsConstructor
@Component
@Transactional(readOnly = true)
public class UserStatusQueryHandlerImpl implements UserStatusQueryHandler {

    private final UserStatusQueryCUInputPort userStatusQueryCUInputPort;
    private final UserStatusMapper userStatusMapper;

    /**
     * Lista todos los estados de usuario disponibles en el sistema.
     *
     * @return lista de {@link UserStatusResponseDTO} con todos los estados de usuario
     */
    @Override
    public List<UserStatusResponseDTO> getAllUserStatuses() {
        return userStatusQueryCUInputPort.getAllUserStatuses().stream()
            .map(userStatusMapper::toResponseDTO)
            .toList();
    }
}

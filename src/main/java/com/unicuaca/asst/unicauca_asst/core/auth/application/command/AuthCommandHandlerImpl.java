package com.unicuaca.asst.unicauca_asst.core.auth.application.command;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.unicuaca.asst.unicauca_asst.core.auth.application.dto.response.AuthResponseDTO;
import com.unicuaca.asst.unicauca_asst.core.auth.application.dto.response.SystemUserResponseDTO;
import com.unicuaca.asst.unicauca_asst.core.auth.application.mappers.SystemUserMapper;
import com.unicuaca.asst.unicauca_asst.core.auth.domain.models.SystemUser;
import com.unicuaca.asst.unicauca_asst.core.auth.domain.ports.input.AuthCUInputPort;

import lombok.RequiredArgsConstructor;

/**
 * Implementación del manejador de comandos de autenticación.
 *
 * <p>Delega la verificación de existencia y estado del usuario al puerto de entrada
 * y transforma el modelo de dominio en un DTO de respuesta.</p>
 *
 * <p>Nota: La generación de tokens JWT se integrará en una fase posterior.
 * Por ahora solo retorna la información del usuario autenticado.</p>
 */
@RequiredArgsConstructor
@Component
@Transactional
public class AuthCommandHandlerImpl implements AuthCommandHandler {

    private final AuthCUInputPort authCUInputPort;
    private final SystemUserMapper systemUserMapper;

    /**
     * Procesa el login con el email ya verificado por Google OAuth.
     *
     * @param email correo electrónico verificado
     * @return respuesta con datos de autenticación y usuario
     */
    @Override
    public AuthResponseDTO login(String email) {
        SystemUser systemUser = authCUInputPort.loginWithGoogle(email);
        SystemUserResponseDTO userResponse = systemUserMapper.toResponseDTO(systemUser);

        return AuthResponseDTO.builder()
            .accessToken(null)
            .refreshToken(null)
            .tokenType("Bearer")
            .expiresIn(null)
            .user(userResponse)
            .build();
    }
}

package com.unicuaca.asst.unicauca_asst.core.auth.domain.services;

import java.util.Optional;

import com.unicuaca.asst.unicauca_asst.common.domain.ports.output.ResultFormatterOutputPort;
import com.unicuaca.asst.unicauca_asst.common.exceptions.structure.ErrorCode;
import com.unicuaca.asst.unicauca_asst.core.auth.domain.models.SystemUser;
import com.unicuaca.asst.unicauca_asst.core.auth.domain.models.enums.UserStatusEnum;
import com.unicuaca.asst.unicauca_asst.core.auth.domain.ports.input.AuthCUInputPort;
import com.unicuaca.asst.unicauca_asst.core.auth.domain.ports.output.SystemUserQueryRepository;

import lombok.RequiredArgsConstructor;

/**
 * Servicio de dominio para la autenticación de usuarios del sistema.
 *
 * <p>Recibe el email ya verificado por la infraestructura (GoogleTokenService)
 * y valida la existencia y el estado del usuario en el sistema.</p>
 */
@RequiredArgsConstructor
public class AuthService implements AuthCUInputPort {

    private final SystemUserQueryRepository systemUserQueryRepository;
    private final ResultFormatterOutputPort resultFormatter;

    /**
     * Autentica un usuario del sistema mediante su correo electrónico previamente verificado.
     *
     * @param email correo electrónico del usuario ya verificado por Google OAuth
     * @return el usuario autenticado
     */
    @Override
    public SystemUser loginWithGoogle(String email) {
        Optional<SystemUser> userOpt = systemUserQueryRepository.getSystemUserByEmail(email);
        if (userOpt.isEmpty()) {
            resultFormatter.throwEntityNotFound(
                ErrorCode.USER_NOT_FOUND,
                "user.user.not_found",
                email
            );
        }
        SystemUser systemUser = userOpt.get();

        String currentStatus = systemUser.getStatus().getName();

        if (UserStatusEnum.INACTIVE.getDescription().equals(currentStatus)) {
            resultFormatter.throwBusinessRuleViolation(
                ErrorCode.USER_INACTIVE,
                "user.user.inactive",
                email
            );
        }

        if (UserStatusEnum.BLOCKED.getDescription().equals(currentStatus)) {
            resultFormatter.throwBusinessRuleViolation(
                ErrorCode.USER_BLOCKED,
                "user.user.blocked",
                email
            );
        }

        return systemUser;
    }
}

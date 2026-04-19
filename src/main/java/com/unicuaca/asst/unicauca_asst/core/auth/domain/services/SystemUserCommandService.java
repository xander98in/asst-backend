package com.unicuaca.asst.unicauca_asst.core.auth.domain.services;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import com.unicuaca.asst.unicauca_asst.common.application.output.ResultFormatterOutputPort;
import com.unicuaca.asst.unicauca_asst.common.exceptions.structure.ErrorCode;
import com.unicuaca.asst.unicauca_asst.core.auth.domain.models.Role;
import com.unicuaca.asst.unicauca_asst.core.auth.domain.models.SystemUser;
import com.unicuaca.asst.unicauca_asst.core.auth.domain.models.UserStatus;
import com.unicuaca.asst.unicauca_asst.core.auth.domain.models.enums.UserStatusEnum;
import com.unicuaca.asst.unicauca_asst.core.auth.domain.ports.input.SystemUserCommandCUInputPort;
import com.unicuaca.asst.unicauca_asst.core.auth.domain.ports.output.RoleQueryRepository;
import com.unicuaca.asst.unicauca_asst.core.auth.domain.ports.output.SystemUserCommandRepository;
import com.unicuaca.asst.unicauca_asst.core.auth.domain.ports.output.SystemUserQueryRepository;
import com.unicuaca.asst.unicauca_asst.core.auth.domain.ports.output.UserStatusQueryRepository;

import lombok.RequiredArgsConstructor;

/**
 * Servicio de dominio para la gestión de comandos de usuarios del sistema.
 *
 * <p>Implementa la lógica de negocio para el registro, actualización, cambio de estado
 * y eliminación de usuarios. Garantiza la integridad de los datos (unicidad de email)
 * y sincroniza las reglas de negocio mediante el sistema de internacionalización (i18n).</p>
 */
@RequiredArgsConstructor
public class SystemUserCommandService implements SystemUserCommandCUInputPort {

    private final SystemUserCommandRepository systemUserCommandRepository;
    private final SystemUserQueryRepository systemUserQueryRepository;
    private final UserStatusQueryRepository userStatusQueryRepository;
    private final RoleQueryRepository roleQueryRepository;
    private final ResultFormatterOutputPort resultFormatter;

    /**
     * Crea un nuevo usuario en el sistema, validando previamente que no exista
     * otro con el mismo correo electrónico.
     *
     * @param systemUser modelo con los datos del usuario a crear
     * @return el usuario creado con su ID asignado
     */
    @Override
    public SystemUser createSystemUser(SystemUser systemUser) {
        // Normalización de datos
        systemUser.setFullName(capitalizeWords(systemUser.getFullName()));
        systemUser.setUsername(systemUser.getUsername().trim().toLowerCase());

        // Regla de Negocio: Unicidad de correo electrónico
        if (systemUserQueryRepository.existsByEmail(systemUser.getEmail())) {
            resultFormatter.throwEntityAlreadyExists(
                ErrorCode.USER_EMAIL_EXISTS,
                "user.user.email_exists",
                systemUser.getEmail()
            );
        }

        // Regla de Negocio: Unicidad de username
        if (systemUserQueryRepository.existsByUsername(systemUser.getUsername())) {
            resultFormatter.throwEntityAlreadyExists(
                ErrorCode.USER_USERNAME_EXISTS,
                "user.user.username_exists",
                systemUser.getUsername()
            );
        }

        // Resolución de roles por ID
        resolveRoles(systemUser);

        // Asignación de estado inicial ACTIVE
        Optional<UserStatus> activeStatusOpt = userStatusQueryRepository.getUserStatusByName(UserStatusEnum.ACTIVE.getDescription());
        if (activeStatusOpt.isEmpty()) {
            resultFormatter.throwEntityNotFound(
                ErrorCode.USER_STATUS_NOT_FOUND,
                "user.user.not_found",
                UserStatusEnum.ACTIVE.getDescription()
            );
        }
        systemUser.setStatus(activeStatusOpt.get());

        // Asignación de fecha de registro
        systemUser.setRegisteredAt(LocalDateTime.now());

        Optional<SystemUser> savedOpt = systemUserCommandRepository.saveSystemUser(systemUser);
        if (savedOpt.isEmpty()) {
            resultFormatter.throwEntityCreationFailed(
                ErrorCode.USER_CREATION_FAILED,
                "user.user.creation_failed",
                systemUser.getEmail()
            );
        }
        return savedOpt.get();
    }

    /**
     * Actualiza la información de un usuario del sistema existente.
     *
     * @param systemUser modelo con los datos actualizados e ID válido
     * @return el usuario con los cambios persistidos
     */
    @Override
    public SystemUser updateSystemUser(SystemUser systemUser) {
        Long id = systemUser.getId();

        // Verificación de existencia previa
        if (!systemUserQueryRepository.existsById(id)) {
            resultFormatter.throwEntityNotFound(
                ErrorCode.USER_NOT_FOUND,
                "user.user.not_found",
                id
            );
        }

        // Normalización de datos
        systemUser.setFullName(capitalizeWords(systemUser.getFullName()));
        systemUser.setUsername(systemUser.getUsername().trim().toLowerCase());

        // Regla de Negocio: email no ocupado por otro usuario
        if (systemUserQueryRepository.isEmailAssignedToDifferentUser(systemUser.getEmail(), id)) {
            resultFormatter.throwEntityAlreadyExists(
                ErrorCode.USER_EMAIL_EXISTS,
                "user.user.email_exists",
                systemUser.getEmail()
            );
        }

        // Regla de Negocio: username no ocupado por otro usuario
        if (systemUserQueryRepository.isUsernameAssignedToDifferentUser(systemUser.getUsername(), id)) {
            resultFormatter.throwEntityAlreadyExists(
                ErrorCode.USER_USERNAME_EXISTS,
                "user.user.update_username_exists",
                systemUser.getUsername()
            );
        }

        // Resolución de roles por ID
        resolveRoles(systemUser);

        Optional<SystemUser> updatedOpt = systemUserCommandRepository.updateSystemUser(systemUser);
        if (updatedOpt.isEmpty()) {
            resultFormatter.throwEntityCreationFailed(
                ErrorCode.USER_UPDATE_FAILED,
                "user.user.update_failed",
                id
            );
        }
        return updatedOpt.get();
    }

    /**
     * Cambia el estado de un usuario del sistema (activar, desactivar, bloquear).
     *
     * @param id identificador del usuario
     * @param statusName nombre del nuevo estado a asignar
     * @return el usuario con el estado actualizado
     */
    @Override
    public SystemUser changeUserStatus(Long id, String statusName) {
        // Verificación de existencia
        Optional<SystemUser> userOpt = systemUserQueryRepository.getSystemUserById(id);
        if (userOpt.isEmpty()) {
            resultFormatter.throwEntityNotFound(
                ErrorCode.USER_NOT_FOUND,
                "user.user.not_found",
                id
            );
        }
        SystemUser systemUser = userOpt.get();

        // Resolución del nuevo estado
        Optional<UserStatus> statusOpt = userStatusQueryRepository.getUserStatusByName(statusName);
        if (statusOpt.isEmpty()) {
            resultFormatter.throwEntityNotFound(
                ErrorCode.USER_STATUS_NOT_FOUND,
                "user.user.not_found",
                statusName
            );
        }
        UserStatus newStatus = statusOpt.get();

        // Validación: el nuevo estado debe ser diferente al actual
        String currentStatusName = systemUser.getStatus().getName();
        if (currentStatusName.equals(statusName)) {
            ErrorCode errorCode;
            String messageKey;

            if (UserStatusEnum.BLOCKED.getDescription().equals(statusName)) {
                errorCode = ErrorCode.USER_ALREADY_BLOCKED;
                messageKey = "user.user.already_blocked";
            } else if (UserStatusEnum.INACTIVE.getDescription().equals(statusName)) {
                errorCode = ErrorCode.USER_ALREADY_INACTIVE;
                messageKey = "user.user.already_inactive";
            } else if (UserStatusEnum.ACTIVE.getDescription().equals(statusName)) {
                errorCode = ErrorCode.USER_ALREADY_ACTIVE;
                messageKey = "user.user.already_active";
            } else {
                errorCode = ErrorCode.BUSINESS_RULE_VIOLATION;
                messageKey = "user.default.business_rule_violation";
            }

            resultFormatter.throwBusinessRuleViolation(errorCode, messageKey, id);
        }

        systemUser.setStatus(newStatus);

        Optional<SystemUser> updatedOpt = systemUserCommandRepository.updateSystemUser(systemUser);
        if (updatedOpt.isEmpty()) {
            resultFormatter.throwEntityCreationFailed(
                ErrorCode.USER_UPDATE_FAILED,
                "user.user.update_failed",
                id
            );
        }
        return updatedOpt.get();
    }

    /**
     * Elimina un usuario del sistema.
     *
     * @param id identificador del usuario a eliminar
     */
    @Override
    public void deleteSystemUser(Long id) {
        if (!systemUserQueryRepository.existsById(id)) {
            resultFormatter.throwEntityNotFound(
                ErrorCode.USER_NOT_FOUND,
                "user.user.not_found",
                id
            );
        }

        systemUserCommandRepository.deleteSystemUserById(id);
    }

    /**
     * Resuelve los roles del usuario: consulta cada rol por su ID en el repositorio
     * y reemplaza los stubs (solo ID) por objetos completos.
     *
     * @param systemUser usuario con roles que contienen solo IDs
     */
    private void resolveRoles(SystemUser systemUser) {
        if (systemUser.getRoles() == null || systemUser.getRoles().isEmpty()) {
            return;
        }

        Set<Role> resolvedRoles = new HashSet<>();
        for (Role roleStub : systemUser.getRoles()) {
            Optional<Role> resolvedOpt = roleQueryRepository.getRoleById(roleStub.getId());
            if (resolvedOpt.isEmpty()) {
                resultFormatter.throwEntityNotFound(
                    ErrorCode.ROLE_NOT_FOUND,
                    "user.role.not_found",
                    roleStub.getId()
                );
            }
            resolvedRoles.add(resolvedOpt.get());
        }
        systemUser.setRoles(resolvedRoles);
    }

    /**
     * Normaliza un texto: trim y primera letra de cada palabra en mayúscula.
     *
     * @param text texto a normalizar (puede ser {@code null})
     * @return el texto con la primera letra de cada palabra en mayúscula, o {@code null} si la entrada era nula
     */
    private String capitalizeWords(String text) {
        if (text == null) return null;
        return Arrays.stream(text.trim().split("\\s+"))
            .map(word -> word.substring(0, 1).toUpperCase() + word.substring(1).toLowerCase())
            .collect(Collectors.joining(" "));
    }
}

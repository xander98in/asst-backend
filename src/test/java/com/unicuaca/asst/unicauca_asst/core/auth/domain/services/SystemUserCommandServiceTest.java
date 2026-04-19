package com.unicuaca.asst.unicauca_asst.core.auth.domain.services;

import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.unicuaca.asst.unicauca_asst.common.domain.ports.output.ResultFormatterOutputPort;
import com.unicuaca.asst.unicauca_asst.common.exceptions.BusinessRuleViolationException;
import com.unicuaca.asst.unicauca_asst.common.exceptions.EntityAlreadyExistsException;
import com.unicuaca.asst.unicauca_asst.common.exceptions.EntityCreationException;
import com.unicuaca.asst.unicauca_asst.common.exceptions.EntityNotFoundPersException;
import com.unicuaca.asst.unicauca_asst.common.exceptions.structure.ErrorCode;
import com.unicuaca.asst.unicauca_asst.core.auth.domain.models.Role;
import com.unicuaca.asst.unicauca_asst.core.auth.domain.models.SystemUser;
import com.unicuaca.asst.unicauca_asst.core.auth.domain.models.UserStatus;
import com.unicuaca.asst.unicauca_asst.core.auth.domain.models.enums.RoleEnum;
import com.unicuaca.asst.unicauca_asst.core.auth.domain.models.enums.UserStatusEnum;
import com.unicuaca.asst.unicauca_asst.core.auth.domain.ports.output.RoleQueryRepository;
import com.unicuaca.asst.unicauca_asst.core.auth.domain.ports.output.SystemUserCommandRepository;
import com.unicuaca.asst.unicauca_asst.core.auth.domain.ports.output.SystemUserQueryRepository;
import com.unicuaca.asst.unicauca_asst.core.auth.domain.ports.output.UserStatusQueryRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SystemUserCommandServiceTest {

    @Mock
    private SystemUserCommandRepository systemUserCommandRepository;

    @Mock
    private SystemUserQueryRepository systemUserQueryRepository;

    @Mock
    private UserStatusQueryRepository userStatusQueryRepository;

    @Mock
    private RoleQueryRepository roleQueryRepository;

    @Mock
    private ResultFormatterOutputPort resultFormatter;

    @InjectMocks
    private SystemUserCommandService systemUserCommandService;

    // ==================================================================================
    // createSystemUser
    // ==================================================================================

    @Nested
    @DisplayName("createSystemUser")
    class CreateSystemUser {

        @Test
        @DisplayName("Debe crear usuario cuando todas las validaciones pasan")
        void should_createUser_when_allValidationsPass() {
            // Arrange
            Role roleStub = Role.builder().id(1L).build();
            Role resolvedRole = Role.builder().id(1L).name("Administrador").keyName(RoleEnum.ADMIN.getDescription()).build();
            UserStatus activeStatus = buildStatus(UserStatusEnum.ACTIVE);
            SystemUser inputUser = buildCreateUser(Set.of(roleStub));
            SystemUser savedUser = buildCreateUser(Set.of(resolvedRole));
            savedUser.setId(1L);

            when(systemUserQueryRepository.existsByEmail("admin@unicauca.edu.co")).thenReturn(false);
            when(systemUserQueryRepository.existsByUsername("admin")).thenReturn(false);
            when(roleQueryRepository.getRoleById(1L)).thenReturn(Optional.of(resolvedRole));
            when(userStatusQueryRepository.getUserStatusByName(UserStatusEnum.ACTIVE.getDescription()))
                    .thenReturn(Optional.of(activeStatus));
            when(systemUserCommandRepository.saveSystemUser(any(SystemUser.class)))
                    .thenReturn(Optional.of(savedUser));

            // Act
            SystemUser result = systemUserCommandService.createSystemUser(inputUser);

            // Assert
            assertThat(result).isNotNull();
            assertThat(result.getId()).isEqualTo(1L);
        }

        @Test
        @DisplayName("Debe lanzar EntityAlreadyExists cuando el email ya existe")
        void should_throwEntityAlreadyExists_when_emailExists() {
            // Arrange
            SystemUser inputUser = buildCreateUser(Set.of(Role.builder().id(1L).build()));
            when(systemUserQueryRepository.existsByEmail("admin@unicauca.edu.co")).thenReturn(true);
            doThrow(new EntityAlreadyExistsException(
                    ErrorCode.USER_EMAIL_EXISTS.getCode(),
                    ErrorCode.USER_EMAIL_EXISTS.getMessageKey(),
                    "user.user.email_exists",
                    new Object[]{"admin@unicauca.edu.co"}))
                .when(resultFormatter).throwEntityAlreadyExists(
                    any(ErrorCode.class), anyString(), any());

            // Act & Assert
            assertThatThrownBy(() -> systemUserCommandService.createSystemUser(inputUser))
                    .isInstanceOf(EntityAlreadyExistsException.class);

            verify(resultFormatter).throwEntityAlreadyExists(
                    ErrorCode.USER_EMAIL_EXISTS,
                    "user.user.email_exists",
                    "admin@unicauca.edu.co");
        }

        @Test
        @DisplayName("Debe lanzar EntityAlreadyExists cuando el username ya existe")
        void should_throwEntityAlreadyExists_when_usernameExists() {
            // Arrange
            SystemUser inputUser = buildCreateUser(Set.of(Role.builder().id(1L).build()));
            when(systemUserQueryRepository.existsByEmail("admin@unicauca.edu.co")).thenReturn(false);
            when(systemUserQueryRepository.existsByUsername("admin")).thenReturn(true);
            doThrow(new EntityAlreadyExistsException(
                    ErrorCode.USER_USERNAME_EXISTS.getCode(),
                    ErrorCode.USER_USERNAME_EXISTS.getMessageKey(),
                    "user.user.username_exists",
                    new Object[]{"admin"}))
                .when(resultFormatter).throwEntityAlreadyExists(
                    any(ErrorCode.class), anyString(), any());

            // Act & Assert
            assertThatThrownBy(() -> systemUserCommandService.createSystemUser(inputUser))
                    .isInstanceOf(EntityAlreadyExistsException.class);

            verify(resultFormatter).throwEntityAlreadyExists(
                    ErrorCode.USER_USERNAME_EXISTS,
                    "user.user.username_exists",
                    "admin");
        }

        @Test
        @DisplayName("Debe lanzar EntityNotFound cuando el rol no existe")
        void should_throwEntityNotFound_when_roleNotFound() {
            // Arrange
            SystemUser inputUser = buildCreateUser(Set.of(Role.builder().id(99L).build()));
            when(systemUserQueryRepository.existsByEmail("admin@unicauca.edu.co")).thenReturn(false);
            when(systemUserQueryRepository.existsByUsername("admin")).thenReturn(false);
            when(roleQueryRepository.getRoleById(99L)).thenReturn(Optional.empty());
            doThrow(new EntityNotFoundPersException(
                    ErrorCode.ROLE_NOT_FOUND.getCode(),
                    ErrorCode.ROLE_NOT_FOUND.getMessageKey(),
                    "user.role.not_found",
                    new Object[]{99L}))
                .when(resultFormatter).throwEntityNotFound(
                    any(ErrorCode.class), anyString(), any());

            // Act & Assert
            assertThatThrownBy(() -> systemUserCommandService.createSystemUser(inputUser))
                    .isInstanceOf(EntityNotFoundPersException.class);

            verify(resultFormatter).throwEntityNotFound(
                    ErrorCode.ROLE_NOT_FOUND,
                    "user.role.not_found",
                    99L);
        }

        @Test
        @DisplayName("Debe lanzar EntityNotFound cuando el estado ACTIVE no existe en catálogo")
        void should_throwEntityNotFound_when_activeStatusNotFound() {
            // Arrange
            Role resolvedRole = Role.builder().id(1L).name("Administrador").keyName(RoleEnum.ADMIN.getDescription()).build();
            SystemUser inputUser = buildCreateUser(Set.of(Role.builder().id(1L).build()));
            when(systemUserQueryRepository.existsByEmail("admin@unicauca.edu.co")).thenReturn(false);
            when(systemUserQueryRepository.existsByUsername("admin")).thenReturn(false);
            when(roleQueryRepository.getRoleById(1L)).thenReturn(Optional.of(resolvedRole));
            when(userStatusQueryRepository.getUserStatusByName(UserStatusEnum.ACTIVE.getDescription()))
                    .thenReturn(Optional.empty());
            doThrow(new EntityNotFoundPersException(
                    ErrorCode.USER_STATUS_NOT_FOUND.getCode(),
                    ErrorCode.USER_STATUS_NOT_FOUND.getMessageKey(),
                    "user.user.not_found",
                    new Object[]{UserStatusEnum.ACTIVE.getDescription()}))
                .when(resultFormatter).throwEntityNotFound(
                    any(ErrorCode.class), anyString(), any());

            // Act & Assert
            assertThatThrownBy(() -> systemUserCommandService.createSystemUser(inputUser))
                    .isInstanceOf(EntityNotFoundPersException.class);

            verify(resultFormatter).throwEntityNotFound(
                    ErrorCode.USER_STATUS_NOT_FOUND,
                    "user.user.not_found",
                    UserStatusEnum.ACTIVE.getDescription());
        }

        @Test
        @DisplayName("Debe lanzar EntityCreationException cuando falla el guardado")
        void should_throwEntityCreationFailed_when_saveFails() {
            // Arrange
            Role resolvedRole = Role.builder().id(1L).name("Administrador").keyName(RoleEnum.ADMIN.getDescription()).build();
            UserStatus activeStatus = buildStatus(UserStatusEnum.ACTIVE);
            SystemUser inputUser = buildCreateUser(Set.of(Role.builder().id(1L).build()));

            when(systemUserQueryRepository.existsByEmail("admin@unicauca.edu.co")).thenReturn(false);
            when(systemUserQueryRepository.existsByUsername("admin")).thenReturn(false);
            when(roleQueryRepository.getRoleById(1L)).thenReturn(Optional.of(resolvedRole));
            when(userStatusQueryRepository.getUserStatusByName(UserStatusEnum.ACTIVE.getDescription()))
                    .thenReturn(Optional.of(activeStatus));
            when(systemUserCommandRepository.saveSystemUser(any(SystemUser.class)))
                    .thenReturn(Optional.empty());
            doThrow(new EntityCreationException(
                    ErrorCode.USER_CREATION_FAILED.getCode(),
                    ErrorCode.USER_CREATION_FAILED.getMessageKey(),
                    "user.user.creation_failed",
                    new Object[]{"admin@unicauca.edu.co"}))
                .when(resultFormatter).throwEntityCreationFailed(
                    any(ErrorCode.class), anyString(), any());

            // Act & Assert
            assertThatThrownBy(() -> systemUserCommandService.createSystemUser(inputUser))
                    .isInstanceOf(EntityCreationException.class);

            verify(resultFormatter).throwEntityCreationFailed(
                    ErrorCode.USER_CREATION_FAILED,
                    "user.user.creation_failed",
                    "admin@unicauca.edu.co");
        }

        @Test
        @DisplayName("Debe omitir resolución de roles cuando roles es null")
        void should_skipResolveRoles_when_rolesIsNull() {
            // Arrange
            UserStatus activeStatus = buildStatus(UserStatusEnum.ACTIVE);
            SystemUser inputUser = buildCreateUser(null);
            SystemUser savedUser = buildCreateUser(null);
            savedUser.setId(1L);

            when(systemUserQueryRepository.existsByEmail("admin@unicauca.edu.co")).thenReturn(false);
            when(systemUserQueryRepository.existsByUsername("admin")).thenReturn(false);
            when(userStatusQueryRepository.getUserStatusByName(UserStatusEnum.ACTIVE.getDescription()))
                    .thenReturn(Optional.of(activeStatus));
            when(systemUserCommandRepository.saveSystemUser(any(SystemUser.class)))
                    .thenReturn(Optional.of(savedUser));

            // Act
            SystemUser result = systemUserCommandService.createSystemUser(inputUser);

            // Assert
            assertThat(result).isNotNull();
            verify(roleQueryRepository, never()).getRoleById(anyLong());
        }

        @Test
        @DisplayName("Debe omitir resolución de roles cuando roles está vacío")
        void should_skipResolveRoles_when_rolesIsEmpty() {
            // Arrange
            UserStatus activeStatus = buildStatus(UserStatusEnum.ACTIVE);
            SystemUser inputUser = buildCreateUser(new java.util.HashSet<>());
            SystemUser savedUser = buildCreateUser(new java.util.HashSet<>());
            savedUser.setId(1L);

            when(systemUserQueryRepository.existsByEmail("admin@unicauca.edu.co")).thenReturn(false);
            when(systemUserQueryRepository.existsByUsername("admin")).thenReturn(false);
            when(userStatusQueryRepository.getUserStatusByName(UserStatusEnum.ACTIVE.getDescription()))
                    .thenReturn(Optional.of(activeStatus));
            when(systemUserCommandRepository.saveSystemUser(any(SystemUser.class)))
                    .thenReturn(Optional.of(savedUser));

            // Act
            SystemUser result = systemUserCommandService.createSystemUser(inputUser);

            // Assert
            assertThat(result).isNotNull();
            verify(roleQueryRepository, never()).getRoleById(anyLong());
        }

        @Test
        @DisplayName("Debe manejar fullName null sin error en capitalizeWords")
        void should_handleNullFullName() {
            // Arrange
            Role resolvedRole = Role.builder().id(1L).name("Administrador").keyName(RoleEnum.ADMIN.getDescription()).build();
            UserStatus activeStatus = buildStatus(UserStatusEnum.ACTIVE);
            SystemUser inputUser = SystemUser.builder()
                    .email("admin@unicauca.edu.co")
                    .username("  Admin  ")
                    .fullName(null)
                    .roles(Set.of(Role.builder().id(1L).build()))
                    .build();
            SystemUser savedUser = SystemUser.builder().id(1L).email("admin@unicauca.edu.co").build();

            when(systemUserQueryRepository.existsByEmail("admin@unicauca.edu.co")).thenReturn(false);
            when(systemUserQueryRepository.existsByUsername("admin")).thenReturn(false);
            when(roleQueryRepository.getRoleById(1L)).thenReturn(Optional.of(resolvedRole));
            when(userStatusQueryRepository.getUserStatusByName(UserStatusEnum.ACTIVE.getDescription()))
                    .thenReturn(Optional.of(activeStatus));
            when(systemUserCommandRepository.saveSystemUser(any(SystemUser.class)))
                    .thenReturn(Optional.of(savedUser));

            // Act
            SystemUser result = systemUserCommandService.createSystemUser(inputUser);

            // Assert
            assertThat(result).isNotNull();
        }
    }

    // ==================================================================================
    // updateSystemUser
    // ==================================================================================

    @Nested
    @DisplayName("updateSystemUser")
    class UpdateSystemUser {

        @Test
        @DisplayName("Debe actualizar usuario cuando todas las validaciones pasan")
        void should_updateUser_when_allValidationsPass() {
            // Arrange
            Role roleStub = Role.builder().id(1L).build();
            Role resolvedRole = Role.builder().id(1L).name("Administrador").keyName(RoleEnum.ADMIN.getDescription()).build();
            SystemUser inputUser = buildUpdateUser(Set.of(roleStub));
            SystemUser updatedUser = buildUpdateUser(Set.of(resolvedRole));

            when(systemUserQueryRepository.existsById(1L)).thenReturn(true);
            when(systemUserQueryRepository.isEmailAssignedToDifferentUser("admin@unicauca.edu.co", 1L)).thenReturn(false);
            when(systemUserQueryRepository.isUsernameAssignedToDifferentUser("admin", 1L)).thenReturn(false);
            when(roleQueryRepository.getRoleById(1L)).thenReturn(Optional.of(resolvedRole));
            when(systemUserCommandRepository.updateSystemUser(any(SystemUser.class)))
                    .thenReturn(Optional.of(updatedUser));

            // Act
            SystemUser result = systemUserCommandService.updateSystemUser(inputUser);

            // Assert
            assertThat(result).isNotNull();
        }

        @Test
        @DisplayName("Debe lanzar EntityNotFound cuando el usuario no existe")
        void should_throwEntityNotFound_when_userNotFound() {
            // Arrange
            SystemUser inputUser = buildUpdateUser(Set.of(Role.builder().id(1L).build()));
            when(systemUserQueryRepository.existsById(1L)).thenReturn(false);
            doThrow(new EntityNotFoundPersException(
                    ErrorCode.USER_NOT_FOUND.getCode(),
                    ErrorCode.USER_NOT_FOUND.getMessageKey(),
                    "user.user.not_found",
                    new Object[]{1L}))
                .when(resultFormatter).throwEntityNotFound(
                    any(ErrorCode.class), anyString(), any());

            // Act & Assert
            assertThatThrownBy(() -> systemUserCommandService.updateSystemUser(inputUser))
                    .isInstanceOf(EntityNotFoundPersException.class);

            verify(resultFormatter).throwEntityNotFound(
                    ErrorCode.USER_NOT_FOUND,
                    "user.user.not_found",
                    1L);
        }

        @Test
        @DisplayName("Debe lanzar EntityAlreadyExists cuando el email pertenece a otro usuario")
        void should_throwEntityAlreadyExists_when_emailAssignedToDifferentUser() {
            // Arrange
            SystemUser inputUser = buildUpdateUser(Set.of(Role.builder().id(1L).build()));
            when(systemUserQueryRepository.existsById(1L)).thenReturn(true);
            when(systemUserQueryRepository.isEmailAssignedToDifferentUser("admin@unicauca.edu.co", 1L)).thenReturn(true);
            doThrow(new EntityAlreadyExistsException(
                    ErrorCode.USER_EMAIL_EXISTS.getCode(),
                    ErrorCode.USER_EMAIL_EXISTS.getMessageKey(),
                    "user.user.email_exists",
                    new Object[]{"admin@unicauca.edu.co"}))
                .when(resultFormatter).throwEntityAlreadyExists(
                    any(ErrorCode.class), anyString(), any());

            // Act & Assert
            assertThatThrownBy(() -> systemUserCommandService.updateSystemUser(inputUser))
                    .isInstanceOf(EntityAlreadyExistsException.class);

            verify(resultFormatter).throwEntityAlreadyExists(
                    ErrorCode.USER_EMAIL_EXISTS,
                    "user.user.email_exists",
                    "admin@unicauca.edu.co");
        }

        @Test
        @DisplayName("Debe lanzar EntityAlreadyExists cuando el username pertenece a otro usuario")
        void should_throwEntityAlreadyExists_when_usernameAssignedToDifferentUser() {
            // Arrange
            SystemUser inputUser = buildUpdateUser(Set.of(Role.builder().id(1L).build()));
            when(systemUserQueryRepository.existsById(1L)).thenReturn(true);
            when(systemUserQueryRepository.isEmailAssignedToDifferentUser("admin@unicauca.edu.co", 1L)).thenReturn(false);
            when(systemUserQueryRepository.isUsernameAssignedToDifferentUser("admin", 1L)).thenReturn(true);
            doThrow(new EntityAlreadyExistsException(
                    ErrorCode.USER_USERNAME_EXISTS.getCode(),
                    ErrorCode.USER_USERNAME_EXISTS.getMessageKey(),
                    "user.user.update_username_exists",
                    new Object[]{"admin"}))
                .when(resultFormatter).throwEntityAlreadyExists(
                    any(ErrorCode.class), anyString(), any());

            // Act & Assert
            assertThatThrownBy(() -> systemUserCommandService.updateSystemUser(inputUser))
                    .isInstanceOf(EntityAlreadyExistsException.class);

            verify(resultFormatter).throwEntityAlreadyExists(
                    ErrorCode.USER_USERNAME_EXISTS,
                    "user.user.update_username_exists",
                    "admin");
        }

        @Test
        @DisplayName("Debe lanzar EntityCreationException cuando falla la actualización")
        void should_throwEntityCreationFailed_when_updateFails() {
            // Arrange
            Role resolvedRole = Role.builder().id(1L).name("Administrador").keyName(RoleEnum.ADMIN.getDescription()).build();
            SystemUser inputUser = buildUpdateUser(Set.of(Role.builder().id(1L).build()));

            when(systemUserQueryRepository.existsById(1L)).thenReturn(true);
            when(systemUserQueryRepository.isEmailAssignedToDifferentUser("admin@unicauca.edu.co", 1L)).thenReturn(false);
            when(systemUserQueryRepository.isUsernameAssignedToDifferentUser("admin", 1L)).thenReturn(false);
            when(roleQueryRepository.getRoleById(1L)).thenReturn(Optional.of(resolvedRole));
            when(systemUserCommandRepository.updateSystemUser(any(SystemUser.class)))
                    .thenReturn(Optional.empty());
            doThrow(new EntityCreationException(
                    ErrorCode.USER_UPDATE_FAILED.getCode(),
                    ErrorCode.USER_UPDATE_FAILED.getMessageKey(),
                    "user.user.update_failed",
                    new Object[]{1L}))
                .when(resultFormatter).throwEntityCreationFailed(
                    any(ErrorCode.class), anyString(), any());

            // Act & Assert
            assertThatThrownBy(() -> systemUserCommandService.updateSystemUser(inputUser))
                    .isInstanceOf(EntityCreationException.class);

            verify(resultFormatter).throwEntityCreationFailed(
                    ErrorCode.USER_UPDATE_FAILED,
                    "user.user.update_failed",
                    1L);
        }
    }

    // ==================================================================================
    // changeUserStatus
    // ==================================================================================

    @Nested
    @DisplayName("changeUserStatus")
    class ChangeUserStatus {

        @Test
        @DisplayName("Debe cambiar estado cuando el nuevo estado es diferente al actual")
        void should_changeStatus_when_newStatusIsDifferent() {
            // Arrange
            SystemUser user = buildSystemUserWithStatus(UserStatusEnum.ACTIVE);
            UserStatus inactiveStatus = buildStatus(UserStatusEnum.INACTIVE);
            SystemUser updatedUser = buildSystemUserWithStatus(UserStatusEnum.INACTIVE);

            when(systemUserQueryRepository.getSystemUserById(1L)).thenReturn(Optional.of(user));
            when(userStatusQueryRepository.getUserStatusByName(UserStatusEnum.INACTIVE.getDescription()))
                    .thenReturn(Optional.of(inactiveStatus));
            when(systemUserCommandRepository.updateSystemUser(any(SystemUser.class)))
                    .thenReturn(Optional.of(updatedUser));

            // Act
            SystemUser result = systemUserCommandService.changeUserStatus(1L, UserStatusEnum.INACTIVE.getDescription());

            // Assert
            assertThat(result).isNotNull();
            assertThat(result.getStatus().getName()).isEqualTo(UserStatusEnum.INACTIVE.getDescription());
        }

        @Test
        @DisplayName("Debe lanzar EntityNotFound cuando el usuario no existe")
        void should_throwEntityNotFound_when_userNotFound() {
            // Arrange
            when(systemUserQueryRepository.getSystemUserById(99L)).thenReturn(Optional.empty());
            doThrow(new EntityNotFoundPersException(
                    ErrorCode.USER_NOT_FOUND.getCode(),
                    ErrorCode.USER_NOT_FOUND.getMessageKey(),
                    "user.user.not_found",
                    new Object[]{99L}))
                .when(resultFormatter).throwEntityNotFound(
                    any(ErrorCode.class), anyString(), any());

            // Act & Assert
            assertThatThrownBy(() -> systemUserCommandService.changeUserStatus(99L, UserStatusEnum.INACTIVE.getDescription()))
                    .isInstanceOf(EntityNotFoundPersException.class);

            verify(resultFormatter).throwEntityNotFound(
                    ErrorCode.USER_NOT_FOUND,
                    "user.user.not_found",
                    99L);
        }

        @Test
        @DisplayName("Debe lanzar EntityNotFound cuando el estado no existe en catálogo")
        void should_throwEntityNotFound_when_statusNotFound() {
            // Arrange
            SystemUser user = buildSystemUserWithStatus(UserStatusEnum.ACTIVE);
            when(systemUserQueryRepository.getSystemUserById(1L)).thenReturn(Optional.of(user));
            when(userStatusQueryRepository.getUserStatusByName("Inexistente")).thenReturn(Optional.empty());
            doThrow(new EntityNotFoundPersException(
                    ErrorCode.USER_STATUS_NOT_FOUND.getCode(),
                    ErrorCode.USER_STATUS_NOT_FOUND.getMessageKey(),
                    "user.user.not_found",
                    new Object[]{"Inexistente"}))
                .when(resultFormatter).throwEntityNotFound(
                    any(ErrorCode.class), anyString(), any());

            // Act & Assert
            assertThatThrownBy(() -> systemUserCommandService.changeUserStatus(1L, "Inexistente"))
                    .isInstanceOf(EntityNotFoundPersException.class);

            verify(resultFormatter).throwEntityNotFound(
                    ErrorCode.USER_STATUS_NOT_FOUND,
                    "user.user.not_found",
                    "Inexistente");
        }

        @Test
        @DisplayName("Debe lanzar BusinessRuleViolation cuando el usuario ya está bloqueado")
        void should_throwBusinessRuleViolation_when_alreadyBlocked() {
            // Arrange
            SystemUser user = buildSystemUserWithStatus(UserStatusEnum.BLOCKED);
            UserStatus blockedStatus = buildStatus(UserStatusEnum.BLOCKED);
            when(systemUserQueryRepository.getSystemUserById(1L)).thenReturn(Optional.of(user));
            when(userStatusQueryRepository.getUserStatusByName(UserStatusEnum.BLOCKED.getDescription()))
                    .thenReturn(Optional.of(blockedStatus));
            doThrow(new BusinessRuleViolationException(
                    ErrorCode.USER_ALREADY_BLOCKED.getCode(),
                    ErrorCode.USER_ALREADY_BLOCKED.getMessageKey(),
                    "user.user.already_blocked",
                    new Object[]{1L}))
                .when(resultFormatter).throwBusinessRuleViolation(
                    any(ErrorCode.class), anyString(), any());

            // Act & Assert
            assertThatThrownBy(() -> systemUserCommandService
                    .changeUserStatus(1L, UserStatusEnum.BLOCKED.getDescription()))
                    .isInstanceOf(BusinessRuleViolationException.class);

            verify(resultFormatter).throwBusinessRuleViolation(
                    ErrorCode.USER_ALREADY_BLOCKED,
                    "user.user.already_blocked",
                    1L);
        }

        @Test
        @DisplayName("Debe lanzar BusinessRuleViolation cuando el usuario ya está inactivo")
        void should_throwBusinessRuleViolation_when_alreadyInactive() {
            // Arrange
            SystemUser user = buildSystemUserWithStatus(UserStatusEnum.INACTIVE);
            UserStatus inactiveStatus = buildStatus(UserStatusEnum.INACTIVE);
            when(systemUserQueryRepository.getSystemUserById(1L)).thenReturn(Optional.of(user));
            when(userStatusQueryRepository.getUserStatusByName(UserStatusEnum.INACTIVE.getDescription()))
                    .thenReturn(Optional.of(inactiveStatus));
            doThrow(new BusinessRuleViolationException(
                    ErrorCode.USER_ALREADY_INACTIVE.getCode(),
                    ErrorCode.USER_ALREADY_INACTIVE.getMessageKey(),
                    "user.user.already_inactive",
                    new Object[]{1L}))
                .when(resultFormatter).throwBusinessRuleViolation(
                    any(ErrorCode.class), anyString(), any());

            // Act & Assert
            assertThatThrownBy(() -> systemUserCommandService
                    .changeUserStatus(1L, UserStatusEnum.INACTIVE.getDescription()))
                    .isInstanceOf(BusinessRuleViolationException.class);

            verify(resultFormatter).throwBusinessRuleViolation(
                    ErrorCode.USER_ALREADY_INACTIVE,
                    "user.user.already_inactive",
                    1L);
        }

        @Test
        @DisplayName("Debe lanzar BusinessRuleViolation cuando el usuario ya está activo")
        void should_throwBusinessRuleViolation_when_alreadyActive() {
            // Arrange
            SystemUser user = buildSystemUserWithStatus(UserStatusEnum.ACTIVE);
            UserStatus activeStatus = buildStatus(UserStatusEnum.ACTIVE);
            when(systemUserQueryRepository.getSystemUserById(1L)).thenReturn(Optional.of(user));
            when(userStatusQueryRepository.getUserStatusByName(UserStatusEnum.ACTIVE.getDescription()))
                    .thenReturn(Optional.of(activeStatus));
            doThrow(new BusinessRuleViolationException(
                    ErrorCode.USER_ALREADY_ACTIVE.getCode(),
                    ErrorCode.USER_ALREADY_ACTIVE.getMessageKey(),
                    "user.user.already_active",
                    new Object[]{1L}))
                .when(resultFormatter).throwBusinessRuleViolation(
                    any(ErrorCode.class), anyString(), any());

            // Act & Assert
            assertThatThrownBy(() -> systemUserCommandService
                    .changeUserStatus(1L, UserStatusEnum.ACTIVE.getDescription()))
                    .isInstanceOf(BusinessRuleViolationException.class);

            verify(resultFormatter).throwBusinessRuleViolation(
                    ErrorCode.USER_ALREADY_ACTIVE,
                    "user.user.already_active",
                    1L);
        }

        @Test
        @DisplayName("Debe lanzar BusinessRuleViolation genérica cuando el estado es desconocido y coincide")
        void should_throwBusinessRuleViolation_when_sameUnknownStatus() {
            // Arrange
            UserStatus unknownStatus = UserStatus.builder().id(99L).name("Desconocido").build();
            SystemUser user = SystemUser.builder()
                    .id(1L).email("admin@unicauca.edu.co").username("admin").status(unknownStatus).build();
            when(systemUserQueryRepository.getSystemUserById(1L)).thenReturn(Optional.of(user));
            when(userStatusQueryRepository.getUserStatusByName("Desconocido"))
                    .thenReturn(Optional.of(unknownStatus));
            doThrow(new BusinessRuleViolationException(
                    ErrorCode.BUSINESS_RULE_VIOLATION.getCode(),
                    ErrorCode.BUSINESS_RULE_VIOLATION.getMessageKey(),
                    "user.default.business_rule_violation",
                    new Object[]{1L}))
                .when(resultFormatter).throwBusinessRuleViolation(
                    any(ErrorCode.class), anyString(), any());

            // Act & Assert
            assertThatThrownBy(() -> systemUserCommandService
                    .changeUserStatus(1L, "Desconocido"))
                    .isInstanceOf(BusinessRuleViolationException.class);

            verify(resultFormatter).throwBusinessRuleViolation(
                    ErrorCode.BUSINESS_RULE_VIOLATION,
                    "user.default.business_rule_violation",
                    1L);
        }

        @Test
        @DisplayName("Debe lanzar EntityCreationException cuando falla la actualización de estado")
        void should_throwEntityCreationFailed_when_statusUpdateFails() {
            // Arrange
            SystemUser user = buildSystemUserWithStatus(UserStatusEnum.ACTIVE);
            UserStatus inactiveStatus = buildStatus(UserStatusEnum.INACTIVE);
            when(systemUserQueryRepository.getSystemUserById(1L)).thenReturn(Optional.of(user));
            when(userStatusQueryRepository.getUserStatusByName(UserStatusEnum.INACTIVE.getDescription()))
                    .thenReturn(Optional.of(inactiveStatus));
            when(systemUserCommandRepository.updateSystemUser(any(SystemUser.class)))
                    .thenReturn(Optional.empty());
            doThrow(new EntityCreationException(
                    ErrorCode.USER_UPDATE_FAILED.getCode(),
                    ErrorCode.USER_UPDATE_FAILED.getMessageKey(),
                    "user.user.update_failed",
                    new Object[]{1L}))
                .when(resultFormatter).throwEntityCreationFailed(
                    any(ErrorCode.class), anyString(), any());

            // Act & Assert
            assertThatThrownBy(() -> systemUserCommandService
                    .changeUserStatus(1L, UserStatusEnum.INACTIVE.getDescription()))
                    .isInstanceOf(EntityCreationException.class);

            verify(resultFormatter).throwEntityCreationFailed(
                    ErrorCode.USER_UPDATE_FAILED,
                    "user.user.update_failed",
                    1L);
        }
    }

    // ==================================================================================
    // deleteSystemUser
    // ==================================================================================

    @Nested
    @DisplayName("deleteSystemUser")
    class DeleteSystemUser {

        @Test
        @DisplayName("Debe eliminar usuario cuando existe")
        void should_deleteUser_when_exists() {
            // Arrange
            when(systemUserQueryRepository.existsById(1L)).thenReturn(true);

            // Act
            systemUserCommandService.deleteSystemUser(1L);

            // Assert
            verify(systemUserCommandRepository).deleteSystemUserById(1L);
        }

        @Test
        @DisplayName("Debe lanzar EntityNotFound cuando el usuario no existe al eliminar")
        void should_throwEntityNotFound_when_userNotFoundOnDelete() {
            // Arrange
            when(systemUserQueryRepository.existsById(99L)).thenReturn(false);
            doThrow(new EntityNotFoundPersException(
                    ErrorCode.USER_NOT_FOUND.getCode(),
                    ErrorCode.USER_NOT_FOUND.getMessageKey(),
                    "user.user.not_found",
                    new Object[]{99L}))
                .when(resultFormatter).throwEntityNotFound(
                    any(ErrorCode.class), anyString(), any());

            // Act & Assert
            assertThatThrownBy(() -> systemUserCommandService.deleteSystemUser(99L))
                    .isInstanceOf(EntityNotFoundPersException.class);

            verify(resultFormatter).throwEntityNotFound(
                    ErrorCode.USER_NOT_FOUND,
                    "user.user.not_found",
                    99L);
            verify(systemUserCommandRepository, never()).deleteSystemUserById(anyLong());
        }
    }

    // ==================================================================================
    // Helpers
    // ==================================================================================

    private SystemUser buildCreateUser(Set<Role> roles) {
        return SystemUser.builder()
                .email("admin@unicauca.edu.co")
                .username("  Admin  ")
                .fullName("juan carlos perez")
                .roles(roles)
                .build();
    }

    private SystemUser buildUpdateUser(Set<Role> roles) {
        return SystemUser.builder()
                .id(1L)
                .email("admin@unicauca.edu.co")
                .username("  Admin  ")
                .fullName("juan carlos perez")
                .roles(roles)
                .build();
    }

    private SystemUser buildSystemUserWithStatus(UserStatusEnum statusEnum) {
        UserStatus status = buildStatus(statusEnum);
        return SystemUser.builder()
                .id(1L)
                .email("admin@unicauca.edu.co")
                .username("admin")
                .status(status)
                .build();
    }

    private UserStatus buildStatus(UserStatusEnum statusEnum) {
        return UserStatus.builder()
                .id(1L)
                .name(statusEnum.getDescription())
                .build();
    }
}

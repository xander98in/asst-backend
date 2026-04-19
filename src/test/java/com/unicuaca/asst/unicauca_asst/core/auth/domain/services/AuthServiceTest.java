package com.unicuaca.asst.unicauca_asst.core.auth.domain.services;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.unicuaca.asst.unicauca_asst.common.domain.ports.output.ResultFormatterOutputPort;
import com.unicuaca.asst.unicauca_asst.common.exceptions.BusinessRuleViolationException;
import com.unicuaca.asst.unicauca_asst.common.exceptions.EntityNotFoundPersException;
import com.unicuaca.asst.unicauca_asst.common.exceptions.structure.ErrorCode;
import com.unicuaca.asst.unicauca_asst.core.auth.domain.models.SystemUser;
import com.unicuaca.asst.unicauca_asst.core.auth.domain.models.UserStatus;
import com.unicuaca.asst.unicauca_asst.core.auth.domain.models.enums.UserStatusEnum;
import com.unicuaca.asst.unicauca_asst.core.auth.domain.ports.output.SystemUserQueryRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private SystemUserQueryRepository systemUserQueryRepository;

    @Mock
    private ResultFormatterOutputPort resultFormatter;

    @InjectMocks
    private AuthService authService;

    // ==================================================================================
    // loginWithGoogle
    // ==================================================================================

    @Nested
    @DisplayName("loginWithGoogle")
    class LoginWithGoogle {

        @Test
        @DisplayName("Debe retornar usuario cuando existe y está activo")
        void should_returnUser_when_activeUser() {
            // Arrange
            SystemUser user = buildSystemUser(UserStatusEnum.ACTIVE);
            when(systemUserQueryRepository.getSystemUserByEmail("admin@unicauca.edu.co"))
                    .thenReturn(Optional.of(user));

            // Act
            SystemUser result = authService.loginWithGoogle("admin@unicauca.edu.co");

            // Assert
            assertThat(result).isNotNull();
            assertThat(result.getEmail()).isEqualTo("admin@unicauca.edu.co");
        }

        @Test
        @DisplayName("Debe lanzar EntityNotFound cuando el usuario no existe")
        void should_throwEntityNotFound_when_userNotFound() {
            // Arrange
            when(systemUserQueryRepository.getSystemUserByEmail("noexiste@unicauca.edu.co"))
                    .thenReturn(Optional.empty());
            doThrow(new EntityNotFoundPersException(
                    ErrorCode.USER_NOT_FOUND.getCode(),
                    ErrorCode.USER_NOT_FOUND.getMessageKey(),
                    "user.user.not_found",
                    new Object[]{"noexiste@unicauca.edu.co"}))
                .when(resultFormatter).throwEntityNotFound(
                    any(ErrorCode.class), anyString(), any());

            // Act & Assert
            assertThatThrownBy(() -> authService.loginWithGoogle("noexiste@unicauca.edu.co"))
                    .isInstanceOf(EntityNotFoundPersException.class);

            verify(resultFormatter).throwEntityNotFound(
                    ErrorCode.USER_NOT_FOUND,
                    "user.user.not_found",
                    "noexiste@unicauca.edu.co");
        }

        @Test
        @DisplayName("Debe lanzar BusinessRuleViolation cuando el usuario está inactivo")
        void should_throwBusinessRuleViolation_when_userIsInactive() {
            // Arrange
            SystemUser user = buildSystemUser(UserStatusEnum.INACTIVE);
            when(systemUserQueryRepository.getSystemUserByEmail("admin@unicauca.edu.co"))
                    .thenReturn(Optional.of(user));
            doThrow(new BusinessRuleViolationException(
                    ErrorCode.USER_INACTIVE.getCode(),
                    ErrorCode.USER_INACTIVE.getMessageKey(),
                    "user.user.inactive",
                    new Object[]{"admin@unicauca.edu.co"}))
                .when(resultFormatter).throwBusinessRuleViolation(
                    any(ErrorCode.class), anyString(), any());

            // Act & Assert
            assertThatThrownBy(() -> authService.loginWithGoogle("admin@unicauca.edu.co"))
                    .isInstanceOf(BusinessRuleViolationException.class);

            verify(resultFormatter).throwBusinessRuleViolation(
                    ErrorCode.USER_INACTIVE,
                    "user.user.inactive",
                    "admin@unicauca.edu.co");
        }

        @Test
        @DisplayName("Debe lanzar BusinessRuleViolation cuando el usuario está bloqueado")
        void should_throwBusinessRuleViolation_when_userIsBlocked() {
            // Arrange
            SystemUser user = buildSystemUser(UserStatusEnum.BLOCKED);
            when(systemUserQueryRepository.getSystemUserByEmail("admin@unicauca.edu.co"))
                    .thenReturn(Optional.of(user));
            doThrow(new BusinessRuleViolationException(
                    ErrorCode.USER_BLOCKED.getCode(),
                    ErrorCode.USER_BLOCKED.getMessageKey(),
                    "user.user.blocked",
                    new Object[]{"admin@unicauca.edu.co"}))
                .when(resultFormatter).throwBusinessRuleViolation(
                    any(ErrorCode.class), anyString(), any());

            // Act & Assert
            assertThatThrownBy(() -> authService.loginWithGoogle("admin@unicauca.edu.co"))
                    .isInstanceOf(BusinessRuleViolationException.class);

            verify(resultFormatter).throwBusinessRuleViolation(
                    ErrorCode.USER_BLOCKED,
                    "user.user.blocked",
                    "admin@unicauca.edu.co");
        }
    }

    // ==================================================================================
    // Helpers
    // ==================================================================================

    private SystemUser buildSystemUser(UserStatusEnum statusEnum) {
        UserStatus status = UserStatus.builder()
                .id(1L).name(statusEnum.getDescription()).build();
        return SystemUser.builder()
                .id(1L)
                .email("admin@unicauca.edu.co")
                .username("admin")
                .status(status)
                .build();
    }
}
